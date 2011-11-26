package com.veris.tomzinho;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.veris.tomzinho.SerialException.Error;

public class Serial {
	private static final String DEFAULT_PORT;

	private SerialPort serialCom;
	private OutputStream OutputToPort;

	static {
		String osname = System.getProperty("os.name", "").toLowerCase();//nome do sistema operacional
		if (osname.startsWith("windows")) {//se começar com windows
			DEFAULT_PORT = "COM12";
		} else {
			DEFAULT_PORT = "/dev/ttyUSB1";
		}
	}
	
	public Serial() throws SerialException {
		this(DEFAULT_PORT);
	}

	public Serial(String port) throws SerialException {
		connect(port);
	}
	/**
	 * Cria List(ArrayList) com todas as portas seriais e parelelas do PC
	 * @return
	 */
	public static Object[] getPortList(){
		List<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();
		
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			portList.add((CommPortIdentifier) portEnum.nextElement());
		}
		
		return portList.toArray();
	}
	/**
	 * Fecha a conexão atual e abre a uma nova conexão com a porta do parametro
	 * @param port
	 * @throws SerialException
	 */
	public void switchPort(CommPortIdentifier port) throws SerialException{
		close();
		connect(port.getName());
	}
	/**
	 * Fecha a conexão com a porta atual
	 * @throws SerialException
	 */
	public void close() throws SerialException {
		System.out.println("Close connection with serial port");
		try {
			OutputToPort.close();
		} catch (IOException e) {
			throw new SerialException(Error.IO);
		}
		serialCom.close();
	}
	/**
	 * Envia a String do parametro pela porta serial
	 * @param text
	 * @throws SerialException
	 */
	public void write(String text) throws SerialException {
		text = text.concat(".");//para o arduino saber que acabou a linha de comando é precisso desse ponto no final da linha
		try {
			OutputToPort.write(text.getBytes());
		} catch (IOException ex) {
			throw new SerialException(Error.IO);
		}
	}
	/**
	 * Faz a conexão na porta passada por parametro
	 * @param port
	 * @throws SerialException
	 */
	public void connect(String port) throws SerialException {
		try {
			CommPortIdentifier portId = null;
			
			//lista todas as portas paralelas e seriais do pc
			//e procura por uma com o mesmo nome do que a passada por parametro(port)
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			while (portEnum.hasMoreElements()) {
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
						.nextElement();
				if (currPortId.getName().equals(port)) {
					portId = currPortId;
					break;
				}

			}
			
			if(portId == null) {
				throw new SerialException(Error.PORT_NOT_FOUND);//se não encontrar o métodos para nessa linha e gera essa exception
			}

			System.out.println("Connecting to port: " + port);
			//abre a porta, parametros o nome do programa que esta usando a porta e a quantidade de milisegundos que ele vai esperar
			//a porta ser aberta
			serialCom = (SerialPort) portId.open("TomzinhoUI", 3000);// identifier,
																	// timeout

			//seta as configurações de conexão com a porta
			serialCom.setSerialPortParams(9600, //9600bits port segundo
					SerialPort.DATABITS_8,//8 bits são transmitidos por vez
					SerialPort.STOPBITS_1, //a cada 8bits transmitidos 1 bit sem dados é transmitidos para sincronização
					SerialPort.PARITY_NONE);//nenhum bit de paridade(para checar se informação foi enviada correta)

			OutputToPort = serialCom.getOutputStream();//pega o stream de escrita, atravez desse stream mandados os comandos pela porta serial
		} catch (PortInUseException ex) {
			throw new SerialException(Error.PORT_IN_USE);
		} catch (UnsupportedCommOperationException ex) {
			throw new SerialException(Error.UNSUPORTED_CONFIGURATIONS);
		} catch (IOException ex) {
			throw new SerialException(Error.IO);
		}
	}
}

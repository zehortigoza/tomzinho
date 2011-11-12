package com.veris.tomzinho;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.veris.tomzinho.SerialException.Error;

public class Serial {
	private static final String DEFAULT_PORT;

	private SerialPort SerialCom;
	private OutputStream OutputToPort;

	static {
		String osname = System.getProperty("os.name", "").toLowerCase();
		if (osname.startsWith("windows")) {
			DEFAULT_PORT = "COM1";
		} else {
			DEFAULT_PORT = "/dev/ttyUSB0";
		}
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			Serial s = new Serial();
			Thread.sleep(2000);
			s.write("l20");
			Thread.sleep(4000);
			s.write("l290");
			System.out.println("Exit");
			s.close();
		} catch (SerialException e) {
			e.printStackTrace();
		}
	}

	public Serial() throws SerialException {
		this(DEFAULT_PORT);
	}

	public Serial(String port) throws SerialException {
		connect(port);
	}

	public void close() throws SerialException {
		try {
			OutputToPort.close();
		} catch (IOException e) {
			throw new SerialException(Error.IO);
		}
		SerialCom.close();
	}

	public void write(String text) throws SerialException {
		text = text.concat(".");
		try {
			OutputToPort.write(text.getBytes());
		} catch (IOException ex) {
			throw new SerialException(Error.IO);
		}
	}

	public void connect(String port) throws SerialException {
		try {
			CommPortIdentifier portId = null;
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
				throw new SerialException(Error.PORT_NOT_FOUND);
			}

			System.out.println("Connecting to port: " + port);
			SerialCom = (SerialPort) portId.open("TomzinhoUI", 3000);// identifier,
																	// timeout

			// set port parameters
			SerialCom.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			OutputToPort = SerialCom.getOutputStream();
		} catch (PortInUseException ex) {
			throw new SerialException(Error.PORT_IN_USE);
		} catch (UnsupportedCommOperationException ex) {
			throw new SerialException(Error.UNSUPORTED_CONFIGURATIONS);
		} catch (IOException ex) {
			throw new SerialException(Error.IO);
		}
	}
}

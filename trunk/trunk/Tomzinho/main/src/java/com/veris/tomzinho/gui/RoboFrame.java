/*
 *  COPYRIGHT 2011 Sanmina-SCI Corporation
 *  ALL RIGHTS RESERVED - TODOS OS DIREITOS RESERVADOS
 *  CONFIDENTIAL, UNPUBLISHED PROPERTY OF Sanmina-SCI Corporation.
 *  PROPRIEDADE CONFIDENCIAL NAO PUBLICADA DA Sanmina-SCI Corporation. 
 * 
 *  created in Oct 29, 2011
 * 
 *  @author welinton_coradini
 */
package com.veris.tomzinho.gui;

import gnu.io.CommPortIdentifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.veris.tomzinho.Serial;
import com.veris.tomzinho.SerialException;
import com.veris.tomzinho.swing.JRaisedButton;

/**
 * @author welinton_coradini
 *
 */
public class RoboFrame extends JFrame{

	/* Default serial version id */
	private static final long serialVersionUID = 1L;
	
	private static final String R1 = "R1";
	private static final int R1_HOME_VALUE = 110;
	private static final String R2 = "R2";
	private static final String R3 = "R3";
	private static final String R4 = "R4";
	private static final String L1 = "L1";
	private static final int L1_HOME_VALUE = 100;
	private static final String L2 = "L2";
	private static final String L3 = "L3";
	private static final String L4 = "L4";
	
	private static final int ROT_MIN = 0;
	private static final int ROT_MAX = 180;

	private static int DEFAULT_HOME_VALUE = (ROT_MIN + ROT_MAX) / 2 + ROT_MIN;
	
	private final CellConstraints cc = new CellConstraints();
	private final FormLayout motorFormLayout = new FormLayout(
			"5dlu, pref", "25dlu, pref, 35dlu, pref, 60dlu, pref, 50dlu, pref, 5dlu");

	private JTextArea textArea;
	private JSlider r1Slider;
	private JSlider r2Slider;
	private JSlider r3Slider;
	private JSlider r4Slider;
	private JSlider l1Slider;
	private JSlider l2Slider;
	private JSlider l3Slider;
	private JSlider l4Slider;
	
	private File file;
	
	private Serial serialCom;

	private int delay = 500;
	
	private boolean autoCap = false;
	
	/**
	 * Constructor
	 *
	 */
	public RoboFrame() {
		super("Control Panel");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(new Dimension(900, 680));
		this.setLocationRelativeTo(null); // centraliza

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);  // maximiza
		this.setExtendedState(JFrame.ICONIFIED | this.getExtendedState());

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"5dlu, pref, 5dlu, fill:pref:grow, 5dlu", "5dlu, fill:pref:grow, 5dlu"));

		builder.add(getControlPanel(), cc.xy(2, 2));

		{
			DefaultFormBuilder leftPanelBuilder = new DefaultFormBuilder(new FormLayout(
					"fill:pref:grow", "fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, pref"));

			leftPanelBuilder.add(getCamPanel(), cc.xy(1, 1));
			leftPanelBuilder.add(getCommandLinePanel(), cc.xy(1, 3));
			leftPanelBuilder.add(getSetupPanel(), cc.xy(1, 5));
			
			JPanel leftPanel = leftPanelBuilder.getPanel();		
			leftPanel.setOpaque(false);
			builder.add(leftPanel, cc.xy(4, 2));
		}


		JPanel mainPanel = builder.getPanel();
		mainPanel.setBackground(Color.WHITE);

		this.getContentPane().add(new JScrollPane(mainPanel));
		this.setVisible(true);
		
		try {
			serialCom = new Serial();
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JPanel getControlPanel(){
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"5dlu, right:pref:grow, 0dlu, center:pref, 0dlu, left:pref:grow, 5dlu", "top:pref, 5dlu, top:pref, 5dlu:grow"));

		builder.add(getLeftPanel(), cc.xy(2, 1));
		builder.add(getImageLabel(), cc.xy(4, 1));
		builder.add(getRightPanel(), cc.xy(6, 1));

		builder.add(getDirectionPanel(), cc.xy(4, 3));

		JPanel controlPanel = builder.getPanel();
		controlPanel.setOpaque(false);
		controlPanel.setBorder(BorderFactory.createTitledBorder(""));

		return controlPanel;
	}

	private JPanel getImageLabel(){
		ImageIcon image = getImageIcon("images/bipede.png");
		image.setImage(image.getImage().getScaledInstance(400, 520,
				Image.SCALE_DEFAULT));

		JLabel label = new JLabel(image);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 520));
		panel.setOpaque(false);
		panel.add(label);

		return panel;
	}

	private JPanel getRightPanel(){
		r1Slider = getSlider(R1_HOME_VALUE, R1);
		r2Slider = getSlider(R2);
		r3Slider = getSlider(R3);
		r4Slider = getSlider(R4);

		DefaultFormBuilder builder = new DefaultFormBuilder(motorFormLayout);

		builder.add(r1Slider, cc.xy(2, 2));
		builder.add(r2Slider, cc.xy(2, 4));
		builder.add(r3Slider, cc.xy(2, 6));
		builder.add(r4Slider, cc.xy(2, 8));

		JPanel panel = builder.getPanel();
		panel.setOpaque(false);

		return panel;
	}

	private JPanel getLeftPanel(){
		l1Slider = getSlider(L1_HOME_VALUE, L1);
		l2Slider = getSlider(L2);
		l3Slider = getSlider(L3);
		l4Slider = getSlider(L4);

		DefaultFormBuilder builder = new DefaultFormBuilder(motorFormLayout);

		builder.add(l1Slider, cc.xy(2, 2));
		builder.add(l2Slider, cc.xy(2, 4));
		builder.add(l3Slider, cc.xy(2, 6));
		builder.add(l4Slider, cc.xy(2, 8));

		JPanel panel = builder.getPanel();
		panel.setOpaque(false);
		return panel;	
	}

	private JSlider getSlider(String servo){
		return getSlider(DEFAULT_HOME_VALUE, servo);
	}

	private JSlider getSlider(int pos, String servo){
		JSlider slider = new JSlider(ROT_MIN, ROT_MAX);
		slider.setOpaque(false);
		//Turn on labels at major tick marks.
		slider.setMajorTickSpacing(45);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(pos);
		slider.addChangeListener(new ServoChangeLister(servo));
		return slider;
	}
	
	private JPanel getDirectionPanel(){

		JRaisedButton upButton = new JRaisedButton(getImageIcon("images/1uparrow.png")); 
		JRaisedButton downButton = new JRaisedButton(getImageIcon("images/1downarrow.png"));
		JRaisedButton leftButton = new JRaisedButton(getImageIcon("images/1leftarrow.png"));
		JRaisedButton rightButton = new JRaisedButton(getImageIcon("images/1rightarrow.png"));
		JRaisedButton homeButton = new JRaisedButton(getImageIcon("images/folder_home.png"));
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				r1Slider.setValue(0); r1Slider.setValue(R1_HOME_VALUE);
				r2Slider.setValue(0); r2Slider.setValue(DEFAULT_HOME_VALUE);
				r3Slider.setValue(0); r3Slider.setValue(DEFAULT_HOME_VALUE);
				r4Slider.setValue(0); r4Slider.setValue(DEFAULT_HOME_VALUE);
				l1Slider.setValue(0); l1Slider.setValue(L1_HOME_VALUE);
				l2Slider.setValue(0); l2Slider.setValue(DEFAULT_HOME_VALUE);
				l3Slider.setValue(0); l3Slider.setValue(DEFAULT_HOME_VALUE);
				l4Slider.setValue(0); l4Slider.setValue(DEFAULT_HOME_VALUE);
			}
		});
		
		DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout("pref, 5dlu, pref, 5dlu, pref", "pref, 5dlu, pref, 5dlu, pref"));

		builder.add(upButton, cc.xy(3, 1)); 
		builder.add(downButton, cc.xy(3, 5)); 
		builder.add(leftButton, cc.xy(1, 3)); 
		builder.add(rightButton, cc.xy(5, 3)); 
		builder.add(homeButton, cc.xy(3, 3));
		
		JPanel panel = builder.getPanel();
		panel.setOpaque(false);
		return panel;	
	}

	private JPanel getCommandLinePanel(){
		textArea = new JTextArea(15, 0);
		textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		JRaisedButton open = new JRaisedButton("<HTML>OPEN</HTML>"); 
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = getFileChooser(JFileChooser.OPEN_DIALOG);
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					if (file.exists()) {
						try {
							BufferedReader bis = new BufferedReader(new FileReader(file));
							StringBuilder content = new StringBuilder("");
							String  s;
							while((s = bis.readLine()) != null){
								content.append(s + "\r\n");
							}
							textArea.setText(content.toString());
							textArea.grabFocus();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						try {
							file.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					RoboFrame.this.setTitle("Control Panel - " + file.getAbsolutePath());
				}
			}
		});
		
		JRaisedButton write = new JRaisedButton("<HTML>WRITE</HTML>"); 
		write.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (file == null){
					JFileChooser fc = getFileChooser(JFileChooser.SAVE_DIALOG);
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						file = fc.getSelectedFile();
						if (!file.exists()) {
							try {
								file.createNewFile();
								RoboFrame.this.setTitle("Control Panel - " + file.getAbsolutePath());
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}else return;
				}
					
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(textArea.getText());
					writer.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}finally{
					if (writer != null)
						try {
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				}
			}
		});
		
		JRaisedButton commit = new JRaisedButton("<HTML>COMMIT</HTML>");
		commit.onMouseClicked(new Runnable() {
			@Override
			public void run() {
				if(serialCom != null && textArea.getText().length() > 0) {
					String lines[] = textArea.getText().split("\r\n");
					for(int i = 0; i<lines.length; i++) {
						String commands[] = lines[i].split(" ");
						StringBuffer commandLine = new StringBuffer();
						for(int z = 0; z<commands.length; z++) {
							if(commands[z].contains(R2)){
								int pos = Integer.parseInt(commands[z].replace(R2,""));
								commands[z] = R2.concat(""+reverseAngle(pos));
							} else if(commands[z].contains(L3)){
								int pos = Integer.parseInt(commands[z].replace(L3,""));
								commands[z] = L3.concat(""+reverseAngle(pos));
							} else if(commands[z].contains(R4)){
								int pos = Integer.parseInt(commands[z].replace(R4,""));
								commands[z] = L4.concat(""+reverseAngle(pos));
							}
									
							if(z > 0){
								commandLine.append(" ");
							}
							commandLine.append(commands[z]);
						}
						try {
							serialCom.write(commandLine.toString());
							Thread.sleep(delay);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		JRaisedButton capture = new JRaisedButton("<HTML>CAPTURE</HTML>");
		capture.onMouseClicked(new Runnable() {
			@Override
			public void run() {
				capture();
			}
		});
		
		DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout("5dlu, fill:pref:grow, 3dlu, fill:pref:grow, 5dlu", 
				"5dlu, pref:grow, 3dlu, pref:grow, 3dlu, pref:grow, 3dlu, pref:grow"));

		builder.add(new JScrollPane(textArea), cc.xyw(2, 2, 3));
		builder.add(open, cc.xy(2, 4));
		builder.add(write, cc.xy(4, 4));
		builder.add(capture, cc.xy(2, 6));
		builder.add(commit, cc.xy(4, 6));

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setOpaque(false);
		return panel;
	}

	private JPanel getSetupPanel(){
		JTextField delayComponent = new JTextField(delay + "");
		delayComponent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try{
					Integer newValue = new Integer(((JTextField)arg0.getSource()).getText());
					delay = newValue.intValue();
					System.out.println("new delay => " + delay);
				}catch (NumberFormatException e) {
					arg0.consume();
				}
			}
		});
		JLabel delayLabel = new JLabel("<HTML>DELAY </HTML>");
		
		JComboBox portCombo = new JComboBox(Serial.getPortList());
		portCombo.setBackground(Color.WHITE);
		portCombo.setFont(new Font("Dialog", Font.PLAIN, 12));
		portCombo.setRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList arg0, Object arg1,
					int arg2, boolean arg3, boolean arg4) {
				if (arg1 == null) return new JLabel();
				CommPortIdentifier port = (CommPortIdentifier) arg1;
				JLabel label = new JLabel(" " + port.getName());
				label.setFont(new Font("Dialog", Font.PLAIN, 12));
				if (arg3) {
					label.setBackground(arg0.getSelectionBackground());
				}else {
					label.setBackground(Color.WHITE);
				}
				
				return label;
			}
		});
		
		portCombo.getModel().setSelectedItem(serialCom);
		portCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					CommPortIdentifier port = 
						(CommPortIdentifier) ((JComboBox)arg0.getSource()).getSelectedItem();
					if (serialCom != null)
						serialCom.switchPort(port);
					else
						serialCom = new Serial(port.getName());
				} catch (SerialException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		JLabel portLabel = new JLabel("<HTML>PORT </HTML>");
		
		JCheckBox checkBox = new JCheckBox("<HTML>AUTO CAPTURE</HTML>");
		checkBox.setOpaque(false);
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				autoCap = ((JCheckBox) arg0.getSource()).isSelected();
				System.out.println("auto capture property changed => " + autoCap);
			}
		});
		
		DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout("5dlu, fill:pref, 3dlu, fill:pref:grow, " +
						"5dlu, fill:pref, 3dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu", 
				"5dlu, pref:grow, 5dlu"));

		builder.add(delayLabel, cc.xy(2, 2));
		builder.add(delayComponent, cc.xy(4, 2));
		builder.add(portLabel, cc.xy(6, 2));
		builder.add(portCombo, cc.xy(8, 2));
		builder.add(checkBox, cc.xy(10, 2));
		
		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setOpaque(false);
		return panel;
	}
	
	private JPanel getCamPanel(){
		DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout("pref:grow", 
				"fill:160dlu:grow"));
		
		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setOpaque(false);
		return panel;
	}
	
	private void capture() {
		StringBuilder sb = new StringBuilder();
		if (textArea.getText().length() > 0)
			sb.append("\r\n");
		sb.append(R1).append(r1Slider.getValue()).append(" ");
		sb.append(R2).append(r2Slider.getValue()).append(" ");
		sb.append(R3).append(r3Slider.getValue()).append(" ");
		sb.append(R4).append(r4Slider.getValue()).append(" ");				
		sb.append(L1).append(l1Slider.getValue()).append(" ");
		sb.append(L2).append(l2Slider.getValue()).append(" ");
		sb.append(L3).append(l3Slider.getValue()).append(" ");
		sb.append(L4).append(l4Slider.getValue()).append(" ");
		textArea.append(sb.toString());
		textArea.grabFocus();
			
	}

	private int reverseAngle(int angle) {
		int pointZero = (ROT_MIN + ROT_MAX) / 2;

		if (angle > pointZero) {
			return pointZero - (angle - pointZero);
		} else {
			return pointZero + (pointZero - angle);
		}
	}
	
	private JFileChooser getFileChooser(int dialogType){
	
		JFileChooser fc = new JFileChooser();
		fc.setDialogType(dialogType);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return ".csv";
			}
			
			@Override
			public boolean accept(File f) {
				if (f != null) {
					if (f.isDirectory()) {
						return true;
					}
					String extension = getExtension(f);
					if (extension != null && extension.equals("csv")) {
						return true;
					}
					;
				}
				return false;
			}
			
			public String getExtension(File f) {
				if (f != null) {
					String filename = f.getName();
					int i = filename.lastIndexOf('.');
					if (i > 0 && i < filename.length() - 1) {
						return filename.substring(i + 1).toLowerCase();
					}
				}
				return null;
			}
		});
		fc.setAcceptAllFileFilterUsed(false);
		
		return fc;
	}
	
	private ImageIcon getImageIcon(String filePath){
		URL imageUrl = Thread.currentThread().getContextClassLoader().getResource(filePath);
		return new ImageIcon(imageUrl);
	}
	
	public static void main(String[] args) {
		new RoboFrame();
	}

	class ServoChangeLister implements ChangeListener{

		private String servo;
		
		private boolean reverseAngle = false;
		
		/**
		 * Constructor
		 *
		 */
		public ServoChangeLister(String servo) {
			this.servo = servo;
			reverseAngle = servo == R2 || servo == L3 || servo == L4;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			try {
				if (autoCap)
					capture();
				
				int value = ((JSlider)e.getSource()).getValue();
				if (reverseAngle)
					value = reverseAngle(value);
				
				String toSerial = servo + value;

				System.out.println(toSerial);
				
				if (serialCom != null){
					serialCom.write(toSerial);
				}else {
					System.out.println("Serial is not instantiated");
				}
			} catch (SerialException e1) {
				e1.printStackTrace();
			}
		}
	}
}

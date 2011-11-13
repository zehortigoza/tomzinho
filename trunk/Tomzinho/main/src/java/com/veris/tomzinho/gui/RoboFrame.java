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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
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
public class RoboFrame extends JFrame {

	/* Default serial version id */
	private static final long serialVersionUID = 1L;

	private static final int ROT_MIN = 0;
	private static final int ROT_MAX = 180;

	private final CellConstraints cc = new CellConstraints();
	private final FormLayout motorFormLayout = new FormLayout("5dlu, pref",
			"25dlu, pref, 35dlu, pref, 60dlu, pref, 50dlu, pref, 5dlu");

	private JTextArea textArea;
	private JSlider r1Slider;
	private JSlider r2Slider;
	private JSlider r3Slider;
	private JSlider r4Slider;
	private JSlider l1Slider;
	private JSlider l2Slider;
	private JSlider l3Slider;
	private JSlider l4Slider;

	private Serial SerialCom;

	/**
	 * Constructor
	 * 
	 */
	public RoboFrame() {
		super("Control Panel");

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setSize(new Dimension(900, 680));
		this.setLocationRelativeTo(null); // centraliza

		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // maximiza
		this.setExtendedState(JFrame.ICONIFIED | this.getExtendedState());

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"5dlu, pref, 5dlu, fill:pref:grow, 5dlu", "5dlu, top:pref"));

		builder.add(getControlPanel(), cc.xy(2, 2));

		{
			DefaultFormBuilder leftPanelBuilder = new DefaultFormBuilder(
					new FormLayout("fill:pref:grow",
							"fill:pref:grow, 5dlu, pref"));

			leftPanelBuilder.add(getCamPanel(), cc.xy(1, 1));
			leftPanelBuilder.add(getCommandLinePanel(), cc.xy(1, 3));

			JPanel leftPanel = leftPanelBuilder.getPanel();
			leftPanel.setOpaque(false);
			builder.add(leftPanel, cc.xy(4, 2));
		}

		JPanel mainPanel = builder.getPanel();
		mainPanel.setBackground(Color.WHITE);

		this.getContentPane().add(new JScrollPane(mainPanel));
		this.setVisible(true);

		try {
			SerialCom = new Serial();
		} catch (SerialException e) {
			e.printStackTrace();
		}

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				try {
					SerialCom.close();
				} catch (SerialException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private JPanel getControlPanel() {
		DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout(
						"5dlu, right:pref:grow, 0dlu, center:pref, 0dlu, left:pref:grow, 5dlu",
						"top:pref, 5dlu, top:pref, 5dlu"));

		builder.add(getLeftPanel(), cc.xy(2, 1));
		builder.add(getImageLabel(), cc.xy(4, 1));
		builder.add(getRightPanel(), cc.xy(6, 1));

		builder.add(getDirectionPanel(), cc.xy(4, 3));

		JPanel controlPanel = builder.getPanel();
		controlPanel.setOpaque(false);
		controlPanel.setBorder(BorderFactory.createTitledBorder(""));

		return controlPanel;
	}

	private JPanel getImageLabel() {
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

	private JPanel getRightPanel() {
		r1Slider = getSlider(110);
		r2Slider = getSlider();
		r3Slider = getSlider();
		r4Slider = getSlider();

		DefaultFormBuilder builder = new DefaultFormBuilder(motorFormLayout);

		builder.add(r1Slider, cc.xy(2, 2));
		builder.add(r2Slider, cc.xy(2, 4));
		builder.add(r3Slider, cc.xy(2, 6));
		builder.add(r4Slider, cc.xy(2, 8));

		JPanel panel = builder.getPanel();
		panel.setOpaque(false);

		return panel;
	}

	private JPanel getLeftPanel() {
		l1Slider = getSlider(100);
		l2Slider = getSlider();
		l3Slider = getSlider();
		l4Slider = getSlider();

		DefaultFormBuilder builder = new DefaultFormBuilder(motorFormLayout);

		builder.add(l1Slider, cc.xy(2, 2));
		builder.add(l2Slider, cc.xy(2, 4));
		builder.add(l3Slider, cc.xy(2, 6));
		builder.add(l4Slider, cc.xy(2, 8));

		JPanel panel = builder.getPanel();
		panel.setOpaque(false);
		return panel;
	}

	private JSlider getSlider() {
		return getSlider(((ROT_MIN + ROT_MAX) / 2) + ROT_MIN);
	}

	private JSlider getSlider(int pos) {
		JSlider slider = new JSlider(ROT_MIN, ROT_MAX);
		slider.setOpaque(false);
		// Turn on labels at major tick marks.
		slider.setMajorTickSpacing(45);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(pos);

		return slider;
	}

	private JPanel getDirectionPanel() {

		JRaisedButton upButton = new JRaisedButton(
				getImageIcon("images/1uparrow.png"));
		JRaisedButton downButton = new JRaisedButton(
				getImageIcon("images/1downarrow.png"));
		JRaisedButton leftButton = new JRaisedButton(
				getImageIcon("images/1leftarrow.png"));
		JRaisedButton rightButton = new JRaisedButton(
				getImageIcon("images/1rightarrow.png"));

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"pref, 3dlu, pref, 3dlu, pref", "pref, 3dlu, pref, 3dlu, pref"));

		builder.add(upButton, cc.xy(3, 1));
		builder.add(downButton, cc.xy(3, 5));
		builder.add(leftButton, cc.xy(1, 3));
		builder.add(rightButton, cc.xy(5, 3));

		JPanel panel = builder.getPanel();
		panel.setOpaque(false);
		return panel;
	}

	private int reverseAngle(int angle) {
		int pointZero = (ROT_MIN + ROT_MAX) / 2;

		if (angle > pointZero) {
			return pointZero - (angle - pointZero);
		} else {
			return pointZero + (pointZero - angle);
		}
	}

	private JPanel getCommandLinePanel() {
		textArea = new JTextArea(15, 0);
		textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		JRaisedButton open = new JRaisedButton("<HTML>OPEN</HTML>");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogType(JFileChooser.OPEN_DIALOG);
				fc.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public boolean accept(File f) {
						if (f != null) {
							if (f.isDirectory()) {
								return true;
							}
							String extension = getExtension(f);
							if (extension != null && extension.equals("txt")) {
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
				File file;

				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					if (file.exists()) {
						try {
							BufferedReader bis = new BufferedReader(
									new FileReader(file));
							StringBuilder content = new StringBuilder("");
							String s;
							while ((s = bis.readLine()) != null) {
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

					}
				}
			}
		});

		JRaisedButton write = new JRaisedButton("<HTML>WRITE</HTML>");
		JRaisedButton commit = new JRaisedButton("<HTML>COMMIT</HTML>");
		commit.onMouseClicked(new Runnable() {
			@Override
			public void run() {
				StringBuffer sb = new StringBuffer();
				sb.append("r1" + r1Slider.getValue());
				sb.append(" r2" + reverseAngle(r2Slider.getValue()));
				sb.append(" r3" + r3Slider.getValue());
				sb.append(" r4" + r4Slider.getValue());
				sb.append(" l1" + l1Slider.getValue());
				sb.append(" l2" + l2Slider.getValue());
				sb.append(" l3" + reverseAngle(l3Slider.getValue()));
				sb.append(" l4" + reverseAngle(l4Slider.getValue()));
				try {
					SerialCom.write(sb.toString());
				} catch (SerialException e) {
					e.printStackTrace();
				}
			}
		});

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"5dlu, pref:grow, 3dlu, pref:grow, 5dlu",
				"5dlu, pref:grow, 3dlu, pref:grow, 3dlu, pref:grow"));

		builder.add(textArea, cc.xyw(2, 2, 3));
		builder.add(open, cc.xy(2, 4));
		builder.add(write, cc.xy(4, 4));
		builder.add(commit, cc.xy(4, 6));

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setOpaque(false);
		return panel;
	}

	private JPanel getCamPanel() {
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"pref:grow", "fill:188dlu:grow"));

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setOpaque(false);
		return panel;
	}

	private ImageIcon getImageIcon(String filePath) {
		URL imageUrl = Thread.currentThread().getContextClassLoader()
				.getResource(filePath);
		return new ImageIcon(imageUrl);
	}

	public static void main(String[] args) {
		new RoboFrame();
	}

}

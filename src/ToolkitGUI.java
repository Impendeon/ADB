import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * Class to draw a GUI for running ADB and Fastboot for a connected Android Device
 * Commands using the Command class and the CMD class
 * Uses a single JFrame, and then a second optional JFrame for further options
 */
public class ToolkitGUI {
	//NEXT STEP COPY THE ADB FILES TO THE USER DIRECTORY FROM RES
	private static JFrame frame;
	private static JFrame advancedFrame;
	private JButton flashSystem;
	private JButton rebootBootloader;
	private JButton rebootRecovery;
	private JButton flashData;
	private JButton flashRecovery;
	private JTextArea logArea;
    private JScrollPane logScroller;
    private JButton moreSettings;
    private JButton wipe;
    private JButton pushAPK;
    private JButton pushFile;
    private JButton reinstallADB;
    private JButton fullBackup;
    private JButton connectedDevices;
    private JButton connectedDevices2;
    private final JFileChooser fc = new JFileChooser();
    private ActionListener filechooser = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			int returnval = fc.showOpenDialog(advancedFrame);
			if (returnval == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				String name = file.getPath();
				if(e.getSource().equals(flashSystem)){
					Command cm = new Command("fastboot flash system " + name);
					CMD.commandWindowsADB(cm);
					logArea.append(cm.getOutput());
					
				}
				else if (e.getSource().equals(flashRecovery)){
					Command cm = new Command("fastboot flash recovery " + name);
					CMD.commandWindowsADB(cm);
					logArea.append(cm.getOutput());
				}
				else if (e.getSource().equals(flashData)){
					Command cm = new Command("fastboot flash data " + name);
					CMD.commandWindowsADB(cm);
					logArea.append(cm.getOutput());
				}
				else if (e.getSource().equals(pushAPK)){
					Command cm = new Command("adb install " + name);
					CMD.commandWindowsADB(cm);
					logArea.append(cm.getOutput());
				}
				else if (e.getSource().equals(pushFile)){
					Command cm = new Command("adb push " + name + " /sdcard/PushedFiles");
					CMD.commandWindowsADB(cm);
					logArea.append(cm.getOutput());
				}
			}
		}
	};
//    private JComboBox backColor;
//    private JComboBox txtColor;
    	
	public void components(){
		rebootBootloader = new JButton("Reboot Bootloader");
		rebootBootloader.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Command cm = new Command("reboot bootloader");
				CMD.commandWindowsADB(cm);
				logArea.append(cm.getOutput());
			}
		});
		rebootRecovery = new JButton("Reboot Recovery");
		rebootRecovery.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Command cm = new Command("reboot recovery");
				CMD.commandWindowsADB(cm);
				logArea.append(cm.getOutput());
			}
		});
		logArea = new JTextArea(10,25);
		logArea.setBackground(Color.BLACK);
		logArea.setForeground(Color.GREEN);
		logScroller = new JScrollPane();
		logScroller.setBorder(BorderFactory.createTitledBorder("Log"));
		logScroller.setViewportView(logArea);
		
		flashSystem = new JButton("Flash System");
		flashSystem.addActionListener(filechooser);
		flashData = new JButton("Flash Data");
		flashData.addActionListener(filechooser);
		flashRecovery = new JButton("Flash Recovery");
		flashRecovery.addActionListener(filechooser);
		moreSettings = new JButton("Advanced Options");
		moreSettings.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawSecondFrame();
			}
		});
		JPanel flashButtons = new JPanel();
		flashButtons.add(rebootBootloader);
		flashButtons.add(rebootRecovery);
		flashButtons.add(flashSystem);
		flashButtons.add(flashData);
		flashButtons.add(flashRecovery);
		flashButtons.add(moreSettings);
		frame.add(flashButtons, BorderLayout.LINE_START);
		frame.add(logScroller, BorderLayout.PAGE_END);
		
	}

	
	public void draw(){
		frame = new JFrame("ADB Toolkit");
		frame.setSize(700, 280);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		components();
		frame.setVisible(true);
	}
	public void drawSecondFrame(){
		advancedFrame = new JFrame("Advanced Options");
		advancedFrame.setSize(325, 250);
		advancedFrame.setResizable(false);
		componentsSecondFrame();
		advancedFrame.setVisible(true);
	}
	public void componentsSecondFrame(){
		wipe = new JButton("Factory Reset");
		wipe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ArrayList<Command>cm = new ArrayList<>();
				Command c = new Command("fastboot wipe data");
				Command c2 = new Command("fastboot wipe cache");
				cm.add(c);
				cm.add(c2);
				CMD.commandWindowsADB(cm);
				logArea.append(CMD.getOutput(cm));
			}
		});
		pushAPK = new JButton("Install App");
		pushAPK.addActionListener(filechooser);
		pushFile = new JButton("Push File");
		pushFile.addActionListener(filechooser);
		reinstallADB = new JButton("Reinstall Files");
		reinstallADB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					CMD.installFiles();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		fullBackup = new JButton("Full Backup");
		fullBackup.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Command cm = new Command("backup -all" + CMD.getDrive() +
						"\\Users\\" + CMD.getUser() + "\\documents\\ADB\\backups\\backup.ab");
				CMD.commandWindowsADB(cm);
				logArea.append(cm.getOutput());
			}
		});
		connectedDevices = new JButton("Check ADB Connected Devices");
		connectedDevices.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Command cm = new Command("devices");
				CMD.commandWindowsADB(cm);
				logArea.append(cm.getOutput());
			}
		});
		connectedDevices2 = new JButton("Check Fastboot Connected Devices");
		connectedDevices2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Command cm = new Command("fastboot devices");
				CMD.commandWindowsADB(cm);
				logArea.append(cm.getOutput());
			}
		});
//		String[] backColors = {"Black" , "Green" , "Orange" , "Cyan"}; //add more
//		backColor = new JComboBox(backColors);
//		backColor.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				if(backColor.getSelectedItem().equals("Black")) logArea.setBackground(Color.BLACK);
//				if(backColor.getSelectedItem().equals("Green")) logArea.setBackground(Color.GREEN);
//				if(backColor.getSelectedItem().equals("Orange")) logArea.setBackground(Color.ORANGE);
//				if(backColor.getSelectedItem().equals("Cyan")) logArea.setBackground(Color.CYAN);
//				System.out.println(backColor.getActionCommand());
//				frame.repaint();
//			}
//		});
		JPanel miscButtons = new JPanel();
		miscButtons.setLayout(new BoxLayout(miscButtons, BoxLayout.PAGE_AXIS));
		miscButtons.add(wipe);
		miscButtons.add(pushAPK);
		miscButtons.add(pushFile);
		miscButtons.add(reinstallADB);
		miscButtons.add(connectedDevices);
		miscButtons.add(connectedDevices2);
		advancedFrame.add(miscButtons, BorderLayout.NORTH);
		
	}
	public static void main(String[] args){	
		//CMD.installFiles();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		ToolkitGUI g = new ToolkitGUI();
		g.draw();
	}
}

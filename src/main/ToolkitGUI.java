package main;

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
import java.util.HashMap;
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

import commands.ADB;

public class ToolkitGUI {
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
	private JButton fullBackup;
	private JButton connectedDevices;
	private JButton connectedDevices2;
	private HashMap<JButton, Source> actionMap;

	enum Source {
		FLASH_DATA, FLASH_RECOVERY, FLASH_SYSTEM, PUSH_APK, PUSH_FILE;
	}

	private final JFileChooser fc = new JFileChooser();
	private ActionListener filechooser = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int returnval = fc.showOpenDialog(advancedFrame);
			if (returnval == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				Source source = actionMap.get(e.getSource());

				switch (source) {
				case FLASH_SYSTEM:
					logArea.append(ADB.flash("system", file));
					break;

				case FLASH_DATA:
					logArea.append(ADB.flash("data", file));
					break;

				case FLASH_RECOVERY:
					logArea.append(ADB.flash("recovery", file));
					break;

				case PUSH_APK:
					logArea.append(ADB.install(file));
					break;

				case PUSH_FILE:
					logArea.append(ADB.push(file));
					break;
				}

			}
		}
	};

	public void components() {
		rebootBootloader = new JButton("Reboot Bootloader");
		rebootBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.append(ADB.pwd());
			}
		});
		rebootRecovery = new JButton("Reboot Recovery");
		rebootRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.append(ADB.pwd());
			}
		});
		logArea = new JTextArea(10, 25);
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
		moreSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

		actionMap = new HashMap<>();
		actionMap.put(flashSystem, Source.FLASH_SYSTEM);
		actionMap.put(flashData, Source.FLASH_DATA);
		actionMap.put(flashRecovery, Source.FLASH_RECOVERY);
		actionMap.put(pushAPK, Source.PUSH_APK);
		actionMap.put(pushFile, Source.PUSH_FILE);

	}

	public void draw() {
		frame = new JFrame("ADB Toolkit");
		frame.setSize(880, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		components();
		frame.setVisible(true);
	}

	public void drawSecondFrame() {
		advancedFrame = new JFrame("Advanced Options");
		advancedFrame.setSize(325, 250);
		advancedFrame.setResizable(false);
		componentsSecondFrame();
		advancedFrame.setVisible(true);
	}

	public void componentsSecondFrame() {
		wipe = new JButton("Factory Reset");
		wipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.append(ADB.wipeData());
				logArea.append(ADB.wipeCache());
			}
		});
		pushAPK = new JButton("Install App");
		pushAPK.addActionListener(filechooser);
		pushFile = new JButton("Push File");
		pushFile.addActionListener(filechooser);
		fullBackup = new JButton("Full Backup");
		fullBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Command library should direcyl return output to append, should have command
				// "BACKUPALL() that returns output"
				// Command cm = new Command("backup -all" + CMD.getDrive() +
				// "\\Users\\" + CMD.getUser() + "\\documents\\ADB\\backups\\backup.ab");
				// CMD.commandWindowsADB(cm);
				logArea.append("TEST");
			}
		});
		connectedDevices = new JButton("Check ADB Connected Devices");
		connectedDevices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.append(ADB.checkADBDevices());
			}
		});
		connectedDevices2 = new JButton("Check Fastboot Connected Devices");
		connectedDevices2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.append(ADB.checkFastbootDevices());
			}
		});
		// String[] backColors = {"Black" , "Green" , "Orange" , "Cyan"}; //add more
		// backColor = new JComboBox(backColors);
		// backColor.addActionListener(new ActionListener(){
		// public void actionPerformed(ActionEvent e){
		// if(backColor.getSelectedItem().equals("Black"))
		// logArea.setBackground(Color.BLACK);
		// if(backColor.getSelectedItem().equals("Green"))
		// logArea.setBackground(Color.GREEN);
		// if(backColor.getSelectedItem().equals("Orange"))
		// logArea.setBackground(Color.ORANGE);
		// if(backColor.getSelectedItem().equals("Cyan"))
		// logArea.setBackground(Color.CYAN);
		// System.out.println(backColor.getActionCommand());
		// frame.repaint();
		// }
		// });
		JPanel miscButtons = new JPanel();
		miscButtons.setLayout(new BoxLayout(miscButtons, BoxLayout.PAGE_AXIS));
		miscButtons.add(wipe);
		miscButtons.add(pushAPK);
		miscButtons.add(pushFile);
		miscButtons.add(connectedDevices);
		miscButtons.add(connectedDevices2);
		advancedFrame.add(miscButtons, BorderLayout.NORTH);

	}

	public static void main(String[] args) {
		// CMD.installFiles();
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

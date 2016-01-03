package com.ocr.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ocr.core.Line;
import com.ocr.core.Scanner;
import com.ocr.ui.components.CommandButton;
import com.ocr.util.GlobalConstants;
import com.ocr.util.ScanUtils;


public class UIContainer {

	
	private JFrame mainFrame;
	
	public UIContainer() {
		
		ScanListener listener = new ScanListener();
		CommandButton scan = new CommandButton( GlobalConstants.SCAN_ACTION, listener );
		
		GridBagConstraints toolbarGridCons = new GridBagConstraints();
		toolbarGridCons.gridx = 0;
		toolbarGridCons.gridy = 0;
		toolbarGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints textGridCons = new GridBagConstraints();
		textGridCons.gridx = 0;
		textGridCons.gridy = 1;
		textGridCons.anchor = GridBagConstraints.LINE_START;
		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( new GridBagLayout() );
		mainPanel.add( scan, toolbarGridCons );
		//mainPanel.add( jTextPane, textGridCons );
		mainPanel.setOpaque(true);
		JScrollPane mainScrollPane = new JScrollPane(mainPanel);
		
		
		// frame for holding everything
		mainFrame = new JFrame( GlobalConstants.TITLE );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( GlobalConstants.PREF_FRAME_WIDTH, GlobalConstants.PREF_FRAME_HEIGHT ) );
		mainFrame.setMinimumSize( new Dimension( GlobalConstants.MIN_FRAME_WIDTH, GlobalConstants.MIN_FRAME_HEIGHT ) );
		//mainFrame.setJMenuBar(menuBar);
		mainFrame.add( mainScrollPane );
		mainFrame.pack();
		//textPane.requestFocusInWindow(); // has to be called after pack() and before setVisible()
		mainFrame.setVisible(true);
	}
	
	
	
	
	private void scan() {
		String imgFile = "img/sampleImg9.png";
		String mapFile = "files/eng15.map";
		String outputFile = "output/output1.txt";
		Scanner scn = new Scanner();
		BufferedImage image = ScanUtils.loadImage( imgFile );
		ArrayList<Line> lines = scn.init( image );
		StringBuilder sb = scn.readCharacters( lines, mapFile ); 
		System.out.println(sb);
		ScanUtils.writeToFile( sb, outputFile );
	}
	
	
	
	
	
	class ScanListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case GlobalConstants.SCAN_ACTION:
					scan();
					break;
				
			}
		}

	}
	
	
	
	
}

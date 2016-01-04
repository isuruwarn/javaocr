package com.ocr.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.ocr.core.Char;
import com.ocr.core.Scanner;
import com.ocr.util.GlobalConstants;
import com.ocr.util.ScanUtils;




/**
 * 
 * 
 * @author isuru
 *
 */
public class UIContainer {

	
	private Scanner scn;
	private BufferedImage inputImage;
	
	private JFrame mainFrame;
	private JButton fileBtn;
	private JButton scanBtn;
	private JButton copyBtn;
	private JButton clearBtn;
	private JButton resolveBtn;
	private JFileChooser jfc;
	private JTextComponent textpPane;
	private JTextField txtInputImage;
	//private JTextField txtOutputFileName;
	private JComboBox<String> selectDialect;
	
	
	
	public UIContainer() {
		
		ScanMainListener mainListener = new ScanMainListener();
		
		fileBtn = new JButton( GlobalConstants.CHOOSE_FILE_ACTION );
		fileBtn.addActionListener(mainListener);
		fileBtn.setEnabled(true);
		
		txtInputImage = new JTextField();
		txtInputImage.setPreferredSize( new Dimension(300, 25) );
		txtInputImage.setMinimumSize( new Dimension(300, 25) );
		txtInputImage.setText( GlobalConstants.SAMPLE_IMG_FILENAME);
		
		//txtOutputFileName = new JTextField();
		//txtOutputFileName.setText( GlobalConstants.SAMPLE_OUTPUT_FILENAME);
		
		String [] dialects = { GlobalConstants.ENGLISH, GlobalConstants.SINHALA };
		selectDialect = new JComboBox<String>(dialects);
		selectDialect.setSelectedItem( String.valueOf( GlobalConstants.ENGLISH ) );
		selectDialect.addActionListener( mainListener );
		
		scanBtn = new JButton( GlobalConstants.SCAN_ACTION );
		scanBtn.addActionListener(mainListener);
		
		copyBtn = new JButton( GlobalConstants.COPY_ACTION );
		copyBtn.addActionListener(mainListener);
		
		clearBtn = new JButton( GlobalConstants.CLEAR_ACTION );
		clearBtn.addActionListener(mainListener);
		
		resolveBtn = new JButton( GlobalConstants.RESOLVE_ACTION );
		resolveBtn.addActionListener(mainListener);
		resolveBtn.setEnabled(false);
		
		JPanel btnToolBar = new JPanel();
		btnToolBar.add(scanBtn);
		btnToolBar.add(copyBtn);
		btnToolBar.add(clearBtn);
		btnToolBar.add(resolveBtn);
		
		// layout manager configurations
		GridBagConstraints fileBtnGridCons = new GridBagConstraints();
		fileBtnGridCons.gridx = 0;
		fileBtnGridCons.gridy = 0;
		fileBtnGridCons.insets = new Insets(30,10,10,10);
		fileBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints txtInputImageGridCons = new GridBagConstraints();
		txtInputImageGridCons.gridx = 1;
		txtInputImageGridCons.gridy = 0;
		txtInputImageGridCons.insets = new Insets(30,0,10,10);
		
		GridBagConstraints selectDialectGridCons = new GridBagConstraints();
		selectDialectGridCons.gridx = 2;
		selectDialectGridCons.gridy = 0;
		selectDialectGridCons.insets = new Insets(30,10,10,10);
		selectDialectGridCons.anchor = GridBagConstraints.LINE_END;
		
		GridBagConstraints btnToolBarGridCons = new GridBagConstraints();
		btnToolBarGridCons.gridx = 0;
		btnToolBarGridCons.gridy = 1;
		btnToolBarGridCons.gridwidth = 3;
		btnToolBarGridCons.insets = new Insets(0,10,10,10);
		btnToolBarGridCons.anchor = GridBagConstraints.CENTER;
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridBagLayout() );
		buttonPanel.add(fileBtn, fileBtnGridCons);
		buttonPanel.add(txtInputImage, txtInputImageGridCons);
		buttonPanel.add(selectDialect, selectDialectGridCons);
		buttonPanel.add(btnToolBar, btnToolBarGridCons);
		
        textpPane = new JTextArea();
		JScrollPane mainScrollPane = new JScrollPane(textpPane);
		
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(BorderLayout.NORTH, buttonPanel);
        mainPanel.add(BorderLayout.CENTER, mainScrollPane);
        
		// frame for holding everything
		mainFrame = new JFrame( GlobalConstants.TITLE );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( GlobalConstants.PREF_FRAME_WIDTH, GlobalConstants.PREF_FRAME_HEIGHT ) );
		mainFrame.setMinimumSize( new Dimension( GlobalConstants.MIN_FRAME_WIDTH, GlobalConstants.MIN_FRAME_HEIGHT ) );
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		jfc = new JFileChooser();
	}
	
	
	
	class ScanMainListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case GlobalConstants.CHOOSE_FILE_ACTION:
					int returnVal = jfc.showOpenDialog(null);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			        	File file = jfc.getSelectedFile();
			        	String filePath = file.getPath();
			        	txtInputImage.setText( filePath ); 
			        }
					break;
					
				case GlobalConstants.SCAN_ACTION:
					scan();
					resolveBtn.setEnabled(true);
					break;
					
				case GlobalConstants.COPY_ACTION:
					textpPane.selectAll();
					textpPane.copy();
					break;
					
				case GlobalConstants.CLEAR_ACTION:
					textpPane.setText("");
					resolveBtn.setEnabled(false);
					break;
				
				case GlobalConstants.RESOLVE_ACTION:
					resolve();
					break;
			}
		}

	}
	
	
	private void scan() {
		
		//String imgFile = "img/sampleImg9.png";
		//String mapFile = "files/eng15.map";
		//String outputFile = "output/output1.txt";
		
		String imgFile = txtInputImage.getText();
		String mapFile = GlobalConstants.ENG_MAP_FILE;
		if( selectDialect.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			mapFile = GlobalConstants.SIN_MAP_FILE;
		}
		
		inputImage = ScanUtils.loadImage( imgFile );
		//ArrayList<Line> lines = scn.init( inputImage );
		//StringBuilder sb = scn.readCharacters( lines, mapFile );
		scn = new Scanner();
		StringBuilder sb = scn.readCharacters( inputImage, mapFile );
		textpPane.setText( sb.toString() );
		
		/*
		String outputFile = GlobalConstants.SAMPLE_OUTPUT_FILENAME;
		ScanUtils.writeToFile( sb, outputFile );
		File file = new File( outputFile );
		try {
            FileReader fr = new FileReader( file.toString() );
            textpane.read(fr, null);
            fr.close();
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
		*/
	}
	
	
	
	private int navIndex = 0;
	private JLabel charImgLbl;
	private JTextField charMappingTxt;
	private ArrayList<Char> unrecognizedChars;
	private String [] charMappings;
	
	private void resolve() {
		
		unrecognizedChars = scn.getUnrecognizedChars();
		charMappings = new String[ unrecognizedChars.size() ];
		
		ImageIcon charImg = getImageIcon();
		charImgLbl = new JLabel(charImg);
		charImgLbl.setPreferredSize( new Dimension( 100, 100 ) );
		charImgLbl.setMinimumSize( new Dimension( 100, 100 ) );
		
		charMappingTxt = new JTextField();
		charMappingTxt.setEditable(false);
		charMappingTxt.setFont( new Font("SansSerif", Font.BOLD, 20) );
		charMappingTxt.setPreferredSize( new Dimension( 80, 80 ) );
		charMappingTxt.setMinimumSize( new Dimension( 80, 80 ) );
		
		JPanel imgPanel = new JPanel();
		imgPanel.add(charImgLbl);
		imgPanel.add(charMappingTxt);
		
		JButton prevBtn = new JButton( GlobalConstants.PREV_MAPPING_ACTION);
		JButton nextBtn = new JButton( GlobalConstants.NEXT_MAPPING_ACTION);
		JButton saveBtn = new JButton( GlobalConstants.SAVE_MAPPING_ACTION);
		JButton saveAllBtn = new JButton( GlobalConstants.SAVEALL_MAPPING_ACTION);
		
		ScanResolveListener resolveListener = new ScanResolveListener();
		prevBtn.addActionListener(resolveListener);
		nextBtn.addActionListener(resolveListener);
		saveBtn.addActionListener(resolveListener);
		saveAllBtn.addActionListener(resolveListener);
		
		JPanel navPanel = new JPanel();
		navPanel.add(prevBtn);
		navPanel.add(nextBtn);
		navPanel.add(saveBtn);
		navPanel.add(saveAllBtn);
		
		JPanel resolvePanel = new JPanel();
		resolvePanel.setPreferredSize( new Dimension(300, 300) );
		resolvePanel.setMinimumSize( new Dimension(300, 300) );
		
		resolvePanel.add(imgPanel);
		resolvePanel.add(navPanel);
		
		JOptionPane.showConfirmDialog( mainFrame, resolvePanel, GlobalConstants.RESOLVE_TITLE, JOptionPane.OK_CANCEL_OPTION );
	}
	
	
	


	class ScanResolveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String command = e.getActionCommand();
			
			switch(command) {
			
			case GlobalConstants.PREV_MAPPING_ACTION:
				if( navIndex > 0 ) navIndex--;
				ImageIcon prevCharImg = getImageIcon();
				charImgLbl.setIcon(prevCharImg);
				String s1 = charMappings[navIndex];
				charMappingTxt.setText(s1);
				break;
			
			case GlobalConstants.NEXT_MAPPING_ACTION:
				if( navIndex < unrecognizedChars.size() ) navIndex++;
				ImageIcon nextCharImg = getImageIcon();
				charImgLbl.setIcon(nextCharImg);
				String s2 = charMappings[navIndex];
				charMappingTxt.setText(s2);
				break;
				
			case GlobalConstants.SAVE_MAPPING_ACTION:
				save();
				break;
			
			case GlobalConstants.SAVEALL_MAPPING_ACTION:
				saveAll();
				break;
			}
		}
		
	}
	
	
	
	private ImageIcon getImageIcon() {
		Char c = unrecognizedChars.get( navIndex );
		BufferedImage charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
		ImageIcon thumbnailIcon = new ImageIcon(charImg);
		return thumbnailIcon;
	}
	
	
	
	private void save() {
		
	}
	
	
	private void saveAll() {
		
	}
	
	
}

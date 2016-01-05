package com.ocr.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.BorderFactory;
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
import com.ocr.util.Symbol;




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
	private JLabel statusLbl;
	private JTextComponent textpPane;
	private JTextField txtInputImage;
	//private JTextField txtOutputFileName;
	private JComboBox<String> selectAlphabet;
	
	
	
	/**
	 * Constructor
	 */
	public UIContainer() {
		
		MainOCRListener mainListener = new MainOCRListener();
		
		fileBtn = new JButton( GlobalConstants.CHOOSE_FILE_ACTION );
		fileBtn.addActionListener(mainListener);
		fileBtn.setEnabled(true);
		
		txtInputImage = new JTextField();
		txtInputImage.setPreferredSize( new Dimension(300, 25) );
		txtInputImage.setMinimumSize( new Dimension(300, 25) );
		txtInputImage.setText( GlobalConstants.SAMPLE_IMG_FILENAME);
		
		//txtOutputFileName = new JTextField();
		//txtOutputFileName.setText( GlobalConstants.SAMPLE_OUTPUT_FILENAME);
		
		String [] alphabets = { GlobalConstants.ENGLISH, GlobalConstants.SINHALA };
		selectAlphabet = new JComboBox<String>(alphabets);
		selectAlphabet.setSelectedItem( String.valueOf( GlobalConstants.ENGLISH ) );
		selectAlphabet.addActionListener( mainListener );
		
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
		
		GridBagConstraints statusLblGridCons = new GridBagConstraints();
		statusLblGridCons.gridx = 0;
		statusLblGridCons.gridy = 2;
		statusLblGridCons.gridwidth = 3;
		statusLblGridCons.insets = new Insets(0,0,0,0);
		statusLblGridCons.anchor = GridBagConstraints.CENTER;
		
		
		statusLbl = new JLabel();
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridBagLayout() );
		buttonPanel.add(fileBtn, fileBtnGridCons);
		buttonPanel.add(txtInputImage, txtInputImageGridCons);
		buttonPanel.add(selectAlphabet, selectDialectGridCons);
		buttonPanel.add(btnToolBar, btnToolBarGridCons);
		buttonPanel.add(statusLbl, statusLblGridCons);
		
		// main text area
        textpPane = new JTextArea();
        textpPane.setEditable(false);
        textpPane.setFont( new Font("SansSerif", Font.PLAIN, 12) );
		JScrollPane mainScrollPane = new JScrollPane(textpPane);
		//textpPane.setPreferredSize( new Dimension(500, 300) );
		//textpPane.setMinimumSize( new Dimension(500, 300) );
		
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
	
	
	
	class MainOCRListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case GlobalConstants.CHOOSE_FILE_ACTION:
					chooseFile();
					break;
					
				case GlobalConstants.SCAN_ACTION:
					scan();
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
	
	
	
	// ******************************
	// start MainOCRListener methods 
	// ******************************
	
	
	private void chooseFile() {
		int returnVal = jfc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = jfc.getSelectedFile();
        	String filePath = file.getPath();
        	txtInputImage.setText( filePath ); 
        }
		
	}
	
	
	private void scan() {
		
		String imgFile = txtInputImage.getText();
		String mapFile = GlobalConstants.ENG_MAP_FILE;
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			mapFile = GlobalConstants.SIN_MAP_FILE;
		}
		
		inputImage = ScanUtils.loadImage( imgFile );
		scn = new Scanner();
		StringBuilder sb = scn.readCharacters( inputImage, mapFile );
		textpPane.setText( sb.toString() );
		
		resolveBtn.setEnabled(true);
		
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
		
		charMappingTxt.setPreferredSize( new Dimension( 50, 50 ) );
		charMappingTxt.setMinimumSize( new Dimension( 50, 50 ) );
		charMappingTxt.setHorizontalAlignment(JTextField.CENTER);
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.VERDANA_FONT_TYPE, Font.BOLD, 30 ) );
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, Font.BOLD, 40 ) );
		}
		
		JPanel imgPanel = new JPanel();
		imgPanel.add(charImgLbl);
		imgPanel.add(charMappingTxt);
		imgPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		
		JButton prevBtn = new JButton( GlobalConstants.PREV_MAPPING_ACTION);
		JButton nextBtn = new JButton( GlobalConstants.NEXT_MAPPING_ACTION);
		JButton saveBtn = new JButton( GlobalConstants.SAVE_MAPPING_ACTION);
		JButton saveAllBtn = new JButton( GlobalConstants.SAVEALL_MAPPING_ACTION);
		JButton clearBtn = new JButton( GlobalConstants.CLEAR_MAPPING_ACTION);
		JButton clearAllBtn = new JButton( GlobalConstants.CLEARALL_MAPPING_ACTION);
		
		PopupListener resolveListener = new PopupListener();
		prevBtn.addActionListener(resolveListener);
		nextBtn.addActionListener(resolveListener);
		saveBtn.addActionListener(resolveListener);
		saveAllBtn.addActionListener(resolveListener);
		clearBtn.addActionListener(resolveListener);
		clearAllBtn.addActionListener(resolveListener);
		
		JPanel navPanel = new JPanel();
		navPanel.add(prevBtn);
		navPanel.add(nextBtn);
		navPanel.add(saveBtn);
		navPanel.add(saveAllBtn);
		navPanel.add(clearBtn);
		navPanel.add(clearAllBtn);
		navPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		
		CharButtonsListener charButtonsListener = new CharButtonsListener();
		
		// layout manager configurations
		
		GridBagConstraints imgPanelGridCons = new GridBagConstraints();
		imgPanelGridCons.gridx = 0;
		imgPanelGridCons.gridy = -1;
		imgPanelGridCons.insets = new Insets(0,0,0,0);
		imgPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints navPanelGridCons = new GridBagConstraints();
		navPanelGridCons.gridx = 0;
		navPanelGridCons.gridy = 1;
		navPanelGridCons.insets = new Insets(0,0,10,0);
		navPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints numBtnPanelGridCons = new GridBagConstraints();
		numBtnPanelGridCons.gridx = 0;
		numBtnPanelGridCons.gridy = 2;
		numBtnPanelGridCons.insets = new Insets(0,0,0,0);
		numBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints spCharBtnPanelGridCons = new GridBagConstraints();
		spCharBtnPanelGridCons.gridx = 0;
		spCharBtnPanelGridCons.gridy = 3;
		spCharBtnPanelGridCons.insets = new Insets(0,0,0,0);
		spCharBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints charBtnPanelGridCons = new GridBagConstraints();
		charBtnPanelGridCons.gridx = 0;
		charBtnPanelGridCons.gridy = 4;
		charBtnPanelGridCons.insets = new Insets(0,0,0,0);
		charBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		JPanel resolvePanel = new JPanel();
		resolvePanel.setLayout( new GridBagLayout() );
		resolvePanel.add( imgPanel, imgPanelGridCons);
		resolvePanel.add( navPanel, navPanelGridCons );
		resolvePanel.add( getNumButtonsPanel(charButtonsListener), numBtnPanelGridCons );
		resolvePanel.add( getSpecialCharButtonsPanel(charButtonsListener), spCharBtnPanelGridCons );
		resolvePanel.add( getCharButtonsPanel(charButtonsListener), charBtnPanelGridCons );
		resolvePanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			resolvePanel.setPreferredSize( new Dimension(500, 430) );
			resolvePanel.setMinimumSize( new Dimension(500, 430) );
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			resolvePanel.setPreferredSize( new Dimension(500, 600) );
			resolvePanel.setMinimumSize( new Dimension(500, 600) );
		}
		
		
		JOptionPane.showConfirmDialog( mainFrame, resolvePanel, GlobalConstants.RESOLVE_TITLE, JOptionPane.OK_CANCEL_OPTION );
	}
	
	// ******************************
	// end MainOCRListener methods 
	// ******************************
	
	
	private JPanel getNumButtonsPanel( CharButtonsListener charButtonsListener ) {
		JPanel numericButtonsPanel = new JPanel();
		numericButtonsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		for( int i=0; i<10; i++ ) {
			JButton numBtn = new JButton( String.valueOf(i) );
			numBtn.addActionListener(charButtonsListener);
			numericButtonsPanel.add(numBtn);
		}
		return numericButtonsPanel;
	}
	
	
	private JPanel getSpecialCharButtonsPanel( CharButtonsListener charButtonsListener ) {
		JPanel specialCharButtonsPanel = new JPanel();
		specialCharButtonsPanel.setPreferredSize( new Dimension(500, 90) );
		specialCharButtonsPanel.setMinimumSize( new Dimension(500, 90) );
		specialCharButtonsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		for( int i=33; i<127; i++ ) {
			if( i>=48 && i < 58 ) {
				// numbers - do nothing
			} else if( i>=65 && i < 91 ) {
				// upper case letters - do nothing
			}else if( i>=97 && i < 123 ) {
				// lower case letters - do nothing
			} else {
				JButton spCharBtn = new JButton( String.valueOf( (char) i) );
				spCharBtn.addActionListener(charButtonsListener);
				specialCharButtonsPanel.add(spCharBtn);
			}
		}
		return specialCharButtonsPanel;
	}
	
	
	private JPanel getCharButtonsPanel( CharButtonsListener charButtonsListener ) {
		
		JPanel charButtonsPanel = new JPanel();
		charButtonsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			
			for( int i=65; i<91; i++ ) {
				
				JButton upperCaseCharBtn = new JButton( String.valueOf( (char) i) );
				upperCaseCharBtn.addActionListener(charButtonsListener);
				charButtonsPanel.add(upperCaseCharBtn);
				
				JButton lowerCaseCharBtn = new JButton( String.valueOf( (char) ( i + 32 ) ) );
				lowerCaseCharBtn.addActionListener(charButtonsListener);
				charButtonsPanel.add(lowerCaseCharBtn);
				
			}
			
			charButtonsPanel.setPreferredSize( new Dimension(500, 150) );
			charButtonsPanel.setMinimumSize( new Dimension(500, 150) );
			
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			
			Font f = new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, java.awt.Font.PLAIN, GlobalConstants.SIN_BUTTON_FONT_SIZE );
			int [] alphabet = { 97, 1400, 1401, 1402, 105, 73, 117, 1407, 79, 85, 
				             101, 1403, 1404, 111, 1405, 1406, 107, 75, 103, 71, 70, 
				             86, 99, 67, 106, 74, 113, 81, 1408, 83, 
				             116, 84, 100, 68, 110, 78, 122, 90, 119, 87, 
				             112, 80, 98, 66, 109, 77, 121, 114, 108, 118, 
				             120, 88, 115, 104, 76, 102, 72, 1304, 89, 82,
				             92, 124, 96, 126, 64, 94, 60, 62, 91, 123,
				             93, 125, 1320, 95, 1322, 1323, 1324, 1321, 65, 69 };
			
			for( int code: alphabet ) {
				String unicodeVal = Symbol.getSymbol(code);
				JButton sinhalaCharBtn = new JButton( unicodeVal );
				sinhalaCharBtn.addActionListener(charButtonsListener);
				sinhalaCharBtn.setFont(f);
				charButtonsPanel.add(sinhalaCharBtn);
			}
			
			charButtonsPanel.setPreferredSize( new Dimension(500, 300) );
			charButtonsPanel.setMinimumSize( new Dimension(500, 300) );
		}
		
		return charButtonsPanel;
	}
	
	
	class CharButtonsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			String currentStr = charMappings[navIndex] == null ? "" : charMappings[navIndex];
			charMappings[navIndex] = currentStr + command;
			charMappingTxt.setText( currentStr + command);
		}
		
	}


	class PopupListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String command = e.getActionCommand();
			
			switch(command) {
			
			case GlobalConstants.PREV_MAPPING_ACTION:
				prev();
				break;
			
			case GlobalConstants.NEXT_MAPPING_ACTION:
				next();
				break;
				
			case GlobalConstants.SAVE_MAPPING_ACTION:
				save();
				break;
			
			case GlobalConstants.SAVEALL_MAPPING_ACTION:
				saveAll();
				break;
			
			case GlobalConstants.CLEAR_MAPPING_ACTION:
				clear();
				break;
			
			case GlobalConstants.CLEARALL_MAPPING_ACTION:
				clearAll();
				break;
				
			}
		}
		
	}
	
	
	// ******************************
	// start PopupListener methods 
	// ******************************
	
	private void prev() {
		if( navIndex > 0 ) navIndex--;
		ImageIcon prevCharImg = getImageIcon();
		charImgLbl.setIcon(prevCharImg);
		String s1 = charMappings[navIndex];
		charMappingTxt.setText(s1);
	}
	
	private void next() {
		if( navIndex < unrecognizedChars.size() ) navIndex++;
		ImageIcon nextCharImg = getImageIcon();
		charImgLbl.setIcon(nextCharImg);
		String s2 = charMappings[navIndex];
		charMappingTxt.setText(s2);
	}
	
	private void save() {
		
	}
	
	
	private void saveAll() {
		
	}
	
	
	private void clear() {
		charMappings[navIndex] = null;
		charMappingTxt.setText("");
	}

	private void clearAll() {
		charMappings = new String[ unrecognizedChars.size() ];
		charMappingTxt.setText("");
	}
	
	
	// ******************************
	// end PopupListener methods 
	// ******************************
	
	
	
	private ImageIcon getImageIcon() {
		Char c = unrecognizedChars.get( navIndex );
		BufferedImage charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
		ImageIcon thumbnailIcon = new ImageIcon(charImg);
		return thumbnailIcon;
	}
	
	
}

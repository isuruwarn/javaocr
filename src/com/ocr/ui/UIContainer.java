package com.ocr.ui;

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

	
	private String mappingsFile;
	private Scanner scn;
	private BufferedImage inputImage;
	
	private Font mainFont;
	private JFrame mainFrame;
	private JButton fileBtn;
	private JButton scanBtn;
	private JButton copyBtn;
	private JButton clearBtn;
	private JButton resolveBtn;
	private JFileChooser jfc;
	private JLabel statusLbl;
	private JTextComponent textpPane;
	private JTextField txtInputImagePath;
	//private JTextField txtOutputFileName;
	private JComboBox<String> selectAlphabet;
	
	
	
	/**
	 * Constructor
	 */
	public UIContainer() {
		
		MainOCRListener mainListener = new MainOCRListener();
		mainFont = new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, GlobalConstants.MAIN_TEXT_FONT_SIZE );
		
		fileBtn = new JButton( GlobalConstants.CHOOSE_FILE_ACTION );
		fileBtn.addActionListener(mainListener);
		fileBtn.setEnabled(true);
		
		txtInputImagePath = new JTextField();
		txtInputImagePath.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtInputImagePath.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtInputImagePath.setText( GlobalConstants.SAMPLE_IMG_FILENAME);
		//txtInputImagePath.setBorder( BorderFactory.createLineBorder( Color.gray ) ); // for debugging
		
		//txtOutputFileName = new JTextField();
		//txtOutputFileName.setText( GlobalConstants.SAMPLE_OUTPUT_FILENAME);
		
		// TODO: read from file
		String [] alphabets = { GlobalConstants.ENGLISH, GlobalConstants.SINHALA };
		selectAlphabet = new JComboBox<String>(alphabets);
		selectAlphabet.addActionListener( mainListener );
		selectAlphabet.setSelectedItem( String.valueOf( GlobalConstants.ENGLISH ) ); // set a default value
		mappingsFile = GlobalConstants.ENG_MAP_FILE; // set a default value
		
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
		
		GridBagConstraints txtInputImgPathGridCons = new GridBagConstraints();
		txtInputImgPathGridCons.gridx = 1;
		txtInputImgPathGridCons.gridy = 0;
		txtInputImgPathGridCons.insets = new Insets(30,0,10,0);
		
		GridBagConstraints selectDialectGridCons = new GridBagConstraints();
		selectDialectGridCons.gridx = 2;
		selectDialectGridCons.gridy = 0;
		selectDialectGridCons.insets = new Insets(30,0,10,0);
		
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
		statusLblGridCons.anchor = GridBagConstraints.PAGE_END;
		
		
		statusLbl = new JLabel();
		//statusLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) ); // for debugging
		statusLbl.setHorizontalAlignment(JTextField.LEFT);
		statusLbl.setPreferredSize( new Dimension( GlobalConstants.STAT_LBL_W, GlobalConstants.STAT_LBL_H ) );
		statusLbl.setMinimumSize( new Dimension( GlobalConstants.STAT_LBL_W, GlobalConstants.STAT_LBL_H ) );
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridBagLayout() );
		buttonPanel.add(fileBtn, fileBtnGridCons);
		buttonPanel.add(txtInputImagePath, txtInputImgPathGridCons);
		buttonPanel.add(selectAlphabet, selectDialectGridCons);
		buttonPanel.add(btnToolBar, btnToolBarGridCons);
		buttonPanel.add(statusLbl, statusLblGridCons);
		
		// main text area
        textpPane = new JTextArea();
        textpPane.setEditable(false);
        textpPane.setFont(mainFont);
        textpPane.setPreferredSize( new Dimension( GlobalConstants.MAIN_TXT_AREA_W, GlobalConstants.MAIN_TXT_AREA_H ) );
        textpPane.setMinimumSize( new Dimension( GlobalConstants.MAIN_TXT_AREA_W, GlobalConstants.MAIN_TXT_AREA_H ) );
		JScrollPane mainScrollPane = new JScrollPane(textpPane);
		
        JPanel mainPanel = new JPanel();
        mainPanel.add(buttonPanel);
        mainPanel.add(mainScrollPane);
        
		// frame for holding everything
		mainFrame = new JFrame( GlobalConstants.TITLE );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( GlobalConstants.MAIN_FRAME_W, GlobalConstants.MAIN_FRAME_H ) );
		mainFrame.setMinimumSize( new Dimension( GlobalConstants.MAIN_FRAME_W, GlobalConstants.MAIN_FRAME_H ) );
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
				
				case GlobalConstants.COMBOBOX_CHANGED_ACTION:
					comboBoxChanged(e);
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
					statusLbl.setText("");
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
        	txtInputImagePath.setText( filePath ); 
        }
		
	}
	
	
	private void scan() {
		
		String imgFile = txtInputImagePath.getText();
		inputImage = ScanUtils.loadImage( imgFile );
		scn = new Scanner();
		StringBuilder sb = scn.readCharacters( inputImage, mappingsFile );
		textpPane.setText( sb.toString() );
		
		int linesRead = scn.getLinesRead();
		int charsRead = scn.getCharsRead();
		int noOfUnrecognizedChars = scn.getUnrecognizedChars().size();
		statusLbl.setText( String.format( GlobalConstants.STAT_LBL_TXT_STR, linesRead, charsRead, noOfUnrecognizedChars ) );
		
		unrecognizedChars = scn.getUnrecognizedChars();
		if( unrecognizedChars.size() > 0 ) {
			resolveBtn.setEnabled(true);
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	private void comboBoxChanged( ActionEvent e ) {
		JComboBox<String> selectBox = (JComboBox<String>) e.getSource();
		String selection = (String) selectBox.getSelectedItem();
		if( selection.equals( GlobalConstants.ENGLISH ) ) {
			mappingsFile = GlobalConstants.ENG_MAP_FILE;
		} else if( selection.equals( GlobalConstants.SINHALA ) ) {
			mappingsFile = GlobalConstants.SIN_MAP_FILE;
		}
	}
	
	
	private Font sinhalaBtnFont;
	private Font charBtnFont;
	private JLabel charImgLbl;
	private JLabel charMappingIndexLbl;
	private JLabel charMappingSavedLbl;
	private JTextField charCodeTxt;
	private JTextField charMappingTxt;
	private CharButtonsListener charButtonsListener;
	
	private int navIndex = 0;
	private ArrayList<Char> unrecognizedChars;
	private String [] charMappings;
	private boolean [] savedMappingsArr;
	
	private void resolve() {
		
		unrecognizedChars = scn.getUnrecognizedChars();
		charMappings = new String[ unrecognizedChars.size() ];
		savedMappingsArr = new boolean[ unrecognizedChars.size() ];
		
		charMappingIndexLbl = new JLabel();
		charMappingIndexLbl.setHorizontalAlignment(JTextField.LEFT);
		charMappingIndexLbl.setVerticalAlignment(JTextField.TOP);
		charMappingIndexLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_IDX_LBL_W, GlobalConstants.CHAR_IDX_LBL_H ) );
		charMappingIndexLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_IDX_LBL_W, GlobalConstants.CHAR_IDX_LBL_H ) );
		//charMappingIndexLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		charImgLbl = new JLabel();
		charImgLbl.setHorizontalAlignment(JTextField.CENTER);
		charImgLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_IMG_LBL_W, GlobalConstants.CHAR_IMG_LBL_H ) );
		charImgLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_IMG_LBL_W, GlobalConstants.CHAR_IMG_LBL_H ) );
		//charImgLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		charCodeTxt = new JTextField();
		charCodeTxt.setPreferredSize( new Dimension( GlobalConstants.CHAR_CODE_TXT_W, GlobalConstants.CHAR_CODE_TXT_H ) );
		charCodeTxt.setMinimumSize( new Dimension( GlobalConstants.CHAR_CODE_TXT_W, GlobalConstants.CHAR_CODE_TXT_H ) );
		charCodeTxt.setHorizontalAlignment(JTextField.CENTER);
		charCodeTxt.setEnabled(true);
		charCodeTxt.setBorder( BorderFactory.createLineBorder( Color.white ) );
		
		JLabel mappingIcon = new JLabel("--->");
		mappingIcon.setFont( new Font( GlobalConstants.VERDANA_FONT_TYPE, Font.BOLD, GlobalConstants.MAIN_TEXT_FONT_SIZE ) );
		//mappingIcon.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		charMappingTxt = new JTextField();
		charMappingTxt.setEditable(false);
		charMappingTxt.setPreferredSize( new Dimension( GlobalConstants.CHAR_MAP_TXT_W, GlobalConstants.CHAR_MAP_TXT_H ) );
		charMappingTxt.setMinimumSize( new Dimension( GlobalConstants.CHAR_MAP_TXT_W, GlobalConstants.CHAR_MAP_TXT_H ) );
		charMappingTxt.setHorizontalAlignment(JTextField.CENTER);
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.VERDANA_FONT_TYPE, Font.BOLD, GlobalConstants.CHAR_MAPPINGS_ENG_FONT_SIZE ) );
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, Font.BOLD, GlobalConstants.CHAR_MAPPINGS_SIN_FONT_SIZE ) );
		}
		
		charMappingSavedLbl = new JLabel();
		charMappingSavedLbl.setHorizontalAlignment(JTextField.CENTER);
		charMappingSavedLbl.setVerticalAlignment(JTextField.TOP);
		charMappingSavedLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_SAVED_IMG_LBL_W, GlobalConstants.CHAR_SAVED_IMG_LBL_H ) );
		charMappingSavedLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_SAVED_IMG_LBL_W, GlobalConstants.CHAR_SAVED_IMG_LBL_H ) );
		//charMappingSavedLbl.setIcon( new ImageIcon( GlobalConstants.CHAR_SAVED_ICO_FILE ) );
		//charMappingSavedLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
		JPanel mappingsImgPanel = new JPanel();
		mappingsImgPanel.add(charMappingIndexLbl);
		mappingsImgPanel.add(charImgLbl);
		mappingsImgPanel.add(mappingIcon);
		mappingsImgPanel.add(charMappingTxt);
		mappingsImgPanel.add(charMappingSavedLbl);
		//mappingsImgPanel.add(charCodeTxt);
		mappingsImgPanel.setPreferredSize( new Dimension( GlobalConstants.MAPPINGS_IMG_PANEL_W, GlobalConstants.MAPPINGS_IMG_PANEL_H ) );
		mappingsImgPanel.setMinimumSize( new Dimension( GlobalConstants.MAPPINGS_IMG_PANEL_W, GlobalConstants.MAPPINGS_IMG_PANEL_H ) );
		mappingsImgPanel.setBackground( Color.white );
		//mappingsImgPanel.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
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
		
		JPanel mappingsNavPanel = new JPanel();
		mappingsNavPanel.add(prevBtn);
		mappingsNavPanel.add(nextBtn);
		mappingsNavPanel.add(saveBtn);
		mappingsNavPanel.add(saveAllBtn);
		mappingsNavPanel.add(clearBtn);
		mappingsNavPanel.add(clearAllBtn);
		//mappingsNavPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		
		charButtonsListener = new CharButtonsListener();
		charBtnFont = new Font( GlobalConstants.SANSSERIF_FONT_TYPE, java.awt.Font.PLAIN, GlobalConstants.REG_CHAR_BUTTON_FONT_SIZE );
		sinhalaBtnFont = new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, java.awt.Font.PLAIN, GlobalConstants.SIN_CHAR_BUTTON_FONT_SIZE );
		
		// layout manager configurations
		
		GridBagConstraints imgPanelGridCons = new GridBagConstraints();
		imgPanelGridCons.gridx = 0;
		imgPanelGridCons.gridy = 0;
		imgPanelGridCons.insets = new Insets(0,0,0,0);
		imgPanelGridCons.anchor = GridBagConstraints.CENTER;

		GridBagConstraints navPanelGridCons = new GridBagConstraints();
		navPanelGridCons.gridx = 0;
		navPanelGridCons.gridy = 2;
		navPanelGridCons.insets = new Insets(0,0,10,0);
		navPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints numBtnPanelGridCons = new GridBagConstraints();
		numBtnPanelGridCons.gridx = 0;
		numBtnPanelGridCons.gridy = 3;
		numBtnPanelGridCons.insets = new Insets(0,0,0,0);
		numBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints spCharBtnPanelGridCons = new GridBagConstraints();
		spCharBtnPanelGridCons.gridx = 0;
		spCharBtnPanelGridCons.gridy = 4;
		spCharBtnPanelGridCons.insets = new Insets(0,0,0,0);
		spCharBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints charBtnPanelGridCons = new GridBagConstraints();
		charBtnPanelGridCons.gridx = 0;
		charBtnPanelGridCons.gridy = 5;
		charBtnPanelGridCons.insets = new Insets(0,0,0,0);
		charBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		JPanel resolveMappingsPanel = new JPanel();
		resolveMappingsPanel.setLayout( new GridBagLayout() );
		resolveMappingsPanel.add( mappingsImgPanel, imgPanelGridCons);
		resolveMappingsPanel.add( mappingsNavPanel, navPanelGridCons );
		resolveMappingsPanel.add( getNumButtonsPanel(), numBtnPanelGridCons );
		resolveMappingsPanel.add( getSpecialCharButtonsPanel(), spCharBtnPanelGridCons );
		resolveMappingsPanel.add( getCharButtonsPanel(), charBtnPanelGridCons );
		//resolveMappingsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			resolveMappingsPanel.setPreferredSize( new Dimension( GlobalConstants.ENG_MAPPINGS_PANEL_W, GlobalConstants.ENG_MAPPINGS_PANEL_H ) );
			resolveMappingsPanel.setMinimumSize( new Dimension( GlobalConstants.ENG_MAPPINGS_PANEL_W, GlobalConstants.ENG_MAPPINGS_PANEL_H ) );
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			resolveMappingsPanel.setPreferredSize( new Dimension( GlobalConstants.SIN_MAPPINGS_PANEL_W, GlobalConstants.SIN_MAPPINGS_PANEL_H ) );
			resolveMappingsPanel.setMinimumSize( new Dimension( GlobalConstants.SIN_MAPPINGS_PANEL_W, GlobalConstants.SIN_MAPPINGS_PANEL_H ) );
		}
		
		setUnrecognizedCharDetails();
		
		JOptionPane.showConfirmDialog( mainFrame, resolveMappingsPanel, GlobalConstants.RESOLVE_TITLE, JOptionPane.CLOSED_OPTION );
		
		scan(); // refresh
		
	}
	
	
	// ******************************
	// end MainOCRListener methods 
	// ******************************
	
	
	private JPanel getNumButtonsPanel( ) {
		JPanel numericButtonsPanel = new JPanel();
		numericButtonsPanel.setPreferredSize( new Dimension( GlobalConstants.NUM_BTNS_PANEL_W, GlobalConstants.NUM_BTNS_PANEL_H ) );
		numericButtonsPanel.setMinimumSize( new Dimension( GlobalConstants.NUM_BTNS_PANEL_W, GlobalConstants.NUM_BTNS_PANEL_H ) );
		//numericButtonsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		for( int i=0; i<10; i++ ) {
			JButton numBtn = createCharButton( String.valueOf(i), charButtonsListener, GlobalConstants.REG_CHAR_BUTTON_W, GlobalConstants.REG_CHAR_BUTTON_H, charBtnFont );
			numericButtonsPanel.add(numBtn);
		}
		return numericButtonsPanel;
	}
	
	
	private JPanel getSpecialCharButtonsPanel() {
		JPanel specialCharButtonsPanel = new JPanel();
		specialCharButtonsPanel.setPreferredSize( new Dimension( GlobalConstants.SP_CHAR_BTNS_PANEL_W, GlobalConstants.SP_CHAR_BTNS_PANEL_H ) );
		specialCharButtonsPanel.setMinimumSize( new Dimension( GlobalConstants.SP_CHAR_BTNS_PANEL_W, GlobalConstants.SP_CHAR_BTNS_PANEL_H ) );
		//specialCharButtonsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		for( int i=33; i<127; i++ ) {
			if( i>=48 && i < 58 ) {
				// numbers - do nothing
			} else if( i>=65 && i < 91 ) {
				// upper case letters - do nothing
			}else if( i>=97 && i < 123 ) {
				// lower case letters - do nothing
			} else {
				JButton spCharBtn = createCharButton( String.valueOf( (char) i ), charButtonsListener, GlobalConstants.REG_CHAR_BUTTON_W, GlobalConstants.REG_CHAR_BUTTON_H, charBtnFont );
				specialCharButtonsPanel.add(spCharBtn);
			}
		}
		return specialCharButtonsPanel;
	}
	
	
	private JPanel getCharButtonsPanel() {
		
		JPanel charButtonsPanel = new JPanel();
		//charButtonsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) ); // for debugging
		
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			
			for( int i=65; i<91; i++ ) {
				JButton upperCaseCharBtn = createCharButton( String.valueOf( (char) i ), charButtonsListener, GlobalConstants.REG_CHAR_BUTTON_W, GlobalConstants.REG_CHAR_BUTTON_H, charBtnFont );
				JButton lowerCaseCharBtn = createCharButton( String.valueOf( (char) ( i + 32 ) ), charButtonsListener, GlobalConstants.REG_CHAR_BUTTON_W, GlobalConstants.REG_CHAR_BUTTON_H, charBtnFont );
				charButtonsPanel.add(upperCaseCharBtn);
				charButtonsPanel.add(lowerCaseCharBtn);
			}
			charButtonsPanel.setPreferredSize( new Dimension( GlobalConstants.ENG_CHAR_BTNS_PANEL_W, GlobalConstants.ENG_CHAR_BTNS_PANEL_H ) );
			charButtonsPanel.setMinimumSize( new Dimension( GlobalConstants.ENG_CHAR_BTNS_PANEL_W, GlobalConstants.ENG_CHAR_BTNS_PANEL_H ) );
			
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			
			int [] alphabet = { 97, 1400, 1401, 1402, 105, 73, 117, 1407, 79, 85, 
				             101, 1403, 1404, 111, 1405, 1406, 107, 75, 103, 71, 70, 
				             86, 99, 67, 106, 74, 113, 81, 1408, 83, 
				             116, 84, 100, 68, 110, 78, 122, 90, 119, 87, 
				             112, 80, 98, 66, 109, 77, 121, 114, 108, 118, 
				             120, 88, 115, 104, 76, 102, 72, 1304, 89, 82,
				             92, 124, 96, 126, 64, 94, 60, 62, 91, 123,
				             93, 125, 1320, 95, 1322, 1323, 1324, 1321, 65, 69 };
			
			for( int code: alphabet ) {
				String unicodeVal = Symbol.getSymbolForASCII(code);
				String symbolName = Symbol.getSymbolNameForASII(code);
				JButton sinhalaCharBtn = createCharButton( unicodeVal, charButtonsListener, GlobalConstants.SIN_CHAR_BUTTON_W, GlobalConstants.SIN_CHAR_BUTTON_H, sinhalaBtnFont );
				sinhalaCharBtn.setToolTipText(symbolName);
				charButtonsPanel.add(sinhalaCharBtn);
			}
			
			charButtonsPanel.setPreferredSize( new Dimension( GlobalConstants.SIN_CHAR_BTNS_PANEL_W, GlobalConstants.SIN_CHAR_BTNS_PANEL_H ) );
			charButtonsPanel.setMinimumSize( new Dimension( GlobalConstants.SIN_CHAR_BTNS_PANEL_W, GlobalConstants.SIN_CHAR_BTNS_PANEL_H ) );
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
		setUnrecognizedCharDetails();
	}
	
	
	private void next() {
		if( navIndex < unrecognizedChars.size() - 1 ) navIndex++;
		setUnrecognizedCharDetails();
	}
	
	
	private void save() {
		
		scn.relaodCharMap( mappingsFile ); // first reload mappings file to get any new mappings
		String newCharCode = unrecognizedChars.get( navIndex ).getCharCode();
		String newCharValue = charMappingTxt.getText();
		
		// check if null or empty value
		if ( newCharValue != null && !newCharValue.isEmpty() ) {
			
			String existingCharValue = scn.getCharValueFromMap( mappingsFile, newCharCode ); // check if char is already mapped
			
			if ( existingCharValue != null && !existingCharValue.isEmpty() && newCharValue.equals(existingCharValue) ) { // mapping already exists
				JOptionPane.showMessageDialog( mainFrame, GlobalConstants.MAPPING_EXISTS_MSG, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.INFORMATION_MESSAGE );
			
			} else {
				
				// create new mapping or update existing mapping
				boolean success = ScanUtils.createOrModifyProperty( mappingsFile, newCharCode, newCharValue );
				
				if(success) {
					JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_SUCCESS_MSG, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.INFORMATION_MESSAGE );
					savedMappingsArr[navIndex] = true;
					charMappingSavedLbl.setIcon( new ImageIcon( GlobalConstants.CHAR_SAVED_ICO_FILE ) );
					
				} else {
					JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_ERROR_MSG1, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.ERROR_MESSAGE );
				}
			}
			
		} else {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_ERROR_MSG2, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.ERROR_MESSAGE );
		}
	}
	
	
	private void saveAll() {
		
		scn.relaodCharMap( mappingsFile ); // first reload mappings file to get any new mappings
		int savedMappings = 0;
		StringBuilder newMappings = new StringBuilder(); // holds new or modified char mappings
		
		
		for( int i=0; i<charMappings.length; i++ ) {
			
			String newCharValue = charMappings[i];
			
			if ( newCharValue != null && !newCharValue.isEmpty() ) {
				
				String newCharCode = unrecognizedChars.get(i).getCharCode();
				String existingCharValue = scn.getCharValueFromMap( mappingsFile, newCharCode ); // check if char is already mapped
				
				if ( existingCharValue == null || existingCharValue.isEmpty() ) { // new char mapping
					
					if( newMappings.indexOf( newCharCode + "=" ) == -1 ) { // check for duplicates
						newMappings.append( newCharCode + "=" + newCharValue + "\n" );
						savedMappingsArr[i] = true;
						savedMappings++;
					}
					
				} else if( !newCharValue.equals(existingCharValue) ) { // char already mapped, but we should update with new value
					
					if( newMappings.indexOf( newCharCode + "=" ) == -1 ) { // check for duplicates
						newMappings.append( newCharCode + "=" + newCharValue + "\n" );
						ScanUtils.deleteProperty( mappingsFile, newCharCode ); // remove old mapping from mappings file
						savedMappingsArr[i] = true;
						savedMappings++;
					}
				}
			}
		}
		
		boolean success = ScanUtils.appendToFile( newMappings, mappingsFile );
		
		if(success) {
			JOptionPane.showMessageDialog( mainFrame, String.format( GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_SUCCESS_MSG, savedMappings), GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.INFORMATION_MESSAGE );
		} else {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_ERROR_MSG, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.ERROR_MESSAGE );
		}
	}
	
	
	private void clear() {
		charMappings[navIndex] = null;
		charMappingTxt.setText("");
		charMappingIndexLbl.setText("");
	}
	
	
	private void clearAll() {
		int resp = JOptionPane.showConfirmDialog( mainFrame, GlobalConstants.CLEAR_ALL_MAPPINGS_CONF_MSG, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.CANCEL_OPTION );
		if( resp == 0 ) { // OK
			charMappings = new String[ unrecognizedChars.size() ];
			charMappingTxt.setText("");
			charMappingIndexLbl.setText("");
		}
	}
	
	
	// ******************************
	// end PopupListener methods 
	// ******************************
	
	
	
	private void setUnrecognizedCharDetails() {
		Char c = unrecognizedChars.get( navIndex );
		BufferedImage charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
		// TODO: display character as scaled image
		//Image scaledCharImg = charImg.getScaledInstance( GlobalConstants.CHAR_IMG_LBL_W, GlobalConstants.CHAR_IMG_LBL_H, Image.SCALE_DEFAULT );
		//BufferedImage charImg2 = new BufferedImage( scaledCharImg.getWidth(null), scaledCharImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		ImageIcon thumbnailIcon = new ImageIcon(charImg);
		charImgLbl.setIcon(thumbnailIcon);
		charImgLbl.setToolTipText( c.getCharCode() );
		charMappingTxt.setText( charMappings[navIndex] );
		charCodeTxt.setText( c.getCharCode() );
		charMappingIndexLbl.setText( String.valueOf(navIndex+1) );
		charMappingSavedLbl.setIcon(null);
		if( savedMappingsArr[navIndex] ) {
			charMappingSavedLbl.setIcon( new ImageIcon( GlobalConstants.CHAR_SAVED_ICO_FILE ) );
		}
	}
	
	
	
	private JButton createCharButton( String text, ActionListener listener, int w, int h, Font font ) {
		JButton charBtn = new JButton();
		charBtn.setText(text);
		//charBtn.setActionCommand(text);
		charBtn.addActionListener(listener);
		charBtn.setPreferredSize( new Dimension( w, h ) );
		charBtn.setMinimumSize( new Dimension( w, h ) );
		charBtn.setFont(font);
		return charBtn;
	}
	
	
}

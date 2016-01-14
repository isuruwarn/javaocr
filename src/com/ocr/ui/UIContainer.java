package com.ocr.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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
	
	private String mappingsFile;
	private BufferedImage inputImage;
	
	private Font mainFont;
	private Font infoFont;
	
	private JFrame mainFrame;
	private JButton inputFileBtn;
	private JButton outputFileBtn;
	private JButton scanBtn;
	private JButton copyBtn;
	private JButton clearBtn;
	private JButton resolveBtn;
	private JTextArea statusLbl;
	private JTextPane textPane;
	private JTextField txtVBlocksPerChar;
	private JTextField txtWhitespaceWidth;
	private JTextField txtBlanklineHeight;
	private JTextField txtInputImagePath;
	private JTextField txtOutputFileName;
	private JComboBox<String> selectAlphabet;
	private JFileChooser inputImgFileChooser;
	private JFileChooser outputFileChooser;
	
	private MappingsKeyEventDispatcher mappingsKeyEventDispatcher;
	
	
	/**
	 * Constructor
	 */
	public UIContainer() {

		scn = new Scanner();
		inputImgFileChooser = new JFileChooser(GlobalConstants.SAMPLE_IMG_FILENAME);
		outputFileChooser = new JFileChooser(GlobalConstants.SAMPLE_OUTPUT_FILENAME);
		MainOCRListener mainListener = new MainOCRListener();
		
		inputFileBtn = new JButton( GlobalConstants.CHOOSE_INPUT_FILE_ACTION );
		inputFileBtn.addActionListener(mainListener);
		inputFileBtn.setEnabled(true);
		
		outputFileBtn = new JButton( GlobalConstants.CHOOSE_OUTPUT_FILE_ACTION );
		outputFileBtn.addActionListener(mainListener);
		outputFileBtn.setEnabled(true);
		
		txtInputImagePath = new JTextField();
		txtInputImagePath.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtInputImagePath.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		
		txtOutputFileName = new JTextField();
		txtOutputFileName.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtOutputFileName.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		
		// TODO: read from file
		String [] alphabets = { GlobalConstants.ENGLISH, GlobalConstants.SINHALA };
		selectAlphabet = new JComboBox<String>(alphabets);
		selectAlphabet.setName( GlobalConstants.SEL_ALPHABET_COMBOBOX );
		selectAlphabet.addActionListener( mainListener );
		
		JLabel lblVBlocksPerChar = new JLabel( GlobalConstants.VBLOCKS_PER_CHAR_LBL );
		lblVBlocksPerChar.setPreferredSize( new Dimension( GlobalConstants.VBLOCKS_PER_CHAR_LBL_W, GlobalConstants.VBLOCKS_PER_CHAR_LBL_H ) );
		lblVBlocksPerChar.setMinimumSize( new Dimension( GlobalConstants.VBLOCKS_PER_CHAR_LBL_W, GlobalConstants.VBLOCKS_PER_CHAR_LBL_H ) );
		lblVBlocksPerChar.setHorizontalAlignment(JTextField.RIGHT);
		
		JLabel lblWhitespaceWidth = new JLabel( GlobalConstants.WHITESPACE_WIDTH_LBL );
		lblWhitespaceWidth.setPreferredSize( new Dimension( GlobalConstants.WHITESPACE_WIDTH_LBL_W, GlobalConstants.WHITESPACE_WIDTH_LBL_H ) );
		lblWhitespaceWidth.setMinimumSize( new Dimension( GlobalConstants.WHITESPACE_WIDTH_LBL_W, GlobalConstants.WHITESPACE_WIDTH_LBL_H ) );
		lblWhitespaceWidth.setHorizontalAlignment(JTextField.RIGHT);
		
		JLabel lblBlanklineHeight = new JLabel( GlobalConstants.BLANKLINE_HEIGHT_LBL );
		lblBlanklineHeight.setPreferredSize( new Dimension( GlobalConstants.BLANKLINE_HEIGHT_LBL_W, GlobalConstants.BLANKLINE_HEIGHT_LBL_H ) );
		lblBlanklineHeight.setMinimumSize( new Dimension( GlobalConstants.BLANKLINE_HEIGHT_LBL_W, GlobalConstants.BLANKLINE_HEIGHT_LBL_H ) );
		lblBlanklineHeight.setHorizontalAlignment(JTextField.RIGHT);
		
		txtVBlocksPerChar = new JTextField(); 
		txtVBlocksPerChar.setPreferredSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtVBlocksPerChar.setMinimumSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtVBlocksPerChar.setHorizontalAlignment(JTextField.CENTER);
		
		txtWhitespaceWidth = new JTextField(); 
		txtWhitespaceWidth.setPreferredSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtWhitespaceWidth.setMinimumSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtWhitespaceWidth.setHorizontalAlignment(JTextField.CENTER);
		
		txtBlanklineHeight = new JTextField(); 
		txtBlanklineHeight.setPreferredSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtBlanklineHeight.setMinimumSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtBlanklineHeight.setHorizontalAlignment(JTextField.CENTER);
		
		JPanel txtBoxPanel = new JPanel();
		txtBoxPanel.add(lblVBlocksPerChar);
		txtBoxPanel.add(txtVBlocksPerChar);
		txtBoxPanel.add(lblWhitespaceWidth);
		txtBoxPanel.add(txtWhitespaceWidth);
		txtBoxPanel.add(lblBlanklineHeight);
		txtBoxPanel.add(txtBlanklineHeight);
		
		
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
		GridBagConstraints inputFileBtnGridCons = new GridBagConstraints();
		inputFileBtnGridCons.gridx = 0;
		inputFileBtnGridCons.gridy = 0;
		inputFileBtnGridCons.insets = new Insets(10,10,10,10);
		inputFileBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints txtInputImgPathGridCons = new GridBagConstraints();
		txtInputImgPathGridCons.gridx = 1;
		txtInputImgPathGridCons.gridy = 0;
		txtInputImgPathGridCons.insets = new Insets(10,0,10,10);
		
		GridBagConstraints selectDialectGridCons = new GridBagConstraints();
		selectDialectGridCons.gridx = 2;
		selectDialectGridCons.gridy = 0;
		selectDialectGridCons.insets = new Insets(10,0,10,0);
		
		GridBagConstraints outputFileBtnGridCons = new GridBagConstraints();
		outputFileBtnGridCons.gridx = 0;
		outputFileBtnGridCons.gridy = 1;
		outputFileBtnGridCons.insets = new Insets(0,0,0,0);
		
		GridBagConstraints txtOutputFilePathGridCons = new GridBagConstraints();
		txtOutputFilePathGridCons.gridx = 1;
		txtOutputFilePathGridCons.gridy = 1;
		txtOutputFilePathGridCons.insets = new Insets(0,0,0,10);
		
		GridBagConstraints txtBoxPanelGridCons = new GridBagConstraints();
		txtBoxPanelGridCons.gridx = 0;
		txtBoxPanelGridCons.gridy = 2;
		txtBoxPanelGridCons.gridwidth = 3;
		txtBoxPanelGridCons.anchor = GridBagConstraints.LINE_START;
		txtBoxPanelGridCons.insets = new Insets(10,0,0,0);
		
		GridBagConstraints btnToolBarGridCons = new GridBagConstraints();
		btnToolBarGridCons.gridx = 0;
		btnToolBarGridCons.gridy = 3;
		btnToolBarGridCons.gridwidth = 3;
		btnToolBarGridCons.insets = new Insets(0,10,0,0);
		btnToolBarGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints statusLblGridCons = new GridBagConstraints();
		statusLblGridCons.gridx = 0;
		statusLblGridCons.gridy = 4;
		statusLblGridCons.gridwidth = 3;
		statusLblGridCons.insets = new Insets(0,0,0,0);
		statusLblGridCons.anchor = GridBagConstraints.PAGE_END;
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridBagLayout() );
		buttonPanel.add( inputFileBtn, inputFileBtnGridCons );
		buttonPanel.add( txtInputImagePath, txtInputImgPathGridCons );
		buttonPanel.add( txtOutputFileName, txtOutputFilePathGridCons );
		buttonPanel.add( selectAlphabet, selectDialectGridCons );
		buttonPanel.add( outputFileBtn, outputFileBtnGridCons );
		buttonPanel.add( txtBoxPanel, txtBoxPanelGridCons );
		buttonPanel.add( btnToolBar, btnToolBarGridCons );
		
		// main text area
		textPane = new JTextPane();
        textPane.setEditable(true);
        textPane.setFont(mainFont);
        JScrollPane mainScrollPane = new JScrollPane(textPane);
        mainScrollPane.setPreferredSize( new Dimension( GlobalConstants.MAIN_TXT_AREA_W, GlobalConstants.MAIN_TXT_AREA_H ) );
        mainScrollPane.setMinimumSize( new Dimension( GlobalConstants.MAIN_TXT_AREA_W, GlobalConstants.MAIN_TXT_AREA_H ) );
        
        infoFont = new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, GlobalConstants.CHAR_MAPPING_INFO_FONT_SIZE );
		
		statusLbl = new JTextArea();
		statusLbl.setPreferredSize( new Dimension( GlobalConstants.STAT_LBL_W, GlobalConstants.STAT_LBL_H ) );
		statusLbl.setMinimumSize( new Dimension( GlobalConstants.STAT_LBL_W, GlobalConstants.STAT_LBL_H ) );
		statusLbl.setEditable(false);
		statusLbl.setOpaque(false); // set background color to frame color
		statusLbl.setFont( infoFont );
		statusLbl.setForeground( Color.gray ); // set text color
		statusLbl.setBorder(null);
		
        JPanel mainPanel = new JPanel();
        mainPanel.add(buttonPanel);
        mainPanel.add(mainScrollPane);
        mainPanel.add(statusLbl);
        
		mainFrame = new JFrame( GlobalConstants.TITLE );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( GlobalConstants.MAIN_FRAME_W, GlobalConstants.MAIN_FRAME_H ) );
		mainFrame.setMinimumSize( new Dimension( GlobalConstants.MAIN_FRAME_W, GlobalConstants.MAIN_FRAME_H ) );
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // position to center of screen 
		mainFrame.setVisible(true);
		
		// set default values
		txtInputImagePath.setText( GlobalConstants.SAMPLE_IMG_FILENAME );
		txtOutputFileName.setText( GlobalConstants.SAMPLE_OUTPUT_FILENAME );
		txtVBlocksPerChar.setText( String.valueOf(Scanner.verticalBlocksPerChar) );
		txtWhitespaceWidth.setText( String.valueOf(Scanner.minWhitespaceWidth) );
		txtBlanklineHeight.setText( String.valueOf(Scanner.minBlanklineHeight) );
		selectAlphabet.setSelectedIndex(0);
		
		//inputImgFileChooser.setSelectedFile( new File( GlobalConstants.SAMPLE_IMG_FILENAME) );
		//outputFileChooser.setSelectedFile( new File( GlobalConstants.SAMPLE_OUTPUT_FILENAME ) );
		
		// for debugging
		/*
		lblVBlocksPerChar.setBorder( BorderFactory.createLineBorder( Color.black ) );
		lblWhitespaceWidth.setBorder( BorderFactory.createLineBorder( Color.black ) );
		lblBlanklineHeight.setBorder( BorderFactory.createLineBorder( Color.black ) );
		txtBoxPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		btnToolBar.setBorder( BorderFactory.createLineBorder( Color.black ) );
		buttonPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		*/
	}
	
	
	
	class MainOCRListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case GlobalConstants.CHOOSE_INPUT_FILE_ACTION:
					chooseFile( inputImgFileChooser, txtInputImagePath );
					break;
				
				case GlobalConstants.CHOOSE_OUTPUT_FILE_ACTION:
					chooseFile( outputFileChooser, txtOutputFileName );
					break;
				
				case GlobalConstants.COMBOBOX_CHANGED_ACTION:
					comboBoxChanged(e);
					break;
				
				case GlobalConstants.SCAN_ACTION:
					scan();
					break;
					
				case GlobalConstants.COPY_ACTION:
					textPane.selectAll();
					textPane.copy();
					break;
					
				case GlobalConstants.CLEAR_ACTION:
					textPane.setText("");
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
	
	
	private void chooseFile( JFileChooser jfc, JTextField txtBox ) {
		int returnVal = jfc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = jfc.getSelectedFile();
        	String filePath = file.getPath();
        	txtBox.setText( filePath ); 
        }
	}
	
	
	private void scan() {
		
		String imgFile = txtInputImagePath.getText();
		inputImage = ScanUtils.loadImage( imgFile );
		
		if( inputImage == null ) {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERROR_LOADING_IMG_MSG, GlobalConstants.ERROR_LOADING_IMG_TITLE, JOptionPane.ERROR_MESSAGE );
			
		} else {
			
			// set basic parameters
			Scanner.verticalBlocksPerChar = Integer.parseInt( txtVBlocksPerChar.getText() );
			Scanner.minWhitespaceWidth = Integer.parseInt( txtWhitespaceWidth.getText() );
			Scanner.minBlanklineHeight = Integer.parseInt( txtBlanklineHeight.getText() );
			
			updateMappingsFile(); // in case user changed blocks per char
			
			StringBuilder sb = scn.readCharacters( inputImage, mappingsFile );
			textPane.setText(sb.toString());
			
			// output result to text file
			//String outputFile = GlobalConstants.SAMPLE_OUTPUT_FILENAME;
			String outputFile = txtOutputFileName.getText();
			ScanUtils.writeToFile( sb, outputFile );
			
			// update status label
			int linesRead = scn.getLinesRead();
			int charsRead = scn.getCharsRead();
			int noOfUnrecognizedChars = scn.getUnrecognizedChars().size();
			statusLbl.setText( String.format( GlobalConstants.STAT_LBL_TXT_STR, scn.getWidth(), scn.getHeight(), linesRead, charsRead, noOfUnrecognizedChars ) );
			
			// get any unrecognized chars and enable resolve button if needed
			unrecognizedChars = scn.getUnrecognizedChars();
			if( unrecognizedChars.size() > 0 ) {
				resolveBtn.setEnabled(true);
			}
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	private void comboBoxChanged( ActionEvent e ) {
		JComboBox<String> selectBox = (JComboBox<String>) e.getSource();
		if( GlobalConstants.SEL_ALPHABET_COMBOBOX.equals( selectBox.getName() ) ) {
			updateMappingsFile();
			updateMainFont();
		}
	}
	
	
	
	private void updateMappingsFile() {
		
		String blocks = txtVBlocksPerChar.getText();
		
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			mappingsFile = String.format( GlobalConstants.ENG_MAP_FILE, blocks );
			
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			mappingsFile = String.format( GlobalConstants.SIN_MAP_FILE, blocks );
		}
	}
	
	
	
	private void updateMainFont() {
		
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			mainFont = new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, GlobalConstants.MAIN_TEXT_ENG_FONT_SIZE );			
		
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			mainFont = new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, Font.PLAIN, GlobalConstants.MAIN_TEXT_SIN_FONT_SIZE );
		}
		textPane.setFont(mainFont);
	}
	
	
	
	private Font sinhalaBtnFont;
	private Font charBtnFont;
	private JLabel charImgLbl;
	private JLabel charBlockImgLbl;
	private JLabel charMappingIndexLbl;
	private JLabel charMappingSavedLbl;
	private JTextArea charInfoTxt;
	private JTextField charMappingTxt;
	private CharButtonsListener charButtonsListener;
	
	private int navIndex = 0;
	private ArrayList<Char> unrecognizedChars;
	private String [] charMappings;
	private boolean [] savedMappingsArr;
	
	private void resolve() {
		
		navIndex = 0;
		unrecognizedChars = scn.getUnrecognizedChars();
		charMappings = new String[ unrecognizedChars.size() ];
		savedMappingsArr = new boolean[ unrecognizedChars.size() ];
		
		// TODO: space to manually enter unicode value of symbol
		
		charMappingIndexLbl = new JLabel();
		charMappingIndexLbl.setHorizontalAlignment(JTextField.LEFT);
		charMappingIndexLbl.setVerticalAlignment(JTextField.CENTER);
		charMappingIndexLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_IDX_LBL_W, GlobalConstants.CHAR_IDX_LBL_H ) );
		charMappingIndexLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_IDX_LBL_W, GlobalConstants.CHAR_IDX_LBL_H ) );
		charMappingIndexLbl.setFont(infoFont);
		charMappingIndexLbl.setForeground (Color.gray); // set text color
		
		charInfoTxt = new JTextArea();
		charInfoTxt.setPreferredSize( new Dimension( GlobalConstants.CHAR_INFO_TXT_W, GlobalConstants.CHAR_INFO_TXT_H ) );
		charInfoTxt.setMinimumSize( new Dimension( GlobalConstants.CHAR_INFO_TXT_W, GlobalConstants.CHAR_INFO_TXT_H ) );
		charInfoTxt.setAlignmentX(JTextField.LEFT);
		charInfoTxt.setAlignmentY(JTextField.TOP);
		charInfoTxt.setFont(infoFont);
		charInfoTxt.setForeground (Color.gray); // set text color
		charInfoTxt.setBackground( Color.white );
		charInfoTxt.setEnabled(true);
		
		JMenuItem charImgMenuItem = new JMenuItem( GlobalConstants.SAVE_IMG_AS_ACTION );
		charImgMenuItem.setName( GlobalConstants.MAPPING_CHAR_IMG_NAME );
		JPopupMenu charImgPopupMenu = new JPopupMenu();
		charImgPopupMenu.add(charImgMenuItem);
		
		JMenuItem blockImgMenuItem = new JMenuItem( GlobalConstants.SAVE_IMG_AS_ACTION );
		blockImgMenuItem.setName( GlobalConstants.MAPPING_BLOCK_REP_IMG_NAME );
		JPopupMenu blockImgPopupMenu = new JPopupMenu();
		blockImgPopupMenu.add(blockImgMenuItem);
		
		charImgLbl = new JLabel();
		charImgLbl.setHorizontalAlignment(JTextField.CENTER);
		charImgLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_IMG_LBL_W, GlobalConstants.CHAR_IMG_LBL_H ) );
		charImgLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_IMG_LBL_W, GlobalConstants.CHAR_IMG_LBL_H ) );
		charImgLbl.setComponentPopupMenu(charImgPopupMenu);
		
		charBlockImgLbl = new JLabel();
		charBlockImgLbl.setHorizontalAlignment(JTextField.CENTER);
		charBlockImgLbl.setVerticalAlignment(JTextField.CENTER);
		charBlockImgLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_BLOCK_IMG_LBL_W, GlobalConstants.CHAR_BLOCK_IMG_LBL_H ) );
		charBlockImgLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_BLOCK_IMG_LBL_W, GlobalConstants.CHAR_BLOCK_IMG_LBL_H ) );
		charBlockImgLbl.setComponentPopupMenu(blockImgPopupMenu);
		
		JLabel mappingIcon = new JLabel( GlobalConstants.MAPPING_ARROW_LBL );
		mappingIcon.setFont( new Font( GlobalConstants.VERDANA_FONT_TYPE, Font.BOLD, GlobalConstants.MAIN_TEXT_ENG_FONT_SIZE ) );
				
		charMappingTxt = new JTextField();
		charMappingTxt.setEditable(false);
		charMappingTxt.setPreferredSize( new Dimension( GlobalConstants.CHAR_MAP_TXT_W, GlobalConstants.CHAR_MAP_TXT_H ) );
		charMappingTxt.setMinimumSize( new Dimension( GlobalConstants.CHAR_MAP_TXT_W, GlobalConstants.CHAR_MAP_TXT_H ) );
		charMappingTxt.setHorizontalAlignment(JTextField.CENTER);
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, GlobalConstants.CHAR_MAPPINGS_ENG_FONT_SIZE ) );
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, Font.PLAIN, GlobalConstants.CHAR_MAPPINGS_SIN_FONT_SIZE ) );
		}
		
		charMappingSavedLbl = new JLabel();
		charMappingSavedLbl.setHorizontalAlignment(JTextField.CENTER);
		charMappingSavedLbl.setVerticalAlignment(JTextField.TOP);
		charMappingSavedLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_SAVED_IMG_LBL_W, GlobalConstants.CHAR_SAVED_IMG_LBL_H ) );
		charMappingSavedLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_SAVED_IMG_LBL_W, GlobalConstants.CHAR_SAVED_IMG_LBL_H ) );
		
		JPanel mappingsImgPanel = new JPanel();
		mappingsImgPanel.add(charMappingIndexLbl);
		//mappingsImgPanel.add(charInfoTxt);
		mappingsImgPanel.add(charImgLbl);
		mappingsImgPanel.add(charBlockImgLbl);
		mappingsImgPanel.add(mappingIcon);
		mappingsImgPanel.add(charMappingTxt);
		mappingsImgPanel.add(charMappingSavedLbl);
		mappingsImgPanel.add(charInfoTxt);
		mappingsImgPanel.setPreferredSize( new Dimension( GlobalConstants.MAPPINGS_IMG_PANEL_W, GlobalConstants.MAPPINGS_IMG_PANEL_H ) );
		mappingsImgPanel.setMinimumSize( new Dimension( GlobalConstants.MAPPINGS_IMG_PANEL_W, GlobalConstants.MAPPINGS_IMG_PANEL_H ) );
		mappingsImgPanel.setBackground( Color.white );
		mappingsImgPanel.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		
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
		charImgMenuItem.addActionListener(resolveListener);
		blockImgMenuItem.addActionListener(resolveListener);
		
		JPanel mappingsNavPanel = new JPanel();
		mappingsNavPanel.add(prevBtn);
		mappingsNavPanel.add(nextBtn);
		mappingsNavPanel.add(saveBtn);
		mappingsNavPanel.add(saveAllBtn);
		mappingsNavPanel.add(clearBtn);
		mappingsNavPanel.add(clearAllBtn);
		
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
		
		
		if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			resolveMappingsPanel.setPreferredSize( new Dimension( GlobalConstants.ENG_MAPPINGS_PANEL_W, GlobalConstants.ENG_MAPPINGS_PANEL_H ) );
			resolveMappingsPanel.setMinimumSize( new Dimension( GlobalConstants.ENG_MAPPINGS_PANEL_W, GlobalConstants.ENG_MAPPINGS_PANEL_H ) );
		} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			resolveMappingsPanel.setPreferredSize( new Dimension( GlobalConstants.SIN_MAPPINGS_PANEL_W, GlobalConstants.SIN_MAPPINGS_PANEL_H ) );
			resolveMappingsPanel.setMinimumSize( new Dimension( GlobalConstants.SIN_MAPPINGS_PANEL_W, GlobalConstants.SIN_MAPPINGS_PANEL_H ) );
		}
		
		setUnrecognizedCharDetails();
		
		// set key event dispatcher
		if( mappingsKeyEventDispatcher == null ) {
			mappingsKeyEventDispatcher = new MappingsKeyEventDispatcher();
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.addKeyEventDispatcher(mappingsKeyEventDispatcher);
		}
		
		// for debugging
		/*charMappingIndexLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charImgLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charBlockImgLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) ); 
		mappingIcon.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charMappingSavedLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charInfoTxt.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		mappingsNavPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		resolveMappingsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );*/
		
		JDialog mappingsDialog = new JDialog( mainFrame, GlobalConstants.RESOLVE_TITLE, true );
		mappingsDialog.getContentPane().add(resolveMappingsPanel);
		mappingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mappingsDialog.pack();
		mappingsDialog.setLocationRelativeTo(null); // position to center of screen
		mappingsDialog.setVisible(true);
		
		// TODO: check for unsaved mappings before close and inform user if needed
		
		// reset key event stuff, otherwise a new object is created each time the popup is opened,
		// which will result in multiple outputs for each key stroke.
		mappingsKeyEventDispatcher = null;
		KeyboardFocusManager.setCurrentKeyboardFocusManager(null);
		
		scan(); // re-scan to refresh
		
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
			
			int [] alphabet = {  97, 1400, 1401, 1402, 105, 73, 117, 1407, 79, 85, 
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
			String charVal = e.getActionCommand();
			setCharMappingText( charVal );
		}
		
	}
	
	
	
	
	private void setCharMappingText( String text ) {
		String currentStr = charMappings[navIndex] == null ? "" : charMappings[navIndex];
		charMappings[navIndex] = currentStr + text;
		charMappingTxt.setText( currentStr + text);
	}
	
	
	
	class MappingsKeyEventDispatcher implements KeyEventDispatcher {
        
		@Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            
        	if( e.getID() == KeyEvent.KEY_PRESSED ) {
            	
        		char keyChar = e.getKeyChar();
                int keyCode = e.getKeyCode();
                //System.out.println( "KEY PRESSED - " + keyChar + " " +  keyCode );
                
        		if( keyCode == 8 ) {
        			String currentStr = charMappings[navIndex] == null ? "" : charMappings[navIndex];
        			String afterBackSpace = currentStr;
        			if( currentStr.length() > 0 ) {
        				afterBackSpace = currentStr.substring( 0, currentStr.length()-1 );
        			}
        			charMappings[navIndex] = afterBackSpace;
        			charMappingTxt.setText( afterBackSpace );
        			
        		} else if( keyCode == 37 ) {
                	prevMapping();
                	
                } else if( keyCode == 39 ) {
                	nextMapping();
                	
                } else if( keyCode >= 32 ) {
                	
                	String newChar = "";
                	
                	if( selectAlphabet.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
                		newChar = String.valueOf(keyChar);
                		
                	} else if( selectAlphabet.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
                		newChar = Symbol.getSymbolForASCII( (int) keyChar );
                		if( newChar == null ) {
                			newChar = String.valueOf(keyChar);
                		}
                	}
                	setCharMappingText( newChar );
                }
                
            }
            return false;
        }
        
    }

	

	class PopupListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String command = e.getActionCommand();
			
			switch(command) {
			
			case GlobalConstants.PREV_MAPPING_ACTION:
				prevMapping();
				break;
			
			case GlobalConstants.NEXT_MAPPING_ACTION:
				nextMapping();
				break;
				
			case GlobalConstants.SAVE_MAPPING_ACTION:
				saveMapping();
				break;
			
			case GlobalConstants.SAVEALL_MAPPING_ACTION:
				saveAllMappings();
				break;
			
			case GlobalConstants.CLEAR_MAPPING_ACTION:
				clear();
				break;
			
			case GlobalConstants.CLEARALL_MAPPING_ACTION:
				clearAll();
				break;
			
			case GlobalConstants.SAVE_IMG_AS_ACTION:
				saveImageDialog( (Component) e.getSource() );
				break;
			}
		}
		
	}
	
	
	// ******************************
	// start PopupListener methods 
	// ******************************
	
	
	private void prevMapping() {
		if( navIndex > 0 ) navIndex--;
		setUnrecognizedCharDetails();
	}
	
	
	private void nextMapping() {
		if( navIndex < unrecognizedChars.size() - 1 ) navIndex++;
		setUnrecognizedCharDetails();
	}
	
	
	private void saveMapping() {
		
		scn.relaodCharMap( mappingsFile ); // first reload mappings file to get any new mappings
		String newCharCode = unrecognizedChars.get( navIndex ).getCharCode();
		String newCharValue = charMappings[navIndex];
		
		// check if null or empty value
		if ( newCharValue != null && !newCharValue.isEmpty() ) {
			
			String existingCharValue = scn.getCharValueFromMap( mappingsFile, newCharCode ); // check if char is already mapped
			
			if ( existingCharValue != null && !existingCharValue.isEmpty() && newCharValue.equals(existingCharValue) ) { // mapping already exists
				JOptionPane.showMessageDialog( mainFrame, GlobalConstants.MAPPING_EXISTS_MSG, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.INFORMATION_MESSAGE );
			
			} else {
				
				// create new mapping or update existing mapping
				boolean success = ScanUtils.setProperty( mappingsFile, newCharCode, newCharValue );
				
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
	
	
	private void saveAllMappings() {
		
		scn.relaodCharMap( mappingsFile ); // first reload mappings file to get any new mappings
		int savedMappings = 0;
		HashMap<String,String> newMappings = new HashMap<String,String>(); // holds new or modified char mappings
		
		for( int i=0; i<charMappings.length; i++ ) {
			
			String newCharValue = charMappings[i];
			
			if ( newCharValue != null && !newCharValue.isEmpty() ) {
				
				String newCharCode = unrecognizedChars.get(i).getCharCode();
				String existingCharValue = scn.getCharValueFromMap( mappingsFile, newCharCode ); // check if char is already mapped
				
				if ( existingCharValue == null || existingCharValue.isEmpty() ) { // new char mapping
					
					newMappings.put( newCharCode, newCharValue );
					savedMappingsArr[i] = true;
					savedMappings++;
					
				} else if( !newCharValue.equals(existingCharValue) ) { // char already mapped, but we should update with new value
					
					newMappings.put( newCharCode, newCharValue );
					ScanUtils.deleteProperty( mappingsFile, newCharCode ); // remove old mapping from mappings file
					savedMappingsArr[i] = true;
					savedMappings++;
				}
			}
		}
		
		boolean success = ScanUtils.setMultipleProperties( mappingsFile, newMappings );
		
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
	
	
	private void saveImageDialog( Component comp ) {
		
		BufferedImage charImg = null;
		String sampleFileName = null;
		Char c = unrecognizedChars.get( navIndex );
		JMenuItem jMenuItem = (JMenuItem) comp;
		String compName = jMenuItem.getName();
		
		if( compName.equals( GlobalConstants.MAPPING_CHAR_IMG_NAME ) ) {
			charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
			sampleFileName = GlobalConstants.SAVE_IMG_FILEPATH + GlobalConstants.MAPPING_CHAR_IMG_NAME + c.getLineNumber() + "-" + c.getCharNumber() + ".png";
			
		} else if( compName.equals( GlobalConstants.MAPPING_BLOCK_REP_IMG_NAME ) ) {
			charImg = c.getBlockImage();
			sampleFileName = GlobalConstants.SAVE_IMG_FILEPATH + GlobalConstants.MAPPING_BLOCK_REP_IMG_NAME + c.getLineNumber() + "-" + c.getCharNumber() + ".png";
			
		}
		
		try {
			JFileChooser jfc = new JFileChooser(GlobalConstants.SAVE_IMG_FILEPATH);
			jfc.setSelectedFile( new File( sampleFileName ) );
			int returnVal = jfc.showDialog( comp, GlobalConstants.SAVE_IMG_FILE );
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	File file = jfc.getSelectedFile();
	        	ImageIO.write( charImg, "png", file );
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// ******************************
	// end PopupListener methods 
	// ******************************
	
	
	
	private void setUnrecognizedCharDetails() {
		
		Char c = unrecognizedChars.get( navIndex );
		//c.printSequence();
		
		BufferedImage charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
		// TODO: display character as scaled image
		ImageIcon charImgIcon = new ImageIcon(charImg);
		charImgLbl.setIcon(charImgIcon);
		charImgLbl.setToolTipText( GlobalConstants.MAPPING_IMG_TOOLTIP );
		
		ImageIcon blockImgIcon = new ImageIcon( c.getBlockImage() );
		charBlockImgLbl.setIcon(blockImgIcon);
		charBlockImgLbl.setToolTipText( GlobalConstants.MAPPING_BLOCK_REP_TOOLTIP );
		
		charMappingTxt.setText( charMappings[navIndex] );
		charMappingIndexLbl.setText( String.format( GlobalConstants.MAPPING_IDX_LBL_TXT, (navIndex+1), unrecognizedChars.size(), c.getCharCode() ) );
		charInfoTxt.setText( String.format( GlobalConstants.MAPPING_INFO_LBL_TXT, c.getLineNumber(), c.getCharNumber(), c.getW(), c.getH(), c.getNoOfHBlocks(), c.getBlockLength(), c.getCharCode() ) );
		
		charMappingSavedLbl.setIcon(null);
		if( savedMappingsArr[navIndex] ) {
			charMappingSavedLbl.setIcon( new ImageIcon( GlobalConstants.CHAR_SAVED_ICO_FILE ) );
		}
	}
	
	
	
	private JButton createCharButton( String text, ActionListener listener, int w, int h, Font font ) {
		JButton charBtn = new JButton();
		charBtn.setText(text);
		charBtn.addActionListener(listener);
		charBtn.setPreferredSize( new Dimension( w, h ) );
		charBtn.setMinimumSize( new Dimension( w, h ) );
		charBtn.setFont(font);
		return charBtn;
	}
	
	
}

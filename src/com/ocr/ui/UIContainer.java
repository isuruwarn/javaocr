package com.ocr.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ocr.core.Char;
import com.ocr.core.Line;
import com.ocr.core.OCRHandler;
import com.ocr.core.OCRRequest;
import com.ocr.core.OCRResult;
import com.ocr.text.Symbol;
import com.ocr.text.TextHandler;
import com.ocr.util.FileUtils;
import com.ocr.util.GlobalConstants;
import com.ocr.util.ImageUtils;




/**
 * 
 * 
 * @author isuru
 *
 */
public class UIContainer {
	
	
	
	private String dialect;
	private BufferedImage inputImage;
	
	private Font mainFont;
	private Font infoFont;
	
	private JFrame mainFrame;
	private JButton inputFileBtn;
	private JButton outputFileBtn;
	private JButton resolveBtn;
	private JButton viewMappedCharsBtn;
	private JButton viewDocImgBtn1;
	private JTextArea statusLbl;
	private JTextPane textPane;
	//private JTextField txtVBlocksPerChar;
	private JTextField txtWhitespaceWidth;
	private JTextField txtBlanklineHeight;
	private JTextField txtInputImagePath;
	private JTextField txtOutputFileName;
	private JLabel ocrEngineLbl;
	private JComboBox<String> selectOCREngine;
	private JComboBox<String> selectDialect;
	private JFileChooser inputImgFileChooser;
	private JFileChooser outputFileChooser;
	
	private OCRHandler ocrHandler;
	private OCRResult ocrResult;
	
	
	
	public UIContainer() {

		ocrHandler = new OCRHandler();
		inputImgFileChooser = new JFileChooser(GlobalConstants.SAMPLE_IMG_FILENAME);
		outputFileChooser = new JFileChooser(GlobalConstants.SAMPLE_OUTPUT_FILENAME);
		MainOCRActionListener mainActionListener = new MainOCRActionListener();
		
		inputFileBtn = new JButton( GlobalConstants.CHOOSE_INPUT_FILE_ACTION );
		inputFileBtn.addActionListener(mainActionListener);
		inputFileBtn.setEnabled(true);
		
		outputFileBtn = new JButton( GlobalConstants.CHOOSE_OUTPUT_FILE_ACTION );
		outputFileBtn.addActionListener(mainActionListener);
		outputFileBtn.setEnabled(true);
		
		txtInputImagePath = new JTextField();
		txtInputImagePath.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtInputImagePath.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		
		txtOutputFileName = new JTextField();
		txtOutputFileName.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtOutputFileName.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		
		ocrEngineLbl = new JLabel( GlobalConstants.SEL_OCR_ENG_LBL);
		
		String [] ocrEngines = { "OCR engine V1", "OCR engine V2", "OCR engine V3", "OCR engine V4" };
		selectOCREngine = new JComboBox<String>(ocrEngines);
		selectOCREngine.setName( GlobalConstants.SEL_OCR_ENG_COMBOBOX );
		selectOCREngine.addActionListener( mainActionListener );
		
		
		// TODO: read from file
		String [] dialects = { GlobalConstants.ENGLISH, GlobalConstants.SINHALA };
		selectDialect = new JComboBox<String>(dialects);
		selectDialect.setName( GlobalConstants.SEL_DIALECT_COMBOBOX );
		selectDialect.addActionListener( mainActionListener );
		
		//JLabel lblVBlocksPerChar = new JLabel( GlobalConstants.VBLOCKS_PER_CHAR_LBL );
		//lblVBlocksPerChar.setPreferredSize( new Dimension( GlobalConstants.VBLOCKS_PER_CHAR_LBL_W, GlobalConstants.VBLOCKS_PER_CHAR_LBL_H ) );
		//lblVBlocksPerChar.setMinimumSize( new Dimension( GlobalConstants.VBLOCKS_PER_CHAR_LBL_W, GlobalConstants.VBLOCKS_PER_CHAR_LBL_H ) );
		//lblVBlocksPerChar.setHorizontalAlignment(JTextField.RIGHT);
		
		JLabel lblWhitespaceWidth = new JLabel( GlobalConstants.WHITESPACE_WIDTH_LBL );
		lblWhitespaceWidth.setPreferredSize( new Dimension( GlobalConstants.WHITESPACE_WIDTH_LBL_W, GlobalConstants.WHITESPACE_WIDTH_LBL_H ) );
		lblWhitespaceWidth.setMinimumSize( new Dimension( GlobalConstants.WHITESPACE_WIDTH_LBL_W, GlobalConstants.WHITESPACE_WIDTH_LBL_H ) );
		lblWhitespaceWidth.setHorizontalAlignment(JTextField.RIGHT);
		
		JLabel lblBlanklineHeight = new JLabel( GlobalConstants.BLANKLINE_HEIGHT_LBL );
		lblBlanklineHeight.setPreferredSize( new Dimension( GlobalConstants.BLANKLINE_HEIGHT_LBL_W, GlobalConstants.BLANKLINE_HEIGHT_LBL_H ) );
		lblBlanklineHeight.setMinimumSize( new Dimension( GlobalConstants.BLANKLINE_HEIGHT_LBL_W, GlobalConstants.BLANKLINE_HEIGHT_LBL_H ) );
		lblBlanklineHeight.setHorizontalAlignment(JTextField.RIGHT);
		
		//txtVBlocksPerChar = new JTextField(); 
		//txtVBlocksPerChar.setPreferredSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		//txtVBlocksPerChar.setMinimumSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		//txtVBlocksPerChar.setHorizontalAlignment(JTextField.CENTER);
		
		txtWhitespaceWidth = new JTextField(); 
		txtWhitespaceWidth.setPreferredSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtWhitespaceWidth.setMinimumSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtWhitespaceWidth.setHorizontalAlignment(JTextField.CENTER);
		
		txtBlanklineHeight = new JTextField(); 
		txtBlanklineHeight.setPreferredSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtBlanklineHeight.setMinimumSize( new Dimension( GlobalConstants.SMALL_TXTBOX_W, GlobalConstants.SMALL_TXTBOX_H ) );
		txtBlanklineHeight.setHorizontalAlignment(JTextField.CENTER);
		
		JPanel txtBoxPanel = new JPanel();
		//txtBoxPanel.add(lblVBlocksPerChar);
		//txtBoxPanel.add(txtVBlocksPerChar);
		txtBoxPanel.add(ocrEngineLbl);
		txtBoxPanel.add(selectOCREngine);
		txtBoxPanel.add(lblWhitespaceWidth);
		txtBoxPanel.add(txtWhitespaceWidth);
		txtBoxPanel.add(lblBlanklineHeight);
		txtBoxPanel.add(txtBlanklineHeight);
		
		
		
		JButton scanBtn = new JButton( GlobalConstants.SCAN_ACTION );
		scanBtn.addActionListener(mainActionListener);
		
		JButton copyBtn = new JButton( GlobalConstants.COPY_ACTION );
		copyBtn.addActionListener(mainActionListener);
		
		JButton clearBtn = new JButton( GlobalConstants.CLEAR_ACTION );
		clearBtn.addActionListener(mainActionListener);
		
		viewDocImgBtn1 = new JButton( GlobalConstants.VIEW_DOC_IMG_ACTION);
		viewDocImgBtn1.addActionListener(mainActionListener);
		viewDocImgBtn1.setEnabled(false);
		

		viewMappedCharsBtn = new JButton( GlobalConstants.VIEW_MAPPED_CHARS_ACTION );
		viewMappedCharsBtn.addActionListener(mainActionListener);
		viewMappedCharsBtn.setEnabled(false);
		
		resolveBtn = new JButton( GlobalConstants.RESOLVE_ACTION );
		resolveBtn.addActionListener(mainActionListener);
		resolveBtn.setEnabled(false);
		
		JPanel btnToolBar = new JPanel();
		btnToolBar.add(scanBtn);
		btnToolBar.add(copyBtn);
		btnToolBar.add(clearBtn);
		btnToolBar.add(viewDocImgBtn1);
		btnToolBar.add(viewMappedCharsBtn);
		btnToolBar.add(resolveBtn);
		
		// layout manager configurations
		GridBagConstraints inputFileBtnGridCons = new GridBagConstraints();
		inputFileBtnGridCons.gridx = 0;
		inputFileBtnGridCons.gridy = 0;
		inputFileBtnGridCons.insets = new Insets(5,10,5,10);
		inputFileBtnGridCons.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints txtInputImgPathGridCons = new GridBagConstraints();
		txtInputImgPathGridCons.gridx = 1;
		txtInputImgPathGridCons.gridy = 0;
		txtInputImgPathGridCons.insets = new Insets(5,0,5,10);
		
		GridBagConstraints selectDialectGridCons = new GridBagConstraints();
		selectDialectGridCons.gridx = 2;
		selectDialectGridCons.gridy = 0;
		selectDialectGridCons.insets = new Insets(5,0,5,0);
		
		/*
		GridBagConstraints outputFileBtnGridCons = new GridBagConstraints();
		outputFileBtnGridCons.gridx = 0;
		outputFileBtnGridCons.gridy = 1;
		outputFileBtnGridCons.insets = new Insets(0,0,0,0);
		
		GridBagConstraints txtOutputFilePathGridCons = new GridBagConstraints();
		txtOutputFilePathGridCons.gridx = 1;
		txtOutputFilePathGridCons.gridy = 1;
		txtOutputFilePathGridCons.insets = new Insets(0,0,0,10);
		*/

		GridBagConstraints txtBoxPanelGridCons = new GridBagConstraints();
		txtBoxPanelGridCons.gridx = 0;
		txtBoxPanelGridCons.gridy = 2;
		txtBoxPanelGridCons.gridwidth = 3;
		txtBoxPanelGridCons.anchor = GridBagConstraints.LINE_START;
		txtBoxPanelGridCons.insets = new Insets(5,5,5,0);
		
		GridBagConstraints btnToolBarGridCons = new GridBagConstraints();
		btnToolBarGridCons.gridx = 0;
		btnToolBarGridCons.gridy = 3;
		btnToolBarGridCons.gridwidth = 3;
		btnToolBarGridCons.insets = new Insets(0,10,5,0);
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
		buttonPanel.add( selectDialect, selectDialectGridCons );
		//buttonPanel.add( outputFileBtn, outputFileBtnGridCons );
		//buttonPanel.add( txtOutputFileName, txtOutputFilePathGridCons );
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
        
        JMenu fileMenu = new JMenu( GlobalConstants.FILE_MENU );
        JMenuItem open = new JMenuItem( GlobalConstants.OPEN_ACTION );
        JMenuItem save = new JMenuItem( GlobalConstants.SAVE_ACTION );
        fileMenu.add(open);
        fileMenu.add(save);
        
        JMenu optionsMenu = new JMenu( GlobalConstants.OPTIONS_MENU );
        
        JMenu mappingsMenu = new JMenu( GlobalConstants.MAPPINGS_MENU );
        JMenuItem unknownChars = new JMenuItem( GlobalConstants.RESOLVE_ACTION );
        JMenuItem knownChars = new JMenuItem( GlobalConstants.VIEW_MAPPED_CHARS_ACTION );
        JMenuItem train = new JMenuItem( GlobalConstants.TRAIN_ACTION );
        mappingsMenu.add(unknownChars);
        mappingsMenu.add(knownChars);
        mappingsMenu.add(train);
		
        JMenu helpMenu = new JMenu( GlobalConstants.HELP_MENU );
        JMenuItem help = new JMenuItem( GlobalConstants.HELP_ACTION );
		JMenuItem about = new JMenuItem( GlobalConstants.ABOUT_ACTION );
		helpMenu.add(help);
		helpMenu.add(about);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);
		menuBar.add(mappingsMenu);
		menuBar.add(helpMenu);
        
		UIHelper.addActionListener( mainActionListener, open, save, unknownChars, knownChars, train, help, about );
		
		mainFrame = new JFrame( GlobalConstants.TITLE );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize( new Dimension( GlobalConstants.MAIN_FRAME_W, GlobalConstants.MAIN_FRAME_H ) );
		mainFrame.setMinimumSize( new Dimension( GlobalConstants.MAIN_FRAME_W, GlobalConstants.MAIN_FRAME_H ) );
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.setJMenuBar(menuBar);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // position to center of screen 
		mainFrame.setVisible(true);
		
		// set default values
		txtWhitespaceWidth.setText( String.valueOf( ocrHandler.getMinWhitespaceWidth() ) ); // default minWhitespaceWidth
		txtBlanklineHeight.setText( String.valueOf( ocrHandler.getMinBlanklineHeight() ) ); // default minBlanklineHeight
		selectDialect.setSelectedIndex(0);
		
		//inputImgFileChooser.setSelectedFile( new File( GlobalConstants.SAMPLE_IMG_FILENAME) );
		//outputFileChooser.setSelectedFile( new File( GlobalConstants.SAMPLE_OUTPUT_FILENAME ) );
		
		// for debugging
		txtInputImagePath.setText( GlobalConstants.SAMPLE_IMG_FILENAME );
		//txtOutputFileName.setText( GlobalConstants.SAMPLE_OUTPUT_FILENAME );
		
		scan();
		//mapUnknownChars();
		
		/*
		lblVBlocksPerChar.setBorder( BorderFactory.createLineBorder( Color.black ) );
		lblWhitespaceWidth.setBorder( BorderFactory.createLineBorder( Color.black ) );
		lblBlanklineHeight.setBorder( BorderFactory.createLineBorder( Color.black ) );
		txtBoxPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		btnToolBar.setBorder( BorderFactory.createLineBorder( Color.black ) );
		buttonPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		*/
	}
	
	
	
	class MainOCRActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case GlobalConstants.CHOOSE_INPUT_FILE_ACTION:
					chooseFile( inputImgFileChooser, txtInputImagePath );
					clear();
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
					clear();
					break;
				
				case GlobalConstants.RESOLVE_ACTION:
					mapUnknownChars(); 
					break;
				
				case GlobalConstants.VIEW_MAPPED_CHARS_ACTION:
					viewMappedChars(); 
					break;
					
				case GlobalConstants.VIEW_DOC_IMG_ACTION:
					viewDocumentImage();
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
		
		try {
			String imgFile = txtInputImagePath.getText();
			
			// set basic parameters
			OCRRequest req = new OCRRequest();
			req.setMinWhitespaceWidth( Integer.parseInt( txtWhitespaceWidth.getText() ) );
			req.setMinBlanklineHeight( Integer.parseInt( txtBlanklineHeight.getText() ) );
			req.setImagePath(imgFile);
			req.setDialect(dialect);
			
			ocrResult = ocrHandler.scan(req);
			textPane.setText( ocrResult.getDocument().toString() );
			inputImage = ocrResult.getInputImage();
			
			// output result to text file
			if( txtOutputFileName.getText() != null && txtOutputFileName.getText().length() > 0 ) {
				//String outputFile = GlobalConstants.SAMPLE_OUTPUT_FILENAME;
				String outputFile = txtOutputFileName.getText();
				FileUtils.writeToFile( ocrResult.getDocument(), outputFile );
			}
			
			// update status label
			int linesRead = ocrResult.getLinesRead();
			int charsRead = ocrResult.getCharsRead();
			int noOfUnrecognizedChars = ocrResult.getUnrecognizedChars().size();
			statusLbl.setText( String.format( GlobalConstants.STAT_LBL_TXT_STR, ocrResult.getWidth(), ocrResult.getHeight(), linesRead, charsRead, noOfUnrecognizedChars ) );
			
			if( ocrResult.getUnrecognizedChars().size() > 0 ) {
				resolveBtn.setEnabled(true);
			}
			
			if( ocrResult.getRecognizedChars().size() > 0 ) {
				viewMappedCharsBtn.setEnabled(true);
			}
			
			viewDocImgBtn1.setEnabled(true);
			
		} catch( IOException ioe ) {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERROR_LOADING_IMG_MSG, GlobalConstants.ERROR_LOADING_IMG_TITLE, JOptionPane.ERROR_MESSAGE );
		}
	}
	
	
	
	private void clear() {
		textPane.setText("");
		statusLbl.setText("");
		resolveBtn.setEnabled(false);
		viewMappedCharsBtn.setEnabled(false);
		viewDocImgBtn1.setEnabled(false);
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void comboBoxChanged( ActionEvent e ) {
		JComboBox<String> selectBox = (JComboBox<String>) e.getSource();
		if( GlobalConstants.SEL_DIALECT_COMBOBOX.equals( selectBox.getName() ) ) {
			dialect = selectDialect.getSelectedItem().toString();
			resolveBtn.setEnabled(false);
			viewMappedCharsBtn.setEnabled(false);
			updateMainFont();
		}
	}
	
	
	
	private void updateMainFont() {
		
		if( selectDialect.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			mainFont = new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, GlobalConstants.MAIN_TEXT_ENG_FONT_SIZE );			
		
		} else if( selectDialect.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			mainFont = new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, Font.PLAIN, GlobalConstants.MAIN_TEXT_SIN_FONT_SIZE );
		}
		textPane.setFont(mainFont);
	}
	
	

	
	private void viewMappedChars() {
		mappings = ocrResult.getRecognizedChars();
		showMappingsDialog( GlobalConstants.KNOWN_MAPPINGS_TITLE );
	}
	
	
	
	
	private void mapUnknownChars() {
		mappings = ocrResult.getUnrecognizedChars();
		showMappingsDialog( GlobalConstants.UNKNOWN_MAPPINGS_TITLE );
	}
	
	
	
	
	
	private JLabel charBlockImgLbl;
	private JLabel charMappingIndexLbl;
	private JLabel charMappingSavedLbl;
	private JTextArea charInfoTxt;
	private JTextField charMappingTxt;
	private JPanel charBlockImgPanel;
	private int navIndex = 0;
	private ArrayList<Char> mappings;
	private HashSet<Integer> updatedMappings; // to keep track of modified mappings
	private HashSet<Integer> savedMappings; // to keep track of saved mappings
	
	private void showMappingsDialog( String title ) {
		
		navIndex = 0;
		updatedMappings = new HashSet<Integer>();
		savedMappings = new HashSet<Integer>();
		
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
		charInfoTxt.setAlignmentY(JTextField.CENTER_ALIGNMENT);
		charInfoTxt.setFont(infoFont);
		charInfoTxt.setForeground (Color.gray); // set text color
		charInfoTxt.setBackground( Color.white );
		charInfoTxt.setEnabled(true);
		
		JMenuItem blockImgMenuItem = new JMenuItem( GlobalConstants.SAVE_IMG_AS_ACTION );
		blockImgMenuItem.setName( GlobalConstants.MAPPING_BLOCK_REP_IMG_NAME );
		JPopupMenu blockImgPopupMenu = new JPopupMenu();
		blockImgPopupMenu.add(blockImgMenuItem);
		
		charBlockImgLbl = new JLabel();
		charBlockImgLbl.setHorizontalAlignment(JTextField.CENTER);
		charBlockImgLbl.setVerticalAlignment(JTextField.CENTER);
		charBlockImgLbl.setComponentPopupMenu(blockImgPopupMenu);
		charBlockImgLbl.addMouseListener( new MappingsMouseListener() );
		
		charBlockImgPanel = new JPanel();
		charBlockImgPanel.setPreferredSize( new Dimension( GlobalConstants.CHAR_BLOCK_IMG_LBL_W, GlobalConstants.CHAR_BLOCK_IMG_LBL_H ) );
		charBlockImgPanel.setMinimumSize( new Dimension( GlobalConstants.CHAR_BLOCK_IMG_LBL_W, GlobalConstants.CHAR_BLOCK_IMG_LBL_H ) );
		charBlockImgPanel.setOpaque(false);
		charBlockImgPanel.add(charBlockImgLbl);
		
		JLabel arrowLbl = new JLabel( GlobalConstants.MAPPING_ARROW_LBL );
		arrowLbl.setPreferredSize( new Dimension( GlobalConstants.ARROW_LBL_W, GlobalConstants.ARROW_LBL_H ) );
		arrowLbl.setMinimumSize( new Dimension( GlobalConstants.ARROW_LBL_W, GlobalConstants.ARROW_LBL_H ) );
		arrowLbl.setFont( new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, 30 ));
		
		charMappingTxt = new JTextField();
		charMappingTxt.setPreferredSize( new Dimension( GlobalConstants.CHAR_MAP_TXT_W, GlobalConstants.CHAR_MAP_TXT_H ) );
		charMappingTxt.setMinimumSize( new Dimension( GlobalConstants.CHAR_MAP_TXT_W, GlobalConstants.CHAR_MAP_TXT_H ) );
		charMappingTxt.setHorizontalAlignment(JTextField.CENTER);
		if( selectDialect.getSelectedItem().equals( GlobalConstants.ENGLISH ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.SANSSERIF_FONT_TYPE, Font.PLAIN, GlobalConstants.CHAR_MAPPINGS_ENG_FONT_SIZE ) );
		} else if( selectDialect.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			charMappingTxt.setFont( new Font( GlobalConstants.ISKOOLA_POTA_FONT_TYPE, Font.PLAIN, GlobalConstants.CHAR_MAPPINGS_SIN_FONT_SIZE ) );
		}
		charMappingTxt.getDocument().addDocumentListener( new MappingsTxtDocumentListener() );
		charMappingTxt.addKeyListener( new MappingsKeyListener() );
		
		charMappingSavedLbl = new JLabel();
		charMappingSavedLbl.setHorizontalAlignment(JTextField.CENTER);
		charMappingSavedLbl.setVerticalAlignment(JTextField.TOP);
		charMappingSavedLbl.setPreferredSize( new Dimension( GlobalConstants.CHAR_SAVED_IMG_LBL_W, GlobalConstants.CHAR_SAVED_IMG_LBL_H ) );
		charMappingSavedLbl.setMinimumSize( new Dimension( GlobalConstants.CHAR_SAVED_IMG_LBL_W, GlobalConstants.CHAR_SAVED_IMG_LBL_H ) );
		
		JPanel mappingsImgPanel = new JPanel();
		mappingsImgPanel.add(charMappingIndexLbl);
		mappingsImgPanel.add(charBlockImgPanel);
		mappingsImgPanel.add(arrowLbl);
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
		JButton clearTxtMapsBtn = new JButton( GlobalConstants.CLEAR_TXT_MAPPINGS_ACTION);
		JButton clearAllTxtMapsBtn = new JButton( GlobalConstants.CLEARALL_TXT_MAPPINGS_ACTION);
		JButton clearKeyPointsBtn = new JButton( GlobalConstants.CLEAR_KP_MAPPINGS_ACTION);
		JButton clearAllKeyPointsBtn = new JButton( GlobalConstants.CLEARALL_KP_MAPPINGS_ACTION);
		JButton viewCharImgBtn = new JButton( GlobalConstants.VIEW_CHAR_IMG_ACTION);
		JButton viewLineImgBtn = new JButton( GlobalConstants.VIEW_LINE_IMG_ACTION);
		JButton viewDocImgBtn2 = new JButton( GlobalConstants.VIEW_DOC_IMG_ACTION);
		
		MappingsActionListener resolveListener = new MappingsActionListener();
		prevBtn.addActionListener(resolveListener);
		nextBtn.addActionListener(resolveListener);
		saveBtn.addActionListener(resolveListener);
		saveAllBtn.addActionListener(resolveListener);
		clearTxtMapsBtn.addActionListener(resolveListener);
		clearAllTxtMapsBtn.addActionListener(resolveListener);
		clearKeyPointsBtn.addActionListener(resolveListener);
		clearAllKeyPointsBtn.addActionListener(resolveListener);
		viewCharImgBtn.addActionListener(resolveListener);
		viewLineImgBtn.addActionListener(resolveListener);
		viewDocImgBtn2.addActionListener(resolveListener);
		
		prevBtn.setToolTipText( GlobalConstants.PREV_MAPPING_ACTION_TOOLTIP );
		nextBtn.setToolTipText( GlobalConstants.NEXT_MAPPING_ACTION_TOOLTIP );
		
		JPanel mappingsNavPanel = new JPanel();
		mappingsNavPanel.add(prevBtn);
		mappingsNavPanel.add(nextBtn);
		mappingsNavPanel.add(saveBtn);
		mappingsNavPanel.add(saveAllBtn);
		
		JPanel mappingsClearPanel = new JPanel();
		mappingsClearPanel.add(clearTxtMapsBtn);
		mappingsClearPanel.add(clearAllTxtMapsBtn);
		mappingsClearPanel.add(clearKeyPointsBtn);
		mappingsClearPanel.add(clearAllKeyPointsBtn);
		
		JPanel viewImagesPanel = new JPanel();
		viewImagesPanel.add(viewCharImgBtn);
		viewImagesPanel.add(viewLineImgBtn);
		viewImagesPanel.add(viewDocImgBtn2);
		
		MappingsSpSinCharActionListener spSinCharListener = new MappingsSpSinCharActionListener();
		JButton ksha = createCharButton( Symbol.KSHA.getUnicodeValue(), Symbol.KSHA.getKeyCode(), Symbol.KSHA.name(), spSinCharListener );
		JButton thalujaNasikya = createCharButton( Symbol.THALUJA_NASIKAYA.getUnicodeValue(), Symbol.THALUJA_NASIKAYA.getKeyCode(), Symbol.THALUJA_NASIKAYA.name(), spSinCharListener );
		JButton sanyogaNasikaya = createCharButton( Symbol.SANYOGA_NASIKAYA.getUnicodeValue(), Symbol.SANYOGA_NASIKAYA.getKeyCode(), Symbol.SANYOGA_NASIKAYA.name(), spSinCharListener );
		JButton kombuDeka = createCharButton( Symbol.KOMBU_DEKA.getUnicodeValue(), Symbol.KOMBU_DEKA.getKeyCode(), Symbol.KOMBU_DEKA.name(), spSinCharListener );
		JButton kombuvaHaGayanuKitta = createCharButton( Symbol.KOMBUVA_HAA_GAYANUKITTA.getUnicodeValue(), Symbol.KOMBUVA_HAA_GAYANUKITTA.getKeyCode(), Symbol.KOMBUVA_HAA_GAYANUKITTA.name(), spSinCharListener );
		JButton digaGaetaPilla = createCharButton( Symbol.DIGA_GAETTA_PILLA.getUnicodeValue(), Symbol.DIGA_GAETTA_PILLA.getKeyCode(), Symbol.DIGA_GAETTA_PILLA.name(), spSinCharListener );
		JButton gayanukitta = createCharButton( Symbol.GAYANUKITTA.getUnicodeValue(), Symbol.GAYANUKITTA.getKeyCode(), Symbol.GAYANUKITTA.name(), spSinCharListener );
		JButton digaGayanukitta = createCharButton( Symbol.DIGA_GAYANUKITTA.getUnicodeValue(), Symbol.DIGA_GAYANUKITTA.getKeyCode(), Symbol.DIGA_GAYANUKITTA.name(), spSinCharListener );
		
		JPanel spSinCharsBtnPanel = new JPanel();
		spSinCharsBtnPanel.add(ksha);
		spSinCharsBtnPanel.add(thalujaNasikya);
		spSinCharsBtnPanel.add(sanyogaNasikaya);
		spSinCharsBtnPanel.add(kombuDeka);
		spSinCharsBtnPanel.add(kombuvaHaGayanuKitta);
		spSinCharsBtnPanel.add(digaGaetaPilla);
		spSinCharsBtnPanel.add(gayanukitta);
		spSinCharsBtnPanel.add(digaGayanukitta);
		
		// layout manager configurations
		GridBagConstraints imgPanelGridCons = new GridBagConstraints();
		imgPanelGridCons.gridx = 0;
		imgPanelGridCons.gridy = 0;
		imgPanelGridCons.insets = new Insets(0,0,0,0);
		imgPanelGridCons.anchor = GridBagConstraints.CENTER;

		GridBagConstraints navPanelGridCons = new GridBagConstraints();
		navPanelGridCons.gridx = 0;
		navPanelGridCons.gridy = 1;
		navPanelGridCons.insets = new Insets(0,0,0,0);
		navPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints clearPanelGridCons = new GridBagConstraints();
		clearPanelGridCons.gridx = 0;
		clearPanelGridCons.gridy = 2;
		clearPanelGridCons.insets = new Insets(0,0,0,0);
		clearPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints viewImgsBtnPanelGridCons = new GridBagConstraints();
		viewImgsBtnPanelGridCons.gridx = 0;
		viewImgsBtnPanelGridCons.gridy = 3;
		viewImgsBtnPanelGridCons.insets = new Insets(0,0,0,0);
		viewImgsBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints spSinCharsBtnPanelGridCons = new GridBagConstraints();
		spSinCharsBtnPanelGridCons.gridx = 0;
		spSinCharsBtnPanelGridCons.gridy = 4;
		spSinCharsBtnPanelGridCons.insets = new Insets(0,0,0,0);
		spSinCharsBtnPanelGridCons.anchor = GridBagConstraints.CENTER;
		
		JPanel resolveMappingsPanel = new JPanel();
		resolveMappingsPanel.setPreferredSize( new Dimension( GlobalConstants.MAPPINGS_PANEL_W, GlobalConstants.MAPPINGS_PANEL_H ) );
		resolveMappingsPanel.setMinimumSize( new Dimension( GlobalConstants.MAPPINGS_PANEL_W, GlobalConstants.MAPPINGS_PANEL_H ) );
		resolveMappingsPanel.setLayout( new GridBagLayout() );
		resolveMappingsPanel.add( mappingsImgPanel, imgPanelGridCons);
		resolveMappingsPanel.add( mappingsNavPanel, navPanelGridCons );
		resolveMappingsPanel.add( mappingsClearPanel, clearPanelGridCons );
		resolveMappingsPanel.add( viewImagesPanel, viewImgsBtnPanelGridCons );
		
		if( selectDialect.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
			resolveMappingsPanel.add( spSinCharsBtnPanel, spSinCharsBtnPanelGridCons );
		}
		
		setCharDetails();
		
		
		// for debugging
		
		/*charMappingIndexLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charBlockImgLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		arrowLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charMappingSavedLbl.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		charInfoTxt.setBorder( BorderFactory.createLineBorder( Color.gray ) );
		mappingsNavPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		resolveMappingsPanel.setBorder( BorderFactory.createLineBorder( Color.black ) );
		*/
		
		JDialog mappingsDialog = new JDialog( mainFrame, title, true );
		mappingsDialog.getContentPane().add(resolveMappingsPanel);
		mappingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mappingsDialog.pack();
		mappingsDialog.setLocationRelativeTo(null); // position to center of screen
		mappingsDialog.setVisible(true);
		
		// TODO: check for unsaved mappings before close and inform user if needed
		
		scan(); // re-scan to refresh
		
	}
	
	
	// ******************************
	// end MainOCRListener methods 
	// ******************************
	
	
	
	class MappingsTxtDocumentListener implements DocumentListener {
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			mappings.get(navIndex).setCharValue( charMappingTxt.getText() );
			updatedMappings.add( navIndex );
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			mappings.get(navIndex).setCharValue( charMappingTxt.getText() );
			updatedMappings.add( navIndex );
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			mappings.get(navIndex).setCharValue( charMappingTxt.getText() );
			updatedMappings.add( navIndex );
		}
		
	}
	
	
	class MappingsMouseListener implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			Char c = mappings.get( navIndex );
			Icon icon = charBlockImgLbl.getIcon();
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			int x = e.getX();
			int y = e.getY();
			int hBlocksInChar = c.getNoOfHBlocks();
			int vBlocksInChar = ocrResult.getVerticalBlocksPerChar();
			int blockWidth = iconWidth / hBlocksInChar;
			int blockHeight = iconHeight / vBlocksInChar;
			//float blockWidth = (float) iconWidth / hBlocksInChar;
			//float blockHeight = (float) iconHeight / vBlocksInChar;
			int colNumber = x / blockWidth;
			int rowNumber = y / blockHeight;
			//int colNumber = Math.round( x / blockWidth );
			//int rowNumber = Math.round( y / blockHeight );
			int clickBlockNumber = ( colNumber * vBlocksInChar ) + rowNumber;

			ArrayList<Integer> keyPoints = null;
			
			if( c.getBlockSequence().get(clickBlockNumber) == 1 ) { // black pixel
				//System.out.println("Black Pixel - clickBlockNumber=" + clickBlockNumber );
				keyPoints = c.getBlackKeyPoints();
				if( keyPoints == null ) {
					keyPoints = new ArrayList<Integer>();
					c.setBlackKeyPoints(keyPoints);
				}
				
			} else { // white pixel
				//System.out.println("White Pixel - clickBlockNumber=" + clickBlockNumber );
				keyPoints = c.getWhiteKeyPoints();
				if( keyPoints == null ) {
					keyPoints = new ArrayList<Integer>();
					c.setWhiteKeyPoints(keyPoints);
				}
			}
			
			if( keyPoints.contains(clickBlockNumber) ) {
				keyPoints.remove( new Integer(clickBlockNumber) );
			} else {
				keyPoints.add(clickBlockNumber);
			}
			updatedMappings.add(navIndex);
			setMappingsBlockImageLbl(c);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
		
	}
	
	
	class MappingsKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			
			String inputKey = String.valueOf( e.getKeyChar() );
			
			if( selectDialect.getSelectedItem().equals( GlobalConstants.SINHALA ) ) {
				
				if( inputKey.matches("[A-Za-z]") || 
					inputKey.equals( "`" ) || 
					inputKey.equals( "~" ) || 
					inputKey.equals( "@" ) || 
					inputKey.equals( "^" ) || 
					inputKey.equals( "_" ) || 
					inputKey.equals( "[" ) || 
					inputKey.equals( "{" ) || 
					inputKey.equals( "]" ) || 
					inputKey.equals( "}" ) || 
					inputKey.equals( "\\" ) || 
					inputKey.equals( "|" ) || 
					inputKey.equals( "<" ) || 
					inputKey.equals( ">" ) ) {
					
					e.consume(); // prevents default action, to stop original character from being printed.
					TextHandler.insertText( inputKey, charMappingTxt );
				}
				
			}
			
		}

		@Override
		public void keyPressed(KeyEvent e) { // capture non-char keys
			int keyCode =  e.getKeyCode();
			if( keyCode == 38 ) {
				nextMapping();
            	
            } else if( keyCode == 40 ) {
            	prevMapping();
            }
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}
		
	}
	
	
	

	class MappingsActionListener implements ActionListener {

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
			
			case GlobalConstants.CLEAR_TXT_MAPPINGS_ACTION:
				clearTextMapping();
				break;
			
			case GlobalConstants.CLEARALL_TXT_MAPPINGS_ACTION:
				clearAllTextMappings();
				break;
			
			case GlobalConstants.CLEAR_KP_MAPPINGS_ACTION:
				clearKeyPointMappings();
				break;
			
			case GlobalConstants.CLEARALL_KP_MAPPINGS_ACTION:
				clearAllKeyPointMappings();
				break;
			
			case GlobalConstants.SAVE_IMG_AS_ACTION:
				saveImageDialog( (Component) e.getSource() );
				break;
				
			case GlobalConstants.VIEW_CHAR_IMG_ACTION:
				viewCharImage();
				break;
				
			case GlobalConstants.VIEW_LINE_IMG_ACTION:
				viewLineImage();
				break;
				
			case GlobalConstants.VIEW_DOC_IMG_ACTION:
				viewDocumentImage();
				break;
			}
			
			charMappingTxt.requestFocusInWindow();
		}
		
	}
	
	
	class MappingsSpSinCharActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String inputKey = e.getActionCommand();
			TextHandler.insertText( inputKey, charMappingTxt );
		}
		
	}
	
	
	// ******************************
	// start PopupListener methods 
	// ******************************
	
	
	private void prevMapping() {
		if( navIndex > 0 ) navIndex--;
		setCharDetails();
	}
	
	
	private void nextMapping() {
		if( navIndex < mappings.size() - 1 ) navIndex++;
		setCharDetails();
	}
	
	
	
	private void saveMapping() {
		
		Char c = mappings.get(navIndex);
		String newCharValue = c.getCharValue();
		
		if ( newCharValue != null && !newCharValue.isEmpty() ) {
			
			try {
				ArrayList<Char> newMappings = new ArrayList<Char>();
				newMappings.add(c);
				int statusCode = ocrHandler.saveMappings( newMappings );
				
				if( statusCode == 0 ) {
					JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_SUCCESS_MSG, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.INFORMATION_MESSAGE );
					savedMappings.add(navIndex);
					charMappingSavedLbl.setIcon( new ImageIcon( GlobalConstants.CHAR_SAVED_ICO_FILE ) );
					
				} else {
					JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_ERROR_MSG1, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.ERROR_MESSAGE );
				}
				
			} catch( Exception e ) {
				JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_ERROR_MSG1, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.ERROR_MESSAGE );
			}
			
		} else {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_ERROR_MSG2, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.ERROR_MESSAGE );
		}
	}
	
	
	
	private void saveAllMappings() {
		
		try {
			int savedMappingsCnt = 0;
			ArrayList<Char> newMappings = new ArrayList<Char>(); // holds new or modified char mappings
			
			//for( int i=0; i<charMappingsArr.length; i++ ) {
			for( int i: updatedMappings) {
				
				//String newCharValue = charMappingsArr[i];
				Char c = mappings.get(i);
				String newCharValue = c.getCharValue();
				
				if ( newCharValue != null && !newCharValue.isEmpty() ) { // TODO - for version 2.0 we should check key points as well
					newMappings.add(c);
					savedMappings.add(i);
					savedMappingsCnt++;
				}
			}
			
			if( newMappings.size() > 0 ) {
			
				int statusCode = ocrHandler.saveMappings( newMappings );
				
				if( statusCode == 0 ) {
					JOptionPane.showMessageDialog( mainFrame, String.format( GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_SUCCESS_MSG, savedMappingsCnt), GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.INFORMATION_MESSAGE );
				} else {
					JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_ERROR_MSG, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.ERROR_MESSAGE );
				}
				
			} else {
				JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MAPPPING_ERROR_MSG2, GlobalConstants.SAVE_MAPPING_TITLE, JOptionPane.ERROR_MESSAGE );
			}
			
		} catch( Exception e ) {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_ERROR_MSG, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.ERROR_MESSAGE );
		}
		
	}
	
	
	private void clearTextMapping() {
		mappings.get(navIndex).setCharValue("");
		charMappingTxt.setText("");
	}
	
	
	private void clearAllTextMappings() {
		int resp = JOptionPane.showConfirmDialog( mainFrame, GlobalConstants.CLEAR_ALL_MAPPINGS_CONF_MSG, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.CANCEL_OPTION );
		if( resp == 0 ) { // OK
			for( int i: updatedMappings) {
				Char c = mappings.get(i);
				c.setCharValue("");
			}
			charMappingTxt.setText("");
		}
	}
	
	private void clearKeyPointMappings() {
		Char c = mappings.get( navIndex );
		c.setBlackKeyPoints(null);
		c.setWhiteKeyPoints(null);
		setMappingsBlockImageLbl(c);
	}
	
	
	private void clearAllKeyPointMappings() {
		int resp = JOptionPane.showConfirmDialog( mainFrame, GlobalConstants.CLEAR_ALL_MAPPINGS_CONF_MSG, GlobalConstants.SAVE_MULTIPLE_MAPPPINGS_TITLE, JOptionPane.CANCEL_OPTION );
		if( resp == 0 ) { // OK
			for( int i: updatedMappings) {
				Char c = mappings.get(i);
				c.setBlackKeyPoints(null);
				c.setWhiteKeyPoints(null);
			}
			Char c = mappings.get( navIndex );
			setMappingsBlockImageLbl(c);
		}
	}
	
	
	
	private void saveImageDialog( Component comp ) {
		
		BufferedImage charImg = null;
		String sampleFileName = null;
		Char c = mappings.get( navIndex );
		JMenuItem jMenuItem = (JMenuItem) comp;
		String compName = jMenuItem.getName();
		
		if( compName.equals( GlobalConstants.MAPPING_CHAR_IMG_NAME ) ) {
			charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
			sampleFileName = GlobalConstants.SAVE_IMG_FILEPATH + GlobalConstants.MAPPING_CHAR_IMG_NAME + c.getLineNumber() + "-" + c.getCharNumber() + ".png";
			
		} else if( compName.equals( GlobalConstants.MAPPING_BLOCK_REP_IMG_NAME ) ) {
			charImg = ImageUtils.getBlockImage( c, ocrResult.getVerticalBlocksPerChar() );
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
	
	
	private void viewCharImage() {
		Char c = mappings.get( navIndex );
		BufferedImage charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
		viewImage( charImg, GlobalConstants.VIEW_CHAR_IMG_ACTION, GlobalConstants.VIEW_CHAR_IMG_LBL_W, GlobalConstants.VIEW_CHAR_IMG_LBL_H );
	}
	
	
	private void viewLineImage() {
		Char c = mappings.get( navIndex );
		Line l = ocrResult.getLines().get( c.getLineNumber() );
		BufferedImage lineImg = inputImage.getSubimage( l.getX(), l.getY(), l.getW(), l.getH() );
		viewImage( lineImg, GlobalConstants.VIEW_LINE_IMG_ACTION, GlobalConstants.VIEW_LINE_IMG_LBL_W, GlobalConstants.VIEW_LINE_IMG_LBL_H );
	}
	
	
	private void viewDocumentImage() {
		viewImage( inputImage, GlobalConstants.VIEW_DOC_IMG_ACTION, GlobalConstants.VIEW_DOC_IMG_LBL_W, GlobalConstants.VIEW_DOC_IMG_LBL_H );
	}
	
	
	private void viewImage( Image img, String msg, int w, int h ) {
		ImageIcon imgIcon = new ImageIcon(img);
		JLabel imgLbl = new JLabel();
		imgLbl.setIcon(imgIcon);
		imgLbl.setBackground( Color.white );
		imgLbl.setOpaque(true);
		imgLbl.setHorizontalAlignment(JTextField.CENTER);
		imgLbl.setVerticalAlignment(JTextField.CENTER);
		JScrollPane scrollPane = new JScrollPane(imgLbl);
		scrollPane.setPreferredSize( new Dimension( w, h ) );
		scrollPane.setMinimumSize( new Dimension( w, h ) );
		JOptionPane.showMessageDialog( mainFrame, scrollPane, msg, JOptionPane.INFORMATION_MESSAGE );
	}
	
	
	// ******************************
	// end PopupListener methods 
	// ******************************
	
	
	
	private void setCharDetails() {
		
		Char c = mappings.get( navIndex );
		//c.printSequence();
		
		// set block image
		setMappingsBlockImageLbl(c);
		charBlockImgLbl.setToolTipText( GlobalConstants.MAPPING_BLOCK_REP_TOOLTIP );
		
		//charMappingTxt.setText( charMappingsArr[navIndex] );
		charMappingTxt.setText( c.getCharValue() );
		charMappingIndexLbl.setText( String.format( GlobalConstants.MAPPING_IDX_LBL_TXT, (navIndex+1), mappings.size(), c.getCharCode() ) );
		charInfoTxt.setText( String.format( GlobalConstants.MAPPING_INFO_LBL_TXT, c.getLineNumber(), c.getCharNumber(), c.getW(), c.getH(), c.getNoOfHBlocks(), c.getBlockLength(), c.getCharCode() ) );
		
		charMappingSavedLbl.setIcon(null);
		if( savedMappings.contains(navIndex) ) {
			charMappingSavedLbl.setIcon( new ImageIcon( GlobalConstants.CHAR_SAVED_ICO_FILE ) );
		}
	}
	
	
	
	private void setMappingsBlockImageLbl( Char c ) {
		
		Image blockImg = ImageUtils.getBlockImage( c, ocrResult.getVerticalBlocksPerChar() );
//		int w = -1;
//		int h = GlobalConstants.CHAR_BLOCK_IMG_LBL_H;
//		if(  c.getW() > c.getH() ) {
//			w = GlobalConstants.CHAR_BLOCK_IMG_LBL_W;
//			h = GlobalConstants.CHAR_BLOCK_IMG_LBL_H;
//		}
		int w = GlobalConstants.CHAR_BLOCK_IMG_LBL_W;
		int h = GlobalConstants.CHAR_BLOCK_IMG_LBL_H;
		Image resizedImage = blockImg.getScaledInstance( w, h, Image.SCALE_DEFAULT );
		ImageIcon blockImgIcon = new ImageIcon( resizedImage );
		charBlockImgLbl.setIcon(blockImgIcon);
	}
	
	

	
	
	
	private JButton createCharButton( String text, String actionCommand, String toolTip, ActionListener listener ) {
		JButton charBtn = new JButton();
		charBtn.setText(text);
		charBtn.setActionCommand(actionCommand);
		charBtn.setToolTipText(toolTip);
		charBtn.addActionListener(listener);
		return charBtn;
	}
	
	
	
	
	
}

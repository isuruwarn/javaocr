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
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.ocr.core.OCRHandler;
import com.ocr.core.OCRRequest;
import com.ocr.core.OCRResult;
import com.ocr.util.FileUtils;
import com.ocr.util.GlobalConstants;




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
		
		JPanel btnToolBar = new JPanel();
		btnToolBar.add(scanBtn);
		btnToolBar.add(copyBtn);
		btnToolBar.add(clearBtn);
		btnToolBar.add(viewDocImgBtn1);
		
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
					UIHelper.chooseFile( inputImgFileChooser, txtInputImagePath );
					clear();
					break;
				
				case GlobalConstants.CHOOSE_OUTPUT_FILE_ACTION:
					UIHelper.chooseFile( outputFileChooser, txtOutputFileName );
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
					
				case GlobalConstants.TRAIN_ACTION:
					openTrainingDialog();
					break;
			}
		}

	}
	
	
	
	// ******************************
	// start MainOCRListener methods 
	// ******************************
	
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
			
			viewDocImgBtn1.setEnabled(true);
			
		} catch( IOException ioe ) {
			JOptionPane.showMessageDialog( mainFrame, GlobalConstants.ERROR_LOADING_IMG_MSG, GlobalConstants.ERROR_LOADING_IMG_TITLE, JOptionPane.ERROR_MESSAGE );
		}
	}
	
	
	
	private void clear() {
		textPane.setText("");
		statusLbl.setText("");
		viewDocImgBtn1.setEnabled(false);
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void comboBoxChanged( ActionEvent e ) {
		JComboBox<String> selectBox = (JComboBox<String>) e.getSource();
		if( GlobalConstants.SEL_DIALECT_COMBOBOX.equals( selectBox.getName() ) ) {
			dialect = selectDialect.getSelectedItem().toString();
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
		//mappings = ocrResult.getRecognizedChars();
		//showMappingsDialog( GlobalConstants.KNOWN_MAPPINGS_TITLE );
	}
	
	
	
	
	private void mapUnknownChars() {
		//mappings = ocrResult.getUnrecognizedChars();
		//showMappingsDialog( GlobalConstants.UNKNOWN_MAPPINGS_TITLE );
	}
	
	
	private void viewDocumentImage() {
		UIHelper.viewImage( inputImage, 
				GlobalConstants.VIEW_DOC_IMG_ACTION, GlobalConstants.VIEW_DOC_IMG_LBL_W, GlobalConstants.VIEW_DOC_IMG_LBL_H,
				mainFrame );
	}
	
	
	private void openTrainingDialog() {
		new TrainingPopup( "", mainFrame );
	}
	
}

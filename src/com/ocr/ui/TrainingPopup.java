package com.ocr.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ocr.util.GlobalConstants;





public class TrainingPopup {
	
	
	private JButton trainingImgBtn;
	private JButton trainingTextBtn;
	private JTextField txtTrainingImgPath;
	private JTextField txtTrainingTextPath;
	private JTextArea txtStatus;
	private JFileChooser trainingImgFileChooser;
	private JFileChooser trainingTextFileChooser;
	
	
	public TrainingPopup( String title, JFrame frame ) {
		
		trainingImgFileChooser = new JFileChooser( GlobalConstants.SAMPLE_TRAINING_IMG_FILENAME );
		trainingTextFileChooser = new JFileChooser( GlobalConstants.SAMPLE_TRAINING_TEXT_FILENAME );
		TrainingPopupActionListener actionListener = new TrainingPopupActionListener();

		trainingImgBtn = new JButton( GlobalConstants.CHOOSE_TRAINING_IMG_ACTION );
		trainingImgBtn.addActionListener(actionListener);
		trainingImgBtn.setEnabled(true);
		
		trainingTextBtn = new JButton( GlobalConstants.CHOOSE_TRAINING_TEXT_ACTION );
		trainingTextBtn.addActionListener(actionListener); 
		trainingTextBtn.setEnabled(true);
		
		txtTrainingImgPath = new JTextField();
		txtTrainingImgPath.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtTrainingImgPath.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		
		txtTrainingTextPath = new JTextField();
		txtTrainingTextPath.setPreferredSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		txtTrainingTextPath.setMinimumSize( new Dimension( GlobalConstants.INPUT_IMG_PATH_TXT_W, GlobalConstants.INPUT_IMG_PATH_TXT_H ) );
		
		txtStatus = new JTextArea();
		txtStatus.setPreferredSize( new Dimension( GlobalConstants.TRAINING_STS_INFO_TXT_W, GlobalConstants.TRAINING_STS_INFO_TXT_H ) );
		txtStatus.setMinimumSize( new Dimension( GlobalConstants.TRAINING_STS_INFO_TXT_W, GlobalConstants.TRAINING_STS_INFO_TXT_H ) );
		txtStatus.setAlignmentX(JTextField.LEFT);
		txtStatus.setAlignmentY(JTextField.CENTER_ALIGNMENT);
		//charInfoTxt.setFont(infoFont);
		txtStatus.setForeground (Color.gray); // set text color
		txtStatus.setBackground( Color.white );
		txtStatus.setEnabled(true);
		
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
		
		GridBagConstraints outputFileBtnGridCons = new GridBagConstraints();
		outputFileBtnGridCons.gridx = 0;
		outputFileBtnGridCons.gridy = 1;
		outputFileBtnGridCons.insets = new Insets(0,0,0,0);
		
		GridBagConstraints txtOutputFilePathGridCons = new GridBagConstraints();
		txtOutputFilePathGridCons.gridx = 1;
		txtOutputFilePathGridCons.gridy = 1;
		txtOutputFilePathGridCons.insets = new Insets(0,0,0,10);
		
		JPanel fileChooserPanel = new JPanel();
		fileChooserPanel.setPreferredSize( new Dimension( GlobalConstants.TRAINING_FILE_BTN_PANEL_W, GlobalConstants.TRAINING_FILE_BTN_PANEL_H ) );
		fileChooserPanel.setMinimumSize( new Dimension( GlobalConstants.TRAINING_FILE_BTN_PANEL_W, GlobalConstants.TRAINING_FILE_BTN_PANEL_H ) );
		fileChooserPanel.setLayout( new GridBagLayout() );
		fileChooserPanel.add( trainingImgBtn, inputFileBtnGridCons );
		fileChooserPanel.add( txtTrainingImgPath, txtInputImgPathGridCons );
		fileChooserPanel.add( trainingTextBtn, outputFileBtnGridCons );
		fileChooserPanel.add( txtTrainingTextPath, txtOutputFilePathGridCons );
		
		JButton saveBtn = new JButton( GlobalConstants.START_TRAINING_ACTION );
		saveBtn.addActionListener(actionListener); 
		
		JPanel cmdBtnPanel = new JPanel();
		cmdBtnPanel.setPreferredSize( new Dimension( GlobalConstants.TRAINING_CMD_BTN_PANEL_W, GlobalConstants.TRAINING_CMD_BTN_PANEL_H ) );
		cmdBtnPanel.setMinimumSize( new Dimension( GlobalConstants.TRAINING_CMD_BTN_PANEL_W, GlobalConstants.TRAINING_CMD_BTN_PANEL_H ) );
		cmdBtnPanel.add(saveBtn);
		
		JPanel mainPanel = new JPanel();
		mainPanel.add(fileChooserPanel);
		mainPanel.add(cmdBtnPanel);
		mainPanel.add(txtStatus);
		mainPanel.setPreferredSize( new Dimension( GlobalConstants.TRAINING_POPUP_W, GlobalConstants.TRAINING_POPUP_H ) );
		mainPanel.setMinimumSize( new Dimension( GlobalConstants.TRAINING_POPUP_W, GlobalConstants.TRAINING_POPUP_H ) );
		
		JDialog mappingsDialog = new JDialog( frame, title, true );
		mappingsDialog.getContentPane().add(mainPanel);
		mappingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mappingsDialog.pack();
		mappingsDialog.setLocationRelativeTo(null); // position to center of screen
		mappingsDialog.setVisible(true);
	}
	
	
	
	

	
	class TrainingPopupActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			switch(command) {
				
				case GlobalConstants.CHOOSE_TRAINING_IMG_ACTION:
					UIHelper.chooseFile( trainingImgFileChooser, txtTrainingImgPath );
					//clear();
					break;
				
				case GlobalConstants.CHOOSE_TRAINING_TEXT_ACTION:
					UIHelper.chooseFile( trainingTextFileChooser, txtTrainingTextPath );
					break;
				
				case GlobalConstants.START_TRAINING_ACTION:
					
					break;
			}
		}

	}
	
	

}

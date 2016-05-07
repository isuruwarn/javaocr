package com.ocr.ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ocr.core.OCRResult;
import com.ocr.util.GlobalConstants;




public class CharMappingPopup {

	
	//private String title;
	
	
	
	public CharMappingPopup( String title, OCRResult ocrResult, JFrame mainFrame ) {
		
		//title = this.title;
		
		JPanel resolveMappingsPanel = new JPanel();
		resolveMappingsPanel.setPreferredSize( new Dimension( GlobalConstants.MAPPINGS_PANEL_W, GlobalConstants.MAPPINGS_PANEL_H ) );
		resolveMappingsPanel.setMinimumSize( new Dimension( GlobalConstants.MAPPINGS_PANEL_W, GlobalConstants.MAPPINGS_PANEL_H ) );
		resolveMappingsPanel.setLayout( new GridBagLayout() );
		
		JDialog mappingsDialog = new JDialog( mainFrame, title, true );
		mappingsDialog.getContentPane().add(resolveMappingsPanel);
		mappingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mappingsDialog.pack();
		mappingsDialog.setLocationRelativeTo(null); // position to center of screen
		mappingsDialog.setVisible(true);
		
		// TODO: check for unsaved mappings before close and inform user if needed
		
	}
	
	
	
	
}

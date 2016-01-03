package com.ocr.ui.components;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;



@SuppressWarnings("serial")
public class CommandButton extends JButton {

	
	public CommandButton( String actionCommand ) {
		this.setText(actionCommand);
		this.setActionCommand(actionCommand);
		this.setMargin( new Insets( 5, 5, 5, 5 ) );
	}
	
	public CommandButton( String actionCommand, ActionListener listener ) {
		this.setText(actionCommand);
		this.setActionCommand(actionCommand);
		this.addActionListener(listener);
		this.setMargin( new Insets( 5, 5, 5, 5 ) );
	}
	

	public CommandButton( String actionCommand, String imagePath ) {
		this.setToolTipText(actionCommand);
		this.setActionCommand(actionCommand);
		this.setIcon( new ImageIcon(imagePath) );
		this.setMargin( new Insets( 5, 5, 5, 5 ) );
	}
	
	
	public CommandButton( String actionCommand, ActionListener listener, String imagePath ) {
		this.setToolTipText(actionCommand);
		this.setActionCommand(actionCommand);
		this.addActionListener(listener);
		this.setIcon( new ImageIcon(imagePath) );
		this.setMargin( new Insets( 5, 5, 5, 5 ) );
	}
	
	
	
}

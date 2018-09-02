package org.warn.ocr.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;





public class UIHelper {
	
	
	
	public static void addActionListener( ActionListener listener, AbstractButton ... items ) {
		for( AbstractButton item: items ) {
			item.addActionListener(listener);
		}
	}
	
	

	public static void viewImage( Image img, String msg, int w, int h, JFrame frame ) {
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
		JOptionPane.showMessageDialog( frame, scrollPane, msg, JOptionPane.INFORMATION_MESSAGE );
	}
	
	

	public static void chooseFile( JFileChooser jfc, JTextField txtBox ) {
		int returnVal = jfc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = jfc.getSelectedFile();
        	String filePath = file.getPath();
        	txtBox.setText( filePath ); 
        }
	}
	

}

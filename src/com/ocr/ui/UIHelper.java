package com.ocr.ui;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;




public class UIHelper {
	
	public static void addActionListener( ActionListener listener, AbstractButton ... items ) {
		
		for( AbstractButton item: items ) {
			item.addActionListener(listener);
		}
	}

}

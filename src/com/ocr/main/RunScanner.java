package com.ocr.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ocr.ui.UIContainer;


public class RunScanner {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("Error - " + e.getMessage());
			//UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		
    	javax.swing.SwingUtilities.invokeLater( new Runnable() {
    		
    		public void run() {
    			new UIContainer();
            }
    		
        });
    }
	
}

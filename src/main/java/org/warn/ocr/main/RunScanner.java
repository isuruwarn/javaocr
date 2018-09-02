package org.warn.ocr.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.warn.ocr.ui.UIContainer;


public class RunScanner {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("Error - " + e.getMessage());
			
		}
		
    	javax.swing.SwingUtilities.invokeLater( new Runnable() {
    		
    		public void run() {
    			new UIContainer();
            }
    		
        });
    }
	
}

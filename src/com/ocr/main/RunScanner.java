package com.ocr.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ocr.ui.UIContainer;


public class RunScanner {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
    	javax.swing.SwingUtilities.invokeLater( new Runnable() {
    		
    		public void run() {
    			
    			/*
    			String imgFile = "img/sampleImg9.png";
    			String mapFile = "files/eng15.map";
    			
    			Scanner scn = new Scanner();
    			BufferedImage image = ScanUtils.loadImage( imgFile );
    			ArrayList<Line> lines = scn.init( image );
    			StringBuilder sb = scn.readCharacters( lines, mapFile ); 
    			System.out.println(sb);
    			*/
    			
    			new UIContainer();
            }
    		
        });
    }
	
}

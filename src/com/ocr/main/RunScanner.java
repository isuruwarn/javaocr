package com.ocr.main;

import com.ocr.ui.UIContainer;


public class RunScanner {

	public static void main(String[] args) {

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

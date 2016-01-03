package com.ocr.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.ocr.core.Line;
import com.ocr.core.Scanner;
import com.ocr.util.GlobalConstants;
import com.ocr.util.ScanUtils;


public class ScanListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		
		switch(command) {
			
			case GlobalConstants.SCAN_ACTION:
				String imgFile = "img/sampleImg9.png";
    			String mapFile = "files/eng15.map";
    			Scanner scn = new Scanner();
    			BufferedImage image = ScanUtils.loadImage( imgFile );
    			ArrayList<Line> lines = scn.init( image );
    			StringBuilder sb = scn.readCharacters( lines, mapFile ); 
    			System.out.println(sb);
				break;
			
		}
	}

}

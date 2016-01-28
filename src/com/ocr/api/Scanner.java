package com.ocr.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.ocr.core.Char;
import com.ocr.core.Line;
import com.ocr.core.MappingsFile;




/**
 * 
 * 
 * @author isuru
 *
 */
public interface Scanner {
	
	StringBuilder scan( BufferedImage inputImage, String dialect );
	
	int getHeight();
	
	int getWidth();
	
	ArrayList<Line> getLines();
	
	int getLinesRead();
	
	int getCharsRead();
	
	ArrayList<Char> getUnrecognizedChars();
	
	int getMinBlanklineHeight();
	
	void setMinBlanklineHeight( int minBlanklineHeight );
	
	int getMinWhitespaceWidth();
	
	void setMinWhitespaceWidth(int minWhitespaceWidth);
	
	int getVerticalBlocksPerChar();
	
	void setVerticalBlocksPerChar(int verticalBlocksPerChar);
	
	MappingsFile getMappingsFile();
	
}

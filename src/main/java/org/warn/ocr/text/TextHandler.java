package org.warn.ocr.text;

import java.util.HashMap;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;


public class TextHandler {
	
	
	private static HashMap<String, String> characterFixMap;
	
	
	static {
		characterFixMap = new HashMap<String, String>();
		characterFixMap.put( "\u0D85\u0DCF", Symbol.AAYANNA.getUnicodeValue() );
		characterFixMap.put( "\u0D85\u0DD0", Symbol.AEYANNA.getUnicodeValue() );
		characterFixMap.put( "\u0D85\u0DD1", Symbol.AEEYANNA.getUnicodeValue() );
		characterFixMap.put( "\u0D91\u0DCA", Symbol.EEYANNA.getUnicodeValue() );
		characterFixMap.put( "\u0D91\u0DD9", Symbol.AIYANNA.getUnicodeValue() );
		characterFixMap.put( "\u0D94\u0DCA", Symbol.OOYANNA.getUnicodeValue() ); 
		characterFixMap.put( "\u0D94\u0DDF", Symbol.AUYANNA.getUnicodeValue() );
		characterFixMap.put( "\u0DD9\u0DD9", Symbol.KOMBU_DEKA.getUnicodeValue() ); 
		characterFixMap.put( "\u0DD9\u0DCA", Symbol.DIGA_KOMBUVA.getUnicodeValue() ); 
		characterFixMap.put( "\u0DD9\u0DCF", Symbol.KOMBUVA_HAA_AELA_PILLA.getUnicodeValue() ); 
		characterFixMap.put( "\u0DD9\u0DCF\u0DCA", Symbol.KOMBUVA_HAA_DIGA_AELA_PILLA.getUnicodeValue() ); 
		characterFixMap.put( "\u0DD9\u0DDF", Symbol.KOMBUVA_HAA_GAYANUKITTA.getUnicodeValue() );
	}

	
	
	public static String getSymbol( String inputKey ) {
		
		for(Symbol symbol: Symbol.values() ) {
			if( symbol.getKeyCode().endsWith( inputKey ) ) {
				return symbol.getUnicodeValue();
			}
		}
		return null;
	}
	
	
	
	public static void insertText( String inputKey, JTextField textBox ) {
		
		try {
	    	int cursorPos = textBox.getCaretPosition();
	    	Element charElement = textBox.getDocument().getDefaultRootElement();
			AttributeSet attributes = charElement.getAttributes();
	    	String newChar = getSymbol(inputKey);
			textBox.getDocument().insertString( cursorPos, newChar, attributes );
			fixUnicodeConversionIssues( textBox );
			
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	public static void fixUnicodeConversionIssues( JTextField textBox ) {
		
		int documentLength = textBox.getDocument().getLength();
		String text = "";
		try {
			text = textBox.getDocument().getText( 0, documentLength );
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		for( String key: characterFixMap.keySet() ) {
			
			int index = text.indexOf(key);
			int length = key.length();
			
			if( index > 0 ) {
				try {
					Element charElement = textBox.getDocument().getDefaultRootElement();
					AttributeSet attributes = charElement.getAttributes();
					textBox.getDocument().remove( index, length );
					textBox.getDocument().insertString( index, characterFixMap.get(key), attributes );
					
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
		
		textBox.requestFocusInWindow();
	}
	
	
	
}

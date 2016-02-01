package com.ocr.exceptions;

public class ScanException extends Exception {

	
	private static final long serialVersionUID = 1L;
	
	
	public ScanException() {
		
	}
	
	
	public ScanException(String message) {
		super(message);
	}
	
	
	public ScanException(Throwable throwable) {
		super(throwable);
	}

}

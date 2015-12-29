package com.ocr.core;

/**
 * Represents an item in the image
 * 
 * @author isuru
 *
 */
public abstract class Item {
	
	private int x;
	private int y;
	private int w;
	private int h;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	
}

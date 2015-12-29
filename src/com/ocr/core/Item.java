package com.ocr.core;

public abstract class Item {
	
	private int x;
	private int y;
	private int w;
	private int h;
	private int startI;
	private int startJ;
	private int endI;
	private int endJ;
	
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
	public int getStartI() {
		return startI;
	}
	public void setStartI(int startI) {
		this.startI = startI;
	}
	public int getStartJ() {
		return startJ;
	}
	public void setStartJ(int startJ) {
		this.startJ = startJ;
	}
	public int getEndI() {
		return endI;
	}
	public void setEndI(int endI) {
		this.endI = endI;
	}
	public int getEndJ() {
		return endJ;
	}
	public void setEndJ(int endJ) {
		this.endJ = endJ;
	}
	
}

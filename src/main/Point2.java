package main;

import java.awt.Point;

public class Point2 extends Point{
	String direction;
	int counter = 0;
	
	public Point2(int X, int Y, String direction) {
		x = X;
		y = Y;
		this.direction = direction;
	}
}

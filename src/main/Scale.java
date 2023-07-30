package main;

import java.awt.geom.Ellipse2D;

public class Scale extends Ellipse2D.Double{
	String direction = "RIGHT";
	GamePanel gp;
	KeyHandler KeyH;
	public Scale(int X, int Y, int snakeWidth, int snakeHeight, GamePanel gp) {
		super(X, Y, snakeWidth, snakeHeight);
		this.gp = gp;
		KeyH = gp.KeyH;
	}
	public void move(int speed) {
		switch(direction) {
		case "UP":
			y = y - speed;
			break;
		case "DOWN":
			y = y + speed;
			break;
		case "RIGHT":
			x = x + speed;
			break;
		case "LEFT":
			x = x - speed;
			break;
		default:
			System.out.println("Direction Error In Class Scale.");
		}
	}
}

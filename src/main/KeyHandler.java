package main; 

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed = false;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	public String currentDirection() {
		String dir = "";
		if(this.upPressed) {
			dir = "UP";
		}
		else if(this.downPressed) {
			dir = "DOWN";
		}
		else if(this.rightPressed) {
			dir = "RIGHT";
		}
		else if(this.leftPressed) {
			dir = "LEFT";
		}
		return dir;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP && !downPressed && gp.smoothCounter >= gp.snakeSize * 1.5) {
			upPressed = true;
			downPressed = false;
			leftPressed = false;
			rightPressed = false;
			String direction = currentDirection();
			gp.directionPoints.add(new Point2((int) gp.snake.get(0).getX(), (int) gp.snake.get(0).getY(), direction));
		}
		else if(keyCode == KeyEvent.VK_DOWN && !upPressed && gp.smoothCounter >= gp.snakeSize * 1.5) {
			upPressed = false;
			downPressed = true;
			leftPressed = false;
			rightPressed = false;
			String direction = currentDirection();
			gp.directionPoints.add(new Point2((int) gp.snake.get(0).getX(), (int) gp.snake.get(0).getY(), direction));
			gp.smoothCounter = 0;
		}
		else if(keyCode == KeyEvent.VK_RIGHT && !leftPressed && gp.smoothCounter >= gp.snakeSize * 1.5) {
			upPressed = false;
			downPressed = false;
			leftPressed = false;
			rightPressed = true;
			String direction = currentDirection();
			gp.directionPoints.add(new Point2((int) gp.snake.get(0).getX(), (int) gp.snake.get(0).getY(), direction));
			gp.smoothCounter = 0;
		}
		else if(keyCode == KeyEvent.VK_LEFT && !rightPressed && gp.smoothCounter > gp.snakeSize * 1.5) {
			upPressed = false;
			downPressed = false;
			leftPressed = true;
			rightPressed = false;
			String direction = currentDirection();
			gp.directionPoints.add(new Point2((int) gp.snake.get(0).getX(), (int) gp.snake.get(0).getY(), direction));
			gp.smoothCounter = 0;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}

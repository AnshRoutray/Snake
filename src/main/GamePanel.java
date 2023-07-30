package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	public final int tileSize = 48;
	public final int screenCol = 12;
	public final int screenRow = 12;
	public final int screenWidth = tileSize * screenRow;
	public final int screenHeight = tileSize * screenCol;
	public int FPS = 60;
	public int snakeSize = 21;
	public int foodSize = 21;
	public int snakeSpeed = 3;
	public int smoothCounter = 0;
	public int score = 0;
	
	public KeyHandler KeyH = new KeyHandler(this);
	public Thread gameThread;
	public ArrayList<Scale> snake = new ArrayList<>();
	public ArrayList<Point2> directionPoints = new ArrayList<>();
	public Rectangle food = new Rectangle(0, 0, foodSize, foodSize);
	public JLabel scoreLabel = new JLabel("<html><font color = 'blue'><h2>Score: " + score + "</h2</font></html>");

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.addKeyListener(KeyH);
		this.setBackground(Color.green);
		this.setVisible(true);
		this.add(scoreLabel);
		scoreLabel.setVisible(true);
		scoreLabel.setBounds(screenHeight - 100, 200, 100, 40);
	}
	
    public boolean doEllipsesCollide(Ellipse2D ellipse1, Ellipse2D ellipse2) {
        double center1X = ellipse1.getCenterX();
        double center1Y = ellipse1.getCenterY();
        double center2X = ellipse2.getCenterX();
        double center2Y = ellipse2.getCenterY();

        double radius1X = ellipse1.getWidth() / 2;
        double radius1Y = ellipse1.getHeight() / 2;
        double radius2X = ellipse2.getWidth() / 2;
        double radius2Y = ellipse2.getHeight() / 2;

        double distanceX = Math.abs(center1X - center2X);
        double distanceY = Math.abs(center1Y - center2Y);

        double sumRadiiX = radius1X + radius2X;
        double sumRadiiY = radius1Y + radius2Y;

        return (distanceX <= sumRadiiX) && (distanceY <= sumRadiiY);
    }
	
	public boolean collidesFood(ArrayList<Scale> snake, Rectangle food) {
		boolean collided = false;
		for(int i = 0; i < snake.size(); i++) {
			if (snake.get(i).intersects(food)) {
				collided = true;
				break;
			}
		}
		return collided;
	}
	
	public boolean collidesHead(ArrayList<Scale> snake) {
		boolean collided = false;
		Scale firstScale = snake.get(0);
		if(firstScale.x >= screenWidth || firstScale.y >= screenHeight || firstScale.x < 0 || firstScale.y < 0){
			return true;
		}
		for(int i = 3; i < snake.size(); i++) {
			Scale scale = snake.get(i);
			if(doEllipsesCollide(firstScale, snake.get(i))){
				collided = true;
				break;
			}
		}
		return collided;
	}
	
	public void addScale() { 
		Scale lastScale = snake.get(snake.size() - 1);
		Scale newScale;
		switch(lastScale.direction) {
		case "UP":
			newScale = new Scale((int)lastScale.x, (int)lastScale.y + snakeSize, snakeSize, snakeSize, this);
			newScale.direction = lastScale.direction;
			snake.add(newScale);
			break;
		case "DOWN":
			newScale = new Scale((int)lastScale.x, (int)lastScale.y - snakeSize, snakeSize, snakeSize, this);
			newScale.direction = lastScale.direction;
			snake.add(newScale);
			break;
		case "RIGHT":
			newScale = new Scale((int)lastScale.x - snakeSize, (int)lastScale.y, snakeSize, snakeSize, this);
			newScale.direction = lastScale.direction;
			snake.add(newScale);
			break;
		case "LEFT":
			newScale = new Scale((int)lastScale.x + snakeSize, (int)lastScale.y, snakeSize, snakeSize, this);
			newScale.direction = lastScale.direction;
			snake.add(newScale);
			break;
		}
	}
	
	public void setUpGame() {
		int snakeBlocks = 3;
		for(int i = 200; i > 200 - (snakeSize * snakeBlocks); i -= snakeSize) {
			snake.add(new Scale(i, 200, snakeSize, snakeSize, this));
		}
		do {
			food.x = (int) (Math.random() * (screenWidth - 75) + snakeSize);
			food.y = (int) (Math.random() * (screenHeight - 75) + snakeSize);
		}while(collidesFood(snake, food));
	}
	
	public void startGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	
	public void update() {
		for(int i = 0; i < snake.size(); i++) {
			for(int j = 0; j < directionPoints.size(); j++) {
				if(snake.get(i).getX() == directionPoints.get(j).x && snake.get(i).getY() == directionPoints.get(j).y) {
					snake.get(i).direction = directionPoints.get(j).direction;
					directionPoints.get(j).counter++;
				}
				if(directionPoints.get(j).counter == snake.size()){
					directionPoints.remove(j);
				}
			}
		}
		for(int i = 0; i < snake.size(); i++) {
			snake.get(i).move(snakeSpeed);
		}
		
		if(collidesFood(snake, food)) {
			do {
				food.x = (int) (Math.random() * (screenWidth - 75) + snakeSize);
				food.y = (int) (Math.random() * (screenHeight - 75) + snakeSize);
			}while(collidesFood(snake, food));
			score++;
			addScale();
		}
		
		if(collidesHead(snake)) {
			gameThread.stop();
		}
		
		scoreLabel.setText("<html><h2>Score: " + score + "</h2></html>");
		
		smoothCounter = smoothCounter + snakeSpeed;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.red);
		for(int i = 0; i < snake.size(); i++) {
			g2.fill(snake.get(i));
		}
		g2.setColor(Color.blue);
		g2.fill(food);
	}

}
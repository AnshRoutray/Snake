package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String args[]) {
		JFrame window = new JFrame();
		GamePanel gamePanel = new GamePanel();
		
		window.add(gamePanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setTitle("Snake");
		
		gamePanel.setUpGame();
		gamePanel.startGame();
	}
}

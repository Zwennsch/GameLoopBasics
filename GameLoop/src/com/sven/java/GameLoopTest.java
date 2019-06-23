package com.sven.java;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sven.java.GameLoopTest.GamePanel;

public class GameLoopTest extends JFrame implements ActionListener{
	

	private GamePanel gamePanel = new GamePanel();
	private JButton startButton = new JButton("Start");
	private JButton quitButton = new JButton("Quit");
	private JButton pauseButton = new JButton("Pause");
	
	public GameLoopTest() {
		super("Fixed Timestep GameLoop Test");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(startButton);
		panel.add(quitButton);
		panel.add(pauseButton);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(gamePanel, BorderLayout.CENTER);
		container.add(panel, BorderLayout.SOUTH);
		setSize(500, 500);
		
		startButton.addActionListener(this);
		quitButton.addActionListener(this);
		pauseButton.addActionListener(this);
		
	}

	public static void main(String[] args) {
		GameLoopTest glt = new GameLoopTest();
		glt.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public class GamePanel extends JPanel {
		float interpolation;
		float ballX, ballY, lastBallX, lastBallY;
		int ballWidth, ballHeight;
		float ballVelX, ballVelY;
		float ballSpeed;
		
		public GamePanel() {
			ballX = lastBallX = 100;
			ballY = lastBallY = 100;
			ballWidth = 25;
			ballHeight = 25;
			ballSpeed = 25;
			ballVelX = (float) (Math.random()* ballSpeed*2 -ballSpeed);
			ballVelY = (float) (Math.random()* ballSpeed*2 -ballSpeed);
			
		}
		
	}
}

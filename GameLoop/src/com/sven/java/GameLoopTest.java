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
	
	private boolean running = false;
	private boolean paused = false;
	
	public GameLoopTest() {
		super("Fixed Timestep GameLoop Test");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		panel.add(startButton);
		panel.add(pauseButton);
		panel.add(quitButton);
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
		Object s = e.getSource();
		if (s.equals(startButton)) {
			System.out.println("Start Button clicked");
			running = !running;
			if(running) {
				startButton.setText("Stop");
				runGameLoop();
			}else {
				startButton.setText("Start");
			}
		}else if (s.equals(pauseButton)) {
			paused = !paused;
			if(paused) {
				pauseButton.setText("Continue");
			}else {
				pauseButton.setText("Pause");
			}
		}else if(s.equals(quitButton)) {
			System.exit(0);
		}
		
	}

	private void runGameLoop() {
		// TODO Auto-generated method stub
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}
	private void gameLoop() {
		// TODO Auto-generated method stub
		final double GAME_HERTZ = 30.0;
		final double TIME_BETWEEN_UPDATES = 1000000000 /GAME_HERTZ;
		final int MAX_UPDATES_PER_RENDER = 5;
		
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
		
		int lastSecondTime = (int) (lastUpdateTime /1000000000);
		
		while(running) {
			double now = System.nanoTime();
			int updateCount = 0;
			while(now-lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_PER_RENDER) {
				
			}
		}
		
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

package com.sven.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameLoopTest extends JFrame implements ActionListener{
	

	private GamePanel gamePanel = new GamePanel();
	private JButton startButton = new JButton("Start");
	private JButton quitButton = new JButton("Quit");
	private JButton pauseButton = new JButton("Pause");
	
	private boolean running = false;
	private boolean paused = false;
	
	private int fps = 60;
	private int frameCount = 0;
	
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
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}
	private void gameLoop() {
		// this is equivalent to the game speed; how many times the update-game method is called per second
		final double GAME_HERTZ = 30.0;
//		get the nano seconds each update should take
		final double TIME_BETWEEN_UPDATES = 1000000000 /GAME_HERTZ;
		final int MAX_UPDATES_PER_RENDER = 5;
//		store the last time we updated
		double lastUpdateTime = System.nanoTime();
//		store the last time we rendered
		double lastRenderTime = System.nanoTime();
//		set the target FPS e.g. how many times the render method should be called per second
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
//		store the last time in seconds
		int lastSecondTime = (int) (lastUpdateTime /1000000000);
		
		while(running) {
			getToolkit().sync();
			double now = System.nanoTime();
			int updateCount = 0;
			System.out.println(fps);
			
			if(!paused) {
				//Do as many game updates as we need to, potentially playing catchup.
				while(now-lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_PER_RENDER) {
					updateGame();
					lastUpdateTime+= TIME_BETWEEN_UPDATES;
					updateCount++;
				}
//			if an update should for some reason take forever and we haven't reached the max_updates_per_render:
//			dont't do this, if you need the EXACT update time!!
				if(now-lastUpdateTime > TIME_BETWEEN_UPDATES) {
					System.out.println("Update take too long!");
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}
//			Render. to do so we need to calculate the interpolation
				float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) /TIME_BETWEEN_UPDATES));
				drawGame(interpolation);
//				I am not sure why the lastRenderTime is equal to now!? Shouldn't it be System.nanoTime()?
				lastRenderTime = now;
				
//				update the FPS; this is only needed , if the actual second we're in is greater than the lastSecondTime. I am still not exactly sure, why thisSecond isn't System.nanoTime()?
				int thisSecond = (int) (lastUpdateTime/1000000000);
				if(thisSecond > lastSecondTime) {
					System.out.println("NEW SECOND: "+ thisSecond + " " +frameCount);
//					since we do this every new second, the fps is essentially the frameCount
					fps = frameCount;
					frameCount =0;
					lastSecondTime = thisSecond;
				}
//				Yield until we have at least reached the target time between renders and target time between updates
				while(now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();
				 //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
               //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
               //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
	                try {Thread.sleep(1);} catch(Exception e) {} 
//		            
	                now = System.nanoTime();
				}
				
			}
			
		}
		
	}

	private void drawGame(float interpolation) {
		gamePanel.setInterpolation(interpolation);
		gamePanel.repaint();
	}

	private void updateGame() {
		System.out.println("Updating the gameLogic");
		gamePanel.update();
	}

	private class GamePanel extends JPanel {
		float interpolation;
		float ballX, ballY, lastBallX, lastBallY;
		int ballWidth, ballHeight;
		float ballVelX, ballVelY;
		float ballSpeed;
		
		int lastDrawX, lastDrawY;
		
		
		public GamePanel() {
			ballX = lastBallX = 100;
			ballY = lastBallY = 100;
			ballWidth = 25;
			ballHeight = 25;
			ballSpeed = 25;
			ballVelX = (float) (Math.random()* ballSpeed*2 -ballSpeed);
			ballVelY = (float) (Math.random()* ballSpeed*2 -ballSpeed);
			
		}


		public void setInterpolation(float interpolation) {
			this.interpolation = interpolation;
		}


		public void update() {
			lastBallX = ballX;
			lastBallY = ballY;
			
			ballX += ballVelX;
			ballY += ballVelY;
			
			if(ballX + ballWidth/2 >= getWidth()) {
				ballVelX *=-1;
				ballX = getWidth() - ballWidth/2;
				ballVelY = (float) Math.random()*ballSpeed*2 - ballSpeed;
			}else if (ballX - ballWidth/2 <=0) {
				ballVelX *=-1;
				ballX = ballWidth/2;
			}
			if(ballY + ballHeight/2 >= getHeight()) {
				ballVelY *= -1;
				ballY = getHeight() - ballHeight/2;
				ballVelX = (float) Math.random()*ballSpeed*2 - ballSpeed;
			}else if (ballY - ballHeight/2 <= 0) {
				ballVelY*=-1;
				ballY = ballHeight/2;
			}
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(getBackground());
			g.fillRect(lastDrawX-1, lastDrawY-1, ballWidth+2, ballHeight+2);
			g.fillRect(5, 0, 75, 30);
			
			g.setColor(Color.RED);
//			calculate the x and y coordinates for drawing the ball
			int drawX = (int) ((ballX - lastBallX)* interpolation + lastBallX -ballWidth/2);
			int drawY = (int) ((ballY - lastBallY)* interpolation + lastBallY -ballHeight/2);
			g.fillOval(drawX, drawY, ballWidth, ballHeight);
			
			lastDrawX = drawX;
			lastDrawY = drawY;
			
			g.setColor(Color.black);
			g.drawString("FPS: "+ fps, 5, 10);
			
			frameCount++;
		}
		
	}
}

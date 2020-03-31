package snakeGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


public class SnakeGame extends JComponent implements KeyListener {
	public static int frameWidth = 300;
	public static int frameHeight = 300;
	protected int BODY_SIZE = 10;
	protected int speed = BODY_SIZE;
	protected int limitWall = 5;
	protected static int score;
	protected int timeDelay = 150;
	protected List<Point> snakeBody = new ArrayList<Point>();
	protected List<Point> wall = new ArrayList<Point>();
	protected List<Point> food = new ArrayList<Point>();
	protected Timer timer;
	protected Timer wallTimer;
	protected String direction = "UP";

	public SnakeGame() {
		addKeyListener(this);
		setFocusable(true);

		snakeBody.add(new Point(Math.floorDiv(frameWidth / 2, BODY_SIZE) * BODY_SIZE,
				Math.floorDiv(frameHeight / 2, BODY_SIZE) * BODY_SIZE));// The start point is at the middle of the
																		// window
		timer = new Timer(timeDelay, new TimerCallBack());
		
		// remove each wall and food after 10 sec
		wallTimer = new Timer(10000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!(wall.size() == 0)) {
//						wall.remove(wall.size() - 1);
					wall.remove(0);
				}
			if(!(food.size() == 0)) {
//				food.remove(food.size() - 1);
				food.remove(0);
			}
				

			}
		});

		timer.start();
		wallTimer.start();

	}

	public void paintComponent(Graphics g) {

		// Draw the snake
		for (int i = 0; i < snakeBody.size(); i++) {
			g.setColor(Color.BLACK);
			g.drawRect(snakeBody.get(i).x, snakeBody.get(i).y, BODY_SIZE, BODY_SIZE);
		}

		// Create wall
		createWall();
		// Draw wall
		for (int i = 0; i < wall.size(); i++) {
			g.setColor(Color.BLACK);
			g.drawRect(wall.get(i).x, wall.get(i).y, BODY_SIZE, BODY_SIZE);
			g.setColor(Color.RED);
			g.fillRect(wall.get(i).x, wall.get(i).y, BODY_SIZE, BODY_SIZE);
		}

		// Create food
		createFood();
		// Draw food
		for (int i = 0; i < food.size(); i++) {
			g.setColor(Color.BLACK);
			g.drawRect(food.get(i).x, food.get(i).y, BODY_SIZE, BODY_SIZE);
			g.setColor(Color.GREEN);
			g.fillRect(food.get(i).x, food.get(i).y, BODY_SIZE, BODY_SIZE);
		}

	}

	protected class TimerCallBack implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			move();
			stop();
			repaint();
		}
	}

	// Move the snake
	public void move() {
		int headX = snakeBody.get(0).x;
		int headY = snakeBody.get(0).y;

		// Move the body

		switch (direction) {
		case "UP":
			snakeBody.add(0, new Point(headX, headY - speed));
			snakeBody.remove(snakeBody.size() - 1);

			// Eating food
			for (int j = 0; j < food.size(); j++) {
				if (snakeBody.get(0).x == food.get(j).x && snakeBody.get(0).y <= food.get(j).y + BODY_SIZE) {
					snakeBody.add(new Point(snakeBody.get(snakeBody.size() - 1).x,
							snakeBody.get(snakeBody.size() - 1).y + BODY_SIZE));
					food.remove(j);
					score += 10;
					System.out.println(score);
				}
			}
			break;

		case "DOWN":
			snakeBody.add(0, new Point(headX, headY + speed));
			snakeBody.remove(snakeBody.size() - 1);

			// Eating food
			for (int j = 0; j < food.size(); j++) {
				if (snakeBody.get(0).x == food.get(j).x && snakeBody.get(0).y >= food.get(j).y - BODY_SIZE) {
					snakeBody.add(new Point(snakeBody.get(snakeBody.size() - 1).x,
							snakeBody.get(snakeBody.size() - 1).y - BODY_SIZE));
					food.remove(j);
					score += 10;
					System.out.println(score);
				}
			}

			break;

		case "LEFT":
			snakeBody.add(0, new Point(headX - speed, headY));
			snakeBody.remove(snakeBody.size() - 1);

			// Eating food
			for (int j = 0; j < food.size(); j++) {
				if (snakeBody.get(0).x <= food.get(j).x + BODY_SIZE && snakeBody.get(0).y == food.get(j).y) {
					snakeBody.add(new Point(snakeBody.get(snakeBody.size() - 1).x + BODY_SIZE,
							snakeBody.get(snakeBody.size() - 1).y));
					food.remove(j);
					score += 10;
					System.out.println(score);
				}
			}
			break;

		case "RIGHT":
			snakeBody.add(0, new Point(headX + speed, headY));
			snakeBody.remove(snakeBody.size() - 1);

			// Eating food
			for (int j = 0; j < food.size(); j++) {
				if (snakeBody.get(0).x >= food.get(j).x - BODY_SIZE && snakeBody.get(0).y == food.get(j).y) {
					snakeBody.add(new Point(snakeBody.get(snakeBody.size() - 1).x - BODY_SIZE,
							snakeBody.get(snakeBody.size() - 1).y));
					food.remove(j);
					score += 10;
					System.out.println(score);
				}
			}
			break;
		}

	}

	// Stop if the snake touch the obstacles
	public void stop() {

		// When touch itself
		for (int i = 1; i < snakeBody.size(); i++) {
			if (snakeBody.get(0).equals(snakeBody.get(i))) {
				timer.stop();
				System.out.println(" Bite itself");
			}
		}

		// When touch the frame
		if (snakeBody.get(0).x < 0 || snakeBody.get(0).x > frameWidth - BODY_SIZE || snakeBody.get(0).y < 0
				|| snakeBody.get(0).y > frameHeight - BODY_SIZE) {
			timer.stop();
			System.out.println("Hit the boundary");
		}

		// When touch the walls
		for (int i = 0; i < wall.size(); i++) {
			if (snakeBody.get(0).equals(wall.get(i))) {
				timer.stop();
				System.out.println("Hit the wall");
			}
		}
	}

	// Randomize a new Point
	public Point randomPoint() {
		int x = Math.floorDiv((int) (Math.random() * ((frameWidth - BODY_SIZE) + 1)), BODY_SIZE) * BODY_SIZE;
		int y = Math.floorDiv((int) (Math.random() * ((frameHeight - BODY_SIZE) + 1)), BODY_SIZE) * BODY_SIZE;

		Point point = new Point(x, y);
		for (int i = 0; i < snakeBody.size(); i++) {
			if (point.equals(snakeBody.get(i))) {
				randomPoint();
			}
		}
		for (int i = 0; i < food.size(); i++) {
			if (point.equals(food.get(i))) {
				randomPoint();
			}
		}
		return point;
	}

	// Randomize food, there are at most 2 food at a time
	public void createFood() {

		if (food.size() < 2) {

			double random = Math.random();
			if (random > 0.93) {
				food.add(randomPoint());

			}
		}
	}

	// Randomize wall, no more than 2 walls at a time
	public void createWall() {
		if(score > 100) {
			limitWall = 6;
		}else if(score >150) {
			limitWall = 8;
		}
		if (wall.size() < limitWall) {
			double rand = Math.random();
			if (rand > 0.85) {
				Point randPoint = randomPoint();
				int num = (int) (Math.random() * 3 + 1);
				switch (num) {
				case 1:
					wall.add(randPoint);
					break;
				case 2:
					wall.add(randPoint);
					wall.add(new Point(randPoint.x, randPoint.y + BODY_SIZE));
					wall.add(new Point(randPoint.x, randPoint.y + 2 * BODY_SIZE));
					wall.add(new Point(randPoint.x, randPoint.y + 3 * BODY_SIZE));
					break;
				case 3:
					wall.add(randPoint);
					wall.add(new Point(randPoint.x + BODY_SIZE, randPoint.y));
					wall.add(new Point(randPoint.x + 2 * BODY_SIZE, randPoint.y));
					wall.add(new Point(randPoint.x + 3 * BODY_SIZE, randPoint.y));
					break;
					
				}

				// Remove if wall has same coordinate with snake
				for (int i = 0; i < wall.size(); i++) {
					for (int j = 0; j < snakeBody.size(); j++) {
						if (wall.get(i).equals(snakeBody.get(j))) {
							wall.remove(i);
						}
					}
				}
				
				// Remove if wall has same coordinate with food
				for (int i = 0; i < wall.size(); i++) {
					for (int j = 0; j < food.size(); j++) {
						if (wall.get(i).equals(food.get(j))) {
							wall.remove(i);
						}
					}
				}
			}
		}
	}

	public static void main(String args[]) {

		JFrame gameWindow = new JFrame("Snake Game");
		gameWindow.setSize(frameWidth, frameHeight);
		
		JLabel label = new JLabel("Score");
		
		gameWindow.add(label);
		SnakeGame game = new SnakeGame();
		gameWindow.add(game);
		
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

		switch (e.getKeyCode()) {
		case 37:
			if (!direction.equals("RIGHT")) {
				direction = "LEFT";
			}

			break;
		case 38:
			if (!direction.equals("DOWN")) {
				direction = "UP";
			}

			break;
		case 39:
			if (!direction.equals("LEFT")) {
				direction = "RIGHT";
			}
			break;
		case 40:
			if (!direction.equals("UP")) {
				direction = "DOWN";
			}
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}

package Client;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import org.json.JSONArray;

import Assets.Player;
import Assets.World;
import Server.Consts;

// this class is used for painting the game 
// and managing user input 
// while game is playing
public class GameScreen extends JPanel implements KeyListener, Runnable {

	// for repaint purposes
	private Game motherGame;

	private World myWorld;
	private Player myPlayer;
//	private int health = 3;
//	private int deltax = 0, deltay = 0;
//	private int myX, myY;

	// otherplayer
	private boolean otherPlayerSeen;
	private Player otherPlayer;

	private Thread paintThread;
	private ClientOutThread outThread;
	private String name;

	private String threadWaiter = ""; // used to unblock the thread

	private boolean gameOver = false;
	private boolean won = false;

	public GameScreen(Game mothergame, ClientOutThread outThread, JSONArray wrld, int startx, int starty,
			boolean blue) {
		this.motherGame = mothergame;
		this.outThread = outThread;
		if (blue)
			name = "Blue";
		else
			name = "Red";
//		myX = startx;
//		myY = starty;
		myWorld = new World(wrld, startx, starty, outThread, blue);
		myPlayer = new Player(startx, starty, blue);
		otherPlayer = new Player(-Consts.TILE_SIZE_PX, -Consts.TILE_SIZE_PX, !blue);
		paintThread = new Thread(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
		setDoubleBuffered(true);
		paintThread.start();
		outThread.setGameScreen(this);
		outThread.start();
	}

	public Player getPlayer() {
		return myPlayer;
	}

	public boolean getGameOver() {
		return gameOver;
	}

	// should have health on client handler side as well
	public void hit() {
		;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!gameOver) {
			try {
				if (myPlayer.getDx() != 0 || myPlayer.getDy() != 0) {
					// if moving do fast refresh
					Thread.sleep(1000 / Consts.FPS);
				} else {
					// if not moving then block on event
					synchronized (threadWaiter) {
						threadWaiter.wait();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myPlayer.moveMe();
//			myPlayer.updateWalkAnimation();

			if (otherPlayerSeen) {
				otherPlayer.moveMe();
				otherPlayer.setOffset(otherPlayer.getX() - myPlayer.getX(), otherPlayer.getY() - myPlayer.getY());
			}
//		
			// repaint if done
			motherGame.repaint();

		}

	}

	public void gameOver() {
		System.out.println("GAME OVER");
		gameOver = true;
		motherGame.updateMusic();
		motherGame.getFr().removeKeyListener(this);
		motherGame.repaint();
	}

	public void win(boolean w) {
		System.out.println("I WIN");
		won = w;
	}

	public void otherPlayerSeen(int x, int y, int dx, int dy, int animationState) {
		otherPlayerSeen = true;
		// change implementation within game screen and then u done
//		otherPlayer.changeDir(dir);
		otherPlayer.setXY(x, y);
		otherPlayer.setDx(dx);
		otherPlayer.setDy(dy);
		otherPlayer.setAnimationState(animationState);
		synchronized (threadWaiter) {
			threadWaiter.notify();
		}
	}

	public void otherPlayerOffScreen() {
		otherPlayerSeen = false;
	}

	@Override
	public void paintComponent(Graphics g) {

		// this should be the deltax deltay i get from server
		myWorld.paintComponent(myPlayer.getDx(), myPlayer.getDy(), g);
		myPlayer.paintComponent(g);
		// draw the hearts in top corner
		if (otherPlayerSeen) {
			// System.out.println("Otherplayer in frame");
			otherPlayer.paintComponent(g);
		}

		if (gameOver) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 5 * 0.1f));
			g2d.setPaint(Color.BLACK);
			g2d.fillRect(0, 0, Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
			g2d.dispose();
			g.setFont(new Font("SansSerif", Font.BOLD, 100));
			if (won) {
				g.drawString(name.toUpperCase() + " TRIUMPHS!!", 100, 100);
			} else {
				g.drawString(name + " faces a bitter defeat...", 100, 100);
			}

			g.drawString("Close the window to quit", 100, 300);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		int deltax = 0, deltay = 0;
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			deltay = -Consts.PLAYER_SPEED;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			deltax = -Consts.PLAYER_SPEED;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			deltay = Consts.PLAYER_SPEED;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			deltax = Consts.PLAYER_SPEED;
			break;
		case KeyEvent.VK_SPACE:
			Point mouse = MouseInfo.getPointerInfo().getLocation();
			outThread.sendBullet(mouse.x, mouse.y);
			break;
		}
		if (deltax != 0)
			myPlayer.setDx(deltax);
		if (deltay != 0)
			myPlayer.setDy(deltay);
		synchronized (threadWaiter) {
			threadWaiter.notify();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int deltax = 1, deltay = 1;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			deltay = 0;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			deltax = 0;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			deltay = 0;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			deltax = 0;
			break;
		}
		if (deltax != 1)
			myPlayer.setDx(deltax);
		if (deltay != 1)
			myPlayer.setDy(deltay);

		synchronized (threadWaiter) {
			threadWaiter.notify();
		}
	}

}

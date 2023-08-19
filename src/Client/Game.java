package Client;

import java.awt.Graphics;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JPanel;

import org.json.JSONArray;

import Server.Consts;

// main game jpanel
// contains connection to server
// has output to server
// pains title or game based on state
public class Game extends JPanel {
	enum GameState {
		TITLE, GAME
	}

	// mother

	// jpanels
	private GameScreen myGameScreen;
	private TitleScreen myTitleScreen;

	private GameState myGameState;

	// net stuff
	private Socket socket;
	private ClientInThread inThread;
	private ClientOutThread outThread;
//	private DataOutputStream dos;
	private MusicThread music;
	private ClientFrame fr;

	public Game(ClientFrame fr) {
		// this is y i need to pass fr
		myTitleScreen = new TitleScreen(this);
		myGameState = GameState.TITLE;
		fr.addMouseListener(myTitleScreen);
		music = new MusicThread();
	}

	public void setFr(ClientFrame fr) {
		this.fr = fr;
	}

	public ClientFrame getFr() {
		return fr;
	}

	@Override
	public void paintComponent(Graphics g) {
		switch (myGameState) {
		case TITLE:
			myTitleScreen.paintComponent(g);
			break;
		case GAME:
			myGameScreen.paintComponent(g);
			break;
		}
	}

	// setup the network connectivity
	public boolean setUpNet() {// throws UnknownHostException, IOException {
		// myTitleScreen.changeState(TitleScreen.TitleState.CONNECTING);
		System.out.println("Attempting set up net");// e1.printStackTrace();
		Socket socket;
		try {
			if (Consts.LOCAL) {
				socket = new Socket("localhost", Consts.SOCKET_NUM);
			} else
				socket = new Socket("54.176.186.80", Consts.SOCKET_NUM);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Connection refused");// e1.printStackTrace();
			return false;
		}

		try {
//			dos = new DataOutputStream(socket.getOutputStream());
			inThread = new ClientInThread(socket, myTitleScreen, this);
			outThread = new ClientOutThread(socket);
			myTitleScreen.setClientOut(outThread);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connected");
		try {
			// System.out.println("Waiting on JOIN");
			this.inThread.join();
			// System.out.println("Done Waiting on JOIN");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// now is where connection must start
		// server needs to know if everyone is ready
		inThread.start();
		return true;
	}

	public void changeState(GameState newState) {
		myGameState = newState;
		music.updateSound(newState, getGameOver());
		repaint();
	}

	public void updateMusic() {
		music.updateSound(myGameState, getGameOver());
	}

	public GameState getState() {
		return myGameState;
	}

	public void setGameSc(JSONArray world, int startx, int starty, boolean blue) {
		myGameScreen = new GameScreen(this, outThread, world, startx, starty, blue);
		inThread.setGameScreen(myGameScreen);
		fr.addKeyListener(myGameScreen);
		fr.removeMouseListener(myTitleScreen);
	}

	public boolean getGameOver() {
		if (myGameScreen == null) {
			return false;
		} else {
			return myGameScreen.getGameOver();
		}
	}

}

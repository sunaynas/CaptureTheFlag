package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Client.Game.GameState;

//waits for input from server and calls diff functions based on what server says
public class ClientInThread extends Thread {

	private DataInputStream dis;
	private TitleScreen titlescreen;
	private GameScreen gamescreen;
	private Game game;
	private boolean setUp = false;

	public ClientInThread(Socket s) throws IOException {
		dis = new DataInputStream(s.getInputStream());
	}

	public ClientInThread(Socket s, TitleScreen title, Game g) throws IOException {
		dis = new DataInputStream(s.getInputStream());
		titlescreen = title;
		game = g;
	}

	public void setGameScreen(GameScreen gs) {
		gamescreen = gs;
	}

	@Override
	public void run() {
//		System.out.println("starting in thread");
		while (!game.getGameOver()) {
			try {
//				boolean blue = true;
				String serverInStr = dis.readUTF();
				JSONObject serverIn = new JSONObject(serverInStr);
				System.out.println("server says: " + serverIn);

				switch (serverIn.getString("Message type")) {
				case "Starting game":
					titlescreen.everyOneIsReady();
					boolean blue = serverIn.getString("Color").equals("Blue");
					// this is not right vvvvvv
					JSONArray wrld = serverIn.getJSONArray("World");
					JSONObject startLoc = serverIn.getJSONObject("Starting location");
					int startx = startLoc.getInt("x");
					int starty = startLoc.getInt("y");
					game.setGameSc(wrld, startx, starty, blue);
					game.changeState(GameState.GAME);
					break;
//				case "Update info":
////					int dx = serverIn.getJSONObject("Deltas").getInt("Delta x");
////					int dy = serverIn.getJSONObject("Deltas").getInt("Delta y");
////					gamescreen.setDyDx(dx, dy);
//					gamescreen.getPlayer().setAnimationState(serverIn.getInt("Animation state"));
//					gamescreen.getPlayer().changeDir(serverIn.getString("Direction"));
//					break;
				case "Other player on screen":
					int x = serverIn.getInt("x"), y = serverIn.getInt("y"), dx = serverIn.getInt("dx"),
							dy = serverIn.getInt("dy");
					int animationState = serverIn.getInt("Animation state");
					gamescreen.otherPlayerSeen(x, y, dx, dy, animationState);
					break;
				case "Other player off screen":
					gamescreen.otherPlayerOffScreen();
					break;
				case "You're hit!":
					gamescreen.hit();
					break;
				case "Game over":
					gamescreen.gameOver();
				}

			} catch (IOException | JSONException i) {
				System.out.println("EXCEPTION");
				System.out.println(i);
				break;
			}
		}
//		this.killSrver.apply(true);
		// call kill from main

	}
}

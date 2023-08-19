package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

public class Server {
	private static int clientCount = 0;
	private static ClientHandler blueClient;
	private static ClientHandler redClient;
	private static World world;
	private static int readyClients = 0;
	private static Queue<Bullet> bullets;
	private static boolean onSameScreen;

	public static void main(String args[]) throws IOException {
		ServerSocket serversocket = new ServerSocket(Consts.SOCKET_NUM);
		System.out.println("Waiting for connections...");
		Server me = new Server();
		world = new World();

		while (clientCount < 2) {
			Socket s = null;
			try {
				// socket object to receive incoming client requests
				s = serversocket.accept();

				System.out.println("A new client is connected : " + s);
				if (clientCount == 0) {
					blueClient = new ClientHandler(s, world.getBx(), world.getBy(), (a) -> {
						return me.sendReady(a);
					});
					blueClient.start();
				} else {
					redClient = new ClientHandler(s, world.getRx(), world.getRy(), (a) -> {
						return me.sendReady(a);
					});
					redClient.start();
				}
				clientCount++;

			} catch (Exception e) {
				e.printStackTrace();
				s.close();
			}
			// serversocket.close();
		}
		System.out.println("Done accepting clients");
	}

	public void sendMsg(JSONObject msg) throws IOException {
		redClient.tellClient(msg);
		blueClient.tellClient(msg);
	}

	private void startGame() {
		System.out.println("Starting game");

		redClient.setServer(this);
		blueClient.setServer(this);

//		System.out.println("servers are set and outthread initialized");

		try {
			// main msg
			JSONObject msg = new JSONObject();
			// stuff that will be in both messages
			msg.put("Message type", "Starting game");
			msg.put("World", world.toJSON());

			// individual stuff like color and starting location
			String temp = msg.toString();
			JSONObject blueMsg = new JSONObject(temp), redMsg = new JSONObject(temp);
			blueMsg.put("Color", "Blue");
			redMsg.put("Color", "Red");

			JSONObject blueStartLoc = new JSONObject(), redStartLoc = new JSONObject();

			System.out.println("blue start x= " + world.getBx() + ", blue start y= " + world.getBy());
			blueStartLoc.put("x", world.getBx());
			blueStartLoc.put("y", world.getBy());
			System.out.println("red start x= " + world.getRx() + ", red start y= " + world.getRy());
			redStartLoc.put("x", world.getRx());
			redStartLoc.put("y", world.getRy());

			blueMsg.put("Starting location", blueStartLoc);
			redMsg.put("Starting location", redStartLoc);

			blueClient.tellClient(blueMsg);
			redClient.tellClient(redMsg);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void sendUpdates() {
		try {
			checkOnSameScreen();
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void checkOnSameScreen() throws JSONException, IOException {
		System.out.println("checking on same screen");
		System.out.println("blue = (" + blueClient.getX() + ", " + blueClient.getY() + ") and red = ("
				+ redClient.getX() + ", " + redClient.getY() + ")");

		if (Math.abs(blueClient.getX() - redClient.getX()) < Consts.SCREEN_WIDTH / 2
				&& Math.abs(blueClient.getY() - redClient.getY()) < Consts.SCREEN_HEIGHT / 2) {
			onSameScreen = true;
			JSONObject drawOtherB = new JSONObject(), drawOtherR = new JSONObject();
			drawOtherB.put("Message type", "Other player on screen");
			drawOtherR.put("Message type", "Other player on screen");
			// should send x and y in terms of me.
			drawOtherB.put("x", redClient.getX());
			drawOtherB.put("y", redClient.getY());
			drawOtherB.put("dx", redClient.getDx());
			drawOtherB.put("dy", redClient.getDy());
			drawOtherB.put("Animation state", redClient.getAnimationState());

			drawOtherR.put("x", blueClient.getX());
			drawOtherR.put("y", blueClient.getY());
			drawOtherR.put("dx", blueClient.getDx());
			drawOtherR.put("dy", blueClient.getDy());
			drawOtherR.put("Animation state", blueClient.getAnimationState());

			blueClient.tellClient(drawOtherB);
			redClient.tellClient(drawOtherR);
		} else if (onSameScreen) {
			onSameScreen = false;
			JSONObject offScreen = new JSONObject();
			offScreen.put("Message type", "Other player off screen");
			sendMsg(offScreen);
		}
	}

	public void createBullet(int endx, int endy, int px, int py) {
		bullets.add(new Bullet(px, py, endx, endy, blueClient, redClient));
		// start a timer for this thread so it is always checking if it hits someone, or
		// is in someone's frame
		// and after some amount of runs removes itself
	}

	public void gameOver(ClientHandler winner) {
		JSONObject gameOver = new JSONObject();
		try {
			gameOver.put("Message type", "Game over");
			sendMsg(gameOver);
//			the client who didnt winn will know that they lost when game over called
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (blueClient != winner) {
			blueClient.tellClient(null);
		}
	}

	public boolean sendReady(boolean b) {
		if (b)
			readyClients++;
		else
			readyClients--;
		System.out.println("Ready clients is now " + readyClients);
		if (readyClients == clientCount && clientCount > 1)
			startGame();
		return true;
	}

}

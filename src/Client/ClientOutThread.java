package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Consts;

public class ClientOutThread extends Thread {
	private GameScreen game;
	private DataOutputStream dos;

	public ClientOutThread(Socket s) throws IOException {
		dos = new DataOutputStream(s.getOutputStream());
	}

	public void setGameScreen(GameScreen gs) {
		game = gs;
	}

	public void sendMsg(JSONObject msg) throws IOException {
		dos.writeUTF(msg.toString());
	}

	public void win() {
		try {
			game.win(true);
			JSONObject iwin = new JSONObject();
			iwin.put("Message type", "I WON");
			sendMsg(iwin);
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendBullet(int endx, int endy) {
		try {
			JSONObject userIn = new JSONObject();
			userIn.put("Message type", "Bullet Created");
			userIn.put("Bullet end x", endx);
			userIn.put("Bullet end y", endy);
			sendMsg(userIn);
		} catch (JSONException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Client out thread started");
		while (!game.getGameOver()) {
			try {

				Thread.sleep(1000 / Consts.SEND_RATE);

				JSONObject out = new JSONObject();
				out.put("Message type", "Update");
				JSONObject loc = new JSONObject();
				loc.put("x", game.getPlayer().getX());
				loc.put("y", game.getPlayer().getY());

				JSONObject delta = new JSONObject();
				delta.put("dx", game.getPlayer().getDx());
				delta.put("dy", game.getPlayer().getDy());

				out.put("Deltas", delta);
				out.put("Location", loc);
				out.put("Animation state", game.getPlayer().getAnimationState());

				sendMsg(out);
			} catch (JSONException | InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

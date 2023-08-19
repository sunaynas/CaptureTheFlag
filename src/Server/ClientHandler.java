package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;

import org.json.JSONException;
import org.json.JSONObject;

// only need to pass 1 lambda fn to tell server if i am connected
// then server can call set server when all are connected
// and communication from client handler to server can be 2 way

public class ClientHandler extends Thread {
	private final DataInputStream dis;
	private final DataOutputStream dos;
	private final Socket s;
	private Server server;
	final Function<Boolean, Boolean> sendReady;
	// player information
	public int x, y, dx = 0, dy = 0;
	public int animationState = 0;
//	public int animationCounter = 0;
//	private Direction dir = Direction.DOWN;

	public ClientHandler(Socket s, int startx, int starty, Function<Boolean, Boolean> sendReady) throws IOException {
		this.s = s;
		x = startx;
		y = starty;
		this.sendReady = sendReady;
		this.dis = new DataInputStream(s.getInputStream());
		this.dos = new DataOutputStream(s.getOutputStream());

	}

	// encode w strings and case
	public void tellClient(JSONObject msg) {
		System.out.println("telling client " + this.s + " " + msg);
		try {
			dos.writeUTF(msg.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Sending message didnt work");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("client handler run is called");
		while (true) {
			try {
				String clientData1 = dis.readUTF();
				JSONObject clientData = new JSONObject(clientData1);
//				System.out.println("Client " + this.s + " says: " + clientData);

				switch (clientData.getString("Message type")) {
				case "Ready state":
					boolean ready = clientData.getBoolean("Ready?");
					this.sendReady.apply(ready);
					break;
				case "Bullet Created":
					try {
						int bulletEndx = clientData.getInt("Bullet end x"),
								bulletEndy = clientData.getInt("Bullet end y");
						server.createBullet(bulletEndx, bulletEndy, x, y);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "Update":
					try {
						x = clientData.getJSONObject("Location").getInt("x");
						y = clientData.getJSONObject("Location").getInt("y");
						dx = clientData.getJSONObject("Deltas").getInt("dx");
						dy = clientData.getJSONObject("Deltas").getInt("dy");
						animationState = clientData.getInt("Animation state");
						server.sendUpdates();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "I WON":
					server.gameOver(this);
					break;
				}
			} catch (EOFException e) {
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Killing myself");
				try {
					this.dis.close();
					this.dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
//				this.rmvMe.apply(this);
				return;
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void setServer(Server server) {
		this.server = server;
	}

	public int getAnimationState() {
		return animationState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

}
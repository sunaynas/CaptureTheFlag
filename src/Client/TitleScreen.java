package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.JSONException;
import org.json.JSONObject;

// this shows user the status of connecting
// takes user input from mouseclick and then instigates stages of net set up
public class TitleScreen extends JPanel implements MouseListener {
	public static enum TitleState {
		DEFAULT, CONNECTING, // TO SERVER
		// READY_QUERY, //asking if ready
		WAITING_FOR_PLAYERS, SETTING_UP, // THEN IT CAN START GAME
		SETTINGS,

	}

	private final int bx = 100, by = 100, bw = 400, bh = 100;
	private boolean ready;
	private Game motherGame;
	private ClientOutThread clientOut;
	private TitleState myState;
	private Timer connectTimer;

	private class NetActionListener implements ActionListener {
		private TitleScreen ts;

		public NetActionListener(TitleScreen ts) {
			this.ts = ts;
		}

		public void actionPerformed(ActionEvent e) {
			if (ts.myState == TitleState.CONNECTING) {
				if (ts.motherGame.setUpNet()) {
					ts.changeState(TitleState.WAITING_FOR_PLAYERS);
				} else {
					ts.connect();
				}
			}
		}
	}

	public TitleScreen(Game motherGame) {
		this.motherGame = motherGame;
		ready = false;
		myState = TitleState.DEFAULT;
		addMouseListener(this);
		setFocusable(true);
		requestFocusInWindow();
		setDoubleBuffered(true);
	}

	public void setClientOut(ClientOutThread cout) {
		clientOut = cout;
	}

	public void connect() {
		connectTimer = new Timer(1000, new NetActionListener(this));
		connectTimer.start();
	}

	public void everyOneIsReady() {
		changeState(TitleState.SETTING_UP);
	}

	@Override
	public void paintComponent(Graphics g) {
		System.out.println("calling title screen paint componenet with state: " + myState);
		switch (myState) {
		case DEFAULT:
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(bx, by, bw, bh);
			g.setColor(Color.BLACK);
			g.drawString("play", bx, by);
			break;
		case CONNECTING:
			g.setColor(Color.BLACK);
			g.setFont(new Font("SansSerif", Font.BOLD, 100));
			g.drawString("connecting to server", bx, by);
			break;
		case WAITING_FOR_PLAYERS:
			g.setFont(new Font("SansSerif", Font.BOLD, 100));
			if (!ready) {
				g.setColor(Color.GREEN);
				g.fillRect(bx, by, bw, bh);
				g.setColor(Color.BLACK);
				g.drawString("ready?", bx, by);
			} else {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(bx, by, bw, bh);
				g.setColor(Color.BLACK);
				g.drawString("ready.", bx, by);
				g.drawString("Waiting on other players..", bx + 300, by + 300);
			}
			break;
		case SETTING_UP:
			g.setColor(Color.BLACK);
			g.setFont(new Font("SansSerif", Font.BOLD, 80));
			g.drawString("building world, establishing connections...", bx, by);
			break;

		}
	}

	public void changeState(TitleState newState) {
		myState = newState;
		motherGame.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Mouse Released!");
		switch (myState) {
		case DEFAULT:
			// if playbutton is pressed
			if (e.getX() >= bx && e.getX() <= bx + bw && e.getY() >= by && e.getY() <= by + bh) {
				changeState(TitleState.CONNECTING);
				connect();
			}
			break;
		case WAITING_FOR_PLAYERS:
			if (e.getX() >= bx && e.getX() <= bx + bw && e.getY() >= by && e.getY() <= by + bh) {
				ready = !ready;
				try {
					JSONObject readyMsg = new JSONObject();
					readyMsg.put("Message type", "Ready state");
					readyMsg.put("Ready?", ready);
					clientOut.sendMsg(readyMsg);
//					if (ready)
//						motherGame.sendMsg("Ready");
//					else
//						motherGame.sendMsg("Not ready");
				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			motherGame.repaint();
			break;
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

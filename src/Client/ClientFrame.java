package Client;

import javax.swing.JFrame;

//Creates frame of client
//adds main game jpanel to framet
public class ClientFrame extends JFrame {

	public ClientFrame(String windowName) {
		super(windowName);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game screen = new Game(this);
		screen.setFr(this);
		add(screen);
		setVisible(true);
	}

	public static void main(String[] args) {
		ClientFrame fr = new ClientFrame("Capture the Flag");
	}
}

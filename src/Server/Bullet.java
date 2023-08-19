package Server;

import javax.swing.Timer;

public class Bullet {
	private int cx, cy;
	private double angle;
	private double dx, dy;

	private Timer mytimer;

	public Bullet(int startx, int starty, int endx, int endy, ClientHandler blue, ClientHandler red) {
		cx = startx;
		cy = starty;
		angle = Math.atan2(endy - starty, endx - startx);
		double hypot = Math.hypot(endy - starty, endx - startx);
		dx = (endx - startx) * Consts.BULLET_RAD / hypot; // divide this by how many segments u want rad cut into
		dy = (endy - starty) * Consts.BULLET_RAD / hypot;
	}

	// internal timer, ater some amt of calls
	//

}

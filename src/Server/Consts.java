package Server;

import java.awt.Toolkit;

//server constants 
public class Consts {
	private Consts() {
	}

	public static final boolean LOCAL = true;
	public static final int SOCKET_NUM = 5555;
	public static final String SCHOOL_EXTERNAL_IP = "10.220.73.52"; // at school
	public static final String HOME_EXTERNAL_IP = "192.168.55.6"; // at home

	public static final int WORLD_SIZE = 6; // tiles // should be odd
	public static final int TILE_SIZE_PX = 400;

	public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(); // in px
	public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50;

	public static final int PLAYER_SPEED = 3; // pixels per 1/fps of a second
	public static final int FPS = 60; // 60;
	public static final int SEND_RATE = 20; // times a second
	public static final int PLAYER_ANIMATION_SPEED = FPS / 12; // how many 1/fps of a second per each animation state

	public static final double BULLET_RAD = 400; // in px
	public static final int BULLET_SPEED = 4; // in px

	// tiletype to int conversion
	public static final int DEFAULT_TILE_NUM = 0;

	public static final int RIGHT_VERTICAL_WALL_TILE_NUM = 1;
	public static final int LEFT_VERTICAL_WALL_TILE_NUM = 2;
	public static final int HORIZONTAL_WALL_TILE_NUM = 3;

	public static final int BR__LWALL_TILE_NUM = 4;
	public static final int BL__LWALL_TILE_NUM = 5;
	public static final int TR__LWALL_TILE_NUM = 6;
	public static final int TL__LWALL_TILE_NUM = 7;

	public static final int BCASTLE_TILE_NUM = 8;
	public static final int RCASTLE_TILE_NUM = 9;

}
// bob.fishtrom
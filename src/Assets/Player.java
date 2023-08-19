package Assets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Server.Consts;

//player from just drawing perspective
// constructs 3 img arr for each direction
// server sets cdir and animation state and based on that draws

public class Player extends JPanel {
	// change to enum
	private Direction cDir = Direction.DOWN;
	private BufferedImage currentImage;
	// 1-3
//	private int animationState;
	private int animationState = 0;
	private int animationCounter = 0;
	private BufferedImage up[], down[], left[], right[];

	private int x, y;
	private int dx = 0, dy = 0;

	private int xoffset = 0;
	private int yoffset = 0;

	public Player(int sx, int sy, boolean blue) {
		x = sx;
		y = sy;
		cDir = Direction.DOWN;
		animationState = 0;

		// seting up image stuff
		setOpaque(false);
		up = new BufferedImage[3];
		down = new BufferedImage[3];
		left = new BufferedImage[3];
		right = new BufferedImage[3];

		try {
			if (blue) {
				down[0] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueBstill.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				down[1] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueBwalk.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				down[2] = ImageUtil.flipImage(down[1], true, false);

				up[0] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueFstill.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				up[1] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueFwalk.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				up[2] = ImageUtil.flipImage(up[1], true, false);

				left[0] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueSstill.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				left[1] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueSwalk.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				left[2] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/blueCharacter/blueSwalk1.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);

				right[0] = ImageUtil.flipImage(left[0], true, false);
				right[1] = ImageUtil.flipImage(left[1], true, false);
				right[2] = ImageUtil.flipImage(left[2], true, false);
			} else {
				down[0] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/redCharacter/redBstill.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				down[1] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/redCharacter/redBwalk.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				down[2] = ImageUtil.flipImage(down[1], true, false);

				up[0] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/redCharacter/redFstill.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				up[1] = ImageUtil.resizeImage(ImageIO.read(getClass().getResource("/images/redCharacter/redFwalk.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				up[2] = ImageUtil.flipImage(up[1], true, false);

				left[0] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/redCharacter/redSstill.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				left[1] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/redCharacter/redSwalk.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);
				left[2] = ImageUtil.resizeImage(
						ImageIO.read(getClass().getResource("/images/redCharacter/redSwalk1.PNG")),
						ImgConsts.PLAYER_WIDTH, ImgConsts.PLAYER_HEIGHT);

				right[0] = ImageUtil.flipImage(left[0], true, false);
				right[1] = ImageUtil.flipImage(left[1], true, false);
				right[2] = ImageUtil.flipImage(left[2], true, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stoppedMoving() {
		animationState = 0;
	}

	public void updateWalkAnimation() {
		if (dx == 0 && dy == 0) {
			animationState = 0;
			return;
		}
		animationCounter++;
		if (animationCounter == Consts.PLAYER_ANIMATION_SPEED) {
			incrementAnimationState();
			animationCounter = 0;
		}
	}

	private void incrementAnimationState() {
		animationState++;
		if (animationState > 2) {
			animationState = 0;
		}
	}

	public void setOffset(int x, int y) {
		xoffset = x;
		yoffset = y;
	}

	// also updates the direction im going in
	public void moveMe() {
		cDir = destToDir(dx, dy);
		updateWalkAnimation();
		if (World.checkCoord(x + dx, y + dy)) {
			x += dx;
			y += dy;
		} else {
			dx = 0;
			dy = 0;
		}
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public void setXY(int x, int y) {
		this.y = y;
		this.x = x;
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

	public int getAnimationState() {
		return animationState;
	}

	public void setAnimationState(int s) {
		animationState = s;
	}

	private Direction destToDir(int dx, int dy) {
		if (dy > 0 && dx > 0)
			return Direction.UP_RIGHT;
		else if (dy > 0 && dx == 0)
			return Direction.UP;
		else if (dy > 0 && dx < 0)
			return Direction.UP_LEFT;
		if (dy < 0 && dx > 0)
			return Direction.DOWN_RIGHT;
		else if (dy < 0 && dx == 0)
			return Direction.DOWN;
		else if (dy < 0 && dx < 0)
			return Direction.DOWN_LEFT;
		else if (dy == 0 && dx > 0)
			return Direction.RIGHT;
		else if (dy == 0 && dx < 0)
			return Direction.LEFT;
		// if x and y r 0
		animationState = 0;
		return cDir;
	}

	public void changeDir(Direction d) {
		cDir = d;
	}

	@Override
	public void paintComponent(Graphics g) {

		int y = (Consts.SCREEN_HEIGHT - ImgConsts.PLAYER_HEIGHT) / 2 + yoffset,
				x = (Consts.SCREEN_WIDTH - ImgConsts.PLAYER_WIDTH) / 2 + xoffset;
//		g.setColor(Color.CYAN);
//		g.fillRect(x-10, y-10, Consts.PLAYER_WIDTH+20, Consts.PLAYER_HEIGHT+20);
		switch (cDir) {
		case DOWN:
		case DOWN_LEFT:
		case DOWN_RIGHT:
			g.drawImage(down[animationState], x, y, null);
			break;
		case UP:
		case UP_RIGHT:
		case UP_LEFT:
			g.drawImage(up[animationState], x, y, null);
			break;
		case LEFT:
			g.drawImage(left[animationState], x, y, null);
			break;
		case RIGHT:
			g.drawImage(right[animationState], x, y, null);
			break;
		}
	}
}

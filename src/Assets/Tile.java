package Assets;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JPanel;

import Server.Consts;

// a square of a certain size, draws differnt things based on the type of the tile
// has an x and y and paints w that xy when paint component called

public class Tile extends JPanel {

	private Image myImg;
	private tileType myTileType;
	private int mx, my;

	public Tile(tileType tp, int x, int y) {
		setOpaque(false);
		myTileType = tp;
		mx = x;
		my = y;

		setType(myTileType);
	}

	public void setType(tileType newtype) {
		myTileType = newtype;
		switch (myTileType) {
		case DEFAULT:
			myImg = ImgConsts.defaultImg.getScaledInstance(Consts.TILE_SIZE_PX, Consts.TILE_SIZE_PX,
					Image.SCALE_DEFAULT);
			break;
		case RCASTLE:
			myImg = ImgConsts.rcastleImg.getScaledInstance(Consts.TILE_SIZE_PX, Consts.TILE_SIZE_PX,
					Image.SCALE_DEFAULT);
			break;
		case BCASTLE:
			myImg = ImgConsts.bcastleImg.getScaledInstance(Consts.TILE_SIZE_PX, Consts.TILE_SIZE_PX,
					Image.SCALE_DEFAULT);
			break;
		case BR_LWALL:
			myImg = ImgConsts.BR_LWALL_Img;
			break;
		case BL_LWALL:
			myImg = ImgConsts.BL_LWALL_Img;
			break;
		case TR_LWALL:
			myImg = ImgConsts.TR_LWALL_Img;
			break;
		case TL_LWALL:
			myImg = ImgConsts.TL_LWALL_Img;
			break;
		case RIGHT_VERTICAL_WALL:
			myImg = ImgConsts.r_v_i_wall_img;
		case LEFT_VERTICAL_WALL:
			myImg = ImgConsts.l_v_i_wall_img;
			break;
		case HORIZONTAL_WALL:
			myImg = ImgConsts.h_i_wall_img;
			break;
		}

	}

	private int px, py;

	// 1 is solid, 0 is not solid, -1 is i win
	public int isSolid(int x, int y) {
		px = x;
		py = y;
		System.out.println("checking (" + x + ", " + y + ")");
		int ret = 0;
		switch (myTileType) {
		case RCASTLE:
		case BCASTLE:
			// if around the base it is solid
			if (insideElipse(x, y, 160, 275, 365 - 160, 430 - 320 + 20)
					|| insideRect(x, y, 175, 90, 350 - 175, 350 - 90)
					|| insideElipse(x, y, 175, 40, 350 - 176, 170 - 70)) {
				System.out.println("Inside red(solid part)");
				ret = 1;
			}

			if (insideRect(x, y, 300, 135 - 25, 355 - 300 - 10, 430 - 135)
					|| insideElipse(x, y, 175 + 20, 40 + 20, 350 - 176 - 35, 170 - 70 - 35)) {

				if (insideElipse(x, y, 175 + 20, 40 + 20, 350 - 176 - 35, 170 - 70 - 35)
						&& x < 175 + 20 + (350 - 176 - 35) / 2) {
					// game over!!
					return -1;
				}
				System.out.println("Inside blue(hole in middle)");
				ret = 0;
			}
//				|| insideRect(x, y, 286, 100, 334 - 280, 152 - 120)

			break;
		case BR_LWALL: // wrong
			if (insideRect(x, y, 0, 205 - 30, 400 - 220 + 20, 260 - 205)
					|| insideRect(x, y, 0, 205 - 30, 220 - 185, 430 - 205)) {
				ret = 1;
			}
			break;
		case BL_LWALL: // wrong
			if (insideRect(x, y, 220, 205 - 30, 400 - 220, 260 - 205)
					|| insideRect(x, y, 0, 205 - 30, 220 - 185, 430 - 205)) {
				ret = 1;
			}
			break;
		case TR_LWALL:
			if (insideRect(x, y, 0, 205 - 30, 400 - 220 + 20, 260 - 205)
					|| insideRect(x, y, 185, 205 - 30, 220 - 185, 430 - 205)) {
				ret = 1;
			}
			break;
		case TL_LWALL: // done
			if (insideRect(x, y, 220, 205 - 30, 400 - 220, 260 - 205)
					|| insideRect(x, y, 185, 205 - 30, 220 - 185, 430 - 205)) {
				ret = 1;
			}
			break;
		case RIGHT_VERTICAL_WALL:
		case LEFT_VERTICAL_WALL:
			// done
			if (insideRect2(x, y, 185, 0, 215, 400)) {
				ret = 1;
			}
			break;
		case HORIZONTAL_WALL:
			if (insideRect(x, y, 0, 205 - 30, 400, 260 - 205)) {
				ret = 1;
			}
			break;
		case DEFAULT:
			ret = 0;
			break;
		}
		return ret;
	}

	// h,k is center, xy is pt ur checking, a is width, b is height
	private boolean insideElipse(double x, double y, double h, double k, double a, double b) {
		h += a / 2;
		k += b / 2;
		a = a / 2;
		b = b / 2;
// checking the equation of
// ellipse with the given point
		double p = ((double) Math.pow((x - h), 2) / (double) Math.pow(a, 2))
				+ ((double) Math.pow((y - k), 2) / (double) Math.pow(b, 2));

//		return p;

		return p <= 1;
	}

	private boolean doRectOverlap(Point l1, Point r1, Point l2, Point r2) {
		// if rectangle has area 0, no overlap
		if (l1.x == r1.x || l1.y == r1.y || r2.x == l2.x || l2.y == r2.y)
			return false;

		// If one rectangle is on left side of other
		if (l1.x > r2.x || l2.x > r1.x)
			return false;

		// If one rectangle is above other
		if (r1.y > l2.y || r2.y > l1.y)
			return false;

		return true;
	}

	private boolean insideRect(int x, int y, int x1, int y1, int width, int height) {

		// checking the equation of
		// ellipse with the given point
		if (x >= x1 && x <= x1 + width && y >= y1 && y <= y1 + height)
			return true;

		return false;

//		return doRectOverlap(new Point(x - (ImgConsts.PLAYER_WIDTH / 2), y - (ImgConsts.PLAYER_HEIGHT / 2)),
//				new Point(x + (ImgConsts.PLAYER_WIDTH / 2), y + (ImgConsts.PLAYER_HEIGHT / 2)), new Point(x1, y1),
//				new Point(x1 + width, y1 + height));
	}

	private boolean insideRect2(int x, int y, int x1, int y1, int x2, int y2) {

		// checking the equation of
		// ellipse with the given point
		if (x >= x1 && x <= x2 && y >= y1 && y <= y2)
			return true;

		return false;

//		return doRectOverlap(new Point(x - (ImgConsts.PLAYER_WIDTH / 2), y - (ImgConsts.PLAYER_HEIGHT / 2)),
//				new Point(x + (ImgConsts.PLAYER_WIDTH / 2), y + (ImgConsts.PLAYER_HEIGHT / 2)), new Point(x1, y1),
//				new Point(x1 + width, y1 + height));
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(myImg, mx, my, null);
	}

	public void incrementxy(int xchange, int ychange) {
		mx -= xchange;
		my -= ychange;
	}

	public tileType getTileType() {
		return myTileType;
	}
}

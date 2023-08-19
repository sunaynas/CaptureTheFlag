package Assets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgConsts {
	public static BufferedImage defaultImg, rcastleImg, bcastleImg, BR_LWALL_Img, BL_LWALL_Img, TR_LWALL_Img,
			TL_LWALL_Img, h_i_wall_img, l_v_i_wall_img, r_v_i_wall_img;

	private ImgConsts() {
	}

	public static void initImgs(Class c) {
		try {
			defaultImg = ImageIO.read(c.getResource("/images/Tiles/defaultTile.png"));
			rcastleImg = ImageIO.read(c.getResource("/images/Tiles/redCastleTile.PNG"));
			bcastleImg = ImageIO.read(c.getResource("/images/Tiles/blueCastleTile.PNG"));
			BR_LWALL_Img = ImageIO.read(c.getResource("/images/Tiles/BR_LWALL.PNG"));
			BL_LWALL_Img = ImageIO.read(c.getResource("/images/Tiles/BL_LWALL.PNG"));
			TL_LWALL_Img = ImageIO.read(c.getResource("/images/Tiles/TL_LWALL.PNG"));
			TR_LWALL_Img = ImageIO.read(c.getResource("/images/Tiles/TR_LWALL.PNG"));
			h_i_wall_img = ImageIO.read(c.getResource("/images/Tiles/HORIZONTAL_IWALL.PNG"));
			l_v_i_wall_img = ImageIO.read(c.getResource("/images/Tiles/LEFT_VERTICAL_IWALL.PNG"));
			r_v_i_wall_img = ImageIO.read(c.getResource("/images/Tiles/RIGHT_VERTICAL_IWALL.PNG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final int PLAYER_SCALE = 5;
	// og img if not diag is 13x17, if diag, 14x17
	private static final int PLAYER_OG_X = 14; // image in px
	private static final int PLAYER_OG_Y = 17; // same thing
	public static final int PLAYER_WIDTH = PLAYER_OG_X * PLAYER_SCALE;
	public static final int PLAYER_HEIGHT = PLAYER_OG_Y * PLAYER_SCALE;

}

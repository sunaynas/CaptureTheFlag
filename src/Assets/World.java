package Assets;

import java.awt.Graphics;

import org.json.JSONArray;
import org.json.JSONException;

import Client.ClientOutThread;
import Server.Consts;

//2d array of tiles, paint component calls paint for all tiles
// when server tells me something has moved i move all the tiles the opp dir
// constructor is sent in int, i convert to tiles and enum tileTYpe

public class World {
	private static Tile[][] worldtiles;
	private final int WORLD_SIZE;
	private static ClientOutThread out;
	private static boolean blue;

	private void fromJSON(JSONArray w, int startx, int starty) throws JSONException {
		int x1 = startx - Consts.SCREEN_WIDTH / 2, // x2 = startx + Consts.SCREEN_WIDTH/2,
				y1 = starty - Consts.SCREEN_HEIGHT / 2;// , y2 = starty + Consts.SCREEN_HEIGHT;
		// x1,y1 would be 0,0 so 0,0 in pixel plane would be -x1,-y1
		int x = -x1, y = -y1;

		for (int i = 0; i < w.length(); ++i) {
			JSONArray rec = w.getJSONArray(i);
			for (int j = 0; j < rec.length(); j++) {
				int val = rec.getInt(j);
				worldtiles[i][j] = new Tile(intToTileType(val), x, y);
				x += Consts.TILE_SIZE_PX;
			}
			x = -x1;
			y += Consts.TILE_SIZE_PX;
		}
	}

	private tileType intToTileType(int i) {
		tileType ret = tileType.DEFAULT;
		switch (i) {
		case Consts.DEFAULT_TILE_NUM:
			ret = tileType.DEFAULT;
			break;
		case Consts.BR__LWALL_TILE_NUM:
			ret = tileType.BR_LWALL;
			break;
		case Consts.BL__LWALL_TILE_NUM:
			ret = tileType.BL_LWALL;
			break;
		case Consts.TR__LWALL_TILE_NUM:
			ret = tileType.TR_LWALL;
			break;
		case Consts.TL__LWALL_TILE_NUM:
			ret = tileType.TL_LWALL;
			break;
		case Consts.RIGHT_VERTICAL_WALL_TILE_NUM:
			ret = tileType.RIGHT_VERTICAL_WALL;
		case Consts.LEFT_VERTICAL_WALL_TILE_NUM:
			ret = tileType.LEFT_VERTICAL_WALL;
			break;
		case Consts.HORIZONTAL_WALL_TILE_NUM:
			ret = tileType.HORIZONTAL_WALL;
			break;
		case Consts.BCASTLE_TILE_NUM:
			ret = tileType.BCASTLE;
			break;
		case Consts.RCASTLE_TILE_NUM:
			ret = tileType.RCASTLE;
			break;
		}
		return ret;
	}

	public World(JSONArray wrld, int startx, int starty, ClientOutThread out, boolean blue) {
		this.blue = blue;
		this.out = out;
		worldtiles = new Tile[wrld.length()][wrld.length()];
		WORLD_SIZE = wrld.length();
		ImgConsts.initImgs(getClass());

		try {
			fromJSON(wrld, startx, starty);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// give world access to outThread, if on otherPlayers flag, call endgame
	public static boolean checkCoord(int x, int y) {
		System.out.println();
		System.out.println("checking coord " + x + y);
		// must b swapped bc itr over world and draw plane is diff
		int ytile = x / 400, xtile = y / 400;
		System.out.println("its at tile " + xtile + ", " + ytile);
		int isSolid = worldtiles[xtile][ytile].isSolid(x % Consts.TILE_SIZE_PX, y % Consts.TILE_SIZE_PX);
		if (isSolid == -1 && ((blue && worldtiles[xtile][ytile].getTileType().equals(tileType.RCASTLE))
				|| (!blue && worldtiles[xtile][ytile].getTileType().equals(tileType.BCASTLE)))) {
			try {
				out.win();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// game over i won
		} else if (isSolid == 0) {
			// not solid, can pass
			System.out.println("the tile is not solid");
			return true;
		}
		System.out.println("the tile is solid");
		return false;
	}

	public void paintComponent(int xchange, int ychange, Graphics g) {
		for (int i = 0; i < WORLD_SIZE; i++) {
			for (int j = 0; j < WORLD_SIZE; j++) {
				worldtiles[i][j].incrementxy(xchange, ychange);
				worldtiles[i][j].paintComponent(g);
			}
		}
	}
}

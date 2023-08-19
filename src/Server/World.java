package Server;

import org.json.JSONArray;

public class World {
	private int world[][]; // x, y => enum
	private int bx, by, rx, ry;

	public World() {
		world = new int[Consts.WORLD_SIZE][Consts.WORLD_SIZE];
		createWorld();
	}

	public JSONArray toJSON() {
		JSONArray jsonArray = new JSONArray();
		for (int[] it : world) {
			JSONArray arr = new JSONArray();
			for (int i : it) {
				arr.put(i); // or some other conversion
			}
			jsonArray.put(arr);
		}
		return jsonArray;
	}

	public void createWorld() {
		int bi = (int) (Math.random() * (Consts.WORLD_SIZE) / 2 - 1);
		int bj = (int) (Math.random() * (Consts.WORLD_SIZE) / 2 - 1);
		int ri = (int) (Math.random() * (Consts.WORLD_SIZE) / 2 - 1);
		int rj = (int) (Math.random() * (Consts.WORLD_SIZE) / 2 - 1);

		if (Math.random() <= 0.5) {
			bi += (Consts.WORLD_SIZE) / 2;
			bj += (Consts.WORLD_SIZE) / 2;
			ri++;
			rj++;
		} else {
			ri += (Consts.WORLD_SIZE) / 2;
			rj += (Consts.WORLD_SIZE) / 2;
			bi++;
			bj++;
		}

		for (int i = 0; i < Consts.WORLD_SIZE; i++) {
			for (int j = 0; j < Consts.WORLD_SIZE; j++) {
				if (i == 0) {
					if (j == 0)
						world[i][j] = Consts.TL__LWALL_TILE_NUM;
					else if (j == Consts.WORLD_SIZE - 1)
						world[i][j] = Consts.TR__LWALL_TILE_NUM;
					else
						world[i][j] = Consts.HORIZONTAL_WALL_TILE_NUM;
				} else if (i == Consts.WORLD_SIZE - 1) {
					if (j == 0)
						world[i][j] = Consts.BL__LWALL_TILE_NUM;
					else if (j == Consts.WORLD_SIZE - 1)
						world[i][j] = Consts.BR__LWALL_TILE_NUM;
					else
						world[i][j] = Consts.HORIZONTAL_WALL_TILE_NUM;
				} else if (j == 0) {
					world[i][j] = Consts.RIGHT_VERTICAL_WALL_TILE_NUM;
				} else if (j == Consts.WORLD_SIZE - 1) {
					world[i][j] = Consts.LEFT_VERTICAL_WALL_TILE_NUM;
				} else {
					// random num from 1 to 100
					int prob = (int) (Math.random() * 100) + 1;
					if (prob >= 21) {
						world[i][j] = Consts.DEFAULT_TILE_NUM;
					} else
						switch (prob / 3) {
						case 0:
							world[i][j] = Consts.RIGHT_VERTICAL_WALL_TILE_NUM;
							break;
						case 1:
							world[i][j] = Consts.LEFT_VERTICAL_WALL_TILE_NUM;
							break;
						case 2:
							world[i][j] = Consts.HORIZONTAL_WALL_TILE_NUM;
							break;
						case 3:
							world[i][j] = Consts.BR__LWALL_TILE_NUM;
							break;
						case 4:
							world[i][j] = Consts.BL__LWALL_TILE_NUM;
							break;
						case 5:
							world[i][j] = Consts.TL__LWALL_TILE_NUM;
							break;
						case 6:
							world[i][j] = Consts.TR__LWALL_TILE_NUM;
							break;
						}

				}
			}

		}

		world[bi][bj] = Consts.BCASTLE_TILE_NUM;
		world[ri][rj] = Consts.RCASTLE_TILE_NUM;
		bx = bj * Consts.TILE_SIZE_PX;
		by = bi * Consts.TILE_SIZE_PX;
		rx = rj * Consts.TILE_SIZE_PX;
		ry = ri * Consts.TILE_SIZE_PX;

		System.out.println("Created world: ");
		for (int i = 0; i < Consts.WORLD_SIZE; i++) {
			for (int j = 0; j < Consts.WORLD_SIZE; j++) {
				System.out.print(world[i][j] + " ");
			}
			System.out.println();
		}
	}

	public int getBx() {
		return bx;
	}

	public int getBy() {
		return by;
	}

	public int getRx() {
		return rx;
	}

	public int getRy() {
		return ry;
	}
}

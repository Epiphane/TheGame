package com.gilded.thegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Art {
	public final static int DIRECTIONS = 4;
	
	public static TextureRegion[][][] mainCharacter;
	public static TextureRegion[][] mainCharacterRed;
	public static TextureRegion[][] mainCharacterStanding;
	public static byte[][] mainCharacterMap;
	
	public static boolean loaded = false;
	
	public static void load () {
		mainCharacter = new TextureRegion[Input.colors.length][][];
		mainCharacter[0] = split("img/player.png", 16, 19);
		mainCharacter[Input.RED] = split("img/player_red.png", 16, 19);
//		mainCharacter[Input.BLUE] = split("img/uh.png", 32, 32);
		mainCharacter[Input.GREEN] = split("img/player.png", 16, 19);
		
		loaded = true;
	} 

	public static TextureRegion load (String name, int width, int height) {
		Texture texture = new Texture(Gdx.files.internal(name));
		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);
		region.flip(false, false);
		return region;
	}

	private static TextureRegion[][] split (String name, int width, int height) {
		return split(name, width, height, false);
	}

	private static TextureRegion[][] split (String name, int width, int height, boolean flipX) {
		Texture texture = new Texture(Gdx.files.internal(name));
		int xSlices = texture.getWidth() / width;
		int ySlices = texture.getHeight() / height;
		TextureRegion[][] res = new TextureRegion[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				res[x][y] = new TextureRegion(texture, x * width, y * height, width, height);
				res[x][y].flip(flipX, false);
			}
		}
		return res;
	}
}

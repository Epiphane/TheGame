package com.gilded.thegame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity extends Sprite {
	public static final float MAX_FALL_SPEED = -0.5f;
	public static final float GRAVITY = -0.020f;
	
	/** What level am I in? */
	protected Level currentLevel;

	/** Am I on the ground? */
	protected boolean onGround = false, againstLWall = false, againstRWall = false;

	/** Current Location (top left) */
	protected float x;
	protected float y;
	/** Current direction */
	protected float dx, dy;

	/**
	 * "Bounciness" - taken from Metagun, have yet to test whether it helps with
	 * not being in blocks
	 */
	protected double bounce = 0.05;

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y, TextureRegion texture) {
		super(texture);
		setSize(getWidth() / TheGame.TILE_SIZE, getHeight() / TheGame.TILE_SIZE);
		this.x = x;
		this.y = y;
	}

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Update me! This function is where we will have all computations such as
	 * AI, wanking off, and the like.
	 */
	public void tick() {
		if (currentLevel == null) // Do nothing if we're not in a world
			return;
	}

	/**
	 * Try to move specified distance.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void tryMove(float dx, float dy) {
		if(Math.abs(dx) < 0.01) dx = 0;
		
		float w = getWidth();
		float h = getHeight();
		onGround = false;
		againstRWall = false;
		againstLWall = false;
		
		// First, try to move horizontally
		if (currentLevel.canMove(this, x + dx, y, w, h)) {
			x += dx;
		} else {
			// Slope?
			if (currentLevel.canMove(this, x + dx, y + dx + 0.5f, w, h)) {
				x += dx;
				y += Math.abs(dx);
			}
			// Nope. Definitely a wall
			else {
				// Hit a wall
				hitWall(dx, dy);
				if(dx != 0 && dy < 0)
					this.dy = 0;
				
				if (dx > 0)
					againstRWall = true;
				else if (dx < 0)
					againstLWall = true;
			}
		}

		// Next, move vertically
		if (currentLevel.canMove(this, x, y + dy, w, h)) {
			y += dy;
		} else {
			if (dy < 0) {
				onGround = true;
			}
			// Hit the wall
			hitWall(dx, dy);
		}
		setPosition(x, y);
	}

	/**
	 * Called when you run into a wall. Basically just "backs you up" until
	 * you're not colliding with the wall anymore.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void hitWall(float dx, float dy) {
		x += dx;
		y += dy;
		
		while(!currentLevel.canMove(this, x, y, getWidth(), getHeight())) {
			x -= dx * 0.01;
			y -= dy * 0.01;
		}
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.x = x;
		this.y = y;
	}
}

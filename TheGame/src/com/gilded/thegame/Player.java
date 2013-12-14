package com.gilded.thegame;


public class Player extends Entity {
	public static final int BASIC = 0;
	public static final int DASHER = 1;
	public static final int PRANCER = 2;
	public static final int FLACCID = 3;
	
	public static final float JUMP_DY = 0.4f;
	public static final float JUMP_DX_OFF_WALL = 0.25f;
	public static final float WALK_SPEED = TheGame.MULTIPLIER_FOR_GOOD_CALCULATIONS / 24f;
	public static final float WALK_FRICTION = 0.93f;

	private int frame;
	private boolean walking;
	private boolean facingRight;
	
	private int state = BASIC;
	
	/* For allllll the animations */
	public int ticksRemaining;
	
	/* During dashes or stone mode we want to ignore input but still keep it in the stack. */
	public boolean ignoreInput = false;
	
	/* Dashing through the snow... */
	private boolean dashing = false;
	public static final float DASHSPEED = 0.6f;
	public static final int DASH_TICKS = 10;
	public static final float DASH_FRICTION = 0.8f;
	public int dashDirection;
	
	/* Stomp it, stomp it good */
	private boolean stomping = false;
	public static final float STOMPSPEED = 0.8f;
	
	/* For the glorious wall cling and jump */
	public static final int CLING_TO_WALL_TICKS = 7;
	
	/* Glide stuffz */
	private boolean isGliding = false;
	public static final float glideFallSlowFactor = 0.5f;
	
	public Player(int x, int y) {
		super(x, y, Art.mainCharacter[0][0]);
	}
	
	/**
	 * Update the player according to the input/level
	 * 
	 * @param input
	 */
	public void tick(Input input)
	{
		super.tick();
		
	
		// Jump
		if(input.buttonStack.shouldJump() && (onGround || againstLWall || againstRWall)) {
			dy = JUMP_DY;
			if(!onGround) { // Just hangin' on..
				if(againstRWall) dx = -JUMP_DX_OFF_WALL;
				else if(againstLWall) dx = JUMP_DX_OFF_WALL;
			}
		}
		
		// Daaash!
		//TODO: Change to a switch/case based on what form the player is in
		if(input.buttonStack.shouldDash() && !ignoreInput) {
			// Initiate dash based on what directions the player is holding
			Point dir = input.buttonStack.airDirection();
			dashDirection = Utility.directionFromOffset(dir);
			
			dashing = true;
			ignoreInput = true;
			ticksRemaining = DASH_TICKS;
			dx = DASHSPEED * dir.x;
			dy = DASHSPEED * dir.y;

			// Account for our buddy Pythagoras
			if(dx != 0 && dy != 0) {
				dx *= 0.8;
				dy *= 0.8;
			}
		}
		
		// Stommmp!
		//TODO: Implement switch/case based on what form the character is in
		if(input.buttonStack.isStomping()) {
			// Not stomping -> stomping
			if(!stomping && !ignoreInput) {
				stomping = true;
				ignoreInput = true;
			
				dx = 0;
				dy = -STOMPSPEED;
			}
		} else {
			// Stomping -> not stomping
			if(stomping && ignoreInput) {
				stomping = false;
				ignoreInput = false;
			}
		}
		
		// Handle gravity
		if (!onGround && !dashing) {
			if (dy > MAX_FALL_SPEED)
				dy += GRAVITY;
		}
		
		// First, set direction we plan to move and do actions
		if(!ignoreInput) {
			if(input.buttonStack.walkDirection() == -1 && dx > -WALK_SPEED) {
				dx -= WALK_SPEED / 10f;
				walking = true;
				if(dx < 0)
					facingRight = false;
			}
			else if(input.buttonStack.walkDirection() == 1 && dx < WALK_SPEED) {
				dx += WALK_SPEED / 10f;
				walking = true;
				if(dx > 0)
					facingRight = true;
			}
			else {
				dx *= WALK_FRICTION;
				walking = false;
			}
		}

//		System.out.print("dx,dy: "+dx+", "+dy+"    ");
		tryMove(dx, dy);
//		System.out.print(againstLWall + " - ");
//		System.out.println(onGround + " when " + dy + " is dy");
		
		// Iterate dashingness
		if(dashing) {
			ticksRemaining--;
			if(ticksRemaining == 0) {
				dashing = false;
				// Slow doooown
				dy *= DASH_FRICTION;
				dx *= DASH_FRICTION;
				
				ignoreInput = false;
			}
		}
		
		if(input.buttonStack.peek() == Input.GLIDE && !onGround)
		{
		    glide();
		}
		
		// Run the animations
		if(walking) {
			if(++frame > 8)
				frame = 0;
			
		}
		
		// Reset rotation
		this.setRotation(0);
		
		if(dashing) {
			// Draw dashing character
			this.setRegion(Art.dashCharacter[DASH_TICKS - ticksRemaining][0]);
			this.setRotation(Utility.dirToDegree(dashDirection));
		}
		else if(againstLWall) {
			// Draw character against wall
			this.setRegion(Art.mainCharacter[CLING_TO_WALL_TICKS - ticksRemaining][1]);
		} else {
			// Draw walking character
			this.setRegion(Art.mainCharacter[frame/3][0]);
			if(!facingRight)
				this.flip(true, false);
			
			if(y < 0) {
				currentLevel.placeCharacter();
			}
		}
	}
	
	private void startAnimation(int animation) {
		
	}
	
	private void glide()
	{
	    isGliding = true;
	    
	    dy *= 0.5f ;
	}
}

package javagame;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{

	Image player, sand, rock, zombie;
	Sound mainBGM, gameBGM, gameoverBGM, shoot;
	private Image alphaMap;
	Animation zombieAnimation;
	Bullet bullet;

	float playerX = 300;
	float playerY = 300;
	float playerSpeed = 3;
	float playerAngle = 90f;
	int playerTunnelingBuffer = 10;


	float zombieX = 40;
	float zombieY = 40;
	float zombieSpeed = 5;
	float zombieAngle = 90f;
	float dx = 0;
	float dy = 0;
	float zombieLength = 0;




	ArrayList<ArrayList<String>> gridmap = new ArrayList<ArrayList<String>>();
	int tileWidth = 50;
	int tileHeight = 50;
	float offsetX = 0;
	float offsetY = 0;
	int tileMapSize = 11;

	Input input;
	Controller controller;
	GameContainer gc;


	public Play(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sgb) throws SlickException
	{
		//IMAGES
		player = new Image("res/images/SGB_player_01.png");
		sand = new Image("res/images/SGB_sand_01.png");
		rock = new Image("res/images/SGB_rock_01.png");
		zombie = new Image("res/images/zombie.png");
		bullet = new Bullet("res/images/bullet.png", player);
		alphaMap = new Image("res/images/alphacloak_vertical.png");

		//SPRITESHEET
		SpriteSheet sheet = new SpriteSheet("res/images/SGB_zombiesprite_01.png", 51, 62);

		//MUSIC
		mainBGM = new Sound("res/sound/BGM/Peace.ogg");
		shoot = new Sound("res/sound/fx/Laser_Shoot22.wav");

		
		//mainBGM.loop();
		


		//loading zombie animation
		zombieAnimation = new Animation();
		for (int i=0;i<3;i++) 
		{
			zombieAnimation.addFrame(sheet.getSprite(i,0), 500);
		}

		gc.getGraphics().setBackground(Color.black);

		this.gc = gc;


		input = gc.getInput();
		controller = Controllers.getController(6);

		controller.setXAxisDeadZone(0.4f);
		controller.setXAxisDeadZone(-0.4f);
		controller.setYAxisDeadZone(0.4f);
		controller.setYAxisDeadZone(-0.4f);

		controller.setRXAxisDeadZone(0.4f);
		controller.setRXAxisDeadZone(-0.4f);
		controller.setRYAxisDeadZone(0.4f);
		controller.setRYAxisDeadZone(-0.4f);

		
		makeBackground(gc.getGraphics());


	}

	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{

		/////////////////////////////////////////////////////////

		//Alpha Mapping
		g.clearAlphaMap();
		/**
		 * SetDrawMode switches the type of the graphics "paintbrush". When working
		 * with alpha mapping, you must switch to the ALPHA_MAP mode, and when
		 * working with ALPHA_BLEND, you must switch to the ALPHA_BLEND mode.
		 */

		/**
		 * Over here we are just drawing the grass sprite at 10,50. It's how the
		 * grass sprite originally looks. Control case basically.
		 */
		//		g.setDrawMode(Graphics.MODE_NORMAL);
		//		textureMap.draw(10,50);

		/**
		 * Now we are setting the paintbrush color to black.
		 */
		g.setColor(Color.black);


		/**
		 * We say draw a black rectangle at this location.
		 */
		g.fillRect(0,0,gc.getWidth(),gc.getHeight());
		/**
		 * Now set the gpaintbrush to white.
		 */
		g.setColor(Color.white);
		// write only alpha



		/**
		 * Now we set the "paintbrush" to alpha_mapping mode. This will enable the Slick2D
		 * engine to render the texture in alpha.
		 */
		g.setDrawMode(Graphics.MODE_ALPHA_MAP);
	
		/**
		 * And now we say for that alpha map texture, put it at location 300,50.
		 */
		//Over here is where you set the alphamap to the player
		alphaMap.draw(playerX + player.getWidth()/2 - alphaMap.getWidth()/2, playerY + player.getHeight()/2 - alphaMap.getHeight()/2);



		/**
		 * Now we set the "paintbrush" to alpha blend. Alpha blend is combining two 
		 * images that has the ability to create new blended colors.
		 */
		g.setDrawMode(Graphics.MODE_ALPHA_BLEND);


		renderBackground(g);
		//g.drawImage(zombie, zombieX, zombieY);
		zombieAnimation.draw(zombieX, zombieY);
		zombieAnimation.getCurrentFrame().setRotation(zombieAngle);


		g.setDrawMode(Graphics.MODE_NORMAL);

		//End Alpha Mapping
		//////////////////////////////////////////////////////////////////////////////
		g.drawImage(player, playerX, playerY);
		g.drawString("X: " + controller.getXAxisValue() + ", Y: " + controller.getYAxisValue(), 20, 20);
		g.drawString("Right X: " + controller.getRXAxisValue() + 
				"\n Right Y: " + controller.getRYAxisValue(), 20, 60);

		playerAngle = (float) ((Math.atan2(controller.getRYAxisValue(), controller.getRXAxisValue())) * (180/Math.PI)) + 90f;
		zombieAngle = (float) (Math.atan2(dy, dx) * (180/Math.PI)) + 90f;
		player.setRotation(playerAngle);
		zombie.setRotation(zombieAngle);

		g.drawString("Player Angle: " + playerAngle, 20, 140);

		if(outOfBounds(bullet) == false)
		{
			g.drawImage(bullet.getImage(), bullet.getBulletX(), bullet.getBulletY());
		}

		g.drawString("Play State", 10, 30);

	}

	private void renderBackground(Graphics g) 
	{

		//render the background
		for(int i = 0; i <= tileMapSize; i++)
		{

			//column
			for(int j = 0; j <= tileMapSize; j++)
			{

				if(gridmap.get(i).get(j) == "r")
				{
					g.drawImage(rock, offsetX, offsetY);
					offsetX += tileWidth;
				}
				else if(gridmap.get(i).get(j) == "s")
				{
					g.drawImage(sand, offsetX, offsetY);
					offsetX += tileWidth;
				}
				else
				{
					System.out.println("Unknown tile");
				}

			}

			offsetX = 0;
			offsetY += tileHeight;

		}

		offsetX = 0;
		offsetY = 0;
		
	}

	private void makeBackground(Graphics g) {

	
		
		Random ran = new Random();
		int chance = ran.nextInt(20);
		//render the background
		for(int i = 0; i <= tileMapSize; i++)
		{
			gridmap.add(new ArrayList<String>());
			//column
			for(int j = 0; j <= tileMapSize; j++)
			{
				//the string will determine what type of tile it is, ex. "r" rock, "s" sand...
				if(chance <= 3)
				{
					gridmap.get(i).add("r");
				}
				
				else
				{
					gridmap.get(i).add("s");
				}
				
				chance = ran.nextInt(20);
				
			}
		}

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{



		//		if(input.isKeyDown(Input.KEY_A) && playerInsideBox(gc)){ playerX--; }
		//		if(input.isKeyDown(Input.KEY_D) && playerInsideBox(gc)){ playerX++; }
		//		if(input.isKeyDown(Input.KEY_S) && playerInsideBox(gc)){ playerY++; }
		//		if(input.isKeyDown(Input.KEY_W) && playerInsideBox(gc)){ playerY--; }

		if(controller.getXAxisValue() <= -0.4 && (playerX + playerTunnelingBuffer >= 0)) { playerX = playerX + (controller.getXAxisValue() * playerSpeed); }
		if(controller.getXAxisValue() >= 0.4 && ((playerX + playerTunnelingBuffer + player.getWidth()) <= gc.getWidth())) { playerX = playerX + (controller.getXAxisValue() * playerSpeed); }
		if(controller.getYAxisValue() <= -0.4 && (playerY + playerTunnelingBuffer >= 0)) { playerY = playerY + (controller.getYAxisValue() * playerSpeed);  }
		if(controller.getYAxisValue() >= 0.4 && ((playerY + playerTunnelingBuffer + player.getHeight()) <= gc.getHeight()))  { playerY = playerY + (controller.getYAxisValue() * playerSpeed);  }

		if(controller.getZAxisValue() >= -1 && controller.getZAxisValue() <= -0.4 && bullet.getBulletIsAlive() == false ) 
		{ 
			bullet.setBulletIsAlive(true);
			bullet.setBulletAngle(playerAngle - 90f);
			bullet.setBulletX(playerX + 25);
			bullet.setBulletY(playerY + 25);
			bullet.setBulletDx(10);
			bullet.setBulletDy(10);
			shoot.play();

		}

		if(bullet.getBulletIsAlive() == true)
		{
			//move the bullet along its 2D trajectory
			bullet.moveBullet();


			if(outOfBounds(bullet) == true)
			{
				bullet.setBulletIsAlive(false);

			}
		}

		dx = playerX - zombieX;
		dy = playerY - zombieY;
		zombieLength = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= zombieLength;
		dy /= zombieLength;
		zombieX += dx;
		zombieY += dy;







	}

	/**
	 * Determines if the gameObject is outside of the screen.
	 * @param b
	 * @return
	 */
	private boolean outOfBounds(Bullet b)
	{
		if(b.getBulletX() <= 0 || b.getBulletX() >= gc.getWidth() ||
				b.getBulletY() <= 0 || b.getBulletY() >= gc.getHeight())
		{
			return true;
		}

		return false;
	}

	public int getID()
	{
		return 1; //returns the ID of this class (play is 1)
	}

}

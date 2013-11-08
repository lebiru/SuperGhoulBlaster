package javagame;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{

	Image sand, rock;
	Sound mainBGM, gameBGM, gameoverBGM, shoot, zombieDie;
	private Image alphaMap;
	
	Bullet bullet;

	Player hero;
	Zombie ghoul;
	
	ArrayList<Zombie> ghoulArmy = new ArrayList<Zombie>();
	

	ArrayList<ArrayList<String>> gridmap = new ArrayList<ArrayList<String>>();
	int tileWidth = 50;
	int tileHeight = 50;
	float offsetX = 0;
	float offsetY = 0;
	int tileMapSize = 11;
	int tileMapWidth;
	int tileMapHeight;

	Input input;
	Controller controller;
	GameContainer gc;


	public Play(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sgb) throws SlickException
	{

		

		//IMAGES
		hero = new Player(new Image("res/images/SGB_player_01.png"));
		sand = new Image("res/images/SGB_sand_01.png");
		rock = new Image("res/images/SGB_rock_01.png");

		bullet = new Bullet("res/images/bullet.png", hero.getImage());
		alphaMap = new Image("res/images/alphacloak_vertical.png");
		
		//Making ghoul army
		Random ran = new Random();
		
		for(int i = 0 ; i < 10; i++ )
		{
			
			ghoul = new Zombie(new Image("res/images/zombie.png").getSubImage(0, 0, 50, 62), ran.nextInt(gc.getWidth()), ran.nextInt(gc.getHeight()));
			ghoul.initializeZombieAnimation(new SpriteSheet("res/images/SGB_zombiesprite_01.png", 50, 62));
			ghoulArmy.add(ghoul);
		}
	

		//SOUND & MUSIC
		shoot = new Sound("res/sound/fx/Gunshot.wav");
		zombieDie = new Sound("res/sound/fx/Zombie Kill.wav");
		Sound gameBGM = new Sound("res/sound/BGM/In Game.ogg");
		gameBGM.loop();

		

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

		//Preparing TileMap
		tileMapWidth = gc.getWidth()/tileWidth;
		tileMapHeight = gc.getHeight()/tileHeight;

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
		//alphaMap.draw(hero.getX() + hero.getWidth()/2 - alphaMap.getWidth()/2, hero.getY() + hero.getHeight()/2 - alphaMap.getHeight()/2);



		/**
		 * Now we set the "paintbrush" to alpha blend. Alpha blend is combining two 
		 * images that has the ability to create new blended colors.
		 */
		g.setDrawMode(Graphics.MODE_ALPHA_BLEND);


		renderBackground(g);
		for(Zombie z : ghoulArmy)
		{
			g.drawImage(z.getImage(), z.getX(), z.getY());
			z.getAnimation().draw(z.getX(), z.getY());
			z.getAnimation().getCurrentFrame().setRotation(z.getAngle());
			z.setAngle((float) (Math.atan2(z.getDY(), z.getDX()) * (180/Math.PI)) + 90f);
			z.getImage().setRotation(z.getAngle());
		}



		g.setDrawMode(Graphics.MODE_NORMAL);

		//End Alpha Mapping
		//////////////////////////////////////////////////////////////////////////////
		g.drawImage(hero.getImage(), hero.getX(), hero.getY());

		input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		float mouseDX;
		float mouseDY;
		float mouseLength;
		mouseDX = mouseX - hero.getX();
		mouseDY = mouseY - hero.getY();
		mouseLength = (float) Math.sqrt(Math.pow(mouseDX, 2) + Math.pow(mouseDY, 2));

		
		//hero.setAngle((float) ((Math.atan2(controller.getRYAxisValue(), controller.getRXAxisValue())) * (180/Math.PI)) + 90f);
		hero.setAngle((float) ((Math.atan2(mouseDY, mouseDX)) * (180/Math.PI)) + 90f);
		//ghoul.setAngle((float) (Math.atan2(ghoul.getDY(), ghoul.getDX()) * (180/Math.PI)) + 90f);
		hero.getImage().setRotation(hero.getAngle());
		//ghoul.getImage().setRotation(ghoul.getAngle());


		if(outOfBounds(bullet) == false)
		{
			g.drawImage(bullet.getImage(), bullet.getBulletX(), bullet.getBulletY());
		}

		g.drawString("Player Rect X: " + hero.getRect().getX() + " Y: " + hero.getRect().getY(), 20, 100);
		//g.drawString("Ghoul Rect X: " + ghoul.getRect().getX() + " Y: " + ghoul.getRect().getY(), 20, 150);


	}



	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{




		if(hero.getHealth() > 0)
		{

//			//MOVE USING CONTROLLER
//			if(controller.getXAxisValue() <= -0.4 && (hero.getX() + hero.getTunnelingBuffer() >= 0)) 
//			{ 
//				hero.setX(hero.getX() + (controller.getXAxisValue() * hero.getSpeed())); 
//			}
//			if(controller.getXAxisValue() >= 0.4 && ((hero.getX() + hero.getTunnelingBuffer() + hero.getWidth()) <= gc.getWidth())) 
//			{ 
//				hero.setX(hero.getX() + (controller.getXAxisValue() * hero.getSpeed())); 
//			}
//
//			if(controller.getYAxisValue() <= -0.4 && (hero.getY() + hero.getTunnelingBuffer() >= 0))
//			{ 
//				hero.setY(hero.getY() + (controller.getYAxisValue() * hero.getSpeed()));  
//			}
//			if(controller.getYAxisValue() >= 0.4 && ((hero.getY() + hero.getTunnelingBuffer() + hero.getHeight()) <= gc.getHeight()))  
//			{ 
//				hero.setY(hero.getY() + (controller.getYAxisValue() * hero.getSpeed()));
//			}
			
			//MOVE USING KEYBOARD
			if(input.isKeyDown(Input.KEY_A) && (hero.getX() + hero.getTunnelingBuffer() >= 0))
			{ 
				hero.setX(hero.getX() - hero.getSpeed()); 
			}
			if(input.isKeyDown(Input.KEY_D) && (hero.getX() + hero.getTunnelingBuffer() + hero.getWidth() <= gc.getWidth()))
			{ 
				hero.setX(hero.getX() + hero.getSpeed()); 
			}
			if(input.isKeyDown(Input.KEY_W) && (hero.getY() + hero.getTunnelingBuffer() >= 0))
			{ 
				hero.setY(hero.getY() - hero.getSpeed()); 
			}
			if(input.isKeyDown(Input.KEY_S) && (hero.getY() + hero.getTunnelingBuffer() + hero.getHeight() <= gc.getHeight()))
			{ 
				hero.setY(hero.getY() + hero.getSpeed()); 
			}


//			//SHOOT USING CONTROLLER
//			if(controller.getZAxisValue() >= -1 && controller.getZAxisValue() <= -0.4 && bullet.getBulletIsAlive() == false ) 
//			{ 
//				bullet.setBulletIsAlive(true);
//				bullet.setBulletAngle(hero.getAngle() - 90f);
//				bullet.setBulletX(hero.getX() + 23);
//				bullet.setBulletY(hero.getY() + 23);
//				bullet.setBulletDx(30);
//				bullet.setBulletDy(30);
//				shoot.play();
//
//			}
			
			//SHOOT USING MOUSE
			if(input.isMousePressed(0) && bullet.getBulletIsAlive() == false ) 
			{ 
				bullet.setBulletIsAlive(true);
				bullet.setBulletAngle(hero.getAngle() - 90f);
				bullet.setBulletX(hero.getX() + 23);
				bullet.setBulletY(hero.getY() + 23);
				bullet.setBulletDx(30);
				bullet.setBulletDy(30);
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



			for(Zombie z : ghoulArmy)
			{
				z.move(hero);
				z.updateRect();
			}
		

			//UPDATE
			hero.updateRect();
			bullet.updateRect();
			

			//COLLISION
			if(hero.getRect().intersects(ghoul.getRect()))
			{
				hero.setHealth(-1);
			}
			
			for(int i = 0; i < ghoulArmy.size(); i++)
			{
				if(bullet.getRect().intersects(ghoulArmy.get(i).getRect()))
				{
					System.out.println("Bullet Hit!" + ghoulArmy.size());
					
					bullet.setBulletIsAlive(false);
					ghoulArmy.remove(i);
					zombieDie.play();
				}
			}
			



		}

		if(hero.getHealth() <= 0)
		{
			sbg.enterState(4);
		}









	}

	private void renderBackground(Graphics g) 
	{

		//render the background
		for(int i = 0; i <= tileMapHeight; i++)
		{

			//column
			for(int j = 0; j <= tileMapWidth; j++)
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
		for(int i = 0; i <= tileMapHeight; i++)
		{
			gridmap.add(new ArrayList<String>());
			//column
			for(int j = 0; j <= tileMapWidth; j++)
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

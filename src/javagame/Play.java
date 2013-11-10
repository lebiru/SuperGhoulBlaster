package javagame;


import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;


public class Play extends BasicGameState{

	Image sand, rock;
	Sound mainBGM, gameBGM, gameoverBGM, shoot, zombieDie;
	private Image alphaMap;

	//Bullet bullet;
	ArrayList<Bullet> bulletManager = new ArrayList<Bullet>();
	int numOfBullets = 3;

	Player hero;
	Zombie ghoul;

	ArrayList<Zombie> ghoulArmy = new ArrayList<Zombie>();
	ArrayList<Point2D> ghoulSpawnPoints = new ArrayList<Point2D>();
	

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



		//bullet = new Bullet("res/images/bullet.png", hero.getImage());
		alphaMap = new Image("res/images/flashlight.png");

		//Making ghoul army
		Random ran = new Random();
		
		//Create Ghoul Spawn Points
		ghoulSpawnPoints = createSpawnPoints(ghoulSpawnPoints, gc);
		
		
		for(int i = 0 ; i < 10; i++ )
		{

			ghoul = new Zombie(new Image("res/images/zombie.png").getSubImage(0, 0, 50, 62), ran.nextInt(gc.getWidth()), ran.nextInt(gc.getHeight()));
			ghoul.setPosition(ghoulSpawnPoints.get(ran.nextInt(ghoulSpawnPoints.size())));
			ghoul.initializeZombieAnimation(new SpriteSheet("res/images/SGB_zombiesprite_01.png", 50, 62));
			ghoulArmy.add(ghoul);
		}

		for(int i = 0; i < numOfBullets; i++)
		{
			bulletManager.add(new Bullet("res/images/bullet.png", hero.getImage()));
		}

		//Player Initialization
		hero.setX(gc.getWidth()/2);
		hero.setY(gc.getHeight()/2);



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

	private ArrayList<Point2D> createSpawnPoints(ArrayList<Point2D> gsp, GameContainer gc) 
	{
		Random ran = new Random();
		int numOfSpawnPoints = 100;
		int spawnZoneBuffer = 100;
		
		float x;
		float y;
		int xFlipper;
		int yFlipper;
		
		for(int i = 0; i <= numOfSpawnPoints; i++)
		{
			xFlipper = ran.nextInt(2);
			yFlipper = ran.nextInt(2);
			
			
			if(xFlipper == 0)
			{
				x = ran.nextInt(spawnZoneBuffer) * -1;
			}
			else
			{
				x = gc.getWidth() + ran.nextInt(spawnZoneBuffer);
			}
			
			if(yFlipper == 0)
			{
				y = ran.nextInt(spawnZoneBuffer) * -1;
			}
			else
			{
				y = gc.getWidth() + ran.nextInt(spawnZoneBuffer);
			}
		
			ghoulSpawnPoints.add(new Point2D.Float(x, y));

		}

		
		return gsp;
	}

	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		
		
		
		g.setBackground(Color.black);

		g.setColor(Color.white);
		g.setDrawMode(Graphics.MODE_ALPHA_MAP);

		alphaMap.draw(hero.getX() + hero.getWidth()/2 - alphaMap.getWidth()/2, hero.getY() + hero.getHeight()/2 - alphaMap.getHeight()/2);


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
		
		for(Bullet b : bulletManager)
		{
			if(outOfBounds(b) == false)
			{
				g.drawImage(b.getImage(), b.getBulletX(), b.getBulletY());
			}

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
		hero.getImage().setRotation(hero.getAngle());



	


		g.drawString("Player Rect X: " + hero.getRect().getX() + " Y: " + hero.getRect().getY() +
				"//n GhoulArmy Size: " + ghoulArmy.size(), 20, 100);



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
			for(Bullet b : bulletManager)
			{
				if(input.isMousePressed(0) && b.getBulletIsAlive() == false) 
				{ 

					b.setBulletIsAlive(true);
					b.setBulletAngle(hero.getAngle() - 90f);
					b.setBulletX(hero.getX() + 23);
					b.setBulletY(hero.getY() + 23);
					b.setBulletDx(30);
					b.setBulletDy(30);
					shoot.play();
				}
			}


			for(Bullet b : bulletManager)
			{
				if(b.getBulletIsAlive() == true)
				{
					//move the bullet along its 2D trajectory
					b.moveBullet();


					if(outOfBounds(b) == true)
					{
						b.setBulletIsAlive(false);

					}
				}
			}




			for(Zombie z : ghoulArmy)
			{
				z.move(hero);
				z.updateRect();
			}


			//UPDATE
			hero.updateRect();

			for(Bullet b : bulletManager)
			{
				b.updateRect();
			}



			//COLLISION


			for(Zombie z : ghoulArmy)
			{
				if(hero.getRect().intersects(z.getRect()))
				{
					hero.setHealth(-1);
				}
			}


			for(Bullet b : bulletManager)
			{
				for(int i = 0; i < ghoulArmy.size(); i++)
				{

					if(b.getRect().intersects(ghoulArmy.get(i).getRect()))
					{
						System.out.println("Bullet Hit!" + ghoulArmy.size());

						b.setBulletIsAlive(false);
						ghoulArmy.remove(i);
						zombieDie.play(0.9f,0.2f);
					}
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

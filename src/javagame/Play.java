package javagame;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box.Filler;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class Play extends BasicGameState{

	Image sand, rock, door;
	Sound shoot, zombieDie, reload, batswing;
	private Image alphaMap;


	float doorX;
	float doorY;
	Rectangle doorRect;

	public int waveNumber = 1;

	ArrayList<Bullet> bulletManager = new ArrayList<Bullet>();
	int reloadSize = 30;
	int numOfBullets = reloadSize;

	Player hero;
	Zombie ghoul;
	BaseballBat bat;

	ArrayList<Zombie> ghoulArmy = new ArrayList<Zombie>();
	ArrayList<Point2D> ghoulSpawnPoints = new ArrayList<Point2D>();

	int numOfZombies = 2;
	float increasingSpeed = 2;

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

	public Sound gameBGM;
	public Sound gameOverBGM;


	public Play(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{



		//IMAGES
		hero = new Player(new Image("res/images/SGB_player_01.png"));
		sand = new Image("res/images/SGB_sand_01.png");
		rock = new Image("res/images/SGB_rock_01.png");
		door = new Image("res/images/door.png");
		bat = new BaseballBat("res/images/baseball_bat.png", hero.getImage());

		doorX = gc.getWidth()/2;
		doorY = 0;
		doorRect = new Rectangle(doorX, doorY, door.getWidth(), door.getHeight());

		alphaMap = new Image("res/images/alphaClock2.png");

		//Making ghoul army
		Random ran = new Random();

		//Create Ghoul Spawn Points
		ghoulSpawnPoints = createSpawnPoints(ghoulSpawnPoints, gc);

		//Create the Zombies
		for(int i = 0 ; i < numOfZombies; i++ )
		{

			ghoul = new Zombie
					(
							new Image("res/images/SGB_zombiesprite_01.png").getSubImage(0, 0, 50, 62), 
							ran.nextInt(gc.getWidth()), 
							ran.nextInt(gc.getHeight())
							);
			ghoul.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
			ghoul.setSpeed(ran.nextInt(2) + 1); //FUN?
			ghoul.initializeZombieAnimation(new SpriteSheet("res/images/SGB_zombiesprite_01.png", 50, 62));
			ghoul.setGhoulIsAlive(true);
			ghoulArmy.add(ghoul);
		}

		//instantiate the bullets
		for(int i = 0; i < numOfBullets; i++)
		{
			bulletManager.add(new Bullet("res/images/bulletOne.png"));
		}

		//Player Initialization
		hero.setX(gc.getWidth()/2);
		hero.setY(gc.getHeight()/2);



		//SOUND & MUSIC
		shoot = new Sound("res/sound/fx/Gunshot.wav");
		zombieDie = new Sound("res/sound/fx/Zombie Kill.wav");		
		reload = new Sound("res/sound/fx/Item.wav");
		batswing = new Sound("res/sound/fx/batswing.wav");

		gameBGM = new Sound("res/sound/BGM/In Game.ogg");
		gameOverBGM = new Sound("res/sound/BGM/Game Over.ogg");

		gc.getGraphics().setBackground(Color.black);

		this.gc = gc;


		input = gc.getInput();


		//Preparing TileMap
		tileMapWidth = gc.getWidth()/tileWidth;
		tileMapHeight = gc.getHeight()/tileHeight;

		makeBackground(gc.getGraphics());
		
		
		/////TESTING BAT
		ghoul = new Zombie
				(
						new Image("res/images/SGB_zombiesprite_01.png").getSubImage(0, 0, 50, 62), 
						ran.nextInt(gc.getWidth()), 
						ran.nextInt(gc.getHeight())
						);
		ghoul.setPosition(new Point2D.Float(200, 300));
		ghoul.setSpeed(0); //FUN?
		ghoul.initializeZombieAnimation(new SpriteSheet("res/images/SGB_zombiesprite_01.png", 50, 62));
		ghoul.setGhoulIsAlive(true);
		ghoulArmy.add(ghoul);


	}


	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{

		g.setBackground(Color.black);
		renderBackground(g);


		for(Zombie z : ghoulArmy)
		{
			if(z.getAlive() == true)
			{
				g.drawImage(z.getImage(), z.getX(), z.getY());
				z.getAnimation().draw(z.getX(), z.getY());
				z.getAnimation().getCurrentFrame().setRotation(z.getAngle());
				z.setAngle((float) (Math.atan2(z.getDY(), z.getDX()) * (180/Math.PI)) + 90f);
				z.getImage().setRotation(z.getAngle());
				g.draw(z.healthBar);

				g.setColor(Color.red);
				g.fillRect(z.healthBar.getX(), z.healthBar.getY(), z.getHealth() * 10, z.healthBar.getHeight());
				g.setColor(Color.white);
			}

		}

		for(Bullet b : bulletManager)
		{
			if(outOfBounds(b) == false && b.getAlive() == true)
			{
				g.drawImage(b.getImage(), b.getBulletX(), b.getBulletY());
			}

		}



		g.drawImage(hero.getImage(), hero.getX(), hero.getY());
		hero.getImage().setRotation(hero.getAngle());

		alphaMap.draw(hero.getX() + 25 - alphaMap.getWidth()/2, hero.getY() + 25 - alphaMap.getHeight()/2);
		alphaMap.setRotation(hero.getAngle());


		int aliveCount = 0;
		for(Zombie z : ghoulArmy)
		{
			if(z.getAlive() == true)
			{
				aliveCount++;
			}
		}

		g.drawString("Ghoul Army Size: " + ghoulArmy.size() + " Num of Alive Zombies: " + aliveCount , 20, 70);
		g.drawString("Number of Avaliable Bullets: " + numOfBullets, 20, 90);
		g.drawString("Bat Alive? " + bat.getAlive() + " Bat Angle: " + bat.getAngle()
				+ "End: " + bat.endAngle + " Start: " + bat.startAngle, 20, 110);
		//		for(int i = 0; i < bulletManager.size(); i++)
		//		{
		//			g.drawString("Alive? " + bulletManager.get(i).getAlive() + " dmg: " + bulletManager.get(i).getDamage(), 20, 100 + (i * 20));
		//		}

		if(checkWaveCleared() == true)
		{
			g.drawImage(door, doorX, doorY);
			g.drawRect(doorX, doorY, door.getWidth(), door.getHeight());
		}

		//SWING BAT
		if(bat.getAlive() == true)
		{
			bat.getImage().setRotation(bat.getAngle());
			g.drawImage(bat.getImage(), hero.getX() - hero.getWidth()/2 + 50, hero.getY() + (hero.getHeight()/2) - bat.getImage().getHeight() + 10);
			bat.getImage().setCenterOfRotation(0, 0);
			bat.swing();
		}





	}



	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{

		input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		float mouseDX;
		float mouseDY;
		float mouseLength;
		mouseDX = mouseX - hero.getX();
		mouseDY = mouseY - hero.getY();
		mouseLength = (float) Math.sqrt(Math.pow(mouseDX, 2) + Math.pow(mouseDY, 2));
		hero.setAngle((float) ((Math.atan2(mouseDY, mouseDX)) * (180/Math.PI)) + 90f);


		if(hero.getHealth() > 0)
		{

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


			//SHOOT USING MOUSE
			if(input.isMousePressed(0))
			{
				for(Bullet b : bulletManager)
				{
					//If we've found a free bullet
					if(b.getAlive() == false && numOfBullets > 0) 
					{ 
						b.turnOn(hero);
						shoot.play();

						numOfBullets--;
						return;
					}
				}

			}

			//SWING BAT
			if(input.isKeyPressed(Input.KEY_LSHIFT))
			{
				bat.turnOn(hero);
				batswing.play();
			}

			
			if(bat.isSwingEnd())
			{
				bat.turnOff();
			}


			//RELOAD USING RIGHT MOUSE BUTTON
			if(input.isMousePressed(1))
			{
				for(int i = 0; i < bulletManager.size(); i++)
				{
					bulletManager.get(i).setBulletIsAlive(false);
				}
				numOfBullets = reloadSize;
				reload.play();

			}


			//MOVE BULLETS
			for(Bullet b : bulletManager)
			{
				if(b.getAlive() == true)
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
				if(z.getAlive() == true)
				{
					z.move(hero);
					z.updateRect();	
				}

			}


			//UPDATE
			hero.updateRect();

			for(Bullet b : bulletManager)
			{
				if(b.getAlive() == true)
				{
					b.updateRect();
				}

			}

			bat.updateRect();



			//COLLISION


			for(Zombie z : ghoulArmy)
			{
				if(z.getAlive() == true)
				{
					if(hero.getRect().intersects(z.getRect()))
					{
						hero.setHealth(-1);
					}
				}

			}

			if(bat.isAlive)
			{
				for(Zombie z : ghoulArmy)
				{
					if(bat.getRect().intersects(z.getRect()))
					{
						System.out.println("BAT HIT");
						z.setHealth(bat.getDamage());		
						if(z.getHealth() <= 0)
						{
							z.setGhoulIsAlive(false);	
						}
						zombieDie.play(0.9f,0.2f);
					}
				}


			}



			//Bullet Collision
			for(int j = 0; j < bulletManager.size(); j++)
			{
				for(int i = 0; i < ghoulArmy.size(); i++)
				{
					if(bulletManager.get(j).getRect().intersects(ghoulArmy.get(i).getRect())
							&& ghoulArmy.get(i).getAlive() == true
							&& bulletManager.get(j).getAlive() == true)
					{
						bulletManager.get(j).setBulletIsAlive(false);
						ghoulArmy.get(i).setHealth(bulletManager.get(j).getDamage());		
						if(ghoulArmy.get(i).getHealth() <= 0)
						{
							ghoulArmy.get(i).setGhoulIsAlive(false);	
						}
						zombieDie.play(0.9f,0.2f);
					}
				}

			}

		}

		//Game Over Condition
		if(hero.getHealth() <= 0)
		{
			gameBGM.stop();
			gameOverBGM.loop();
			sbg.enterState(4, new FadeOutTransition(Color.red, 1000), new FadeInTransition(Color.black, 1000));
		}

		//Cleared Wave Condition
		if(checkWaveCleared() == true)
		{

			if(hero.getRect().intersects(doorRect))
			{				
				//Enter the shopping state
				sbg.enterState(5, new FadeOutTransition(Color.white, 1000), new FadeInTransition(Color.white, 1000));
			}


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


	private ArrayList<Point2D> createSpawnPoints(ArrayList<Point2D> gsp, GameContainer gc) 
	{
		Random ran = new Random();
		int numOfSpawnPoints = numOfZombies;

		float x = 0;
		float y = 0;
		int quadFlipper;


		for(int i = 0; i <= numOfSpawnPoints; i++)
		{
			quadFlipper = ran.nextInt(4);

			//TOP
			if(quadFlipper == 0)
			{
				x = ran.nextInt(gc.getWidth());
				y = -100;
			}

			//RIGHT
			if(quadFlipper == 1)
			{
				x = gc.getWidth() + 100;
				y = ran.nextInt(gc.getHeight());
			}

			//BOTTOM
			if(quadFlipper == 2)
			{
				x = ran.nextInt(gc.getWidth());
				y = gc.getHeight()+ 100;
			}

			//LEFT
			if(quadFlipper == 3)
			{
				x = -100;
				y = ran.nextInt(gc.getHeight());
			}


			System.out.println("x: " + x + "  y: " + y);
			gsp.add(new Point2D.Float(x, y));

		}

		return gsp;
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

	public boolean checkWaveCleared()
	{
		boolean clear = true;
		for(Zombie z : ghoulArmy)
		{
			if(z.getAlive() == true)
			{
				clear = false;
			}
		}

		return clear;
	}

	public void cleanUpLevel()
	{
		Random ran = new Random();
		for(Zombie z : ghoulArmy)
		{
			z.setGhoulIsAlive(true);
			z.resetHealth();
			z.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
		}
		hero.setX(gc.getWidth()/2);
		hero.setY(gc.getHeight()/2);


	}

	public void gameOverCleanUpLevel()
	{
		Random ran = new Random();

		for(Zombie z : ghoulArmy)
		{
			z.setGhoulIsAlive(true);
			z.resetHealth();
			z.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
		}

		numOfZombies = 2;

		hero.resetHealth();
		hero.setX(gc.getWidth()/2);
		hero.setY(gc.getHeight()/2);

	}

	public int getWaveNumber()
	{
		return waveNumber;
	}

	public void increaseLevelDifficulty() throws SlickException
	{
		int numOfEnemiesAdded = 2;
		increasingSpeed += 0.1;
		numOfZombies += numOfEnemiesAdded;

		ghoulSpawnPoints = createSpawnPoints(ghoulSpawnPoints, gc);

		Random ran = new Random();
		for(int i = 0 ; i < numOfEnemiesAdded; i++ )
		{

			ghoul = new Zombie
					(
							new Image("res/images/SGB_zombiesprite_01.png").getSubImage(0, 0, 50, 62), 
							ran.nextInt(gc.getWidth()), 
							ran.nextInt(gc.getHeight())
							);
			ghoul.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
			ghoul.setSpeed(ran.nextFloat() * increasingSpeed + 1); //FUN?
			ghoul.initializeZombieAnimation(new SpriteSheet("res/images/SGB_zombiesprite_01.png", 50, 62));
			ghoul.setGhoulIsAlive(true);
			ghoulArmy.add(ghoul);
		}
	}



}






















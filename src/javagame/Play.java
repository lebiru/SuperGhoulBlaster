package javagame;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.input.Controller;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import java.awt.font.*;
import java.io.IOException;


public class Play extends BasicGameState
{

	Image sand, rock, dune, grass, bush;
	Sound shoot, zombieDie, reload, batswing, batHit, coin, footsteps, playerDie, doorSound;

	StatusBar sb;
	ChainLogic cl;

	Image door;
	float doorX;
	float doorY;
	Rectangle doorRect;
	Animation doorAnimation;
	SpriteSheet doorSpriteSheet;

	Random growlRandom = new Random();
	int growlHolder = 0;

	Money m;

	Font UIFont1;
	org.newdawn.slick.UnicodeFont bombardFont;

	public int waveNumber = 1;
	String levelWaveMessage = "Level ";
	float levelWaveMessageCountdown = 100;
	int numOfZombies = 2;
	float increasingSpeed = 2;
	int numOfBosses = waveNumber / 2;

	ArrayList<Bullet> bulletManager = new ArrayList<Bullet>();
	int reloadSize = 5;
	int numOfBullets = reloadSize;
	float reloadTimeMax = 100;
	float reloadTime = reloadTimeMax;
	float reloadTick = 2;
	boolean canShoot = true;

	ArrayList<Coin> coinManager = new ArrayList<Coin>();
	int numOfCoins = 5;


	Player hero;
	Zombie ghoul;
	BaseballBat bat;
	Flashlight fl;

	boolean isPaused = false;

	ArrayList<Zombie> ghoulArmy = new ArrayList<Zombie>();
	ArrayList<Point2D> ghoulSpawnPoints = new ArrayList<Point2D>();
	ArrayList<Sound> zombieGrowls = new ArrayList<Sound>();

	//GameOver Statistics
	double bulletsFired = 0;
	double bulletsHit = 0;
	int zombiesKilled = 0;
	int totalCoinsEarned = 0;
	////////////////////////


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
	public Sound bossBGM;


	public Play(int state)
	{

	}

	@SuppressWarnings("unchecked")
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{

		this.gc = gc;
		cl = new ChainLogic();

		//Setting Up the Font
		try {
			UIFont1 = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, 
					org.newdawn.slick.util.ResourceLoader.getResourceAsStream("res/images/fonts/BOMBARD.ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UIFont1 = UIFont1.deriveFont(java.awt.Font.PLAIN, 24.0f);
		bombardFont = new org.newdawn.slick.UnicodeFont(UIFont1);
		bombardFont.addAsciiGlyphs();
		bombardFont.getEffects().add(new ColorEffect(java.awt.Color.GREEN));
		bombardFont.loadGlyphs();
		//End Setting up the Font


		//IMAGES
		hero = new Player(new Image("res/images/SGB_player_01.png"));
		sand = new Image("res/images/SGB_sand_01.png");
		rock = new Image("res/images/SGB_rock_01.png");
		bush = new Image("res/images/levelart/bush.png");
		grass = new Image("res/images/levelart/grass.png");
		dune = new Image("res/images/levelart/dune.png");

		doorAnimation = new Animation();
		doorSpriteSheet = new SpriteSheet("res/images/SGB_DoorSprite_02.png", 135, 134);
		for(int i = 0; i <= 35; i++)
		{
			doorAnimation.addFrame(doorSpriteSheet.getSprite(i, 0), 100);
		}



		bat = new BaseballBat("res/images/SGB_baseballbat_02.png", hero);
		m = new Money();

		doorX = gc.getWidth()/2 - doorAnimation.getWidth()/2;
		doorY = 0;
		doorRect = new Rectangle(doorX, doorY, doorAnimation.getWidth(), doorAnimation.getHeight());

		fl = new Flashlight("res/images/flashlight.png", "res/images/flashLightDarkness.png");

		//Making ghoul army
		Random ran = new Random();

		//Create Ghoul Spawn Points
		ghoulSpawnPoints = createSpawnPoints(ghoulSpawnPoints, gc);

		//Create the Zombies
		for(int i = 0 ; i < numOfZombies; i++ )
		{

			ghoul = new Zombie
					(
							new SpriteSheet("res/images/SGB_zombiesprite_02.png", 50, 58), 
							new SpriteSheet("res/images/SGB_ZombieBoss_01.png", 200, 232),
							ran.nextInt(gc.getWidth()), 
							ran.nextInt(gc.getHeight())
							);
			ghoul.setImage(ghoul.getRegularAnimation().getCurrentFrame());
			ghoul.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
			ghoul.setSpeed(ran.nextInt(2) + 1);
			ghoul.setGhoulIsAlive(true);
			ghoulArmy.add(ghoul);
		}

		//initializing zombie Growls
		for(int i = 1; i < 11; i++)
		{
			zombieGrowls.add(new Sound("res/sound/fx/Zombie Growls/ZG " + i + ".ogg"));
		}


		//instantiate the bullets
		for(int i = 0; i < numOfBullets; i++)
		{
			bulletManager.add(new Bullet("res/images/bulletOne.png"));
		}

		//instantiate the coins
		for(int i = 0; i < numOfCoins; i++)
		{
			coinManager.add(new Coin(new SpriteSheet("res/images/SGB_coin_01.png", 50, 50)));
		}

		//Player Initialization
		hero.setX(gc.getWidth()/2);
		hero.setY(gc.getHeight()/2);



		//SOUND & MUSIC
		shoot = new Sound("res/sound/fx/Gunshot.ogg");
		zombieDie = new Sound("res/sound/fx/Zombie Kill.ogg");		
		reload = new Sound("res/sound/fx/Item.ogg");
		batswing = new Sound("res/sound/fx/Bat Miss.ogg");
		coin = new Sound("res/sound/fx/Coin.ogg");
		batHit = new Sound("res/sound/fx/Bat.ogg");
		footsteps = new Sound("res/sound/fx/Footsteps.ogg");
		playerDie = new Sound("res/sound/fx/Player Death.ogg");
		doorSound = new Sound("res/sound/fx/Creaky Door Opening.ogg");

		gameBGM = new Sound("res/sound/BGM/In Game.ogg");
		gameOverBGM = new Sound("res/sound/BGM/Game Over.ogg");
		bossBGM = new Sound("res/sound/BGM/Boss Fight.ogg");

		gc.getGraphics().setBackground(Color.black);



		input = gc.getInput();

		//Preparing TileMap
		tileMapWidth = gc.getWidth()/tileWidth;
		tileMapHeight = gc.getHeight()/tileHeight;

		makeBackground(gc.getGraphics());

		sb = new StatusBar(gc, hero, new Image("res/images/SGB_hud_01.png"));


	}


	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{

		g.setBackground(Color.black);
		renderBackground(g);

		g.setFont(bombardFont);


		for(Zombie z : ghoulArmy)
		{
			if(z.getAlive() == true)
			{

				if(isBossLevel())
				{
					z.getBossAnimation().draw(z.getX() - 25, z.getY() - 25, z.bossSize, z.bossSize);

					z.getBossAnimation().getCurrentFrame().setCenterOfRotation(100,115);

					z.getBossAnimation().getCurrentFrame().setRotation(Math.round(z.getAngle()));
				}
				else
				{
					z.getRegularAnimation().draw(z.getX(), z.getY());
					z.getRegularAnimation().getCurrentFrame().setCenterOfRotation(25, 25);
					z.getRegularAnimation().getCurrentFrame().setRotation(Math.round(z.getAngle()));
				}



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
		
		for(Coin c : coinManager)
		{
			if(c.isAlive())
			{
				if(c.getTick() >= c.getTickMax()/2)
				{
					c.getAnimation().draw(c.getX(), c.getY());	
				}
				else if(c.getTick() < c.getTickMax()/2 && c.getTick()%2 == 0 || c.getTick()%3 == 0)
				{
					c.getAnimation().draw(c.getX(), c.getY());	
				}	

			}
		}

		if(checkWaveCleared() == true)
		{
			doorAnimation.draw(doorX, doorY);		
		}

		fl.flashlightImage.draw(hero.getX() + 25 - fl.flashlightImage.getWidth()/2, hero.getY() + 25 - fl.flashlightImage.getHeight()/2);
		fl.flashlightImage.setRotation(hero.getAngle());
		fl.darknessImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		fl.darknessImage.setAlpha(fl.getPower());



		//SWING BAT
		if(bat.getAlive() == true)
		{
			bat.getImage().setRotation(bat.getAngle());
			g.drawImage(bat.getImage(), hero.getX() - hero.getWidth()/2 + 50, hero.getY() + (hero.getHeight()/2) - bat.getImage().getHeight() + 10);
			bat.getImage().setCenterOfRotation(0, 0);
			bat.swing();

		}

		//Draw HUD
		sb.render(g);

		//Display Level Message
		if(levelWaveMessageCountdown >= 0)
		{
			if(isBossLevel())
			{	
				g.drawString("BOSS LEVEL!", gc.getWidth()/2, gc.getHeight()/6);
			}
			else
			{
				g.drawString(levelWaveMessage + " " + waveNumber, gc.getWidth()/2, gc.getHeight()/6);
			}

		}

		

		if(cl.getChains() >= 2)
		{
			g.drawString(cl.getChains() + " Chains!", 10, 50);
		}


	}



	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{

		input = gc.getInput();

		if(isBossLevel())
		{
			if(!bossBGM.playing())
			{
				bossBGM.loop();
			}
		}
		else
		{
			if(!gameBGM.playing())
			{
				gameBGM.loop();
			}
		}

		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		float mouseDX;
		float mouseDY;
		float mouseLength;
		mouseDX = mouseX - hero.getX();
		mouseDY = mouseY - hero.getY();
		mouseLength = (float) Math.sqrt(Math.pow(mouseDX, 2) + Math.pow(mouseDY, 2));
		hero.setAngle((float) ((Math.atan2(mouseDY, mouseDX)) * (180/Math.PI)) + 90f);

		if(input.isKeyPressed(Input.KEY_P))
		{
			isPaused = !isPaused;
		}

		if(!isPaused)
		{
			//MOVE USING KEYBOARD
			hero.move(input, gc, footsteps);



			//SHOOT USING MOUSE
			if(input.isMousePressed(0) && canShoot == true)
			{
				for(Bullet b : bulletManager)
				{
					//If we've found a free bullet
					if(b.getAlive() == false && numOfBullets > 0) 
					{ 
						b.turnOn(hero);
						shoot.play();
						numOfBullets--;
						bulletsFired++;
						return;
					}
				}

			}

			//SWING BAT
			if(input.isKeyPressed(Input.KEY_SPACE))
			{
				bat.turnOn(hero);
				batswing.play(1f, 0.2f);
			}


			if(bat.isSwingEnd())
			{
				bat.turnOff();
			}


			//RELOAD USING RIGHT MOUSE BUTTON
			if(input.isMousePressed(1))
			{
				reloadGunTurnOn();
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
					z.updateRect(isBossLevel());	
					if(z.isDamaged())
					{
						z.updateDamageTick();
					}
					z.updateAngle();
					if(z.canGrowl(growlRandom.nextInt(90)))
					{
						growlHolder = growlRandom.nextInt(10);
						if(!zombieGrowls.get(growlHolder).playing())
						{
							zombieGrowls.get(growlHolder).play(1f, z.getVolume());
						}
					}
				}
			}


			//UPDATE
			hero.updateRect();
			if(hero.isDamaged())
			{
				hero.updateDamageTick();
			}
			cl.update();

			for(Bullet b : bulletManager)
			{
				if(b.getAlive() == true)
				{
					b.updateRect();
				}

			}

			bat.updateRect(hero);
			sb.update(m, hero, fl, reloadTime);
			levelWaveMessageCountdown -= 1;
			if(checkWaveCleared() == false)
			{
				fl.update();
			}
			if(canShoot == false)
			{
				reloadGun();
			}

			for(Coin c : coinManager)
			{
				if(c.isAlive())
				{
					c.update();
				}
			}

			//COLLISION


			for(Zombie z : ghoulArmy)
			{
				if(z.getAlive() == true)
				{
					if(hero.getRect().intersects(z.getRect()))
					{
						hero.setHealth(-1);
						hero.turnOnDamaged();
						if(!hero.grunt.playing())
						{
							hero.grunt.play();
						}

					}
				}

			}

			if(bat.isAlive)
			{
				for(Zombie z : ghoulArmy)
				{
					if(z.getAlive() == true && bat.getCircle().intersects(z.getRect()))
					{

						z.setHealth(bat.getDamage());
						z.turnOnDamaged(bat.getKnockback());
						if(bat.hasPlayedSwingHit == false)
						{
							//zombieDie.play(0.9f,0.2f);
							batHit.play();
							bat.hasPlayedSwingHit = true;
						}


						if(z.getHealth() <= 0)
						{
							cl.turnOn();
							z.setGhoulIsAlive(false);
							zombiesKilled++;
							totalCoinsEarned += ghoulArmy.get(0).moneyValue;
							m.setCurrentCoin(m.currentCoin + ghoulArmy.get(0).moneyValue);

							for(Coin c : coinManager)
							{
								if(!c.isAlive())
								{
									c.turnOn(z);
									return;
								}
							}

						}

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
						ghoulArmy.get(i).turnOnDamaged(5f);
						bulletsHit++;
						if(ghoulArmy.get(i).getHealth() <= 0)
						{
							ghoulArmy.get(i).setGhoulIsAlive(false);	
							cl.turnOn();
							m.setCurrentCoin(m.currentCoin + ghoulArmy.get(i).moneyValue);
							totalCoinsEarned += ghoulArmy.get(i).moneyValue;
							zombiesKilled++;

							for(Coin c : coinManager)
							{
								if(!c.isAlive())
								{
									c.turnOn(ghoulArmy.get(i));
									return;
								}
							}

						}
						zombieDie.play(0.9f,0.2f);
					}
				}

			}

			for(Coin c : coinManager)
			{
				if(c.isAlive() && hero.getRect().intersects(c.getRect()))
				{
					c.turnOff();
					m.setCurrentCoin(m.currentCoin + c.getCoinValue());
					totalCoinsEarned += c.getCoinValue();
					coin.play();
				}

				else if(c.isAlive() && c.getTick() <= 0)
				{
					c.turnOff();
				}

			}



			//Game Over Condition
			if(hero.getHealth() <= 0)
			{
				gameBGM.stop();
				bossBGM.stop();
				playerDie.play();
				gameOverBGM.loop();
				sbg.enterState(4, new FadeOutTransition(Color.red, 1000), new FadeInTransition(Color.black, 1000));
			}

			//Cleared Wave Condition
			if(checkWaveCleared() == true)
			{
				if(hero.getRect().intersects(doorRect))
				{				
					//Enter the shopping state
					gameBGM.stop();
					bossBGM.stop();
					doorSound.play();
					((Shop)sbg.getState(5)).levelUp.loop();
					sbg.enterState(5, new FadeOutTransition(Color.white, 1000), new FadeInTransition(Color.white, 1000));
				}
			}
		}

	}

	private void reloadGun() 
	{
		reloadTime -= reloadTick;
		if(reloadTime <= 0)
		{
			for(int i = 0; i < bulletManager.size(); i++)
			{
				bulletManager.get(i).setBulletIsAlive(false);
			}
			numOfBullets = reloadSize;
			canShoot = true;
			reloadTime = reloadTimeMax;
		}

	}

	private void reloadGunTurnOn() 
	{
		reload.play();
		canShoot = false;


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
				else if(gridmap.get(i).get(j) == "b")
				{
					g.drawImage(bush, offsetX, offsetY);
					offsetX += tileWidth;
				}

				else if(gridmap.get(i).get(j) == "g")
				{
					g.drawImage(grass, offsetX, offsetY);
					offsetX += tileWidth;
				}

				else if(gridmap.get(i).get(j) == "d")
				{
					g.drawImage(dune, offsetX, offsetY);
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

	private void makeBackground(Graphics g) 
	{
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
				else if (chance <= 6 && chance > 3)
				{
					gridmap.get(i).add("b");
				}
				else if (chance <= 9 && chance > 7)
				{
					gridmap.get(i).add("g");
				}
				else if (chance <= 12 && chance > 8)
				{
					gridmap.get(i).add("d");
				}

				//empty sand
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

	public void gameOverCleanUpLevel(StateBasedGame sbg)
	{
		Random ran = new Random();

		
		System.out.println("Ghoul Army size: " + ghoulArmy.size());
		System.out.println("Num of Zombies: " + numOfZombies);
		ghoulArmy.removeAll(ghoulArmy);
		
		Zombie ghoul = new Zombie();
		try {
			ghoul = new Zombie
					(
							new SpriteSheet("res/images/SGB_zombiesprite_02.png", 50, 58), 
							new SpriteSheet("res/images/SGB_ZombieBoss_01.png", 200, 232),
							ran.nextInt(gc.getWidth()), 
							ran.nextInt(gc.getHeight())
							);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ghoul.setImage(ghoul.getRegularAnimation().getCurrentFrame());
		ghoul.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
		ghoul.setSpeed(ran.nextInt(2) + 1);
		ghoul.setGhoulIsAlive(true);
		ghoulArmy.add(ghoul);
		
		System.out.println("Ghoul Army size: " + ghoulArmy.size());
		System.out.println("Num of Zombies: " + numOfZombies);
		for(Zombie z : ghoulArmy)
		{
			z.setGhoulIsAlive(true);
			z.resetHealth();
			z.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
		}

		System.out.println("Ghoul Army size: " + ghoulArmy.size());
		System.out.println("Num of Zombies: " + numOfZombies);
		
		
		
		numOfZombies = 0;
		numOfBosses = 0;
		waveNumber = 1;
		
		System.out.println("Ghoul Army size: " + ghoulArmy.size());
		System.out.println("Num of Zombies: " + numOfZombies);

		m.reset();

		hero.resetHealth();
		hero.setX(gc.getWidth()/2);
		hero.setY(gc.getHeight()/2);
		
		//reset Upgrades
		reloadTick = 2f;
		for(Bullet b : bulletManager)
		{
			b.resetDamage();
		}
		bat.resetDamage();
		bat.resetKnockback();
		
		//Reset Shop
		((Shop)sbg.getState(5)).gunPowerLevel = 0;
		((Shop)sbg.getState(5)).gunReloadLevel = 0;
		((Shop)sbg.getState(5)).batPowerLevel = 0;
		((Shop)sbg.getState(5)).batKnockbackLevel = 0;
		
		((Shop)sbg.getState(5)).gunDamageLevel = ((Shop)sbg.getState(5)).upgradeLevel.get(0);
		((Shop)sbg.getState(5)).gunReloadLevelImage = ((Shop)sbg.getState(5)).upgradeLevel.get(0);
		((Shop)sbg.getState(5)).batPowerLevelImage = ((Shop)sbg.getState(5)).upgradeLevel.get(0);
		((Shop)sbg.getState(5)).batPowerLevelImage = ((Shop)sbg.getState(5)).upgradeLevel.get(0);

		bulletsFired = 0;
		bulletsHit = 0;
		totalCoinsEarned = 0;
		zombiesKilled = 0;
		fl.resetPower();


		levelWaveMessageCountdown = 100;
		
		

	}

	public int getWaveNumber()
	{
		return waveNumber;
	}

	public void increaseLevelDifficulty(boolean isBossLevel) throws SlickException
	{
		levelWaveMessageCountdown = 100;
		int numOfEnemiesAdded = 2;
		increasingSpeed += 0.1;
		numOfZombies += numOfEnemiesAdded;
		waveNumber += 1;


		ghoulSpawnPoints = createSpawnPoints(ghoulSpawnPoints, gc);

		Random ran = new Random();


		if(isBossLevel())
		{
			//If boss level
			numOfBosses = waveNumber/2;
			for(int i = 0 ; i <= numOfBosses; i++ )
			{
				ghoul = new Zombie
						(
								new SpriteSheet("res/images/SGB_zombiesprite_02.png", 50, 58), 
								new SpriteSheet("res/images/SGB_ZombieBoss_01.png", 200, 232),
								ran.nextInt(gc.getWidth()), 
								ran.nextInt(gc.getHeight())
								);
				ghoul.setImage(ghoul.getBossAnimation().getCurrentFrame());
				ghoul.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
				ghoul.setSpeed(ran.nextInt(2) + 1);
				ghoul.setGhoulIsAlive(true);
				ghoulArmy.add(ghoul);
			}
		}

		else
		{
			//if not boss level
			for(int i = 0 ; i < numOfEnemiesAdded; i++ )
			{

				ghoul = new Zombie
						(
								new SpriteSheet("res/images/SGB_zombiesprite_02.png", 50, 58),
								new SpriteSheet("res/images/SGB_ZombieBoss_01.png", 200, 232),
								ran.nextInt(gc.getWidth()), 
								ran.nextInt(gc.getHeight())
								);
				ghoul.setImage(ghoul.getRegularAnimation().getCurrentFrame());
				ghoul.setPosition(ghoulSpawnPoints.get((ran.nextInt(ghoulSpawnPoints.size()))));
				ghoul.setSpeed(ran.nextInt(2) + 1);
				ghoul.setGhoulIsAlive(true);
				ghoulArmy.add(ghoul);
			}
		}


	}

	public boolean isBossLevel()
	{
		if(waveNumber%5 == 0)
		{
			return true;
		}

		return false;
	}



}






















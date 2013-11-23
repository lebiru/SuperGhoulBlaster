package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class Shop extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[4];
	Image playButton, upgradeGunPowerButton, refillLightButton, reloadButton, batKnockbackButton;
	Image gunImage, baseballBatImage;
	Image shopBackground;
	Sound levelUp;
	StateBasedGame sbg;
	GameContainer gc;
	
	Sound buy, notEnoughMoney;

	int gunColumn;
	int batColumn;

	int upgradeOneRow;
	int upgradeTwoRow;
	int upgradeThreeRow;

	int currentCoin = 0;

	String currentMessage;

	private final int gunPowerCost = 50;
	private final int flCost = 100;

	public Shop(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{

		playButton = new Image("res/images/buttons/SGB_buttonplay_01.png");
		upgradeGunPowerButton = new Image("res/images/buttons/upgradeGunPower.png");
		refillLightButton = new Image("res/images/buttons/refillLightButton.png");
		reloadButton = new Image("res/images/buttons/reloadButton.png");

		gunImage = new Image("res/images/SGB_gun_01.png");
		baseballBatImage = new Image("res/images/SGB_baseballbat_01.png");

		shopBackground = new Image("res/images/splashScreens/SGB_SplashShop_01.jpg");
		
		levelUp = new Sound("res/sound/BGM/Level Up.ogg");
		buy = new Sound("res/sound/fx/buy.wav");
		notEnoughMoney = new Sound("res/sound/fx/notEnoughMoney.wav");
		
		

		currentMessage = "Welcome...";

		gunColumn = gc.getWidth()/5 * 2;
		batColumn = gc.getWidth()/5 * 3;

		upgradeOneRow = gc.getHeight()/4;
		upgradeTwoRow = gc.getHeight()/3;
		upgradeThreeRow = gc.getHeight()/2;


		this.gc = gc;
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, playButton, 200, 400 + (0*100), 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[1] = new MouseOverArea(gc, upgradeGunPowerButton, gunColumn, 400 + (1*100), upgradeGunPowerButton.getWidth(), upgradeGunPowerButton.getHeight(), this);
		areas[1].setNormalColor(new Color(1,1,1,0.8f));
		areas[1].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[2] = new MouseOverArea(gc, refillLightButton, gunColumn, 400 + (2*100), 
				refillLightButton.getWidth(), refillLightButton.getHeight(), this);
		areas[2].setNormalColor(new Color(1,1,1,0.8f));
		areas[2].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[3] = new MouseOverArea(gc, reloadButton, gunColumn, 400, 
				reloadButton.getWidth(), reloadButton.getHeight(), this);
		areas[3].setNormalColor(new Color(1,1,1,0.8f));
		areas[3].setMouseOverColor(new Color(1,1,1,0.9f));

	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		shopBackground.draw(0, 0, gc.getWidth(), gc.getHeight());

		//This is what the shop keeper might be saying
		g.drawString(currentMessage, 10, 30);
		g.drawString("Coins: " + currentCoin, 10, 50);

		g.drawImage(gunImage, gunColumn, 100);
		g.drawImage(baseballBatImage, batColumn, 100);	
		
		

		for (int i=0;i<4;i++) 
		{
			areas[i].render(gc, g);
		}
	}

	//for updating logics of the game
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{

		currentCoin = ((Play)sbg.getState(1)).m.currentCoin;

	}

	public int getID()
	{
		return 5; //returns the ID of this class (shop is 5)
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : " + source);

		if (source == areas[0]) 
		{
			System.out.println("Entering Next Level");
			((Play)sbg.getState(1)).cleanUpLevel();
			try {
				((Play)sbg.getState(1)).increaseLevelDifficulty(((Play)sbg.getState(1)).isBossLevel());
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			levelUp.stop();
			if((((Play)sbg.getState(1)).isBossLevel()))
			{
				((Play)sbg.getState(1)).bossBGM.loop();
			}
			else
			{
				((Play)sbg.getState(1)).gameBGM.loop();
			}
			

			sbg.enterState(1, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 1000) );
		}

		//Gun Power Upgrade
		if (source == areas[1]) 
		{
			if(currentCoin >= gunPowerCost)
			{
				System.out.println("Upgrading Gun Power");
				for(Bullet b : ((Play)sbg.getState(1)).bulletManager)
				{
					b.setBulletDamage(2);
				}

				//Decreasing Player Money
				((Play)sbg.getState(1)).m.decreaseCurrentCoin(gunPowerCost);
				currentMessage = "Nice doing business with ya.";
				buy.play();

			}
			else
			{
				System.out.println("Not Enough Money!");
				currentMessage = "Not enough money kid. Go kill some more zombies.";
				notEnoughMoney.play();
			}

		}

		//Refill Flashlight
		if (source == areas[2]) 
		{
			if(currentCoin >= flCost)
			{
				System.out.println("Refilling Flashlight");
				((Play)sbg.getState(1)).fl.setPower(0);

				//Decreasing Player Money
				((Play)sbg.getState(1)).m.decreaseCurrentCoin(flCost);

				currentMessage = "Nice doing business with ya.";
				buy.play();

			}
			else
			{
				currentMessage = "That costs 100 coins. You don't have enough money.";
				notEnoughMoney.play();
			}

		}

		//Reload Upgrade
		if (source == areas[3]) 
		{
			if(currentCoin >= 30)
			{
				System.out.println("Reload Upgrade");
				((Play)sbg.getState(1)).reloadTick = ((Play)sbg.getState(1)).reloadTick + 0.5f;

				//Decreasing Player Money
				((Play)sbg.getState(1)).m.decreaseCurrentCoin(30);
				currentMessage = "Nice doing business with ya.";
				buy.play();

			}
			else
			{
				currentMessage = "That costs 30 coins. You don't have enough money.";
				notEnoughMoney.play();
			}

		}

	}


}














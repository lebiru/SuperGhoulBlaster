package javagame;

import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class Shop extends BasicGameState implements ComponentListener{


	int areasSize = 6;
	private MouseOverArea[] areas = new MouseOverArea[areasSize];
	Image playButton, upgradeGunPowerButton, refillLightButton, 
	reloadButton, batKnockbackButton, batDamageButton;
	
	Image gunDamageLevel, gunReloadLevelImage, batKnockLevelImage, batPowerLevelImage;
	Image lv0, lv1, lv2, lv3, lv4, lv5;
	ArrayList<Image> upgradeLevel = new ArrayList<Image>();
	
	int gunPowerLevel = 0;
	int gunReloadLevel = 0;
	int batKnockbackLevel = 0;
	int batPowerLevel = 0;
	
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
		upgradeGunPowerButton = new Image("res/images/ShopButtons/SGB_ShopButton_GunDamage_01.png");
		refillLightButton = new Image("res/images/ShopButtons/SGB_ShopButton_RefillFlashlight_01.png");
		reloadButton = new Image("res/images/ShopButtons/SGB_ShopButton_GunReloadSpeed_01.png");
		batKnockbackButton = new Image("res/images/ShopButtons/SGB_ShopButton_BatKnockback_01.png");
		batDamageButton = new Image("res/images/ShopButtons/SGB_ShopButton_BatDamage_01.png");
		
		gunDamageLevel = new Image("res/images/ShopButtons/SGB_ShopButton_Level0.png");
		gunReloadLevelImage = new Image("res/images/ShopButtons/SGB_ShopButton_Level0.png");
		batKnockLevelImage = new Image("res/images/ShopButtons/SGB_ShopButton_Level0.png");
		batPowerLevelImage = new Image("res/images/ShopButtons/SGB_ShopButton_Level0.png");
		
		for(int i = 0; i < areasSize; i++)
		{
			upgradeLevel.add(new Image("res/images/ShopButtons/SGB_ShopButton_Level" + i + ".png"));
		}

		
		shopBackground = new Image("res/images/splashScreens/SGB_SplashShop_01.jpg");

		levelUp = new Sound("res/sound/BGM/Level Up.ogg");
		buy = new Sound("res/sound/fx/buy.wav");
		notEnoughMoney = new Sound("res/sound/fx/notEnoughMoney.wav");

		currentMessage = "Welcome...";

		gunColumn = gc.getWidth()/12;
		batColumn = gc.getWidth()/7 * 5;

		upgradeOneRow = gc.getHeight()/10 * 6;
		upgradeTwoRow = gc.getHeight()/10 * 7;
		upgradeThreeRow = gc.getHeight()/10 * 8;


		this.gc = gc;
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, playButton, gc.getWidth()/2 - 50, gc.getHeight() - 100, 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[1] = new MouseOverArea(gc, upgradeGunPowerButton, gunColumn, upgradeOneRow, 
				upgradeGunPowerButton.getWidth(), upgradeGunPowerButton.getHeight(), this);
		areas[1].setNormalColor(new Color(1,1,1,0.8f));
		areas[1].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[2] = new MouseOverArea(gc, refillLightButton, gunColumn, 400 + (2*100), 
				refillLightButton.getWidth(), refillLightButton.getHeight(), this);
		areas[2].setNormalColor(new Color(1,1,1,0.8f));
		areas[2].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[3] = new MouseOverArea(gc, reloadButton, gunColumn, upgradeTwoRow, 
				reloadButton.getWidth(), reloadButton.getHeight(), this);
		areas[3].setNormalColor(new Color(1,1,1,0.8f));
		areas[3].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[4] = new MouseOverArea(gc, batKnockbackButton, batColumn, upgradeOneRow, 
				batKnockbackButton.getWidth(), batKnockbackButton.getHeight(), this);
		areas[4].setNormalColor(new Color(1,1,1,0.8f));
		areas[4].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[5] = new MouseOverArea(gc, batDamageButton, batColumn, upgradeTwoRow, 
				batDamageButton.getWidth(), batDamageButton.getHeight(), this);
		areas[5].setNormalColor(new Color(1,1,1,0.8f));
		areas[5].setMouseOverColor(new Color(1,1,1,0.9f));

	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		shopBackground.draw(0, 0, gc.getWidth(), gc.getHeight());

		//This is what the shop keeper might be saying
		g.drawString(currentMessage, 10, 30);
		g.drawString("Coins: " + currentCoin, 10, 50);

		for (int i = 0 ; i < areasSize ; i++) 
		{
			areas[i].render(gc, g);
		}
		
		gunDamageLevel.draw(gunColumn + upgradeGunPowerButton.getWidth() + 10, upgradeOneRow);
		gunReloadLevelImage.draw(gunColumn + reloadButton.getWidth() + 10, upgradeTwoRow);
		batKnockLevelImage.draw(batColumn + batKnockbackButton.getWidth() - 90, upgradeOneRow);
		batPowerLevelImage.draw(batColumn + batDamageButton.getWidth() - 90, upgradeTwoRow);
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
			try 
			{
				((Play)sbg.getState(1)).increaseLevelDifficulty(((Play)sbg.getState(1)).isBossLevel());
			} catch (SlickException e) 
			{
				System.out.println(e);
			}
			levelUp.stop();
			if((((Play)sbg.getState(1)).isBossLevel()))
			{
				//((Play)sbg.getState(1)).bossBGM.loop();
			}
			else
			{
				//((Play)sbg.getState(1)).gameBGM.loop();
			}


			sbg.enterState(1, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 1000) );
		}

		//Gun Power Upgrade
		if (source == areas[1]) 
		{
			if(currentCoin >= gunPowerCost && gunPowerLevel < 5)
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
				gunPowerLevel++;
				gunDamageLevel = upgradeLevel.get(gunPowerLevel); 

			}
			else if(gunPowerLevel >= 5)
			{
				currentMessage = "Maxed level reached!";
				notEnoughMoney.play();
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
			if(currentCoin >= 30 && gunReloadLevel < 5)
			{
				System.out.println("Reload Upgrade");
				((Play)sbg.getState(1)).reloadTick = ((Play)sbg.getState(1)).reloadTick + 0.5f;

				//Decreasing Player Money
				((Play)sbg.getState(1)).m.decreaseCurrentCoin(30);
				currentMessage = "Nice doing business with ya.";
				buy.play();
				gunReloadLevel++;
				gunReloadLevelImage = upgradeLevel.get(gunReloadLevel); 

			}
			else if(gunReloadLevel >= 5)
			{
				currentMessage = "Maxed level reached!";
				notEnoughMoney.play();
			}
			else
			{
				currentMessage = "That costs 30 coins. You don't have enough money.";
				notEnoughMoney.play();
			}

		}

		//Bat Knockback Upgrade
		if (source == areas[4]) 
		{
			if(currentCoin >= 30 && batKnockbackLevel < 5)
			{
				System.out.println("Bat Knockback Upgrade");

				((Play)sbg.getState(1)).bat.setKnockback(((Play)sbg.getState(1)).bat.getKnockback() + 3f);

				//Decreasing Player Money
				((Play)sbg.getState(1)).m.decreaseCurrentCoin(30);
				currentMessage = "Nice doing business with ya.";
				buy.play();
				batKnockbackLevel++;
				batKnockLevelImage = upgradeLevel.get(batKnockbackLevel);
				

			}
			else if(batKnockbackLevel >= 5)
			{
				currentMessage = "Maxed level reached!";
				notEnoughMoney.play();
			}
			else
			{
				currentMessage = "That costs 30 coins. You don't have enough money.";
				notEnoughMoney.play();
			}

		}

		//Bat Damage Upgrade
		if (source == areas[5]) 
		{
			if(currentCoin >= 30 && batPowerLevel < 5)
			{
				System.out.println("Bat Damage Upgrade");

				((Play)sbg.getState(1)).bat.setDamage(((Play)sbg.getState(1)).bat.getDamage() + 2f);

				//Decreasing Player Money
				((Play)sbg.getState(1)).m.decreaseCurrentCoin(30);
				currentMessage = "Nice doing business with ya.";
				buy.play();
				batPowerLevel++;
				batPowerLevelImage = upgradeLevel.get(batPowerLevel);

			}
			else if(batPowerLevel >= 5)
			{
				currentMessage = "Maxed level reached!";
				notEnoughMoney.play();
			}
			else
			{
				currentMessage = "That costs 30 coins. You don't have enough money.";
				notEnoughMoney.play();
			}

		}

		//Not enough time to implement
		//Gun Pierce Upgrade
//		if (source == areas[6]) 
//		{
//			if(currentCoin >= 30)
//			{
//				System.out.println("Gun Pierce Upgrade");
//
//				for(Bullet b : ((Play)sbg.getState(1)).bulletManager)
//				{
//					b.setPierceLevel(b.getPierceLevel() + 1);
//				}
//				
//
//				//Decreasing Player Money
//				((Play)sbg.getState(1)).m.decreaseCurrentCoin(30);
//				currentMessage = "Nice doing business with ya.";
//				buy.play();
//
//			}
//			else
//			{
//				currentMessage = "That costs 30 coins. You don't have enough money.";
//				notEnoughMoney.play();
//			}
//
//		}

	}


}














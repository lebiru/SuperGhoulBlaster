package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class StatusBar 
{

	float health;
	float flPower;
	float reloadWidth;
	int coins;

	GameContainer gc;

	int HUDHeightOffset = 100;
	int HUDWidthOffset = 10; 

	Rectangle statusBarHUD;
	Image statusBarImage;

	Rectangle heroHealthRect;
	Rectangle heroHealthRectFull;

	ShapeFill reloadFill;
	Rectangle reloadRect;
	


	int columnOne; 
	int columnTwo;
	int columnThree; 

	int rowOne;
	int rowTwo;

	boolean isVisible = false;

	int level;
	


	StatusBar(GameContainer gc, Player hero, Image i)
	{
		this.gc = gc;
		statusBarHUD = new Rectangle(HUDWidthOffset, gc.getHeight() - HUDHeightOffset, gc.getWidth() - (HUDWidthOffset * 2), HUDHeightOffset - HUDWidthOffset);
		statusBarImage = i;
		

		heroHealthRect = new Rectangle(gc.getWidth()/2, gc.getHeight() - 50, 200, 10);
		heroHealthRectFull = new Rectangle(gc.getWidth()/2 + 1, gc.getHeight() - 51, heroHealthRect.getWidth(), 9);

		health = hero.getHealth();

		columnOne = gc.getWidth()/10; 
		columnTwo = gc.getWidth()/2;
		columnThree = (gc.getWidth()/4)*3; 

		rowOne = gc.getHeight() - 100;
		rowTwo = gc.getHeight() - 50;
		
		reloadRect = new Rectangle(columnOne, rowTwo, 100, 10);
		reloadFill = new GradientFill(reloadRect.getX(), reloadRect.getY(), Color.yellow, 
				reloadRect.getX() + reloadRect.getWidth(), reloadRect.getY() + reloadRect.getHeight(), Color.orange);
	}

	public void setStatusBarHealth(float newHealth)
	{
		health = newHealth;
	}

	public float getStatusBarHealth()
	{
		return health;
	}

	public void setFlashlightPower(float newFLPower)
	{
		flPower = newFLPower;
	}

	public float getFlashlightPower()
	{
		return flPower;
	}

	public void setCoins(int newCoins)
	{
		coins = newCoins;
	}

	public int getCoins()
	{
		return coins;
	}

	public void setVisible(boolean visibility)
	{
		isVisible = visibility;
	}

	public boolean getVisibility()
	{
		return isVisible;
	}

	public void update(Money m, Player hero, Flashlight fl, float reloadWidth)
	{
		coins = m.currentCoin;
		health = hero.getHealth();
		flPower = fl.getPower();
		this.reloadWidth = reloadWidth;

	}

	public Graphics render(Graphics g)
	{
		//g.draw(this.statusBarHUD);
		g.drawImage(statusBarImage, HUDWidthOffset, gc.getHeight() - HUDHeightOffset + 95, 
				gc.getWidth() - (HUDWidthOffset * 1), HUDHeightOffset - HUDWidthOffset + 550, 
				0,0,statusBarImage.getWidth(), statusBarImage.getHeight());
		g.drawString("Coins: " + coins, columnOne, rowOne);
		g.drawString("Health ", columnTwo, rowOne);
		g.drawString("Flashlight Power: " + Math.round((100 - (flPower * 100))) + "%", columnThree, rowOne);

		g.draw(heroHealthRect);
		g.setColor(Color.red);
		g.fillRect(heroHealthRect.getX(), heroHealthRect.getY(), (health * 2) - 1, heroHealthRectFull.getHeight());
		g.setColor(Color.white);
		
		g.draw(reloadRect, reloadFill);
		g.fillRect(reloadRect.getX(), reloadRect.getY(), reloadWidth, 10);
		

		return g;
	}

	public Rectangle getRect()
	{
		return statusBarHUD;
	}


}













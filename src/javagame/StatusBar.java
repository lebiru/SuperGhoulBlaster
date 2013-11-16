package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class StatusBar 
{

	float health;
	float flPower;
	int coins;
	
	GameContainer gc;
	
	int HUDHeightOffset = 100;
	int HUDWidthOffset = 10; 
	
	Rectangle statusBarHUD;
	Rectangle heroHealthRect;
	Rectangle heroHealthRectFull;
	
	boolean isVisible = false;
	
	int level;
	
	
	StatusBar(GameContainer gc, Player hero)
	{
		this.gc = gc;
		statusBarHUD = new Rectangle(HUDWidthOffset, gc.getHeight() - HUDHeightOffset, gc.getWidth() - (HUDWidthOffset * 2), HUDHeightOffset - HUDWidthOffset);
		
		heroHealthRect = new Rectangle(gc.getWidth()/2, gc.getHeight() - 50, 200, 10);
		heroHealthRectFull = new Rectangle(gc.getWidth()/2 + 1, gc.getHeight() - 51, heroHealthRect.getWidth(), 9);
		
		health = hero.getHealth();
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
	
	public void update(Money m, Player hero, Flashlight fl)
	{
		coins = m.currentCoin;
		health = hero.getHealth();
		flPower = fl.getPower();
		
	}
	
	public Graphics render(Graphics g)
	{
		g.draw(this.statusBarHUD);
		g.drawString("Coins: " + coins, gc.getWidth()/4, gc.getHeight() - 70);
		g.drawString("Health ", gc.getWidth()/2, gc.getHeight() - 70);
		g.drawString("Flashlight Power: " + (100 - (flPower * 100)) + "%", (gc.getWidth()/4)*3, gc.getHeight() - 70);
		
		g.draw(heroHealthRect);
		g.setColor(Color.red);
		g.fillRect(heroHealthRect.getX(), heroHealthRect.getY(), (health * 2) - 1, heroHealthRectFull.getHeight());
		g.setColor(Color.white);
		
		return g;
	}
	
	public Rectangle getRect()
	{
		return statusBarHUD;
	}
	
	
}













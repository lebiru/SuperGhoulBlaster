package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class StatusBar 
{

	float health;
	float flashlightPower;
	int coins;
	
	GameContainer gc;
	
	int HUDHeightOffset = 100;
	int HUDWidthOffset = 10; 
	
	Rectangle statusBarHUD;
	
	boolean isVisible = false;
	
	int level;
	
	
	StatusBar(GameContainer gc)
	{
		this.gc = gc;
		statusBarHUD = new Rectangle(HUDWidthOffset, gc.getHeight() - HUDHeightOffset, gc.getWidth() - (HUDWidthOffset * 2), HUDHeightOffset - HUDWidthOffset);
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
		flashlightPower = newFLPower;
	}
	
	public float getFlashlightPower()
	{
		return flashlightPower;
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
	
	public void update()
	{
		
	}
	
	public Graphics render(Graphics g)
	{
		g.draw(this.statusBarHUD);
		g.drawString("Coins: ", gc.getWidth()/4, gc.getHeight() - 70);
		g.drawString("Health: ", gc.getWidth()/2, gc.getHeight() - 70);
		g.drawString("Flashlight Power: ", (gc.getWidth()/4)*3, gc.getHeight() - 70);
		
		return g;
	}
	
	public Rectangle getRect()
	{
		return statusBarHUD;
	}
	
	
}













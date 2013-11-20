package javagame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Coin extends Image
{

	private float coinX;
	private float coinY;
	private float coinWidth;
	private float coinHeight;
	
	private int coinValue;
	
	private float tickMax = 200f;
	private float tick;
	
	private boolean isAlive;
	
	private int coinAnimationSpeed = 500;
	
	//private Image image;
	private Animation animation;
	private Rectangle coinRect;
	
	public Coin(SpriteSheet sp) throws SlickException
	{
		isAlive = false;
		tick = tickMax;
		coinValue = 5;
		
		animation = new Animation();
		Image holder;
		
		
		for (int i=0 ; i <= 3 ; i++) 
		{
			holder = sp.getSprite(i, 0);
			animation.addFrame(holder, coinAnimationSpeed);
		}
	}
	
	public Rectangle getRect()
	{
		return coinRect;
	}
	
	public Animation getAnimation()
	{
		return animation;
	}
	
	public void update()
	{
		coinRect.setBounds(coinX, coinY, coinWidth, coinHeight);
		tick--;
	}
	
	public float getTick()
	{
		return tick;
	}
	
	public float getTickMax()
	{
		return tickMax;
	}
	
	public void turnOn(Zombie z)
	{
		isAlive = true;
		coinX = z.getX();
		coinY = z.getY();
		coinWidth = animation.getCurrentFrame().getWidth();
		coinHeight = animation.getCurrentFrame().getHeight();
		coinRect = new Rectangle(coinX, coinY, coinWidth, coinHeight);
		
	}
	
	public void turnOff()
	{
		isAlive = false;
		tick = tickMax;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	public float getX()
	{
		return coinX;
	}
	
	public float getY()
	{
		return coinY;
	}
	
	public int getCoinValue()
	{
		return coinValue;
	}
}















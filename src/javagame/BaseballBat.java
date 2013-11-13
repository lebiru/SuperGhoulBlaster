package javagame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class BaseballBat {

	Rectangle batRect;
	Image batImage;
	
	float batX;
	float batY;
	
	float batWidth;
	float batHeight;
	
	int damage = 2;
	
	float batAngle = 90f;
	float startAngle;
	float endAngle;
	
	float speed = 40f;
	
	boolean isAlive = false;
	
	
	
	BaseballBat(String string, Image parent) throws SlickException
	{
		this.batRect = new Rectangle(this.batX, this.batY, this.batWidth, this.batHeight);
		this.batImage = new Image(string);
	}
	
	public void updateRect() 
	{
		this.batRect.setLocation(this.batX, this.batY);
	}

	public Image getImage() 
	{
		return batImage;
	}

	public void turnOn(Player hero) 
	{
		isAlive = true;	
		startAngle = hero.getAngle() + 180;
		endAngle = startAngle + 180;
		batAngle = startAngle;
	}
	
	public boolean getAlive()
	{
		return isAlive;
	}
	
	public void swing()
	{
		this.batAngle += speed;
		
	}

	public float getAngle() 
	{
		return this.batAngle;
	}

	public void setAngle(float angle) 
	{
		this.batAngle = angle;
	}
	
	/**
	 * Checks to see if the bat has completed it's swing.
	 * If it has, we turn the bat off. If not, we continue it's live logic.
	 * @return
	 */
	public boolean isSwingEnd()
	{
		if(Math.abs(batAngle) >= endAngle)
		{
			return true;
		}
		
		return false;
		
	}

	public void turnOff() 
	{
		isAlive = false;
	}
	
	public Rectangle getRect()
	{
		return batRect;
	}

	public int getDamage() 
	{
		return damage;
	}
	
}

















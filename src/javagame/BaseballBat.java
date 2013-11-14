package javagame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class BaseballBat {

	Circle batCircle;
	Image batImage;
	
	float batX;
	float batY;
	
	float batRadius = 70;
	
	float damage = 1f;
	
	float batAngle = 90f;
	float startAngle;
	float endAngle;
	
	boolean hasPlayedSwingHit = false;
	
	float speed = 40f;
	
	boolean isAlive = false;
	
	
	
	BaseballBat(String string, Player hero) throws SlickException
	{
		this.batImage = new Image(string);
		//this.batX = hero.getX() - hero.getWidth()/2 + 50;
		//this.batY = hero.getY() + (hero.getHeight()/2) - this.batImage.getHeight() + 10;
		batX = hero.getX();
		batY = hero.getY();
		this.batCircle = new Circle(this.batX, this.batY, this.batRadius);
		
		
	}
	
	public void updateRect(Player hero) 
	{
		this.batX = hero.getX() - 45;
		this.batY = hero.getY() - 50;
		this.batCircle.setLocation(batX, batY);
		
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

	//this could be a problem for enemies hitting coordinate -500, -500, ie big bosses...
	public void turnOff() 
	{
		isAlive = false;
		hasPlayedSwingHit = false;
		batX = -500;
		batY = -500;
	}
	
	//Called when hits a zombie, continues playing animation.
	public void turnOffLogic()
	{
		batX = -500;
		batY = -500;
	}
	
	public Circle getCircle()
	{
		return batCircle;
	}

	public float getDamage() 
	{
		return damage;
	}

	public float getX() 
	{
		return this.batX;
	}
	public float getY()
	{
		return this.batY;
	}
	
}

















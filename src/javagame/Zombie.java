package javagame;

import java.awt.geom.Point2D;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Zombie 
{
	
	private float zombieX = 300;
	private float zombieY = 300;
	
	float zombiedx = 0;
	float zombiedy = 0;
	
	private float zombieWidth = 50;
	private float zombieHeight = 50;
	private float zombieSpeed = 3;
	private float zombieAngle = 90f;
	private int zombieHealth = 10;
	
	int moneyValue = 10;
	
	private boolean isAlive = false;
	
	Animation zombieAnimation;
	
	float zombieLength = 0;
	
	Rectangle zombieRect;
	Image zombieImage;
	
	Rectangle healthBar;
	
	
	Zombie(Image i, float startX, float startY) throws SlickException
	{
		this.zombieX = startX;
		this.zombieY = startY;
		this.zombieRect = new Rectangle(this.zombieX, this.zombieY, this.zombieWidth, this.zombieHeight);
		this.zombieImage = i;
		
		healthBar = new Rectangle(zombieX + 50, zombieY + 50, 100, 20);
		
		
	}
	
	void setSpeed(float newSpeed)
	{
		this.zombieSpeed = newSpeed;
	}
	
	void setX(float x)
	{
		this.zombieX = x;
	}
	
	void setY(float y)
	{
		this.zombieY = y;
	}
	
	Image getImage()
	{
		return this.zombieImage;
	}
	
	void updateRect()
	{
		this.zombieRect.setLocation(zombieX, zombieY);
	}
	
	Rectangle getRect()
	{
		return zombieRect;
	}
	
	float getX()
	{
		return zombieX;
	}
	
	float getY()
	{
		return zombieY;
	}
	
	float getAngle()
	{
		return zombieAngle;
	}
	
	float getSpeed()
	{
		return zombieSpeed;
	}
	
	float getWidth()
	{
		return zombieImage.getWidth();
	}
	
	float getHeight()
	{
		return zombieImage.getHeight();
	}
	
	void setAngle(float angle)
	{
		this.zombieAngle = angle;
	}
	
	int getHealth()
	{
		return zombieHealth;
	}
	
	float getDX()
	{
		return zombiedx;
	}
	
	float getDY()
	{
		return zombiedy;
	}

	public void move(Player hero) 
	{
		zombiedx = hero.getX() - zombieX;
		zombiedy = hero.getY() - zombieY;
		zombieLength = (float) Math.sqrt(zombiedx*zombiedx + zombiedy*zombiedy);
		zombiedx /= zombieLength;
		zombiedy /= zombieLength;
		zombieX += (zombiedx * zombieSpeed);
		zombieY += (zombiedy * zombieSpeed);
		
		healthBar.setBounds(zombieX + 50, zombieY + 50, 100, 20);
		
	}

	public void initializeZombieAnimation(SpriteSheet sp) 
	{
		//loading zombie animation
		float green = new Random().nextFloat();
		float red = new Random().nextFloat();
		float blue = new Random().nextFloat();
		
		Image holder;
		
		zombieAnimation = new Animation();
		for (int i=0;i<3;i++) 
		{
			holder = sp.getSprite(i, 0);
			holder.setImageColor(red, green, blue);
			zombieAnimation.addFrame(holder, 500);
			
		}
		
	}
	
	Animation getAnimation()
	{
		return zombieAnimation;
	}

	public void setPosition(Point2D point2d) 
	{
		this.zombieX = (float)point2d.getX();
		this.zombieY = (float)point2d.getY();		
	}

	public void setGhoulIsAlive(boolean b) 
	{
		this.isAlive = b;
		
	}
	
	public boolean getAlive()
	{
		return this.isAlive;
	}
	
	public void setHealth(float f)
	{
		this.zombieHealth -= f;
	}

	public void resetHealth() 
	{
		this.zombieHealth = 10;
		
	}

	
}














package javagame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Bullet extends Image
{

	private float bulletX;
	private float bulletY;
	private float bulletDx;
	private float bulletDy;
	private float bulletAngle;
	private int   bulletDamage;
	private boolean bulletIsAlive;
	
	private Image image;
	
	Rectangle bulletRect;
	
	public Bullet(String string, Image parent) throws SlickException 
	{
		this.image = new Image(string);
		bulletIsAlive = false;
		bulletRect = new Rectangle(bulletX, bulletY, 10, 10);
	
	}
	
	public float getBulletX()
	{
		return bulletX;
	}
	
	public float getBulletY()
	{
		return bulletY;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public float getBulletAngle()
	{
		return bulletAngle;
	}
	
	public void setBulletIsAlive(boolean a)
	{
		bulletIsAlive = a;
	}
	
	public void setBulletAngle(float newAngle)
	{
		bulletAngle = newAngle;
	}
	
	public void setBulletX(float newX)
	{
		bulletX = newX;
	}
	
	public void setBulletY(float newY)
	{
		bulletY = newY;
	}
	
	public void moveBullet()
	{
		bulletX += Math.cos(bulletAngle * (Math.PI / 180)) * bulletDx;
		bulletY += Math.sin(bulletAngle * (Math.PI / 180)) * bulletDy;
		this.image.setRotation(bulletAngle + 90f);
	}

	public void setBulletDx(int i) 
	{
		bulletDx = i;
		
	}

	public void setBulletDy(int i) 
	{
		bulletDy = i;
		
	}
	
	public Rectangle getRect()
	{
		return bulletRect;
	}

	public void updateRect() 
	{
		this.bulletRect.setLocation(bulletX, bulletY);
	}

	public boolean getAlive() 
	{
		return this.bulletIsAlive;
	}


}

















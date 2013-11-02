package javagame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bullet extends Image
{

	private float bulletX;
	private float bulletY;
	private float bulletDx;
	private float bulletDy;
	private float bulletAngle;
	private boolean bulletIsAlive;
	private Image image;
	
	public Bullet(String string, Image parent) throws SlickException 
	{
		this.image = new Image(string);
		bulletIsAlive = false;
	
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
	
	public boolean getBulletIsAlive()
	{
		return bulletIsAlive;
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
	}

	public void setBulletDx(int i) 
	{
		bulletDx = i;
		
	}

	public void setBulletDy(int i) 
	{
		bulletDy = i;
		
	}

}

















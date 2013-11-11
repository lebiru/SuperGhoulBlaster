package javagame;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class Player 
{	
	float playerX = 300;
	float playerY = 300;
	
	float playerCenterX;
	float playerCenterY;
	
	public float playerWidth = 50;
	public float playerHeight = 50;
	
	float playerSpeed = 5;
	float playerAngle = 90f;
	
	float tunnelingBuffer = 5;
	
	int playerHealth = 100;
	
	Rectangle playerRect;
	Image playerImage;
	
	Player(Image i)
	{
		this.playerRect = new Rectangle(this.playerX, this.playerY, this.playerWidth, this.playerHeight);
		this.playerImage = i;
		this.playerCenterX = playerWidth/2;
		this.playerCenterY = playerHeight/2;
	}
	
	void setX(float x)
	{
		this.playerX = x;
	}
	
	void setY(float y)
	{
		this.playerY = y;
	}
	
	Image getImage()
	{
		return this.playerImage;
	}
	
	void updateRect()
	{
		this.playerRect.setLocation(playerX, playerY);
	}
	
	Rectangle getRect()
	{
		return playerRect;
	}
	
	float getX()
	{
		return playerX;
	}
	
	float getY()
	{
		return playerY;
	}
	
	float getAngle()
	{
		return playerAngle;
	}
	
	float getSpeed()
	{
		return playerSpeed;
	}
	
	float getWidth()
	{
		return playerImage.getWidth();
	}
	
	float getHeight()
	{
		return playerImage.getHeight();
	}
	
	void setAngle(float angle)
	{
		this.playerAngle = angle;
	}
	
	int getHealth()
	{
		return playerHealth;
	}
	
	void setHealth(int foo)
	{
		playerHealth += foo;
	}
	
	void resetHealth()
	{
		playerHealth = 100;
	}
	
	float getTunnelingBuffer()
	{
		return tunnelingBuffer;
	}

	
	
	
	
	
	
	
}

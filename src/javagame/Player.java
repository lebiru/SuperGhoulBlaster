package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
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

	public void move(Input in, GameContainer gc, Sound footsteps) 
	{
		

		if(in.isKeyDown(Input.KEY_A) && (this.getX() + this.getTunnelingBuffer() >= 0))
		{ 
			this.setX(this.getX() - this.getSpeed());
			if(!footsteps.playing())
			{
				footsteps.play();
			}
		}
		if(in.isKeyDown(Input.KEY_D) && (this.getX() + this.getTunnelingBuffer() + this.getWidth() <= gc.getWidth()))
		{ 
			this.setX(this.getX() + this.getSpeed()); 
			if(!footsteps.playing())
			{
				footsteps.play();
			}
		}
		if(in.isKeyDown(Input.KEY_W) && (this.getY() + this.getTunnelingBuffer() >= 0))
		{ 
			this.setY(this.getY() - this.getSpeed()); 
			if(!footsteps.playing())
			{
				footsteps.play();
			}
		}
		if(in.isKeyDown(Input.KEY_S) && (this.getY() + this.getTunnelingBuffer() + this.getHeight() <= gc.getHeight()))
		{ 
			this.setY(this.getY() + this.getSpeed()); 
			if(!footsteps.playing())
			{
				footsteps.play();
			}
		}
		if(!in.isKeyDown(Input.KEY_A) &&
				!in.isKeyDown(Input.KEY_D) &&
				!in.isKeyDown(Input.KEY_W) &&
				!in.isKeyDown(Input.KEY_S))
		{
			footsteps.stop();
		}




	}








}

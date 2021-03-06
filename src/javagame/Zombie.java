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

	public float bossSize = 200;
	private float zombieX = 300;
	private float zombieY = 300;
	private float zombieWidth = 50;
	private float zombieHeight = 58;
	private float zombieSpeed = 3;
	private float zombieAngle = 90f;
	float zombiedx = 0;
	float zombiedy = 0;
	float zombieLength = 0;

	private int zombieHealth = 10;
	int moneyValue = 10;

	private boolean isAlive = false;

	Animation zombieAnimation;
	Animation bossAnimation;
	int zombieAnimationSpeed = 50;

	Rectangle zombieRect;
	Rectangle healthBar;
	
	Image zombieImage;
	Image bossImage;
	
	private float dmgTicker = 0f;
	private float dmgTickerMax = 10f;
	private float dmgTickerIncrement = 1f;
	private boolean isDamaged = false;
	
	private float knockback = 0f; //this should be a weapon attribute

	Zombie(SpriteSheet sp, SpriteSheet bsp, float startX, float startY) throws SlickException
	{
		this.zombieX = startX;
		this.zombieY = startY;
		this.zombieRect = new Rectangle(this.zombieX, this.zombieY, this.zombieWidth, this.zombieHeight);

		healthBar = new Rectangle(zombieX + 50, zombieY + 50, 100, 20);

		Image holder;

		zombieAnimation = new Animation();
		bossAnimation = new Animation();
		for (int i = 0; i < 32;i++) 
		{
			holder = sp.getSprite(i, 0);
			zombieAnimation.addFrame(holder, zombieAnimationSpeed);
		}
		
		for (int i = 0; i < 32;i++) 
		{
			holder = bsp.getSprite(i, 0);
			bossAnimation.addFrame(holder, zombieAnimationSpeed);
		}
	}

	public Zombie() 
	{
		
	}

	public void setImage(Image i)
	{
		this.zombieImage = i;
	}
	
	void setSpeed(float newSpeed)
	{
		this.zombieSpeed = newSpeed;
	}

	void setX(float x)
	{
		this.zombieX = x;
	}
	
	float getLength()
	{
		return zombieLength;
	}

	void setY(float y)
	{
		this.zombieY = y;
	}

	Image getImage()
	{
		return this.zombieImage;
	}

	void updateRect(boolean isBossLevel)
	{
		if(isBossLevel)
		{
			this.zombieRect.setLocation(zombieX, 
										zombieY);
			this.zombieRect.setSize(bossSize, bossSize);
		}
		else
		{
			this.zombieRect.setLocation(zombieX, zombieY);
			this.zombieRect.setSize(zombieAnimation.getCurrentFrame().getWidth(),
									zombieAnimation.getCurrentFrame().getHeight());
		}
		
		
		
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

		Image holder;

		zombieAnimation = new Animation();
		for (int i = 0; i < 32;i++) 
		{
			holder = sp.getSprite(i, 0);
			zombieAnimation.addFrame(holder, zombieAnimationSpeed);
		}
		
		bossAnimation = new Animation();
		for (int i = 0; i < 32;i++) 
		{
			holder = sp.getSprite(i, 0);
			bossAnimation.addFrame(holder, zombieAnimationSpeed);
		}
		
		

	}

	Animation getRegularAnimation()
	{
		return zombieAnimation;
	}
	
	Animation getBossAnimation()
	{
		return bossAnimation;
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


	public void turnOnDamaged(float f) 
	{
		isDamaged = true;
		knockback = f;
		
	}
	
	public void turnOffDamaged()
	{
		isDamaged = false;
	}

	public boolean isDamaged() 
	{
		return isDamaged;
	}

	public void updateDamageTick() 
	{
		if(dmgTicker <= dmgTickerMax)
		{
			dmgTicker += dmgTickerIncrement;
			//turnRed
			for(int i = 0 ; i < zombieAnimation.getFrameCount(); i++)
			{
				zombieAnimation.getImage(i).setImageColor(255, 0, 0);
			}
			//moveBack
			zombieX += (zombiedx * (zombieSpeed + knockback)) * -1;
			zombieY += (zombiedy * (zombieSpeed + knockback)) * -1;
			
		}
		else
		{
			for(int i = 0 ; i < zombieAnimation.getFrameCount(); i++)
			{
				zombieAnimation.getImage(i).setImageColor(100, 100, 100);
			}
			turnOffDamaged();
			dmgTicker = 0f;
			knockback = 0f;
		}
		
	}

	public void updateAngle() 
	{
		this.zombieAngle = (int) Math.round((Math.atan2(this.getDY(), this.getDX()) * (180/Math.PI)) + 90f);
		for(int i = 0; i < this.zombieAnimation.getFrameCount(); i++)
		{
			this.zombieAnimation.getImage(i).setRotation(zombieAngle);
			
		}
		for(int i = 0; i < this.bossAnimation.getFrameCount(); i++)
		{
			this.bossAnimation.getImage(i).setRotation(zombieAngle);
			
		}
		
	}

	public boolean canGrowl(int chance) 
	{
		if(chance < 1 && this.zombieLength < 700)
		{
			return true;
		}
		return false;
	}

	public float getVolume() 
	{
		return (((this.zombieLength * 100) / 700) / 100); 
	}

}














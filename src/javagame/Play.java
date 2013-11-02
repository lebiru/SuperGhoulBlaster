package javagame;

import java.util.ArrayList;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{
	
	Image player, sand, zombie;
	Bullet bullet;
	
	float playerX = 300;
	float playerY = 300;
	float playerSpeed = 5;
	float playerAngle = 90f;
	int playerTunnelingBuffer = 10;
	
	
	float zombieX = 40;
	float zombieY = 40;
	float zombieSpeed = 5;
	float zombieAngle = 90f;
	float dx = 0;
	float dy = 0;
	float zombieLength = 0;
	
	
	
	
	ArrayList<ArrayList<String>> gridmap = new ArrayList<ArrayList<String>>();
	int tileWidth = 50;
	int tileHeight = 50;
	float offsetX = 0;
	float offsetY = 0;
	int tileMapSize = 11;
	
	Input input;
	Controller controller;
	GameContainer gc;
	
	
	public Play(int state)
	{
		
	}
	
	public void init(GameContainer gc, StateBasedGame sgb) throws SlickException
	{
		player = new Image("res/test.png");
		sand = new Image("res/sand.png");
		zombie = new Image("res/zombie.png");
		bullet = new Bullet("res/images/bullet.png", player);
		this.gc = gc;
		
		
		input = gc.getInput();
		controller = Controllers.getController(6);
		
		controller.setXAxisDeadZone(0.4f);
		controller.setXAxisDeadZone(-0.4f);
		controller.setYAxisDeadZone(0.4f);
		controller.setYAxisDeadZone(-0.4f);
		
		controller.setRXAxisDeadZone(0.4f);
		controller.setRXAxisDeadZone(-0.4f);
		controller.setRYAxisDeadZone(0.4f);
		controller.setRYAxisDeadZone(-0.4f);
		
		
		//creating the gridmap, or terrain. It's a 2D ArrayList.
		
		
		//Screen width / gridtile width = num of tiles for 1 row
		//row
		for(int i = 0; i <= tileMapSize; i++)
		{
			gridmap.add(new ArrayList<String>());
			//column
			for(int j = 0; j <= tileMapSize; j++)
			{
				//the string will determine what type of tile it is, ex. "r" rock, "s" sand...
				gridmap.get(i).add("r");
			}
		}
		
		for(int i = 0; i <= tileMapSize; i++)
		{
			
			//column
			for(int j = 0; j <= tileMapSize; j++)
			{
				System.out.print(gridmap.get(i).get(j));
				
			}
			System.out.print("\n");
		}
		
		
	}
	
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		
		for(int i = 0; i <= tileMapSize; i++)
		{
			
			//column
			for(int j = 0; j <= tileMapSize; j++)
			{
				if(gridmap.get(i).get(j) == "r")
				{
					g.drawImage(sand, offsetX, offsetY);
					offsetX += tileWidth;
				}
				
		
				
			}
			
			offsetX = 0;
			offsetY += tileHeight;
			
		}
		
		offsetX = 0;
		offsetY = 0;
		
		
		g.drawString("X: " + controller.getXAxisValue() + ", Y: " + controller.getYAxisValue(), 20, 20);
		g.drawString("Right X: " + controller.getRXAxisValue() + 
				   "\n Right Y: " + controller.getRYAxisValue(), 20, 60);
		
		playerAngle = (float) ((Math.atan2(controller.getRYAxisValue(), controller.getRXAxisValue())) * (180/Math.PI)) + 90f;
		zombieAngle = (float) (Math.atan2(dy, dx) * (180/Math.PI)) + 90f;
		player.setRotation(playerAngle);
		zombie.setRotation(zombieAngle);
		
		
		//g.drawLine(playerX + (player.getWidth()/2), playerY + (player.getHeight()/2), (float) Math.sin(playerAngle) + 50, (float) Math.cos(playerAngle) + 50);
		g.drawString("Player Angle: " + playerAngle, 20, 140);
		
		g.drawImage(player, playerX, playerY);
		g.drawImage(zombie, zombieX, zombieY);
		if(outOfBounds(bullet) == false)
		{
			g.drawImage(bullet.getImage(), bullet.getBulletX(), bullet.getBulletY());
		}
		
		g.drawString("Play State", 10, 30);
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		
		
		
//		if(input.isKeyDown(Input.KEY_A) && playerInsideBox(gc)){ playerX--; }
//		if(input.isKeyDown(Input.KEY_D) && playerInsideBox(gc)){ playerX++; }
//		if(input.isKeyDown(Input.KEY_S) && playerInsideBox(gc)){ playerY++; }
//		if(input.isKeyDown(Input.KEY_W) && playerInsideBox(gc)){ playerY--; }
		
		if(controller.getXAxisValue() <= -0.4 && (playerX + playerTunnelingBuffer >= 0)) { playerX = playerX + (controller.getXAxisValue() * playerSpeed); }
		if(controller.getXAxisValue() >= 0.4 && ((playerX + playerTunnelingBuffer + player.getWidth()) <= gc.getWidth())) { playerX = playerX + (controller.getXAxisValue() * playerSpeed); }
		if(controller.getYAxisValue() <= -0.4 && (playerY + playerTunnelingBuffer >= 0)) { playerY = playerY + (controller.getYAxisValue() * playerSpeed);  }
		if(controller.getYAxisValue() >= 0.4 && ((playerY + playerTunnelingBuffer + player.getHeight()) <= gc.getHeight()))  { playerY = playerY + (controller.getYAxisValue() * playerSpeed);  }
		
		if(controller.getZAxisValue() >= -1 && controller.getZAxisValue() <= -0.4 && bullet.getBulletIsAlive() == false ) 
		{ 
			bullet.setBulletIsAlive(true);
			bullet.setBulletAngle(playerAngle - 90f);
			bullet.setBulletX(playerX + 25);
			bullet.setBulletY(playerY + 25);
			bullet.setBulletDx(5);
			bullet.setBulletDy(5);
		}
		
		if(bullet.getBulletIsAlive() == true)
		{
			//move the bullet along its 2D trajectory
			bullet.moveBullet();
			
			
			if(outOfBounds(bullet) == true)
			{
				bullet.setBulletIsAlive(false);
				
			}
		}
	
		dx = playerX - zombieX;
		dy = playerY - zombieY;
		zombieLength = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= zombieLength;
		dy /= zombieLength;
		zombieX += dx;
		zombieY += dy;
		
		
	}
	
	/**
	 * Determines if the gameObject is outside of the screen.
	 * @param b
	 * @return
	 */
	private boolean outOfBounds(Bullet b)
	{
		if(b.getBulletX() <= 0 || b.getBulletX() >= gc.getWidth() ||
		   b.getBulletY() <= 0 || b.getBulletY() >= gc.getHeight())
		{
			return true;
		}
		
		return false;
	}

	public int getID()
	{
		return 1; //returns the ID of this class (play is 1)
	}

}

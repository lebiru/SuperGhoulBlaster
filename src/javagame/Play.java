package javagame;

import java.util.ArrayList;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{
	
	Image player, sand;
	float playerX = 300;
	float playerY = 300;
	float playerSpeed = 5;
	float playerAngle = 90f;
	
	ArrayList<ArrayList<String>> gridmap = new ArrayList<ArrayList<String>>();
	int tileWidth = 50;
	int tileHeight = 50;
	int offsetX = 0;
	int offsetY = 0;
	
	Input input;
	Controller controller;
	
	
	public Play(int state)
	{
		
	}
	
	public void init(GameContainer gc, StateBasedGame sgb) throws SlickException
	{
		player = new Image("res/test.png");
		sand = new Image("res/sand.png");
		
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
		for(int i = 0; i <= 5; i++)
		{
			gridmap.add(new ArrayList<String>());
			//column
			for(int j = 0; j <= 5; j++)
			{
				gridmap.get(i).add("r");
			}
		}
		System.out.println("hi");
		
		for(int i = 0; i <= 5; i++)
		{
			
			//column
			for(int j = 0; j <= 5; j++)
			{
				System.out.print(gridmap.get(i).get(j));
				
			}
			System.out.print("\n");
		}
		
		
	}
	
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		
//		for(int i = 0; i <= 5; i++)
//		{
//			
//			//column
//			for(int j = 0; j <= 5; j++)
//			{
//				if(gridmap.get(i).get(j) == "r")
//				{
//					g.drawImage(sand, offsetX + tileWidth, offsetY + tileHeight);
//					offsetX += tileWidth;
//				}
//				
//		
//				
//			}
//			
//			offsetX = 0;
//			offsetY += tileHeight;
//			
//		}
		
		offsetX = 0;
		offsetY = 0;
		
		
		g.drawString("X: " + controller.getXAxisValue() + ", Y: " + controller.getYAxisValue(), 20, 20);
		g.drawString("Right X: " + controller.getRXAxisValue() + 
				   "\n Right Y: " + controller.getRYAxisValue() + 
				   "\n Angle: " + (Math.atan2(controller.getRYAxisValue(), controller.getRXAxisValue())) * (180/Math.PI) + 90f , 20, 60);
		playerAngle = (float) ((Math.atan2(controller.getRYAxisValue(), controller.getRXAxisValue())) * (180/Math.PI)) + 90f;
		player.setRotation(playerAngle);
		g.drawImage(player, playerX, playerY);
		g.drawString("Play State", 10, 30);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		
		
		System.out.println(controller.getZAxisValue());
		if(input.isKeyDown(Input.KEY_A)){ playerX--; }
		if(input.isKeyDown(Input.KEY_D)){ playerX++; }
		if(input.isKeyDown(Input.KEY_S)){ playerY++; }
		if(input.isKeyDown(Input.KEY_W)){ playerY--; }
		
		if(controller.getXAxisValue() <= -0.4) { playerX = playerX + (controller.getXAxisValue() * playerSpeed); }
		if(controller.getXAxisValue() >= 0.4) { playerX = playerX + (controller.getXAxisValue() * playerSpeed); }
		if(controller.getYAxisValue() <= -0.4) { playerY = playerY + (controller.getYAxisValue() * playerSpeed);  }
		if(controller.getYAxisValue() >= 0.4) { playerY = playerY + (controller.getYAxisValue() * playerSpeed);  }
		
		
		
	}
	
	public int getID()
	{
		return 1; //returns the ID of this class (play is 1)
	}

}

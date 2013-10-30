package javagame;

import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState{
	
	Image player, sand;
	int playerX = 50;
	int playerY = 50;
	
	ArrayList<ArrayList<String>> gridmap = new ArrayList<ArrayList<String>>();
	int tileWidth = 50;
	int tileHeight = 50;
	int offsetX = 0;
	int offsetY = 0;
	
	
	public Play(int state)
	{
		
	}
	
	public void init(GameContainer gc, StateBasedGame sgb) throws SlickException
	{
		player = new Image("res/test.png");
		sand = new Image("res/sand.png");
		
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
		
		for(int i = 0; i <= 5; i++)
		{
			
			//column
			for(int j = 0; j <= 5; j++)
			{
				if(gridmap.get(i).get(j) == "r")
				{
					g.drawImage(sand, offsetX + tileWidth, offsetY + tileHeight);
					offsetX += tileWidth;
				}
				
		
				
			}
			
			offsetX = 0;
			offsetY += tileHeight;
			
		}
		
		offsetX = 0;
		offsetY = 0;
		
		
		g.drawImage(player, playerX, playerY);
		g.drawString("Play State", 10, 30);
	}
	
	public void update(GameContainer gc, StateBasedGame sgb, int delta) throws SlickException
	{
		Input input = gc.getInput();
		if(input.isKeyDown(Input.KEY_A)){ playerX--; }
		if(input.isKeyDown(Input.KEY_D)){ playerX++; }
		if(input.isKeyDown(Input.KEY_S)){ playerY++; }
		if(input.isKeyDown(Input.KEY_W)){ playerY--; }
		
	}
	
	public int getID()
	{
		return 1; //returns the ID of this class (play is 1)
	}

}

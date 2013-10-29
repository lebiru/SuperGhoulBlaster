package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame
{

	public static final String gamename = "November Game";
	public static final int menu = 0;
	public static final int play = 1;
	
	
	public Game(String gamename)
	{
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
		
	}
	
	//gamecontainer manages game engine stuff, like framerate, game loop, etc...
	public void initStatesList(GameContainer gc) throws SlickException
	{
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		//enterState is the first screen the computer will show
		this.enterState(menu);
		
	}
	
	public static void main(String[] args) 
	{
		AppGameContainer appgc;
		try
		{
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setDisplayMode(640, 360, false); //third argument is fullscreen
			appgc.start();
		}catch(SlickException e)
		{
			e.printStackTrace();
		}

	}

}

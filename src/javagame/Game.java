package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame
{

	public static final String gamename = "November Game";
	public static final int menu = 0;
	public static final int play = 1;
	public static final int about = 2;
	public static final int controls = 3;
	public static final int gameOver = 4;
	
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT= 600;
	static final int maxFPS = 60;
	
	
	public Game(String gamename)
	{
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
//		this.addState(new About(about));
//		this.addState(new Controls(controls));
//		this.addState(new GameOver(gameOver));
		
	}
	
	//gamecontainer manages game engine stuff, like framerate, game loop, etc...
	public void initStatesList(GameContainer gc) throws SlickException
	{
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		//this.getState(about).init(gc, this);
		//this.getState(controls).init(gc, this);
		//this.getState(gameOver).init(gc, this);
		
		//enterState is the first screen the computer will show
		this.enterState(menu);
		
	}
	
	public static void main(String[] args) 
	{
		AppGameContainer appgc;
		try
		{
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setTargetFrameRate(maxFPS);
			appgc.setVSync(true);
			appgc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false); //third argument is fullscreen
			appgc.start();
		}catch(SlickException e)
		{
			e.printStackTrace();
		}

	}

}

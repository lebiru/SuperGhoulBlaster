package javagame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Game extends StateBasedGame
{

	public static final String gamename = "Super Ghoul Blaster";
	public static final int menu = 0;
	public static final int play = 1;
	public static final int about = 2;
	public static final int controls = 3;
	public static final int gameOver = 4;
	public static final int shop = 5;
	
	public static int SCREEN_WIDTH = 600;
	public static int SCREEN_HEIGHT= 600;
	static final int maxFPS = 60;
	

	public Game(String gamename) throws SlickException
	{
		super(gamename);
		this.addState(new About(about));
		this.addState(new Shop(shop));
		this.addState(new Menu(menu));
		this.addState(new Play(play));
		this.addState(new Controls(controls));
		this.addState(new GameOver(gameOver));
		
	}
	
	//gamecontainer manages game engine stuff, like framerate, game loop, etc...
	public void initStatesList(GameContainer gc) throws SlickException
	{
		this.enterState(about);
	}
	
	public static void main(String[] args) 
	{
		AppGameContainer appgc;
		try
		{
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setTargetFrameRate(maxFPS);
			appgc.setVSync(true);
		

			appgc.setDisplayMode(Display.getDesktopDisplayMode().getWidth(), 
								 Display.getDesktopDisplayMode().getHeight(), 
								 false); //third argument is fullscreen
			appgc.setIcon("res/images/icon.png");
			appgc.start();
			
		}catch(SlickException e)
		{
			e.printStackTrace();
		}

	}

}

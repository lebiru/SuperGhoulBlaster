package javagame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

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
		this.addState(new Menu(menu));
		this.addState(new Play(play));
		this.addState(new About(about));
		this.addState(new Controls(controls));
		this.addState(new GameOver(gameOver));
		this.addState(new Shop(shop));
		
		
	}
	
	//gamecontainer manages game engine stuff, like framerate, game loop, etc...
	public void initStatesList(GameContainer gc) throws SlickException
	{

//		this.getState(menu).init(gc, this);
//		this.getState(play).init(gc, this);
//		this.getState(about).init(gc, this);
//		this.getState(controls).init(gc, this);
//		this.getState(gameOver).init(gc, this);
		
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
			
			
			DisplayMode[] modes;
			//Enabling Full-Screen Mode
			try {
				modes = Display.getAvailableDisplayModes();
			    for (int i=0;i<modes.length;i++) {
		             DisplayMode current = modes[i];
		             System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
		                                 current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
		             SCREEN_WIDTH = current.getWidth();
		             SCREEN_HEIGHT = current.getHeight();
		         }
		         System.out.println(appgc.getAspectRatio());
			} catch (LWJGLException e) {
				
				e.printStackTrace();
			}

	     
	         
	         
			appgc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false); //third argument is fullscreen
			appgc.start();
		}catch(SlickException e)
		{
			e.printStackTrace();
		}

	}

}

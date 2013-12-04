package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class About extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image backButton, creditsImage;
	
	
	Image logo;
	StateBasedGame sbg;

	private float countdown = 100f;
	
	public About(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{


		//replace these with "Play" and "About"
		//backButton = new Image("res/images/SGB_buttonmainmenu_01.png");
		creditsImage = new Image("res/images/splashScreens/SGB_SplashScreenCredits_02.jpg");
		this.sbg = sbg;
//
//		areas[0] = new MouseOverArea(gc, backButton, 200, 400 + (0*100), 200, 90, this);
//		areas[0].setNormalColor(new Color(1,1,1,0.8f));
//		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));



	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		g.setBackground(Color.black);
		
		creditsImage.draw(0, 0, gc.getWidth(), gc.getHeight());

//		for (int i=0; i < 1 ; i++) 
//		{
//			areas[i].render(gc, g);
//		}
	}

	//for updating logics of the game
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		countdown--;
		if(countdown <= 0)
		{
			sbg.enterState(0, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 1000) );
		}

	}

	public int getID()
	{
		return 2; //returns the ID of this class (menu is 0)
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : "+source);

		if (source == areas[0]) 
		{
			System.out.println("Entering Menu");
			sbg.enterState(0);
		}

	}


}



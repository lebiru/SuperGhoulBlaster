package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;


public class About extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image backButton;
	
	Image logo;
	StateBasedGame sbg;

	public About(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{


		//replace these with "Play" and "About"
		backButton = new Image("res/images/SGB_buttonmainmenu_01.png");
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, backButton, 200, 400 + (0*100), 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));



	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		g.setBackground(Color.black);
		



		for (int i=0; i < 1 ; i++) 
		{
			areas[i].render(gc, g);
		}
	}

	//for updating logics of the game
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{


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



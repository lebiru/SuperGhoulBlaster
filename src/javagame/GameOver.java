package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;


public class GameOver extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image playMenu;
	Image aboutMenu;
	Image logo;
	StateBasedGame sbg;

	public GameOver(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{


		//replace these with "Play" and "About"
		playMenu = new Image("res/images/SGB_buttonretry_01.png");
		aboutMenu = new Image("res/images/SGB_buttongiveup_01.png");
		logo = new Image("res/images/SGB_logo_01.png");
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, playMenu, 200, 400 + (0*100), 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[1] = new MouseOverArea(gc, aboutMenu, 200, 400 + (1*100), 200, 90, this);
		areas[1].setNormalColor(new Color(1,1,1,0.8f));
		areas[1].setMouseOverColor(new Color(1,1,1,0.9f));


	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		g.setBackground(Color.white);
		logo.draw(50, 0, 0.50f);



		for (int i=0;i<2;i++) {
			areas[i].render(gc, g);
		}
	}

	//for updating logics of the game
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		


	}

	public int getID()
	{
		return 4; //returns the ID of this class 
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : "+source);

		if (source == areas[0]) 
		{
			System.out.println("Play Again pressed");
			((Play)sbg.getState(1)).gameOverBGM.stop();
			((Play)sbg.getState(1)).gameBGM.loop();
			((Play)sbg.getState(1)).gameOverCleanUpLevel();
			sbg.enterState(1);
		}

		else if (source == areas[1]) 
		{
			System.out.println("Main Menu Pressed");
			((Play)sbg.getState(1)).gameOverBGM.stop();
			((Menu)sbg.getState(0)).titleBGM.loop();
			((Play)sbg.getState(1)).gameOverCleanUpLevel();
			sbg.enterState(0);
		}
	}


}



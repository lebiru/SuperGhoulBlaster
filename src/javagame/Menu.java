package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;


public class Menu extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image playMenu;
	//Image aboutMenu;
	Image backgroundImage;
	StateBasedGame sbg;
	
	Sound titleBGM;

	public Menu(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{

		//replace these with "Play" and "About"
		playMenu = new Image("res/images/buttons/SGB_buttonplay_01.png");
		//aboutMenu = new Image("res/images/SGB_buttoncredits_01.png");
		backgroundImage = new Image("res/images/splashScreens/SGB_logo_02.jpg");
		
		//Music
		titleBGM = new Sound("res/sound/BGM/Title.ogg");

		titleBGM.loop();
		
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, playMenu, gc.getWidth()/2 - 100, gc.getHeight() - 200, 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));

//		areas[1] = new MouseOverArea(gc, aboutMenu, 200, 400 + (1*100), 200, 90, this);
//		areas[1].setNormalColor(new Color(1,1,1,0.8f));
//		areas[1].setMouseOverColor(new Color(1,1,1,0.9f));


	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		g.setBackground(Color.blue);
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());

		for (int i=0;i<1;i++) 
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
		return 0; //returns the ID of this class (menu is 0)
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : "+source);

		if (source == areas[0]) 
		{
			//Enter How to Play
			System.out.println("Entering How to Play");
			sbg.enterState(3);
		}

//		else if (source == areas[1]) 
//		{
//			//Enter the Credits 
//			System.out.println("Entering Credits");
//			sbg.enterState(2);
//		}
	}


}



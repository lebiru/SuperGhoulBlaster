package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;


public class Menu extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image playMenu;
	Image aboutMenu;
	
	public Menu(int state)
	{
		
	}
	
	public void init(GameContainer gc, StateBasedGame sgb) throws SlickException
	{
	
		//replace these with "Play" and "About"
		playMenu = new Image("res/images/playMenu.png");
		aboutMenu = new Image("res/images/creditsMenu.png");
		for (int i=0;i<2;i++) {
			areas[i] = new MouseOverArea(gc, playMenu, 300, 100 + (i*100), 200, 90, this);
			areas[i].setNormalColor(new Color(1,1,1,0.8f));
			areas[i].setMouseOverColor(new Color(1,1,1,0.9f));
		}
		
	}
	
	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		
		g.drawString("Super Ghoul Blaster!", gc.getWidth()/2 - 100, 20);
		
		for (int i=0;i<2;i++) {
			areas[i].render(gc, g);
		}
	}
	
	//for updating logics of the game
	public void update(GameContainer gc, StateBasedGame sgb, int delta) throws SlickException
	{

		
	}
	
	public int getID()
	{
		return 0; //returns the ID of this class (menu is 0)
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : "+source);
		for (int i=0;i<2;i++) {
			if (source == areas[i]) {
				System.out.println("Option "+(i+1)+" pressed!");
			}
		}
		
		
	}
	
}

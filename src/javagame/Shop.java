package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class Shop extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image playButton, logo, upgradeGunPowerButton;
	StateBasedGame sbg;
	GameContainer gc;

	public Shop(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{

		//replace these with "Play" and "About"
		playButton = new Image("res/images/playMenu.png");
		logo = new Image("res/images/SGB_logo_01.png");
		upgradeGunPowerButton = new Image("res/images/upgradeGunPower.png");
		
		
		this.gc = gc;
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, playButton, 200, 400 + (0*100), 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));
		
		areas[1] = new MouseOverArea(gc, upgradeGunPowerButton, 200, 400 + (1*100), upgradeGunPowerButton.getWidth(), upgradeGunPowerButton.getHeight(), this);
		areas[1].setNormalColor(new Color(1,1,1,0.8f));
		areas[1].setMouseOverColor(new Color(1,1,1,0.9f));
		
	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{
		g.setBackground(Color.lightGray);
		logo.draw(50, 0, 0.50f);


		g.drawString("What'er yer buyin?", 300, 300);

		for (int i=0;i<2;i++) 
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
		return 5; //returns the ID of this class (shop is 5)
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : " + source);

		if (source == areas[0]) 
		{
			System.out.println("Entering Next Level");
			((Play)sbg.getState(1)).cleanUpLevel();
			try {
				((Play)sbg.getState(1)).increaseLevelDifficulty();
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sbg.enterState(1, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 1000) );
		}
		
		if (source == areas[1]) 
		{
			System.out.println("Upgrading Gun Power");
			for(Bullet b : ((Play)sbg.getState(1)).bulletManager)
			{
				b.setBulletDamage(2);
			}
			
		}

	}


}
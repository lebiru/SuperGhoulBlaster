package javagame;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.*;


public class GameOver extends BasicGameState implements ComponentListener{


	private MouseOverArea[] areas = new MouseOverArea[2];
	Image playMenu;
	Image aboutMenu;
	Image gameOverBackground;
	StateBasedGame sbg;

	Font UIFont1;
	org.newdawn.slick.UnicodeFont bombardFont;

	public GameOver(int state)
	{

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{


		//replace these with "Play" and "About"
		playMenu = new Image("res/images/buttons/SGB_buttonretry_01.png");
		aboutMenu = new Image("res/images/buttons/SGB_buttongiveup_01.png");
		gameOverBackground = new Image("res/images/splashScreens/SGB_SplashScreenGameOver_01.jpg");
		this.sbg = sbg;

		areas[0] = new MouseOverArea(gc, playMenu, gc.getWidth()/5, (int) (gc.getHeight()/1.95), 200, 90, this);
		areas[0].setNormalColor(new Color(1,1,1,0.8f));
		areas[0].setMouseOverColor(new Color(1,1,1,0.9f));

		areas[1] = new MouseOverArea(gc, aboutMenu, gc.getWidth()/5, (int) ((int) gc.getHeight()/1.5), 200, 90, this);
		areas[1].setNormalColor(new Color(1,1,1,0.8f));
		areas[1].setMouseOverColor(new Color(1,1,1,0.9f));

		//Setting Up the Font
		try {
			UIFont1 = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, 
					org.newdawn.slick.util.ResourceLoader.getResourceAsStream("res/images/fonts/BOMBARD.ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UIFont1 = UIFont1.deriveFont(java.awt.Font.PLAIN, 24.0f);
		bombardFont = new org.newdawn.slick.UnicodeFont(UIFont1);
		bombardFont.addAsciiGlyphs();
		bombardFont.getEffects().add(new ColorEffect(java.awt.Color.GREEN));
		bombardFont.loadGlyphs();
		//End Setting up the Font


	}

	//for drawing things on screen
	public void render(GameContainer gc, StateBasedGame sgb, Graphics g) throws SlickException
	{

		gameOverBackground.draw(0, 0, gc.getWidth(), gc.getHeight());
		g.setFont(bombardFont);
		
		g.drawString("Zombies Killed: " + ((Play)sgb.getState(1)).zombiesKilled, 
				gc.getWidth()/1.80f, gc.getHeight()/1.9f);
		g.drawString("Accuracy: " + Math.round((((Play)sgb.getState(1)).bulletsHit / ((Play)sgb.getState(1)).bulletsFired) * 100 )+ "%", 
				gc.getWidth()/1.80f, gc.getHeight()/1.75f);
		g.drawString("Total Coins Earned: " + ((Play)sgb.getState(1)).totalCoinsEarned, 
				gc.getWidth()/1.80f, gc.getHeight()/1.60f);


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
			((Play)sbg.getState(1)).gameOverCleanUpLevel(sbg);
			sbg.enterState(1);
		}

		else if (source == areas[1]) 
		{
			System.out.println("Main Menu Pressed");
			((Play)sbg.getState(1)).gameOverBGM.stop();
			((Menu)sbg.getState(0)).titleBGM.loop();
			((Play)sbg.getState(1)).gameOverCleanUpLevel(sbg);
			sbg.enterState(0);
		}
	}


}



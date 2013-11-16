package javagame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Flashlight 
{
	public Image flashlightImage;
	public Image darknessImage;
	private float flPower = 0.0f; //The higher flPower, the more dark the screen is.
	private float flIncrement = 0.02f; //how much power does the flashlight lose per update
	private float powerTick = 0; //A stopwatch that determines when flashlight loses some power
	private float powerCycle = 100; //determines what the threshold of the powerTick is. 
	
	Flashlight(String light, String darkness) throws SlickException
	{
		this.flashlightImage = new Image(light);
		this.darknessImage = new Image(darkness);
	}
	
	public float getPower()
	{
		return flPower;
	}
	
	public void setPower(float newPower)
	{
		this.flPower = newPower;
	}
	
	public void losePower()
	{
		flPower += flIncrement;
	}
	
	public void update()
	{
		if(flPower <= 1f) //If the flashlight still has energy
		{
			powerTick++;
			if(powerTick >= powerCycle)
			{
				losePower();
				powerTick = 0;
			}
		}
		
	}
	
	
}

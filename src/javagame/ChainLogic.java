package javagame;

public class ChainLogic 
{
	
	int numOfChain = 0;
	boolean active = false;
	float delta = 100f;
	
	ChainLogic()
	{
		
	}
	
	public int getChains()
	{
		return numOfChain;
	}
	
	public float getDelta()
	{
		return delta;
	}
	
	public boolean getIsActive()
	{
		return active;
	}
	
	public void setChain(int n)
	{
		numOfChain = n;
	}
	
	public void turnOn()
	{
		active = true;
		delta = 100f;
		numOfChain++;
	}
	
	public void reset()
	{
		numOfChain = 0;
		active = false;
		delta = 100f;
	}
	
	public void update()
	{
		if(active)
		{
			delta--;
			if(delta <= 0)
			{
				reset();
			}
				
		}
		
		
	}

}

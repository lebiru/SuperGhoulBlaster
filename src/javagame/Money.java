package javagame;

/**
 * Is responsible for holding all the money information for the player.
 * @author Biru
 *totalCoin: The total number of coins the player has collected in the playthrough.
 *currentCoin: The total number of coins the player currently has.
 */

public class Money 
{
	
	int totalCoin;
	int currentCoin;
	
	Money()
	{
		totalCoin =  0;
		currentCoin = 0;
	}
	
	public void setTotalCoin(int newTotal)
	{
		totalCoin = newTotal;
	}
	
	public int getTotalCoin()
	{
		return totalCoin;
	}
	
	public void setCurrentCoin(int newCurrent)
	{
		currentCoin = newCurrent;
	}
	
	public int getCurrentCoin()
	{
		return currentCoin;
	}
	
	public void decreaseCurrentCoin(int decreaseAmount)
	{
		currentCoin -= decreaseAmount;
	}

	public void reset() 
	{
		totalCoin = 0;
		currentCoin = 0;	
	}

	
}

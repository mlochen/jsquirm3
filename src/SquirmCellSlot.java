import java.util.ArrayList;

public class SquirmCellSlot
{
	private ArrayList<SquirmCell> occupants = new ArrayList<SquirmCell>();
	
	public void addOccupant(SquirmCell cell)
	{
		if (occupants.contains(cell))
		{
	        SquirmError.error("addOccupant - occupant already present");
		}
		else
		{
			occupants.add(cell);
		}
	}
	
	public void removeOccupant(SquirmCell cell)
	{
		if (!occupants.contains(cell))
		{
	        SquirmError.error("removeOccupant - occupant not present");
		}
		else
		{
			occupants.remove(cell);
		}
	}
	
	public ArrayList<SquirmCell> getOccupants()
	{
		return occupants;
	}
	
	public boolean isEmpty()
	{
		return occupants.isEmpty();
	}
}

import java.util.ArrayList;
import java.util.Random;

public class SquirmChemistry
{
	private ArrayList<SquirmReaction> reactions;
	
	public SquirmChemistry()
	{
		reactions = new ArrayList<SquirmReaction>();
	}
	
	public void addReaction(SquirmReaction r)
	{
		reactions.add(r);
	}
	
	private boolean tryReaction(SquirmCell cell,
                                ArrayList<SquirmCell> nearbyCells,
                                SquirmReaction r, double reactionRange2)
	{
		if (r.aType != 'x' && r.aType != 'y' && r.aType != 'z'
            && r.aType != cell.getType())
		{
			return false;
		}
		
		if (r.aState != cell.getState())
		{
			return false;
		}
		
		for (SquirmCell bCell : nearbyCells)
		{
			if (bCell == cell)
			{
				continue;
			}
			
			if (r.bState != bCell.getState())
			{
				continue;
			}
			
			if (r.bType != 'x' && r.bType != 'y' && r.bType != 'z'
                && r.bType != bCell.getType())
			{
				continue;
			}
			
			if ((r.bType == 'x' || r.bType == 'y' || r.bType == 'z')
                && r.bType == r.aType && bCell.getType() != cell.getType())
			{
				continue;
			}
			
			if ((r.curABBond && !cell.hasBondWith(bCell))
                || (!r.curABBond && cell.hasBondWith(bCell)))
			{
				continue;
			}
			
			if (r.nInputs == 3)
			{
				for (SquirmCell cCell : nearbyCells)
				{
					if (cCell == bCell || cCell == cell)
					{
						continue;
					}
					
					if (r.cState != cCell.getState())
					{
						continue;
					}
					
					if ((r.cType != 'x' && r.cType != 'y' && r.cType != 'z')
                        && r.cType != cCell.getType())
					{
						continue;
					}
					
					if ((r.cType == 'x' || r.cType == 'y' || r.cType == 'z')
                        && r.cType == r.aType
                        && cCell.getType() != cell.getType())
					{
						continue;
					}
					
					if ((r.cType == 'x' || r.cType == 'y' || r.cType == 'z')
	                    && r.cType == r.bType
	                    && cCell.getType() != bCell.getType())
					{
						continue;
					}
					
					if ((r.curACBond && !cell.hasBondWith(cCell))
                        || (!r.curACBond && cell.hasBondWith(cCell)))
					{
						continue;
					}
					
					if ((r.curBCBond && !bCell.hasBondWith(cCell))
                        || (!r.curBCBond && bCell.hasBondWith(cCell)))
					{
						continue;
					}
					
					C2DVector bloc = bCell.getLocation();
					C2DVector cloc = cCell.getLocation();
					C2DVector v = C2DVector.sub(bloc, cloc);
					if (!(C2DVector.getLength2(v) < reactionRange2))
					{
						continue;
					}
					
					if (!testProb(r.cases))
					{
						continue;
					}
					
					cell.setState(r.futAState);
					bCell.setState(r.futBState);
					cCell.setState(r.futCState);
					if (r.curABBond && !r.futABBond)
					{
						cell.debond(bCell);
					}
					else if (!r.curABBond && r.futABBond)
					{
						cell.bondTo(bCell);
					}
					if (r.curBCBond && !r.futBCBond)
					{
						bCell.debond(cCell);
					}
					else if (!r.curBCBond && r.futBCBond)
					{
						bCell.bondTo(cCell);
					}
					if (r.curACBond && !r.futACBond)
					{
						cell.debond(cCell);
					}
					else if (!r.curACBond && r.futACBond)
					{
						cell.bondTo(cCell);
					}
				}
			}
			else
			{
				if (!testProb(r.cases))
				{
					continue;
				}
				
				cell.setState(r.futAState);
				bCell.setState(r.futBState);
				if (cell.hasBondWith(bCell) && !r.futABBond)
				{
					cell.debond(bCell);
				}
				else
				{
					cell.bondTo(bCell);
				}
			}
		}
		return false;
	}
	
	public int react(SquirmCell cell, ArrayList<SquirmCell> nearbyCells,
                     double reactionRange2)
	{
		for (SquirmReaction r : reactions)
		{
			if (tryReaction(cell, nearbyCells, r, reactionRange2))
			{
				return reactions.indexOf(r);
			}
		}
		return -1;
	}
	
	private boolean testProb(long cases)
	{
		if (cases == 0)
		{
			SquirmError.error("Zero passed to testProb!");
		}
		return (new Random().nextLong() % cases) == 0;
	}
}

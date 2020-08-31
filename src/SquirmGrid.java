import java.util.ArrayList;

public class SquirmGrid
{
	private SquirmCellSlot[][] cellGrid;
	protected ArrayList<SquirmCell> cells = new ArrayList<SquirmCell>();
	private SquirmChemistry chemistry = new SquirmChemistry();
	private C2DVector size;
	private long iterations;
	private boolean doFlood;
	private int floodPeriod;
	private int floodSector;
	private double slotSize;
	private int slotsX, slotsY;
	private double lostSpeed;
		
	public SquirmGrid(int width, int height)
	{
		doFlood = false;
		floodPeriod = 50000;
		floodSector = 2;
		
		lostSpeed = 0;
		
		size = new C2DVector(width, height);
		
		slotSize = SquirmCell.RADIUS * 5;
		slotsX = (int)Math.ceil(size.getX() / slotSize);
		slotsY = (int)Math.ceil(size.getY() / slotSize);
		cellGrid = new SquirmCellSlot[slotsX][slotsY];
		for (int x = 0; x < slotsX; x++)
		{
			for (int y = 0; y < slotsY; y++)
			{
				cellGrid[x][y] = new SquirmCellSlot();
			}
		}
		
		iterations = 0;
	}
	
	public void initSimple(int n)
	{
		final int S = 39, T = 40, E = 41;
		
		SquirmReaction reactions[] = {
		// crumpled membranes lose atoms spontaneously
		new SquirmReaction('a', S, true, 'a', S, true, 'a', S, false, S, false, 0, false, S, true, 100),
		// stretched membranes acquire atoms spontaneously
		new SquirmReaction('a', S, true, 'a', S, false, 'a', 0, false, S, false, S, true, S, true, 100),
		
		// some extra ones to allow membrane growth at T places too
		new SquirmReaction('a', S, true, 'a', T, false, 'a', 0, false, S, false, T, true, S, true),
		new SquirmReaction('a', T, true, 'a', T, false, 'a', 0, false, T, false, T, true, S, true),
		
		// the new set of base duplication reactions
		new SquirmReaction('x', 2, true, 'y', 1, 7, true, 4, false),
		new SquirmReaction('x', 4, false, 'y', 3, 5, true, 7, false),
		new SquirmReaction('x', 5, false, 'x', 0, 6, true, 6, false),
		new SquirmReaction('x', 6, false, 'y', 7, 3, true, 4, false),
		new SquirmReaction('x', 6, true, 'y', 4, 1, false, 2, false),
		new SquirmReaction('x', 7, true, 'y', 1, 2, true, 2, false),
		
		// the standard splitting reactions
		new SquirmReaction('x', 2, true, 'y', 8, 9, true, 1, false),
		new SquirmReaction('x', 9, true, 'y', 9, 8, false, 8, false),
		
		// start the duplication
		new SquirmReaction('a', T, true, 'e', 1, 10, true, 5, false),
		new SquirmReaction('a', 10, false, 'e', 6, T, true, 3, false),
		new SquirmReaction('e', 6, true, 'e', 3, 2, true, 3, false),
		
		// start the splitting
		new SquirmReaction('f', 2, true, 'a', T, 9, true, 11, false),
		new SquirmReaction('a', 11, false, 'f', 3, 11, true, 9, false),
		
        // start the cell division
		new SquirmReaction('a', 11, true, 'a', S, 11, true, 12, false),
		new SquirmReaction('f', 1, false, 'a', 12, 13, true, T, false),
		
        // the pulling sequence
		new SquirmReaction('x', 13, true, 'y', 1, 14, true, 15, false),
		new SquirmReaction('a', 11, false, 'x', 15, 11, true, 16, false),
		new SquirmReaction('x', 14, true, 'y', 16, 29, true, 16, false),
		new SquirmReaction('x', 29, true, 'a', 11, 17, false, 11, false),
		new SquirmReaction('x', 17, true, 'y', 16, 17, true, 13, false),
		new SquirmReaction('x', 13, true, 'e', 8, 14, true, 15, false),
		
        // finish the cell division
		new SquirmReaction('e', 13, true, 'a', T, 18, true, 19, false),
		new SquirmReaction('e', 13, true, 'a', 19, 18, true, 20, false),
		new SquirmReaction('a', 20, false, 'a', 11, 21, true, 22, false),
		new SquirmReaction('e', 18, true, 'a', 22, 35, false, 23, false),
		new SquirmReaction('e', 18, true, 'a', 21, 35, false, 24, false),
		new SquirmReaction('a', 23, true, 'a', T, 25, true, 26, false),
		new SquirmReaction('a', 25, true, 'a', T, 27, true, 26, false),
		new SquirmReaction('a', 24, false, 'a', 26, 28, true, 29, false),
		new SquirmReaction('a', 29, true, 'a', 27, T, false, 30, false),
		new SquirmReaction('a', 30, true, 'a', 26, 31, true, T, false),
		new SquirmReaction('a', 28, true, 'a', S, 32, true, 33, false),
		new SquirmReaction('a', 32, true, 'a', S, 34, true, 33, false),
		new SquirmReaction('a', 33, false, 'a', 31, S, true, 36, false),
		new SquirmReaction('a', 34, true, 'a', S, 35, false, S, false),
		new SquirmReaction('a', 35, true, 'a', 33, 37, true, S, false),
		new SquirmReaction('a', 37, true, 'a', 36, T, false, T, false),
		
		// start the enzyme production
		new SquirmReaction('x', 35, true, 'y', 17, false, 'd', 0, false, 1, true, 38, true, E, false),
		new SquirmReaction('d', E, true, 'a', 38, true, 'y', 17, false, E, false, 1, true, 38, true),
		new SquirmReaction('d', E, true, 'b', 38, true, 'y', 17, false, E, false, 1, true, 38, true),
		new SquirmReaction('d', E, true, 'c', 38, true, 'y', 17, false, E, false, 1, true, 38, true),
		new SquirmReaction('d', E, true, 'd', 38, false, 'd', 0, false, E, false, 35, false, E, true),
		new SquirmReaction('d', E, true, 'f', 38, 0, false, 1, false)};

		for (SquirmReaction r : reactions)
		{
			chemistry.addReaction(r);
		}
		
		SquirmCell eCell, fCell;
		
		int sx = (int)((size.getX() / 2) - (9 * SquirmCell.RADIUS));
		int sy = (int)((size.getY() / 2) - (9 * SquirmCell.RADIUS + 50));
		
		eCell = createNewCell(sx + 1 * SquirmCell.RADIUS * 2, sy, 'e', 1).bondTo(
                createNewCell(sx + 2 * SquirmCell.RADIUS * 2, sy, 'b', 1).bondTo(
                createNewCell(sx + 3 * SquirmCell.RADIUS * 2, sy, 'b', 1).bondTo(
                createNewCell(sx + 4 * SquirmCell.RADIUS * 2, sy, 'a', 1).bondTo(
                createNewCell(sx + 5 * SquirmCell.RADIUS * 2, sy, 'c', 1).bondTo(
                createNewCell(sx + 6 * SquirmCell.RADIUS * 2, sy, 'b', 1).bondTo(
                createNewCell(sx + 7 * SquirmCell.RADIUS * 2, sy, 'd', 1).bondTo(
                (fCell = createNewCell(sx + 8 * SquirmCell.RADIUS * 2, sy, 'f', 1)))))))));
		
		int length = 2;
		int width = 8;
		double startX = sx;
		double startY = sy - SquirmCell.RADIUS * 2;
		SquirmCell start = createNewCell(startX, startY, 'a', T);
		start.bondTo(eCell);
		SquirmCell a = start, b;
		for (int i = 0; i < length; i++)
		{
			b = createNewCell(startX, startY + (i + 1) * SquirmCell.RADIUS * 2, 'a', S);
			b.bondTo(a);
			a = b;
		}
		for (int i = 0; i < width; i++)
		{
			b = createNewCell(startX + (i + 1) * SquirmCell.RADIUS * 2, startY + length * SquirmCell.RADIUS * 2, 'a', S);
			if (i == width - 1)
			{
				b.setState(T);
				b.bondTo(fCell);
			}
			b.bondTo(a);
			a = b;
		}
		for (int i = length - 1; i >= 0; i--)
		{
			b = createNewCell(startX + (width + 1) * SquirmCell.RADIUS * 2, startY + (i + 1) * SquirmCell.RADIUS * 2, 'a', S);
			b.bondTo(a);
			a = b;
		}
		for (int i = width; i >= 0; i--)
		{
			b = createNewCell(startX + (i + 1) * SquirmCell.RADIUS * 2, startY, 'a', S);
			b.bondTo(a);
			a = b;
		}
		a.bondTo(start);
		
		for (int i = 0; i < n; i++)
		{
			createRandomCell();
		}
	}
	
	public void doTimeStep()
	{
		recomputeVelocitiesAndReact();
		moveCells();
		if (doFlood && (iterations % floodPeriod) == 0 && iterations > 0)
		{
			doFlood();
		}
		iterations++;
	}
	
	public ArrayList<SquirmCell> getAllWithinRadius(C2DVector loc, double r)
	{
		int cx = (int)Math.floor(loc.getX() / slotSize);
		int cy = (int)Math.floor(loc.getY() / slotSize);
		double r2 = r * r;
		
		ArrayList<SquirmCell> cells = new ArrayList<SquirmCell>();
		
		if (cx >= 0 && cy >= 0 && cx < slotsX && cy < slotsY)
		{
			int searchSlots = (int)Math.ceil(r / slotSize);
			int xStart = Math.max(0, cx - searchSlots);
			int xEnd = Math.min(slotsX - 1, cx + searchSlots);
			int yStart = Math.max(0, cy - searchSlots);
			int yEnd = Math.min(slotsY - 1, cy + searchSlots);
			for (int x = xStart; x <= xEnd; x++)
			{
				for (int y = yStart; y <= yEnd; y++)
				{
					SquirmCellSlot slot = cellGrid[x][y];
					if (!slot.isEmpty())
					{
						for (SquirmCell cell : slot.getOccupants())
						{
							C2DVector otherLoc = cell.getLocation();
							if (C2DVector.getDist2(otherLoc, loc) < r2)
							{
								cells.add(cell);
							}
						}
					}
				}
			}
		}
		return cells;
	}
	
	public boolean checkLocation(C2DVector v)
	{
		boolean xOk = v.getX() >= 0 && v.getX() < size.getX();
		boolean yOk = v.getY() >= 0 && v.getY() < size.getY();
		return xOk && yOk;
	}
	
	public void putCell(SquirmCell cell)
	{
		int cx = (int)Math.floor(cell.getLocation().getX() / slotSize);
		int cy = (int)Math.floor(cell.getLocation().getY() / slotSize);
		cx = Math.max(0, cx);
		cx = Math.min(cx, slotsX - 1);
		cy = Math.max(0, cy);
		cy = Math.min(cy, slotsX - 1);
		cellGrid[cx][cy].addOccupant(cell);
	}
	
	public void removeCell(SquirmCell cell)
	{
		int cx = (int)Math.floor(cell.getLocation().getX() / slotSize);
		int cy = (int)Math.floor(cell.getLocation().getY() / slotSize);
		cx = Math.max(0, cx);
		cx = Math.min(cx, slotsX - 1);
		cy = Math.max(0, cy);
		cy = Math.min(cy, slotsX - 1);
		cellGrid[cx][cy].removeOccupant(cell);
	}
	
	private double springForce(double r2)
	{
		final double RANGE = 2 * SquirmCell.RADIUS;
		final double RANGE2 = RANGE * RANGE;
		
		if (r2 > RANGE2)
		{
			return 0.4 * (Math.sqrt(r2) / RANGE - 1);
		}
		else
		{
			return 0;
		}
	}
	
	private double computeForce(double r2, double range, double magnitude)
	{
		if (r2 < (range * range))
		{
			return magnitude * (range / Math.sqrt(r2) - 1);
		}
		else
		{
			return 0;
		}
	}
	
	private SquirmCell createNewCell(double x, double y, char type, int state)
	{
		C2DVector location = new C2DVector(x, y);
		if (checkLocation(location))
		{
			SquirmCell cell = new SquirmCell(location, type, state);
			putCell(cell);
			cells.add(cell);
			return cell;
		}
		else
		{
			SquirmError.error("createNewCell - slot taken");
			return null;
		}
	}
	
	private SquirmCell createRandomCell()
	{
		double x = Math.random() * size.getX();
		double y = Math.random() * size.getY();
		char type = "aaaaabcdef".charAt((int)(Math.random() * 10));
		return createNewCell(x, y, type, 0);
	}
	
	private void recomputeVelocitiesAndReact()
	{
		// how far out do the physical forces extend?
		final double PHYS_RANGE = SquirmCell.RADIUS * 2;
		final double PHYS_RANGE2 = PHYS_RANGE * PHYS_RANGE;
		// how far out can reactions extend?
		final double REACTION_RANGE = SquirmCell.RADIUS * 2.5;
		final double REACTION_RANGE2 = REACTION_RANGE * REACTION_RANGE;
		// no effect acts beyond this distance
		final double MAX_RANGE = Math.max(PHYS_RANGE2, REACTION_RANGE2);
		
		// find the search radius distance in terms of the number of slots
		int searchSlots = (int)Math.ceil(MAX_RANGE / slotSize);
		
		// for each slot in the grid, get the list of cells within the
		// neighborhood. Use this list to recompute the velocities of the

		C2DVector to;
		double toLen2;
		double wallRange;
		
		final double XY_DIST = 2 * Math.sqrt(MAX_RANGE * MAX_RANGE / 2);
		
		for (int cx = 0; cx < slotsX; cx++)
		{
			for (int cy = 0; cy < slotsY; cy++)
			{
				// the cells in the central square
				ArrayList<SquirmCell> centralCells = cellGrid[cx][cy].getOccupants();
				ArrayList<SquirmCell> nearbyCells = initSearchArea(cx, cy, searchSlots);
				
				for (SquirmCell cell : centralCells)
				{
					// the cells in the search area (including central_cells)
					ArrayList<SquirmCell> reactionCandidates = new ArrayList<SquirmCell>();
					C2DVector force = new C2DVector(0, 0);
					
					if (cell.getBondedCells().isEmpty())
					{
						// no forces
					}
					else
					{
						for (SquirmCell other : nearbyCells)
						{
							if (cell == other)
							{
								continue;
							}
							
							to = C2DVector.sub(cell.getLocation(), other.getLocation());
							
							if (Math.abs(to.getX()) + Math.abs(to.getY()) > XY_DIST)
							{
								continue;
							}
							
							toLen2 = C2DVector.getLength2(to);
							
							if (toLen2 < REACTION_RANGE2)
							{
								ArrayList<SquirmCell> tooClose = getAllWithinRadius(other.getLocation(), SquirmCell.RADIUS * 1.5);
								if (tooClose.size() <= 1)
								{
									reactionCandidates.add(other);
								}
							}
							
							if ((cell.getBondedCells().isEmpty() && cell.getState() == 0)
                                || (other.getBondedCells().isEmpty() && other.getState() == 0))
							{
								// no forces
							}
							else
							{
								to = C2DVector.mul(to, computeForce(toLen2, PHYS_RANGE, 0.4));
								force = C2DVector.add(force, to);
							}
						}
					}
					
	                // pull forces from bonded cells
					for (SquirmCell other : cell.getBondedCells())
					{
						if (other != cell)
						{
							to = C2DVector.sub(other.getLocation(), cell.getLocation());
							to = C2DVector.mul(to, springForce(C2DVector.getLength2(to)));
							force = C2DVector.add(force, to);
						}
						else
						{
							SquirmError.error("Cell bonded with itself!?");
						}
					}
					
	                // forces from walls
					wallRange = SquirmCell.RADIUS;
					if (cell.getLocation().getX() < wallRange)
					{
						to = new C2DVector(cell.getLocation().getX(), 0);
						to = C2DVector.mul(to, wallRange / cell.getLocation().getX() - 1);
						force = C2DVector.add(force, to);
					}
					if (cell.getLocation().getX() > size.getX() - wallRange)
					{
						to = new C2DVector(cell.getLocation().getX() - size.getX(), 0);
						to = C2DVector.mul(to, wallRange / (size.getX() - cell.getLocation().getX()) - 1);
						force = C2DVector.add(force, to);
					}
					if (cell.getLocation().getY() < wallRange)
					{
						to = new C2DVector(0, cell.getLocation().getY());
						to = C2DVector.mul(to, wallRange / cell.getLocation().getY() - 1);
						force = C2DVector.add(force, to);
					}
					if (cell.getLocation().getY() > size.getY() - wallRange)
					{
						to = new C2DVector(0, cell.getLocation().getY() - size.getY());
						to = C2DVector.mul(to, wallRange / (size.getY() - cell.getLocation().getY()) - 1);
						force = C2DVector.add(force, to);
					}
					
					cell.setVelocity(C2DVector.add(cell.getVelocity(), force));
					
	                // we must limit the velocity else atoms can pass through each other
					double speed = C2DVector.getLength(cell.getVelocity());
					if (speed > SquirmCell.MAX_VELOCITY)
					{
						lostSpeed += speed - SquirmCell.MAX_VELOCITY;
						cell.setVelocity(C2DVector.mul(cell.getVelocity(), SquirmCell.MAX_VELOCITY / speed));
					}
					else if (speed < SquirmCell.MAX_VELOCITY - 1 && lostSpeed > 1 && speed != 0)
					{
						cell.setVelocity(C2DVector.mul(cell.getVelocity(), (speed + 1) / speed));
						lostSpeed -= 1;
					}
										
					reactionCandidates.add(cell);
					chemistry.react(cell, reactionCandidates, REACTION_RANGE2);
				}
			}
		}
	}
	
	private void moveCells()
	{
		C2DVector newLoc;
		
		for (SquirmCell cell : cells)
		{
			int oldSlotX = (int)Math.floor(cell.getLocation().getX() / slotSize);
			int oldSlotY = (int)Math.floor(cell.getLocation().getY() / slotSize);
			
			newLoc = C2DVector.add(cell.getLocation(), cell.getVelocity());
			if (checkLocation(newLoc))
			{
				cell.setLocation(newLoc);
			}
			else
			{
				C2DVector cellLoc = cell.getLocation();
				if (cellLoc.getX() < 0)
				{
					cell.setLocation(new C2DVector(0, cellLoc.getY()));
				}
				else if (cellLoc.getX() > size.getX())
				{
					cell.setLocation(new C2DVector(size.getX(), cellLoc.getY()));
				}
				if (cellLoc.getY() < 0)
				{
					cell.setLocation(new C2DVector(cellLoc.getX(), 0));
				}
				else if (cellLoc.getY() > size.getX())
				{
					cell.setLocation(new C2DVector(cellLoc.getX(), size.getY()));
				}
			}
			
			int newSlotX = (int)Math.floor(cell.getLocation().getX() / slotSize);
			int newSlotY = (int)Math.floor(cell.getLocation().getY() / slotSize);
			
			if (oldSlotX != newSlotX || oldSlotY != newSlotY)
			{
				cellGrid[oldSlotX][oldSlotY].removeOccupant(cell);
				cellGrid[newSlotX][newSlotY].addOccupant(cell);
			}
		}
	}
	
	private void doFlood()
	{
		double left[] = {0, size.getX() / 2, size.getX() / 2, 0};
		double top[] = {0, 0, size.getY() / 2, size.getY() / 2};
		for (SquirmCell cell : cells)
		{
			double cellX = cell.getLocation().getX();
			double cellY = cell.getLocation().getY();
			if (cellX >= left[floodSector]
                && cellX <= left[floodSector] + size.getX() / 2
                && cellY >= top[floodSector]
                && cellY < top[floodSector] + size.getY() / 2)
			{
				cell.breakAllBonds();
				cell.setState(0);
				cell.assignRandomVelocity();
			}
		}
		floodSector = (floodSector + 1) % 4;
	}
	
	private ArrayList<SquirmCell> initSearchArea(int cx, int cy,
                                             int searchSlots)
	{
		ArrayList<SquirmCell> nearbyCells = new ArrayList<SquirmCell>();
		int iStart = Math.max(0, cx - searchSlots);
		int iEnd = Math.min(slotsX - 1, cx + searchSlots);
		int jStart = Math.max(0, cy - searchSlots);
		int jEnd = Math.min(slotsY - 1, cy + searchSlots);
		for (int i = iStart; i <= iEnd; i++)
		{
			for (int j = jStart; j <= jEnd; j++)
			{
				for (SquirmCell cell : cellGrid[i][j].getOccupants())
				{
					nearbyCells.add(cell);
				}
			}
		}
		return nearbyCells;
	}
}

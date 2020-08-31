import java.util.ArrayList;

public class SquirmCell extends SquirmCellProperties
{
    public static final double RADIUS = 6;
    public static final double MAX_VELOCITY = RADIUS * 0.4;
    
    private C2DVector location;
    private C2DVector velocity;
    private ArrayList<SquirmCell> bondedCells = new ArrayList<SquirmCell>();
    
    public SquirmCell(C2DVector location, char type, int state)
    {
        super(type, state);
        this.location = location;
        assignRandomVelocity();
    }
    
    public C2DVector getLocation()
    {
        return location;
    }
    
    public void setLocation(C2DVector location)
    {
        this.location = location;
    }
    
    public C2DVector getVelocity()
    {
        return velocity;
    }
    
    public void setVelocity(C2DVector velocity)
    {
        this.velocity = velocity;
    }
    
    public ArrayList<SquirmCell> getBondedCells()
    {
        return bondedCells;
    }
    
    public SquirmCell bondTo(SquirmCell cell)
    {
        makeBondWith(cell);
        cell.makeBondWith(this);
        return this;
    }
    
    public void debond(SquirmCell cell)
    {
        if (bondedCells.contains(cell))
        {
            bondedCells.remove(cell);
        }
        else
        {
            SquirmError.error("debond - not bonded to this cell");
        }
        
        if (cell.getBondedCells().contains(this))
        {
            cell.getBondedCells().remove(this);
        }
        else
        {
            SquirmError.error("debond - cell not bonded to us");
        }
    }
    
    public boolean hasBondWith(SquirmCell cell)
    {
        return bondedCells.contains(cell);
    }
    
    public void breakAllBonds()
    {
        while (bondedCells.size() != 0)
        {
            debond(bondedCells.get(0));
        }
    }
    
    public void assignRandomVelocity()
    {
        double angle = Math.random() * (2 * Math.PI);
        double x = MAX_VELOCITY * Math.cos(angle);
        double y = MAX_VELOCITY * Math.sin(angle);
        velocity = new C2DVector(x, y);
    }
    
    private void makeBondWith(SquirmCell cell)
    {
        if (hasBondWith(cell))
        {
            SquirmError.error("makeBondWith - already bonded");
        }
        else
        {
            bondedCells.add(cell);
        }
    }
}

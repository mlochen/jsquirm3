public class SquirmCellProperties
{
    private char type;
    private int state;
    
    public SquirmCellProperties(char type, int state)
    {
        this.type = type;
        this.state = state;
    }
    
    public char getType()
    {
        return type;
    }
    
    public void setType(char type)
    {
        this.type = type;
    }
    
    public int getState()
    {
        return state;
    }
    
    public void setState(int state)
    {
        this.state = state;
    }
}

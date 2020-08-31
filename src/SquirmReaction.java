public class SquirmReaction
{
    public char aType, bType, cType;
    public int aState, bState, cState;
    public int futAState, futBState, futCState;
    public boolean curABBond, curBCBond, curACBond;
    public boolean futABBond, futBCBond, futACBond;
    public boolean assignEnzymeOfA;
    public long cases;
    public int nInputs;
    
    public SquirmReaction()
    {
        
    }
    
    SquirmReaction(char aType, int aState, boolean curABBond,
                   char bType, int bState, boolean curBCBond,
                   char cType, int cState, boolean curACBond,
                   int futAState, boolean futABBond,
                   int futBState, boolean futBCBond,
                   int futCState, boolean futACBond,
                   long cases)
    {
        this.aType = aType;
        this.aState = aState;
        this.curABBond = curABBond;
        this.bType = bType;
        this.bState = bState;
        this.curBCBond = curBCBond;
        this.cType = cType;
        this.cState = cState;
        this.curACBond = curACBond;
        this.futAState = futAState;
        this.futABBond = futABBond;
        this.futBState = futBState;
        this.futBCBond = futBCBond;
        this.futCState = futCState;
        this.futACBond = futACBond;
        this.cases = cases; 
        this.nInputs = 3;
        this.assignEnzymeOfA = false;
    }


    SquirmReaction(char aType, int aState, boolean curABBond,
                   char bType, int bState, boolean curBCBond,
                   char cType, int cState, boolean curACBond,
                   int futAState, boolean futABBond,
                   int futBState, boolean futBCBond,
                   int futCState, boolean futACBond)
    {
        this(aType, aState, curABBond,
             bType, bState, curBCBond,
             cType, cState, curACBond,
             futAState, futABBond,
             futBState, futBCBond,
             futCState, futACBond,
             1);
    }
    
    SquirmReaction(char aType, int aState, boolean curABBond,
                   char bType, int bState,
                   int futAState, boolean futABBond,
                   int futBState,
                   boolean aeoa, long cases)
    {
        this.aType = aType;
        this.aState = aState;
        this.curABBond = curABBond;
        this.bType = bType;
        this.bState = bState;
        this.futAState = futAState;
        this.futABBond = futABBond;
        this.futBState = futBState;
        this.cases = cases;
        this.nInputs = 2;
        this.assignEnzymeOfA = aeoa;
    }
    
    SquirmReaction(char aType, int aState, boolean curABBond,
                   char bType, int bState,
                   int futAState, boolean futABBond,
                   int futBState,
                   boolean aeoa)
    {
        this(aType, aState, curABBond,
             bType, bState,
             futAState, futABBond,
             futBState,
             aeoa, 1);
    }
}

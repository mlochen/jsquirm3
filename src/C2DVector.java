public class C2DVector
{
	private double x, y;
	
	public C2DVector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public C2DVector(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public static C2DVector add(C2DVector a, C2DVector b)
	{
		return new C2DVector(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	public static C2DVector sub(C2DVector a, C2DVector b)
	{
		return new C2DVector(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
	public static C2DVector mul(C2DVector a, double f)
	{
		return new C2DVector(a.getX() * f, a.getY() * f);
	}

	public static double getLength(C2DVector a)
	{
		return Math.sqrt(a.getX() * a.getX() + a.getY() * a.getY());
	}
	
	public static double getLength2(C2DVector a)
	{
		return a.getX() * a.getX() + a.getY() * a.getY();
	}
	
	public static double getDist2(C2DVector a, C2DVector b)
	{
		double deltaX = a.getX() - b.getX(); 
		double deltaY = a.getY() - b.getY();
		return deltaX * deltaX + deltaY * deltaY;
	}
}

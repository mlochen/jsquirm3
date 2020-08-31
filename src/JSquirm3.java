public class JSquirm3
{
	public static void main(String[] args)
	{
		final int width = 400;
		final int height = 300;
		final double density = 1.0f / 200;
		
		SquirmGridSwing grid = new SquirmGridSwing(width, height);
		grid.initSimple((int)(width * height * density));
		
		while (true)
		{
			grid.doTimeStep();
			grid.draw();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SquirmGridSwing extends SquirmGrid
{
	SquirmPanel panel;
	
	public SquirmGridSwing(int width, int height)
	{
		super(width, height);
		panel = new SquirmPanel(width, height, cells);
	}

	public void draw()
	{
		panel.repaint();
	}
}

class SquirmPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	ArrayList<SquirmCell> cells;
	
	public SquirmPanel(int width, int height, ArrayList<SquirmCell> cells)
	{
		this.cells = cells;
		this.setPreferredSize(new Dimension(width, height));
		
		JFrame frame = new JFrame("JSquirm3");
		frame.setTitle("JSquirm3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public final void paintComponent(Graphics g)
	{
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        
        for (SquirmCell cell : cells)
        {
        	if (cell.getState() == 0)
        	{
        		Color c = getColor(cell.getType());
        		Color ca = new Color(c.getRed(), c.getGreen(), c.getBlue(), 25);
        		g2d.setColor(ca);
        	}
        	else
        	{
        		g2d.setColor(getColor(cell.getType()));
        	}
        	
        	//double s = scale * SquirmCell.RADIUS * 0.8;
        	
        	int x1 = (int)(cell.getLocation().getX() - SquirmCell.RADIUS);
        	int y1 = (int)(cell.getLocation().getY() - SquirmCell.RADIUS);
          	int x2 = (int)(SquirmCell.RADIUS * 2);
        	int y2 = (int)(SquirmCell.RADIUS * 2);
        	g2d.fillOval(x1, y1, x2, y2);
        }
        
        g2d.setColor(Color.DARK_GRAY);
        for (SquirmCell cell : cells)
        {
        	for (SquirmCell other : cell.getBondedCells())
        	{
        		if (other.hashCode() < cell.hashCode())
        		{
        			continue;
        		}
        		
        		int x1 = (int)cell.getLocation().getX(); 
        		int y1 = (int)cell.getLocation().getY();
        		int x2 = (int)other.getLocation().getX(); 
        		int y2 = (int)other.getLocation().getY();
        		g2d.drawLine(x1, y1, x2, y2);
        	}
        }
        
		Toolkit.getDefaultToolkit().sync();
	}
	
	private Color getColor(char type)
	{
		switch (type)
		{
			case 'a': return new Color(220,220,0); // yellow
			case 'b': return new Color(128,128,128); // gray
			case 'c': return new Color(0,255,255); // cyan
			case 'd': return new Color(0,0,255); // blue
			case 'e': return new Color(255,0,0); // red
			case 'f': return new Color(0,255,0); // green
			default: return new Color(0, 0, 0);
		}
	}
}

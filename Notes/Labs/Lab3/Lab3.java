import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RandomBarChart extends JPanel{
	
	protected static final Graphics Graphic = null;
	JFrame panel;
	JButton button;
	Random rand = new Random();
	
	public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 int baseline = 200;
         Graphics2D g2d = (Graphics2D) g.create();
         int size = Math.min(getWidth() - 4, getHeight() - 4) / 10;
         int y = (getHeight() - (size * 10)) / 2;
         for (int hor = 0; hor < 10; hor++) {
             int x = (getWidth() - (size * 10)) / 2;

             for (int ver = 0; ver < 10; ver++) {
                 g.drawRect(x, y, size, size);
                 
                 
                 x += size;
             }
             y += size;
             
             
             }
         int x = (getWidth() - (size * 10)) / 2;
         for (int i = 0; i<10; i++) {
        	 int randNumber = (int) (Math.random()*400);
        		
        	 g2d.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
        	 g2d.setStroke(new BasicStroke(10));
        	 g2d.drawLine(x,500,x,randNumber);
            
             x= x+34;
                     	 
         }
       
        }
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Random Bar Chart");
		JButton button = new JButton("Redraw");

		frame.getContentPane();
		frame.setSize(600,400);
		frame.setLayout(new BorderLayout());
		frame.add(new RandomBarChart(), BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		button.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.repaint();
				
			}
			
		});
	}
}


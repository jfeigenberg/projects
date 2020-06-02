package gui;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

//display class largely sourced from CodeNMore's Youtube tutorial
public class Display {

	private JFrame frame;
	private Canvas canvas;
	private PathFinder pathFinder;
	
	private String title;
	private int width, height;
	
	public Display(String t, int w, int h, PathFinder p) {
		title = t;
		width = w;
		height = h;
		
		pathFinder = p;
		
		createDisplay();
	}
	
	private void createDisplay() {
		
		frame = new JFrame(title);
		frame.setSize(width,height);
		//make sure program closes properly
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//user cannot resize window
		frame.setResizable(false);
		//window opens in middle
		frame.setLocationRelativeTo(null);
		frame.add(pathFinder);
		
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width,height));
		canvas.setMaximumSize(new Dimension(width,height));
		canvas.setMinimumSize(new Dimension(width,height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		//so frame can make microadjustments for canvas
		frame.pack();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}

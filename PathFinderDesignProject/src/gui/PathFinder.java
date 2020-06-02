package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

import pathFinder.PathNode;
import pathFinder.PathTree;

//program runs through this class
public class PathFinder extends JPanel implements ActionListener, Runnable {
	
	//runs a timer for displaying 'animation', uses Runnable/thread to screenshot and update pathSpace
	private Timer t;
	private long startTime;
    private int seconds;
    
    private boolean running = false;
    private Thread thread;

	private Display display;
	
	private int width, height;
	private int[] startPos, endPos;
	//boolean determines if program will do aStar or greedy best first search
	private boolean aStar;
	
	//tree builds and searches space for path btwn startPos, endPos
	private PathTree tree;
	
	//for animating - stack contains multiple states of the board as the tree searches
	private static Stack<int[][]> pathSpaceStack;
	
	//current path space, can be updated by tree
	public static PathNode[][] pathSpace = new PathNode[PathTree.BOARD_SIZE][PathTree.BOARD_SIZE];
	
	//just in case stack runs out, can just keep displaying final state
	private static int[][] finalPathSpace;
	
	//captures current nodeStates for displaying - not sure if it completely worked
	public static void screenShot() {
		
		int[][] curr = new int[PathTree.BOARD_SIZE][PathTree.BOARD_SIZE];
		
		for(int x=0;x<PathTree.BOARD_SIZE;x++) {
			for(int y=0;y<PathTree.BOARD_SIZE;y++) {
				curr[x][y] = pathSpace[x][y].getNodeState();
			}
		}
		
		pathSpaceStack.push(curr);
		
	}
	
	public PathFinder(int w, int h, int[] startPos, int[] endPos, boolean aStar) {
		super();
		
		setBounds(0, 0, w, h);

		width = w;
		height = h;
		
		this.startPos = startPos;
		this.endPos = endPos;
		this.aStar = aStar;
		
	}
	
	public void init() {
		
		pathSpaceStack = new Stack<int[][]>();
		
		for(int x=0;x<PathTree.BOARD_SIZE;x++) {
			for(int y=0;y<PathTree.BOARD_SIZE;y++) {
				pathSpace[x][y] = new PathNode(null, x,y);
			}
		}
		
		
		//WALLS
		pathSpace[6][7].setNodeState(-1);
		pathSpace[6][8].setNodeState(-1);
		pathSpace[9][6].setNodeState(-1);
		pathSpace[10][7].setNodeState(-1);
		pathSpace[11][8].setNodeState(-1);
		pathSpace[7][7].setNodeState(-1);
		pathSpace[8][6].setNodeState(-1);
		pathSpace[10][10].setNodeState(-1);
		pathSpace[11][11].setNodeState(-1);
		pathSpace[13][13].setNodeState(-1);
		
		display = new Display("PathFinder", width, height, this);
		
		//to have an initial picture of the board before thread starts
		screenShot();
		
		tree = new PathTree(startPos, endPos, aStar);
		
		if(tree.isAStar()) {
			
			try {
				tree.AStarSearch(tree.getStartNode());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			
			try {
				tree.GreedyBestSearch(tree.getStartNode());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//after search is done, starts timer to display
		startTime = System.currentTimeMillis();
		seconds = 1;
		t = new Timer(1000, this);
		t.setCoalesce(true);
		t.setRepeats(false);
		t.setInitialDelay(50);
		t.start();
		
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.clearRect(0, 0, width, height);
		
		int[][] curr;
		
		if(pathSpaceStack.size() == 1) {
			finalPathSpace = pathSpaceStack.pop();
			curr = finalPathSpace;
		}
		else if(pathSpaceStack.isEmpty()){
			curr = finalPathSpace;
		}
		else {
			curr = pathSpaceStack.pop();
		}
		
		for(int x=0;x<PathTree.BOARD_SIZE;x++) {
			for(int y=0;y<PathTree.BOARD_SIZE;y++) {
				//always get a weird exception here even if displaying works
				paintNode(g,x,y,curr[x][y]);
			}
		}
		
	}
	
	public void paintNode(Graphics g, int x, int y, int s) {
		
		//wall
		if(s == -1) {
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.BLACK);
			g.fillRect(30*x,30*y,30,30);
		} 
				
		//foreign
		if(s == 0){
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.GRAY);
			g.fillRect(30*x,30*y,30,30);
		}
				
		//frontier
		if(s == 1){
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.YELLOW);
			g.fillRect(30*x,30*y,30,30);
		}
				
		//explored
		if(s == 2){
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.ORANGE);
			g.fillRect(30*x,30*y,30,30);
		}
				
		//startNode
		if(s == 3){
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.BLUE);
			g.fillRect(30*x,30*y,30,30);
		}
				
		//endNode
		if(s == 4){
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.RED);
			g.fillRect(30*x,30*y,30,30);
		}
			
		//on path
		if(s == 5) {
			g.setColor(Color.BLACK);
			g.drawRect(30*x,30*y,30,30);
			g.setColor(Color.MAGENTA);
			g.fillRect(30*x,30*y,30,30);
		}
				
	}


	@Override
	public void actionPerformed(ActionEvent e) {

        if(tree.isComplete()){
        	repaint();
        }
        
        t.setInitialDelay(50);
        t.start();

	}
	
	//updates pathSpace thru tree
	public void tick() {
		
		tree.tick();
		
	}

	@Override
	public void run() {
		
		init();
		
		while(running) {
			tick();
			screenShot();
		}
		
		stop();
		
	}
	
	public synchronized void start() {
		
		if(running) {
			return;
		}
		
		running = true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public synchronized void stop() {
		
		if(!running) {
			return;
		}
		
		running = false;
		
		try {
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
}

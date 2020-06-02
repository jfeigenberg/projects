package pathFinder;

import java.util.ArrayList;

import gui.PathFinder;

public class PathNode {
	
	private boolean expanded;
	
	private ArrayList<PathNode> children;
	//position doesn't change so can use immutable array
	private int x,y;
	private PathNode parent;
	
	/*
	 * -1 : wall (not in frontier or explored)
	 * 0 : foreign
	 * 1 : in frontier
	 * 2 : explored
	 * 3 : start node
	 * 4 : end node
	 * 5 : node on final path
	 */
	private int nodeState;
	
	//total path cost for A* is f(n)
	private int f;
	//path cost from start to node
	private int g;
	//heuristic (estimated distance from node to goal)
	private int h;
	
	public PathNode(int x, int y) {
		nodeState = 0;
		parent = null;
		this.x = x;
		this.y = y;
		expanded = false;
		
	}
	
	public PathNode(PathNode p, int x, int y) {
		nodeState = 0;
		parent = p;
		this.x = x;
		this.y = y;
		expanded = false;

	}
	
	public PathNode(PathNode p, int x, int y, int state) {
		nodeState = state;
		parent = p;
		this.x = x;
		this.y = y;
		expanded = false;
		
	}

	public int getNodeState() {
		return nodeState;
	}
	
	public void setNodeState(int s) {
		
		if(nodeState == 3 || nodeState == 4 || nodeState == 5) {
			return;
			//startNode, endNode and path shouldn't have to change
		}
		
		nodeState = s;
		
	}
	
	public boolean isWall() {
		return nodeState == -1;
	}
	
	public PathNode getParent() {
		return parent;
	}
	
	public void setParent(PathNode p) {
		parent = p;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getF() {
		return f;
	}
	
	public void setF(int n) {
		f = n;
	}
	
	public int getG() {
		return g;
	}
	
	public void setG(int n) {
		g = n;
	}
	
	public int getH() {
		return h;
	}
	
	public void setH(int n) {
		h = n;
	}
	
	public ArrayList<PathNode> getChildren(){
		return children;
	}
	
	//euclidean style
	public void calculateH(PathNode endNode) {
		
		h = (int) Math.sqrt(Math.pow(endNode.x-x,2)+Math.pow(endNode.y-y,2));
		
	}
	
	public void calculateG(PathNode startNode) {
		
		PathNode currentNode = this;	
		g=0;
		
		while(currentNode.parent != null) {
			currentNode = currentNode.parent;
			g++;
		}
		
	}
	
	public void calibrateNode(PathNode startNode, PathNode endNode) {
		if(isWall()) {
			return;
		}
		
		calculateG(startNode);
		calculateH(endNode);
		setF(g+h);
		
	}
	
	public void expand(PathNode startNode, PathNode endNode) {
		
		if(isWall()) {
			return;
		}
		
		children = new ArrayList<PathNode>();
		
		//4 cardinal directions on grid
		
		//uses pathSpace to check if it is a legal expansion
		
		if(x < PathTree.BOARD_SIZE-1) {
			if(!(PathFinder.pathSpace[x+1][y].isWall())) {
				PathNode n0 = new PathNode(this,x+1,y,1);
				children.add(n0);
			}
		}
		
		if(y < PathTree.BOARD_SIZE-1){ 
			if(!(PathFinder.pathSpace[x][y+1].isWall())){
				PathNode n1 = new PathNode(this,x,y+1,1);
				children.add(n1);
			}
		}
		
		if(x != 0) { 
			if(!(PathFinder.pathSpace[x-1][y].isWall())){
				PathNode n2 = new PathNode(this,x-1,y,1);
				children.add(n2);
			}
		}
		
		if(y != 0) { 
			if(!(PathFinder.pathSpace[x][y-1].isWall())){
				PathNode n3 = new PathNode(this,x,y-1,1);
				children.add(n3);
			}
		}
		
	}
	
	public PathNode clone() {
		return new PathNode(parent, x, y, nodeState);
	}
	
	public void tick() {
		PathFinder.pathSpace[x][y] = this;
	}
	
	
	//in the case of a search, two nodes may not both be calibrated (same f), but can have same posiiton
	@Override
	public boolean equals(Object o) {
		PathNode n = (PathNode) o;
		return (n.x == x) && (n.y == y);
	}
	
	
	@Override
	public String toString() {
		return "(" + x +","+y +")";
	}

}
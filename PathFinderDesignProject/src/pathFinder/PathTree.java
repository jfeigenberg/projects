package pathFinder;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import gui.PathFinder;

public class PathTree {
	
	public static final int BOARD_SIZE = 20;
	
	private ArrayList<PathNode> path; 
	
	//pops least expensive f(n) first
	private PriorityQueue<PathNode> AStarFrontier;
	private PriorityQueue<PathNode> GreedyBestFrontier;
	private ArrayList<PathNode> explored;
	
	private PathNode startNode;
	private PathNode endNode;
	
	//determines is search is A* or Greedy Best First
	private boolean isAStar;
	
	private boolean complete;
	
	//does this need another check to see if there is a better path?????
	public PathTree(int[] startPos, int[] endPos, boolean aStar) {
		
		complete = false;
		
		endNode = new PathNode(null, endPos[0], endPos[1], 4);
		startNode = new PathNode(null,startPos[0], startPos[1],3);
		
		startNode.calibrateNode(startNode,endNode);
		endNode.calibrateNode(startNode, endNode);
		
		startNode.tick();
		endNode.tick();
		
		isAStar = aStar;
		startNode.calibrateNode(startNode, endNode);
		explored = new ArrayList<PathNode>();
		
		if(isAStar) {
			AStarFrontier = new PriorityQueue<PathNode>(new AStarPathNodeComparator());
			AStarFrontier.offer(startNode);
		}
		
		else {
			GreedyBestFrontier = new PriorityQueue<PathNode>(new GreedyBestPathNodeComparator());
			GreedyBestFrontier.offer(startNode);
		}
		

	}
	
	public boolean isAStar() {
		return isAStar;
	}
	
	public PathNode getStartNode() {
		return startNode;
	}
	
	public PathNode getEndNode() {
		return endNode;
	}
	
	public ArrayList<PathNode> getPath(){
		return path;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	//f = g + h
	//see notes for GBF search for analogous explanation
	public ArrayList<PathNode> AStarSearch(PathNode node) throws Exception {
		
		if(node.equals(endNode)) {
			return getSolutionPath(node);
		}
		
		if(AStarFrontier.isEmpty()) {
			throw new Exception("Cannot find path.");
		}
		
		PathNode n = AStarFrontier.poll();
	
		explored.add(n);
		n.expand(startNode, endNode);
		
		for(PathNode c : n.getChildren()) {
			
			c.calibrateNode(startNode, endNode);
			
			if(!(AStarFrontier.contains(c) || explored.contains(c))) {
				
				AStarFrontier.offer(c);
				
			}
			
			else if(AStarFrontier.contains(c)) {
				
				AStarFrontier.remove(c);
				AStarFrontier.offer(c);
				
			}
			
		}
		
		return AStarSearch(n);
		
	}
	
	//f = h
	public ArrayList<PathNode> GreedyBestSearch(PathNode node) throws Exception {
		
		if(node.equals(endNode)) {
			complete = false;
			getSolutionPath(node);
		}
		
		if(GreedyBestFrontier.isEmpty()) {
			throw new Exception("Search failed.");
		}
		
		PathNode n = GreedyBestFrontier.poll();
		explored.add(n);
		n.expand(startNode, endNode);
		
		for(PathNode c : n.getChildren()) {
			
			c.calibrateNode(startNode, endNode);
			
			if(!(GreedyBestFrontier.contains(c) || explored.contains(c))) {
				
				GreedyBestFrontier.offer(c);

			}
			
			//I don't know if this else if statement qualifies as making sure the frontier nodes have the lowest f-value
			
			else if(GreedyBestFrontier.contains(c)) {
				
				GreedyBestFrontier.remove(c);
				GreedyBestFrontier.offer(c);
				
			}
			
			/*not actually a complete GBF search - below 'else if' statement shows my attempt to complete, 
			 * although iterator next() method is fail-fast and throws exception,
			 * maybe a continue statement with a while loop would've worked better?
			 */
			
			/*else if(GreedyBestFrontier.contains(c)) {
				
				Iterator<PathNode> i = GreedyBestFrontier.iterator();
				
				while(i.hasNext()) {
					PathNode p = (PathNode) i.next();
					if(c.getH() < p.getH() && p.equals(n)) {
						
						GreedyBestFrontier.offer(c);
						
					}
				}
			}*/
			
		}
		return GreedyBestSearch(n);
		
	}
	
	public ArrayList<PathNode> getSolutionPath(PathNode node){

		path = new ArrayList<PathNode>();

		while(node.getParent() != null) {
			
			path.add(0,node);
			node = node.getParent();
			
		}
		
		complete = true;
		return path;
		
	}
	
	//ticks every significant node w/ appropriate states for display
	public void tick() {
		
		if(isAStar) {
		
			for(PathNode a : AStarFrontier) {
				a.setNodeState(1);
				a.tick();
			}
		}
		
		else {
			for(PathNode a : GreedyBestFrontier) {
				a.setNodeState(1);
				a.tick();
			}
		}
		
		for(PathNode a : explored) {
			a.setNodeState(2);
			a.tick();
		}
		
		for(PathNode a : path) {
			a.setNodeState(5);
			a.tick();
		}
		
		startNode.setNodeState(3);
		endNode.setNodeState(4);
		startNode.tick();
		endNode.tick();
		
	}

}


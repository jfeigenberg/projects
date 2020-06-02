package pathFinder;

import java.util.Comparator;

public class AStarPathNodeComparator implements Comparator<PathNode> {

	@Override
	public int compare(PathNode n1, PathNode n2) {
		//cool ternary operator
		return n1.getF() > n2.getF() ? 1 : n1.getF() == n2.getF() ? 0 : -1;
	}
}
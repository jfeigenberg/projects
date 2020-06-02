package pathFinder;

import java.util.Comparator;

public class GreedyBestPathNodeComparator implements Comparator<PathNode> {

	@Override
	//looks just at heuristic distance value
	public int compare(PathNode n1, PathNode n2) {
		//cool ternary operator
		return n1.getH() > n2.getH() ? 1 : n1.getH() == n2.getH() ? 0 : -1;
	}
}

package tsp.deliverable;

import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

public class LocalSearchHeuristic extends AHeuristic {
	private Solution bestEver;
	private LSA_SwapNeighborhood neighbors;

	public LocalSearchHeuristic(Instance instance, String name) throws Exception {
		super(instance, name);
		bestEver=null;
		neighbors=new LSA_SwapNeighborhood(instance, "neighbors");
		// TODO Auto-generated constructor stub
	}

	public Solution getSolution() {
		return bestEver;
	}

	public void setBestEver(Solution bestEver) {
		this.bestEver = bestEver;
	}

	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		List<Solution> neighborhood = neighbors.getNeighborhood(bestEver);
		for(Solution s : neighborhood) {
			if(s.evaluate()<bestEver.evaluate()) bestEver=s;
		}
		System.out.println(bestEver.evaluate());
	}

}

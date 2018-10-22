package tsp.deliverable;

import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

public class LocalSearchHeuristic extends AHeuristic {
	private Solution bestEver;
	private Solution currentBest;
	private LSA_SwapNeighborhood neighbors1;
	private LSA_2optNeighborhood neighbors2;
	private LSA_ShiftNeighborhood neighbors3;
	private int methodChange;

	public LocalSearchHeuristic(Instance instance, String name) throws Exception {
		super(instance, name);
		bestEver=null;
		neighbors1=new LSA_SwapNeighborhood(instance, "neighbors");
		neighbors2=new LSA_2optNeighborhood(instance, "neighbors");
		neighbors3=new LSA_ShiftNeighborhood(instance, "neighbors");
		methodChange = 0;
		// TODO Auto-generated constructor stub
	}

	public Solution getSolution() {
		return bestEver;
	}

	public void setBestEver(Solution bestEver) {
		this.bestEver = bestEver;
	}
	
	public void setCurrentEver(Solution currentBest) {
		this.currentBest = currentBest;
	}

	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		switch (methodChange%3) {
		case 1: 
			List<Solution> neighborhood1 = neighbors1.getNeighborhood(bestEver);
			for(Solution s : neighborhood1) { if(s.evaluate()<=bestEver.evaluate()) {currentBest=s;}}
			if(currentBest.evaluate()<bestEver.evaluate()) {bestEver=currentBest;System.out.println(bestEver.evaluate()+" : 1");}
			else {methodChange++;}
			break;
		
		case 0:
			List<Solution> neighborhood2 = neighbors2.getNeighborhood(bestEver); 
			for(Solution s : neighborhood2) { if(s.evaluate()<=bestEver.evaluate()) {currentBest=s;}}
			if(currentBest.evaluate()<bestEver.evaluate()) {bestEver=currentBest;System.out.println(bestEver.evaluate()+" : 2");}
			else {methodChange++;}
			break;
			
		case 2:
			List<Solution> neighborhood3 = neighbors3.getNeighborhood(bestEver); 
			for(Solution s : neighborhood3) { if(s.evaluate()<=bestEver.evaluate()) {currentBest=s;}}
			if(currentBest.evaluate()<bestEver.evaluate()) {bestEver=currentBest;System.out.println(bestEver.evaluate()+" : 3");}
			else {methodChange++;}
			break;
		}
	}

}

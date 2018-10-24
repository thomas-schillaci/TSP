package tsp.deliverable;

import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/*
 * Variable neighborhood search method
 */
public class LocalSearchHeuristic extends AHeuristic {
	private Solution bestEver;
	private Solution currentBest;
	private LSA_SwapNeighborhood neighbors1;
	private LSA_2optNeighborhood neighbors2;
	private LSA_ShiftNeighborhood neighbors3;
	private int methodChange;
	private int stop;

	public LocalSearchHeuristic(Instance instance, String name) throws Exception {
		super(instance, name);
		bestEver=null;
		neighbors1=new LSA_SwapNeighborhood(instance, "neighbors");
		neighbors2=new LSA_2optNeighborhood(instance, "neighbors");
		neighbors3=new LSA_ShiftNeighborhood(instance, "neighbors");
		methodChange = 0;
		stop = 0;
		// TODO Auto-generated constructor stub
	}
	
	public LocalSearchHeuristic(Instance instance,Solution sol) throws Exception {
		super(instance, "Local Search");
		neighbors1=new LSA_SwapNeighborhood(instance, "neighbors");
		neighbors2=new LSA_2optNeighborhood(instance, "neighbors");
		neighbors3=new LSA_ShiftNeighborhood(instance, "neighbors");
		methodChange = 0;
		stop = 0;
		setBestEver(sol);
		setCurrentBest(sol);
	}

	public Solution getSolution() {
		return bestEver;
	}

	public void setBestEver(Solution bestEver) {
		this.bestEver = bestEver;
	}
	
	public void setCurrentBest(Solution currentBest) {
		this.currentBest = currentBest;
	}
	
	public boolean isStopped() {
		return stop>=3;
	}

	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		while(!isStopped()) {
		switch (methodChange%3) {
		//The program starts with the 2-opt local search as it is fast and efficient
		case 0:
			List<Solution> neighborhood2 = neighbors2.getNeighborhood(bestEver); 
			for(Solution s : neighborhood2) { if(s.evaluate()<=currentBest.evaluate()) {currentBest=s;}}
			if(currentBest.evaluate()<bestEver.evaluate()) {bestEver=currentBest;stop=0;}
			else {methodChange++;stop++;}
			break;
		//Trying to swap the last city index proves to be quite useful as the 2-opt method usually fail to
		// find a correct last city choice.
		case 1: 
			List<Solution> neighborhood1 = neighbors1.getNeighborhood(bestEver);
			for(Solution s : neighborhood1) { if(s.evaluate()<=currentBest.evaluate()) {currentBest=s;}}
			if(currentBest.evaluate()<bestEver.evaluate()) {bestEver=currentBest;stop=0;}
			else {methodChange++;stop++;}
			break;
		//Shifting parts of the solutions sometimes helps to escape a local minimum when the instances are big.
		case 2:
			List<Solution> neighborhood3 = neighbors3.getNeighborhood(bestEver); 
			for(Solution s : neighborhood3) { if(s.evaluate()<=currentBest.evaluate()) {currentBest=s;}}
			if(currentBest.evaluate()<bestEver.evaluate()) {bestEver=currentBest;stop=0;}
			else {methodChange++;stop++;}
			break;
		}
		}
	}

}

package tsp.deliverable;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.neighborhood.ANeighborhood;

/**
 * @author leovu
 *
 */
public class LSA_SwapNeighborhood extends ANeighborhood {

	public LSA_SwapNeighborhood(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * @return Generates the list of neighbors of the Solution sol by swapping the last city of the path with the others
	 * 
	 * */
	public List<Solution> getNeighborhood(Solution sol) throws Exception {
		// TODO Auto-generated method stub
		List<Solution> solutions = new ArrayList<Solution>();
		Solution swap;
		for(int i=1;i<super.m_instance.getNbCities()-1;i++) {
			swap = swap(i,super.m_instance.getNbCities()-1,sol);
			if (swap.getObjectiveValue()<sol.getObjectiveValue()) solutions.add(swap);
		}
		return solutions;
	}
	
/**
 * @return Creates a new solution where the index i with the index j in the Solution sol are switched
 *  
 */
	public Solution swap(int i, int j, Solution sol) {
		int temp;
		Solution solution = sol.copy();
		try {
			temp = solution.getCity(j);
			solution.setCityPosition(sol.getCity(i), j);
			solution.setCityPosition(temp, i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return solution;
	}
}

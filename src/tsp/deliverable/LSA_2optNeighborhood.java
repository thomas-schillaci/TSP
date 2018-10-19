package tsp.deliverable;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.neighborhood.ANeighborhood;
/**
 * 
 * @author leovu
 *
 */
public class LSA_2optNeighborhood extends ANeighborhood{

	public LSA_2optNeighborhood(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * @return Generates the list of neighbors of the Solution sol with the 2-opt method
	 * 
	 * */
	public List<Solution> getNeighborhood(Solution sol) throws Exception {
		// TODO Auto-generated method stub
		List<Solution> solutions = new ArrayList<Solution>();
		for (int i = 1; i < super.m_instance.getNbCities() - 2; i++) {
	           for (int j = i + 1; j < super.m_instance.getNbCities() - 1; j++) {
	        	   if(
							m_instance.getDistances(sol.getCity(i-1), sol.getCity(i))+m_instance.getDistances(sol.getCity(j), sol.getCity(j+1))>
					        m_instance.getDistances(sol.getCity(i), sol.getCity(j+1))+m_instance.getDistances(sol.getCity(i-1), sol.getCity(j))
					        //we need to check if the triangle inequality is verified
	               ) solutions.add(twoOptSwap(i, j, sol));
	           }
	       }
		return solutions;
	}
	
	/**
	 * @return Creates a new solution where we have inverted the order of the cities between 
	 * the index i and the index j (including i and j) in the Solution sol
	 *  
	 */
		public Solution twoOptSwap(int i, int j, Solution sol) {
			Solution solution = sol.copy();
			try {
				int dec = 0;
				for(int k=i;k<=j;k++) {
					solution.setCityPosition(sol.getCity(j-dec),k);
					dec++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return solution;
		}

}

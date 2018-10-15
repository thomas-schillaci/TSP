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
		for(int i=1;i<super.m_instance.getNbCities()-1;i++) {
			for(int j=1; j<super.m_instance.getNbCities()-1;j++) {
				if(j!=i-1 && j!=i && j!=i+1 && 
						m_instance.getDistances(sol.getCity(i), sol.getCity(i+1))+m_instance.getDistances(sol.getCity(j), sol.getCity(j+1))>
				        m_instance.getDistances(sol.getCity(i), sol.getCity(j))+m_instance.getDistances(sol.getCity(i+1), sol.getCity(j+1))
				        //we need to check if the triangle inequality is verified
						) solutions.add(swap(i+1,j,sol));
			}
		}
		return solutions;
	}
	
	/**
	 * @return Creates a new solution where we have switched the index i with the index j in the Solution sol
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

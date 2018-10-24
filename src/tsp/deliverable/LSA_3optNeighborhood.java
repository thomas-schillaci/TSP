package tsp.deliverable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.neighborhood.ANeighborhood;
/**
 * 
 * @author leovu
 *
 */
public class LSA_3optNeighborhood extends ANeighborhood{

	public LSA_3optNeighborhood(Instance instance, String name) throws Exception {
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
		for (int i = 1; i < super.m_instance.getNbCities() - 3; i++) {
	           for (int j = i + 1; j < super.m_instance.getNbCities() - 2; j++) {
	        	   for (int k= j + 1; k < super.m_instance.getNbCities()-1; k++) {
	        		   solutions.addAll(threeOptSwap(i,j,k,sol));
	        	   }
	           }
	       }
		return solutions;
	}
	

private List<Solution> threeOptSwap(int i, int j, int k, Solution sol) throws Exception {
	List<Solution> solutions = new ArrayList<Solution>();
	//sol has the connections (i,i+1) (j,j+1) (k,k+1)	
	double currentDistance = super.m_instance.getDistances(i, i+1)+
			super.m_instance.getDistances(j, j+1)+
			super.m_instance.getDistances(k, k+1);
	
	//case 1 : (i,i+1) (j,k) (j+1,k+1)
	if (super.m_instance.getDistances(i, i+1)+
			super.m_instance.getDistances(j, k)+
			super.m_instance.getDistances(j+1, k+1) < currentDistance) {
		solutions.add(swap(j+1,k,sol));
		currentDistance=super.m_instance.getDistances(i, i+1)+
				super.m_instance.getDistances(j, k)+
				super.m_instance.getDistances(j+1, k+1);
	}
	//case 2 : (i,j) (i+1,j+1) (k,k+1)
	if (super.m_instance.getDistances(i, j)+
			super.m_instance.getDistances(i+1, j+1)+
			super.m_instance.getDistances(k, k+1) < currentDistance) {
	solutions.add(swap(i+1,j,sol));
	currentDistance =super.m_instance.getDistances(i, j)+
			super.m_instance.getDistances(i+1, j+1)+
			super.m_instance.getDistances(k, k+1);
	}
	//case 3 : (i,j) (i+1,k) (j+1,k+1)
	if (super.m_instance.getDistances(i, j)+
			super.m_instance.getDistances(i+1, k)+
			super.m_instance.getDistances(j+1, k+1) < currentDistance) {
	solutions.add(swap(i+1,j,swap(j+1,k,sol)));
	currentDistance = super.m_instance.getDistances(i, j)+
			super.m_instance.getDistances(i+1, k)+
			super.m_instance.getDistances(j+1, k+1);
	}
	//case 4 : (i,j+1) (k,i+1) (j,k+1)
	if (super.m_instance.getDistances(i, j+1)+
			super.m_instance.getDistances(k, i+1)+
			super.m_instance.getDistances(j, k+1) < currentDistance) {
	solutions.add(swap(i+1,j+1,swap(j,k,sol)));
	currentDistance = super.m_instance.getDistances(i, j+1)+
			super.m_instance.getDistances(k, i+1)+
			super.m_instance.getDistances(j, k+1);
	}
	//case 5 : (i,j+1) (j,k) (i+1,k+1) -> (i,j+1) (j,i+1) (k, k+1)
	if (super.m_instance.getDistances(i, j+1)+
			super.m_instance.getDistances(j, k)+
			super.m_instance.getDistances(i+1, k+1) < currentDistance) {
	solutions.add(swap(i+1,j+1,swap(k,j+1,sol)));
	currentDistance =super.m_instance.getDistances(i, j+1)+
			super.m_instance.getDistances(j, k)+
			super.m_instance.getDistances(i+1, k+1);
	}
	//case 6 : (i,k) (i+1,j+1) (j,k+1) 
	if (super.m_instance.getDistances(i, k)+
			super.m_instance.getDistances(i+1, j+1)+
			super.m_instance.getDistances(j, k+1) < currentDistance) {
	solutions.add(swap(j,k,swap(i+1,j,sol)));
	currentDistance=super.m_instance.getDistances(i, k)+
			super.m_instance.getDistances(i+1, j+1)+
			super.m_instance.getDistances(j, k+1);
	}
	//case 7 : (i,k) (j,j+1) (i+1,k+1)
	if (super.m_instance.getDistances(i, k)+
			super.m_instance.getDistances(j, j+1)+
			super.m_instance.getDistances(i+1, k+1) < currentDistance) {
	solutions.add(swap(i+1,k,sol));
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

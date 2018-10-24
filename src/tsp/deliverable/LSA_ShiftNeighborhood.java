package tsp.deliverable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.neighborhood.ANeighborhood;

/*
 * 
 */
public class LSA_ShiftNeighborhood extends ANeighborhood {
	static final int length = 8;

	public LSA_ShiftNeighborhood(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Solution> getNeighborhood(Solution sol) throws Exception {
		// TODO Auto-generated method stub
		List<Solution> solutions = new ArrayList<Solution>();
		for(int i = 1; i<super.m_instance.getNbCities()-length; i++) {
			for(int j = 1; j<i;j++) {
				solutions.add(shift(i,j,sol));
			}
			for(int k = i+1;k<super.m_instance.getNbCities()-length;k++) {
				solutions.add(shift(i,k,sol));
			}
		}
		return solutions;
	}
	
	public static Solution shift(int i, int j, Solution sol) throws Exception {
		if(i>j) {return shift(j,i,sol);}
		else {
		Solution sol2 = sol.copy();
		int communes =Math.max(0, length+i-j);
		for(int k = i; k<i+length;k++) {
			sol2.setCityPosition(sol.getCity(k), k-i+j);
		}
		for(int k = j+communes; k<j+length;k++) {
			sol2.setCityPosition(sol.getCity(k), i+(k-j-communes));
		}
		return sol2;
		}
	}

}

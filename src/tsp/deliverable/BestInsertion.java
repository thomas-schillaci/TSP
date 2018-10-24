package tsp.deliverable;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/** 
 * The best insertion consists in finding the best position of insertion of the next city at each step.
 * It is a greddy algorithm.
 */

public class BestInsertion extends AHeuristic{
	public BestInsertion(Instance instance) throws Exception {
		super(instance, "best insertion");
	}

	@Override
	public void solve() throws Exception {
		ArrayList<Integer> used=new ArrayList<Integer>(0);
		Solution solution=new Solution(m_instance);
		int firstCity=1;
		used.add(firstCity);
		solution.setCityPosition(firstCity, 0);
		solution.setCityPosition(firstCity,  m_instance.getNbCities());
		int secondCity=firstCity+1;
		used.add(secondCity);
		solution.setCityPosition(secondCity, 1);
		for(int i=3;i<m_instance.getNbCities();i++) {
			solution.setCityPosition(bestInsertion(i,used), i);
		}
		m_solution=solution;
		m_solution.evaluate();
	}
	

	public int bestInsertion(int city, ArrayList<Integer> used) throws Exception {
		long addedLength=m_instance.getDistances(used.get(0),city)+m_instance.getDistances(city,used.get(1))-m_instance.getDistances(used.get(0),used.get(1));
		int bestInsertion=1;
		for (int i=0;i<used.size()-1;i++) {
			if(addedLength>m_instance.getDistances(used.get(i),city)+m_instance.getDistances(city,used.get(i+1))-m_instance.getDistances(used.get(i),used.get(i+1))) {
				addedLength=m_instance.getDistances(used.get(i),city)+m_instance.getDistances(city,used.get(i+1))-m_instance.getDistances(used.get(i),used.get(i+1));
				bestInsertion=i+1;
			}
		}
		used.add(used.get(used.size()));
		for (int j=used.size()-1;j>bestInsertion;j--) {
			used.set(j, used.get(j-1));
		}
		return bestInsertion;
	}
}

package tsp.deliverable;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/**
 * 	The nearest Neighbor consists in finding the nearest city at every step. 
 * 	It is a greedy algorithm that doesn't necessarily find the best solution.
 * @author Charles
 *
 */
public class NearestNeighbor extends AHeuristic{

	public NearestNeighbor(Instance instance) throws Exception {
		super(instance, "nearest neighbor");
	}

	@Override
	public void solve() throws Exception {
		ArrayList<Integer> unused= new ArrayList<>();
		for (int j=0;j<m_instance.getNbCities();j++) {
			unused.add(j);
		}
		Solution solution=new Solution(m_instance);
		int firstCity=(int)(Math.random()*m_instance.getNbCities());
		solution.setCityPosition(firstCity, 0);
		solution.setCityPosition(firstCity, m_instance.getNbCities());
		unused.remove(firstCity);
		for (int i=1;i<m_instance.getNbCities();i++) {
			int nextCity=findNearestNeighbor(solution.getCity(i-1),unused);
			unused.remove(unused.indexOf(nextCity));
			solution.setCityPosition(nextCity, i);
			m_solution=solution;
			m_solution.evaluate();
		}
	}
	
	public int findNearestNeighbor(int city, ArrayList<Integer> unused) {
		int nearest = unused.get(0);
		for (int candidate : unused) {
			try {
				boolean replace=(m_instance.getDistances(city, candidate)<m_instance.getDistances(city, nearest));
				if (replace) nearest=candidate;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return nearest;
	}
}

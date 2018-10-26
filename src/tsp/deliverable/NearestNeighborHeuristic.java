package tsp.deliverable;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/**
 * 	The nearest Neighbor consists in finding the nearest city at every step. 
 * 	It is a greedy algorithm that doesn't necessarily find the best solution, but it is very fast.
 * 	
 * @author Charles
 *
 */
public class NearestNeighborHeuristic extends AHeuristic{

	public NearestNeighborHeuristic(Instance instance) throws Exception {
		super(instance, "nearest neighbor");
	}

	@Override
	public void solve() throws Exception {
		ArrayList<Integer> unused= new ArrayList<>();     	// unused is the list of cities yet to visit
		for (int j=0;j<m_instance.getNbCities();j++) {		
			unused.add(j);									//at first, evey city is yet to be visited
		}
		Solution solution=new Solution(m_instance);
		int firstCity=(int)(Math.random()*m_instance.getNbCities());	//we choose the first city randomly
		solution.setCityPosition(firstCity, 0);
		solution.setCityPosition(firstCity, m_instance.getNbCities());
		unused.remove(firstCity);							
		for (int i=1;i<m_instance.getNbCities();i++) {		
			int nextCity=findNearestNeighbor(solution.getCity(i-1),unused);
			unused.remove(unused.indexOf(nextCity));
			solution.setCityPosition(nextCity, i);
		}
		m_solution=solution;
		m_solution.evaluate();
	}

/**
 * 
 * @param city
 * @param citiesToVisit
 * @return the nearest city of city within the list citiesToVisit
 */
	public int findNearestNeighbor(int city, ArrayList<Integer> citiesToVisit) {
		int nearest = citiesToVisit.get(0);
		for (int candidate : citiesToVisit) {
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
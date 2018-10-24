package tsp.deliverable;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/**
 * The best insertion chooses a first city randomly, the second city as the
 * nearest from the first one. Then at each step it finds the best association
 * of city and position of insertion to minimize the added length. It is greedy.
 * 
 * @author Charles
 *
 */
public class BestInsertionHeuristic extends AHeuristic {

	public BestInsertionHeuristic(Instance instance) throws Exception {
		super(instance, "bestInsertion");
	}

	@Override
	public void solve() throws Exception {
		ArrayList<Integer> used = new ArrayList<Integer>(); // used is the list of city already visited, at first it is
															// empty
		ArrayList<Integer> unused = new ArrayList<>(); // unused is the list of cities yet to visit
		for (int j = 0; j < m_instance.getNbCities(); j++) {
			unused.add(j); // at first, every city is yet to be visited
		}
		Solution solution = new Solution(m_instance);
		int firstCity = (int) (Math.random() * m_instance.getNbCities()); // choose the first city randomly
		used.add(firstCity);
		unused.remove(firstCity);
		int secondCity = findNearestNeighbor(firstCity, unused); // we choose the nearest city to the first one as
																	// second city
		used.add(secondCity);
		unused.remove(unused.indexOf(secondCity));

		for (int i = 3; i < m_instance.getNbCities() + 1; i++) {
			int[] bestInsertion = bestInsertion(used, unused);
			used.add(bestInsertion[1], bestInsertion[0]);
			unused.remove(unused.indexOf(bestInsertion[0]));
		}
		for (int k = 0; k < m_instance.getNbCities(); k++) {
			solution.setCityPosition(used.get(k), k);
		}
		solution.setCityPosition(used.get(0), m_instance.getNbCities());
		m_solution = solution;
		m_solution.evaluate();
	}

	public int[] bestInsertion(ArrayList<Integer> used, ArrayList<Integer> unused) throws Exception {
		int city = unused.get(0);
		int position = 1;
		long addedLength = m_instance.getDistances(used.get(0), unused.get(0))
				+ m_instance.getDistances(unused.get(0), used.get(1))
				- m_instance.getDistances(used.get(0), used.get(1));
		for (int unusedCity : unused) {
			if (addedLength > m_instance.getDistances(used.get(used.size() - 1), unusedCity)
					+ m_instance.getDistances(unusedCity, used.get(0)) - m_instance.getDistances(used.get(used.size() - 1), used.get(0))) {
				city = unusedCity;
				position = used.size();
			}
			for (int i = 0; i < used.size() - 1; i++) {
				if (addedLength > m_instance.getDistances(used.get(i), unusedCity) + m_instance.getDistances(unusedCity, used.get(i+1))
						- m_instance.getDistances(used.get(i), used.get(i+1))) {
					addedLength = m_instance.getDistances(used.get(i), unusedCity) + m_instance.getDistances(unusedCity, used.get(i+1))
							- m_instance.getDistances(used.get(i), used.get(i+1));
					city = unusedCity;
					position = i + 1;
				}
			}
		}
		int[] bestInsertion = { city, position };
		return bestInsertion;
	}

	public int findNearestNeighbor(int city, ArrayList<Integer> citiesToVisit) {
		int nearest = citiesToVisit.get(0);
		for (int candidate : citiesToVisit) {
			try {
				boolean replace = (m_instance.getDistances(city, candidate) < m_instance.getDistances(city, nearest));
				if (replace)
					nearest = candidate;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return nearest;
	}
}

// giovanni.lo-bianco@imt-atlantique.fr

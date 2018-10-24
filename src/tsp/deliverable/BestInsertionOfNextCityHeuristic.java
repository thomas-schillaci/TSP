package tsp.deliverable;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/** 
 * The best insertion consists in finding the best position of insertion of the next city at each step.
 * It is a greddy algorithm.
 */

public class BestInsertionOfNextCityHeuristic extends AHeuristic{
	public BestInsertionOfNextCityHeuristic(Instance instance) throws Exception {
		super(instance, "best insertion of next city");
	}

	@Override
	public void solve() throws Exception {
		ArrayList<Integer> used=new ArrayList<Integer>();
		Solution solution=new Solution(m_instance);
		int firstCity=0;									// we start with the two first cities and insert all the next ones
		used.add(firstCity);
		int secondCity=firstCity+1;
		used.add(secondCity);
		for(int i=2;i<m_instance.getNbCities();i++) {
			used.add(bestInsertion(i,used), i);
		}
		for (int k=0;k<m_instance.getNbCities();k++) {
			solution.setCityPosition(used.get(k), k);
		}
		solution.setCityPosition(used.get(0), m_instance.getNbCities());
		m_solution=solution;
		m_solution.evaluate();
	}
	
/**
 * Finds the best position to insert the city city in the List of cities that are already placed.
 * @param city
 * @param used
 * @return
 * @throws Exception
 */

	public int bestInsertion(int city, ArrayList<Integer> used) throws Exception {
		long addedLength=m_instance.getDistances(used.get(0),city)+m_instance.getDistances(city,used.get(1))-m_instance.getDistances(used.get(0),used.get(1));
		int bestInsertion=1;
		for (int i=1;i<used.size()-1;i++) {
			if(addedLength>m_instance.getDistances(used.get(i),city)+m_instance.getDistances(city,used.get(i+1))-m_instance.getDistances(used.get(i),used.get(i+1))) {
				addedLength=m_instance.getDistances(used.get(i),city)+m_instance.getDistances(city,used.get(i+1))-m_instance.getDistances(used.get(i),used.get(i+1));
				bestInsertion=i+1;
			}
		}
		return bestInsertion;
	}

}

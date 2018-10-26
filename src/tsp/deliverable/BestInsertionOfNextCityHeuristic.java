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
		ArrayList<Integer> used=new ArrayList<Integer>();				//used is the list of cities that were visited, empty at first
		Solution solution=new Solution(m_instance);
		int firstCity= (int) (Math.random() * m_instance.getNbCities());
		used.add(firstCity);
		int secondCity= (int) (Math.random() * m_instance.getNbCities()); // we choose the two first cities randomly 
		used.add(secondCity);
		for(int i=0;i<m_instance.getNbCities();i++) {
			if ((i!=firstCity && i!=secondCity))	used.add(bestInsertion(i,used), i);
		}
		for (int k=0;k<m_instance.getNbCities();k++) {
			solution.setCityPosition(used.get(k), k);
		}
		solution.setCityPosition(used.get(0), m_instance.getNbCities());
		m_solution=solution;
		m_solution.evaluate();
	}
	
/**
 * @param city
 * @param used
 * @return	the best position to insert the city city within the list of cities used
 * @throws Exception
 */

	public int bestInsertion(int city, ArrayList<Integer> used) throws Exception {
		long addedLength=m_instance.getDistances(used.get(0),city)+m_instance.getDistances(city,used.get(1))-m_instance.getDistances(used.get(0),used.get(1));
		int bestInsertion=1;
		if (addedLength>m_instance.getDistances(used.get(used.size()-1),city)+m_instance.getDistances(city,used.get(0))-m_instance.getDistances(used.get(used.size()-1),used.get(0))){
			addedLength=m_instance.getDistances(used.get(used.size()-1),city)+m_instance.getDistances(city,used.get(0))-m_instance.getDistances(used.get(used.size()-1),used.get(0));
			bestInsertion=used.size();
		}
		for (int i=1;i<used.size()-1;i++) {
			if(addedLength>m_instance.getDistances(used.get(i),city)+m_instance.getDistances(city,used.get(i+1))-m_instance.getDistances(used.get(i),used.get(i+1))) {
				addedLength=m_instance.getDistances(used.get(i),city)+m_instance.getDistances(city,used.get(i+1))-m_instance.getDistances(used.get(i),used.get(i+1));
				bestInsertion=i+1;
			}
		}
		return bestInsertion;
	}

}

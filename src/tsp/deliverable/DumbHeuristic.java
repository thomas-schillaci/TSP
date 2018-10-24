package tsp.deliverable;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

/**
 * A dumb heuristic setting the city of index i at the i-th position
 * Used to test other heuristics
 */
public class DumbHeuristic extends AHeuristic {

    public DumbHeuristic(Instance instance) throws Exception {
        super(instance, "Dumb Heuristic");
    }

    @Override
    public void solve() throws Exception {
        for (int i = 0; i < m_instance.getNbCities(); i++) m_solution.setCityPosition(i, i);
        m_solution.setCityPosition(0, m_instance.getNbCities());
    }
}

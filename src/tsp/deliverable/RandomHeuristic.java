package tsp.deliverable;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

import java.util.ArrayList;

/**
 * Creates a random solution
 * Used to test the other heuristics
 */
public class RandomHeuristic extends AHeuristic {

    public RandomHeuristic(Instance instance) throws Exception {
        super(instance, "Random Heuristic");
    }

    @Override
    public void solve() throws Exception {
        m_solution = new Solution(m_instance);
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < m_instance.getNbCities(); i++) indices.add(i);
        int index = 0;
        while (indices.size() > 0) {
            int city = (int) (Math.random() * indices.size());
            try {
                m_solution.setCityPosition(indices.get(city), index++);
            } catch (Exception e) {
                e.printStackTrace();
            }
            indices.remove(city);
        }
    }
}

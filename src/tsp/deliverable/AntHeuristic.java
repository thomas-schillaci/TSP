package tsp.deliverable;

import com.sun.org.apache.bcel.internal.generic.POP;
import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Ant Heuristic
 */
public class AntHeuristic extends AHeuristic {

    private final int POPULATION = 1000;
    private final float ALPHA = 4.0f, BETA = 0.9f, GAMMA = 1.0f, Q = 100.0f, RHO = 0.2f;
    private float[][] intensities;
    private long lastObjectiveValue = -1;

    public AntHeuristic(Instance instance) throws Exception {
        this(null, instance);
    }

    /**
     * This constructor is used to replace the first iteration
     * Instead of letting the ants selecting *randomly* the path (with a weight regarding the nearest cities)
     * The heuristic is initialized with ants following the path of an other heuristic startingHeuristic
     */
    public AntHeuristic(AHeuristic startingHeuristic, Instance instance) throws Exception {
        super(instance, "Ant Heuristic");
        intensities = new float[m_instance.getNbCities()][m_instance.getNbCities()];
        if(startingHeuristic!=null) {
            Solution[] ants = new Solution[POPULATION];
            for (int i = 0; i < POPULATION; i++) {
                startingHeuristic.solve();
                ants[i] = startingHeuristic.getSolution();
            }
            layPheromones(closePaths(ants));
        }
    }

    @Override
    public void solve() throws Exception {
        findBest(layPheromones(closePaths(travel(initAnts()))));
    }

    /**
     * The list of ants is initialized with the city 0 as the starting one
     */
    private Solution[] initAnts() {
        Solution[] ants = new Solution[POPULATION];

        try {
            for (int k = 0; k < POPULATION; k++) {
                ants[k] = new Solution(m_instance);
                ants[k].setCityPosition(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ants;
    }

    /**
     * Each ant from ants creates its own path taking into consideration the nearest cities and the pheromones
     */
    private Solution[] travel(Solution[] ants) {
        try {
            List<Integer>[] toVisit = new ArrayList[POPULATION];
            for (int k = 0; k < POPULATION; k++) {
                toVisit[k] = new ArrayList<>(m_instance.getNbCities() - 1);
                for (int j = 1; j < m_instance.getNbCities(); j++) toVisit[k].add(j);
            }
            for (int i = 1; i < m_instance.getNbCities(); i++) {
                for (int k = 0; k < POPULATION; k++) {
                    float weights[] = new float[toVisit[k].size()];
                    float totalWeight = 0;
                    int c1 = ants[k].getCity(i - 1);
                    for (int j = 0; j < toVisit[k].size(); j++) {
                        int c2 = toVisit[k].get(j);
                        float tau = intensities[c1][c2];
                        float n = 1.0f / m_instance.getDistances(c1, c2);
                        weights[j] = GAMMA + (float) ((tau != 0 ? Math.pow(tau, ALPHA) : 1.0f) * Math.pow(n, BETA));
                        totalWeight += weights[j];
                    }
                    float weight = (float) (Math.random() * totalWeight);
                    int index = -1;
                    float sum = 0;
                    for (int j = 0; j < toVisit[k].size(); j++) {
                        sum += weights[j];
                        if (sum >= weight) {
                            index = j;
                            break;
                        }
                    }
                    ants[k].setCityPosition(toVisit[k].get(index), i);
                    toVisit[k].remove(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ants;
    }

    /**
     * This methods links the last city as being the same one as the first one
     * And evaluates the objective value of each ant
     */
    private Solution[] closePaths(Solution[] ants) {
        try {
            for (Solution solution : ants) {
                solution.setCityPosition(0, m_instance.getNbCities());
                solution.evaluate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ants;
    }

    /**
     * Each ants lays pheromones on its path considering its objective value (the smaller the objective value, the more the quantity of pheromones)
     */
    private Solution[] layPheromones(Solution[] ants) {
        try {
            for (int k = 0; k < POPULATION; k++) {
                long length = ants[k].getObjectiveValue();
                for (int i = 1; i < m_instance.getNbCities(); i++) {
                    int c1 = ants[k].getCity(i - 1), c2 = ants[k].getCity(i);
                    intensities[c1][c2] += Q / length;
                    intensities[c2][c1] += Q / length;
                }
            }

            for (int i = 0; i < m_instance.getNbCities(); i++) {
                for (int j = 0; j < m_instance.getNbCities(); j++) {
                    intensities[i][j] *= 1.0f - RHO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ants;
    }

    /**
     * Finds the ant with the best objective value and sets it as the m_solution
     */
    private void findBest(Solution[] ants) {
        if(m_solution.getObjectiveValue()==0) m_solution=ants[0];
        for (int i = 0; i < ants.length; i++)
            if (ants[i].getObjectiveValue() < m_solution.getObjectiveValue()) m_solution = ants[i];

        lastObjectiveValue = m_solution.getObjectiveValue();
    }

    public long getLastObjectiveValue() {
        return lastObjectiveValue;
    }

}

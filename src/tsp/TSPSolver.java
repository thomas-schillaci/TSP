package tsp;


import tsp.deliverable.BestInsertionHeuristic;
import tsp.deliverable.GeneticHeuristic;
import tsp.deliverable.LocalSearchHeuristic;
import tsp.deliverable.NearestNeighborHeuristic;
import tsp.heuristic.AHeuristic;

/**
 * This class is the place where you should enter your code and from which you can create your own objects.
 * <p>
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * <p>
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes:
 * - #m_instance :  the Instance object which contains the problem data
 * - #m_solution : the Solution object to modify. This object will store the result of the program.
 * - #m_timeLimit : the maximum time limit (in seconds) given to the program.
 *
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 */
public class TSPSolver {

    // -----------------------------
    // ----- ATTRIBUTS -------------
    // -----------------------------

    /**
     * The Solution that will be returned by the program.
     */
    private Solution m_solution;

    /**
     * The Instance of the problem.
     */
    private Instance m_instance;

    /**
     * Time given to solve the problem.
     */
    private long m_timeLimit;


    // -----------------------------
    // ----- CONSTRUCTOR -----------
    // -----------------------------

    /**
     * Creates an object of the class Solution for the problem data loaded in Instance
     *
     * @param instance  the instance of the problem
     * @param timeLimit the time limit in seconds
     */
    public TSPSolver(Instance instance, long timeLimit) {
        m_instance = instance;
        m_solution = new Solution(m_instance);
        m_timeLimit = timeLimit;
    }

    // -----------------------------
    // ----- METHODS ---------------
    // -----------------------------

    /**
     * Solves the problem by:
     * Choosing a starting heuristic with regards of the problem's size
     * For small problems (n<=72), uses a BestInsertionHeuristic
     * For large problems (n>72), uses the best of 1000 NearestNeighborsHeuristic
     * Then passes the starting heuristic through the LocalSearchHeuristic to obtain a solution
     * Then tries to decrease the objective value by passing the solution through the GeneticHeuristic
     * Then shifts the cities to have city zero as index zero
     */
    public void solve() throws Exception {
        long startTime = System.currentTimeMillis();

        AHeuristic heuristic = (m_instance.getNbCities() > 72 ? new NearestNeighborHeuristic(m_instance) : new BestInsertionHeuristic(m_instance));
        heuristic.solve();

        heuristic = new LocalSearchHeuristic(m_instance, heuristic.getSolution());
        heuristic.solve();

        heuristic = new GeneticHeuristic(heuristic.getSolution(), m_instance);
        long maxElapsedTime = -1;
        long now = System.currentTimeMillis();
        while (now - startTime < m_timeLimit * 1000 - 5 * maxElapsedTime) {
            long start = System.currentTimeMillis();
            heuristic.solve();
            now = System.currentTimeMillis();
            if (now - start > maxElapsedTime) maxElapsedTime = now - start;
        }

        m_solution = heuristic.getSolution();
        int i = 0;
        while (m_solution.getCity(i) != 0) {
            i++;
        }
        Solution solution = m_solution.copy();
        m_solution.setCityPosition(0, 0);
        m_solution.setCityPosition(0, m_instance.getNbCities());
        for (int j = i + 1; j < m_instance.getNbCities(); j++) {
            m_solution.setCityPosition(solution.getCity(j), j - i);
        }
        for (int k = 0; k < i; k++) {
            m_solution.setCityPosition(solution.getCity(k), k + m_instance.getNbCities() - i);
        }
    }

    // -----------------------------
    // ----- GETTERS / SETTERS -----
    // -----------------------------

    /**
     * @return the problem Solution
     */
    public Solution getSolution() {
        return m_solution;
    }

    /**
     * @return problem data
     */
    public Instance getInstance() {
        return m_instance;
    }

    /**
     * @return Time given to solve the problem
     */
    public long getTimeLimit() {
        return m_timeLimit;
    }

    /**
     * Initializes the problem solution with a new Solution object (the old one will be deleted).
     *
     * @param solution : new solution
     */
    public void setSolution(Solution solution) {
        this.m_solution = solution;
    }

    /**
     * Sets the problem data
     *
     * @param instance the Instance object which contains the data.
     */
    public void setInstance(Instance instance) {
        this.m_instance = instance;
    }

    /**
     * Sets the time limit (in seconds).
     *
     * @param time time given to solve the problem
     */
    public void setTimeLimit(long time) {
        this.m_timeLimit = time;
    }

}

package tsp;

import tsp.deliverable.*;
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
     * **TODO** Modify this method to solve the problem.
     * <p>
     * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
     * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
     * <p>
     * You can print using the error output (`System.err.print()` or `System.err.println()`).
     * <p>
     * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
     * <p>
     * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
     *
     * @throws Exception may return some error, in particular if some vertices index are wrong.
     */
    public void solve() throws Exception {
        long startTime = System.currentTimeMillis();

        AHeuristic heuristic = new GeneticHeuristic(new DumbHeuristic(m_instance), m_instance);
        while (System.currentTimeMillis() - startTime < m_timeLimit * 1000) {
            heuristic.solve();
            System.out.println(((GeneticHeuristic) heuristic).getLastObjectiveValue());
        }

        m_solution = heuristic.getSolution();
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

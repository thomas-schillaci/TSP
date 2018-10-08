package tsp.deliverable;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

import java.util.ArrayList;

/**
 * A genetic-algorithm-approach
 * The n-th call on the solve function provides the best solution from the n-th generation
 * The idea is to create a GeneticHeuristic object within the TSPSolver.call function
 * and call the solve function as much as possible within the m_timeLimit time span
 * POPULATION stores the number of solutions calculated within one solve call
 * MUTATION_RATE represents the probability for a gene to undergo the mutate function
 *
 * @author thomas-schillaci
 */
public class GeneticHeuristic extends AHeuristic {

    private final int POPULATION = 100 * Runtime.getRuntime().availableProcessors();
    private final float MUTATION_RATE = 0.001f;

    private Solution[] chromosomes;
    private long lastObjectiveValue = -1;

    public GeneticHeuristic(Instance m_instance) throws Exception {
        super(m_instance, "Genetic Heuristic");
        chromosomes = new Solution[POPULATION];
        for (int i = 0; i < POPULATION; i++) chromosomes[i] = createChromosome();
        evaluate();
    }

    /**
     * Generates a new generation
     *
     * @return the best solution from the new generation
     */
    @Override
    public void solve() {
        reproduction();
        mutation();
        closeChromosomes();
        evaluate();

        Solution best = chromosomes[0];
        for (Solution chromosome : chromosomes) if (getScore(chromosome) > getScore(best)) best = chromosome;
        lastObjectiveValue = best.getObjectiveValue();
        System.out.println(getScore(best));

        m_solution = best;
    }

    private Solution createChromosome() {
        Solution chromosome = new Solution(m_instance);
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < m_instance.getNbCities(); i++) indices.add(i);
        int index = 0;
        while (indices.size() > 0) {
            int city = (int) (Math.random() * indices.size());
            try {
                chromosome.setCityPosition(indices.get(city), index++);
            } catch (Exception e) {
                e.printStackTrace();
            }
            indices.remove(city);
        }

        return chromosome;
    }

    /**
     * Creates a new generation by selecting randomly chromosomes regarding how they scored
     */
    private void reproduction() {
        float totalScore = 0;
        for (Solution chromosome : chromosomes) totalScore += getScore(chromosome);
        Solution[] newGeneration = new Solution[POPULATION];
        if(totalScore<0) System.out.println("Negative total score");

        Solution best = chromosomes[0];
        for (Solution chromosome : chromosomes) if (getScore(chromosome) > getScore(best)) best = chromosome;

        newGeneration[0] = best.copy();

        for (int i = 1; i < newGeneration.length; i++) {
            int index = (int) (Math.random() * totalScore);
            float count = 0;
            for (Solution chromosome : chromosomes) {
                count += getScore(chromosome);
                if (count >= index) {
                    newGeneration[i] = chromosome.copy();
                    break;
                }
            }
        }
        chromosomes = newGeneration;
    }

    /**
     * Selects the genes to mutate
     */
    private void mutation() {
        for (int j = 1; j < chromosomes.length; j++) {
            Solution chromosome = chromosomes[j];
            for (int i = 0; i < m_instance.getNbCities() - 1; i++) {
                if (Math.random() > MUTATION_RATE) continue;
                mutate(chromosome, i);
            }
        }
    }

    /**
     * Switches the index-th gene with another gene within the chromosome
     */
    private void mutate(Solution chromosome, int index) {
        int other = (int) (Math.random() * chromosome.getInstance().getNbCities());
        if (other == index) other = (other + 1) % chromosome.getInstance().getNbCities();

        try {
            int tmp = chromosome.getCity(index);
            chromosome.setCityPosition(chromosome.getCity(other), index);
            chromosome.setCityPosition(tmp, other);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Links the first and last indices
     */
    private void closeChromosomes() {
        try {
            for (Solution chromosome : chromosomes)
                chromosome.setCityPosition(chromosome.getCity(0), m_instance.getNbCities());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void evaluate() {
        for (Solution chromosome : chromosomes) {
            try {
                chromosome.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return a score based on the chromosome's objective value
     * The higher the score, the better
     */
    private double getScore(Solution chromosome) {
        double score = Math.pow(10_000_000.0f / chromosome.getObjectiveValue(),4);
        return score;
    }

    public long getLastObjectiveValue() {
        return lastObjectiveValue;
    }

}
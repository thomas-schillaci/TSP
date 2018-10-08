package tsp.deliverable;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;
import tsp.metaheuristic.AMetaheuristic;

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

/* idees = MUTATION_RATE evolutif : par rapport au temps ou score ; partir d'une solution intuitive et peu couteuse plutôt que l'aléa; éviter les mutations des suites de villes qui scorent le moins */
public class GeneticHeuristic extends AHeuristic {

    private final int POPULATION = 500;
    private final float MUTATION_RATE = 0.01f;

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
        int totalScore = 0;
        for (Solution chromosome : chromosomes) totalScore += getScore(chromosome);
        Solution[] newGeneration = new Solution[POPULATION];

        Solution best = chromosomes[0];
        for (Solution chromosome : chromosomes) if (getScore(chromosome) > getScore(best)) best = chromosome;

        newGeneration[0]=best;

        for (int i = 1; i < newGeneration.length; i++) {
            int index = (int) (Math.random() * totalScore);
            int count = 0;
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
        for (Solution chromosome : chromosomes) {
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
        for (Solution chromosome : chromosomes) {
            try {
                chromosome.setCityPosition(chromosome.getCity(0), m_instance.getNbCities());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    private long getScore(Solution chromosome) {
        long score = (long) Math.exp(10_000_000.0f/chromosome.getObjectiveValue());
        return score;
    }

    public long getLastObjectiveValue() {
        return lastObjectiveValue;
    }

}
package tsp.deliverable;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A genetic-algorithm-approach
 * The n-th call on the solve function provides the best solution from the n-th generation
 * The idea is to create a GeneticHeuristic object within the TSPSolver.call function
 * and call the solve function as much as possible within the m_timeLimit time span
 * POPULATION stores the number of solutions calculated within one solve call
 * MUTATION_RATE represents the probability for a gene to undergo the mutate function
 */
public class GeneticHeuristic extends AHeuristic {

    private final int POPULATION = 800;
    private final float MUTATION_RATE = 0.1f;
    private int swathLength;

    private Solution[] chromosomes;
    private long lastObjectiveValue = -1;
    private float meanDistance=0;

    public GeneticHeuristic(Solution solution, Instance m_instance) throws Exception {
        this(new AHeuristic(m_instance,"") {
            @Override
            public void solve() throws Exception {}
            @Override
            public Solution getSolution() {
                return solution;
            }
        },m_instance);
    }

    private GeneticHeuristic(AHeuristic startingHeuristic, Instance m_instance) throws Exception {
        super(m_instance, "Genetic Heuristic");
        chromosomes = new Solution[POPULATION];
        for (int i = 0; i < chromosomes.length; i++) {
            startingHeuristic.solve();
            chromosomes[i] = startingHeuristic.getSolution();
        }
        evaluate();
        swathLength = m_instance.getNbCities() / 2;
        for (int i = 0; i < m_instance.getNbCities(); i++)
            for (int j = 0; j < m_instance.getNbCities(); j++)
                meanDistance += (float) m_instance.getDistances(i, j) / m_instance.getNbCities() / m_instance.getNbCities();
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

    /**
     * Creates a new generation by selecting randomly chromosomes regarding how they scored
     */
    private void reproduction() {
        float totalScore = 0;
        for (Solution chromosome : chromosomes) totalScore += getScore(chromosome);

        Solution best = chromosomes[0];
        for (Solution chromosome : chromosomes) if (getScore(chromosome) > getScore(best)) best = chromosome;
        Solution[] newGeneration = new Solution[POPULATION];
        newGeneration[0] = best.copy();
        for (int i = 1; i < newGeneration.length; i++)
            newGeneration[i] = (Math.random()<1.0?directCopy(totalScore):pmx(totalScore));

        chromosomes = newGeneration;
    }

    /**
     * Selects *randomly* a father (considering its weight) and sets it as the child
     */
    private Solution directCopy(float totalScore) {
        int index = (int) (Math.random() * totalScore);
        float count = 0;
        for (Solution chromosome : chromosomes) {
            count += getScore(chromosome);
            if (count >= index) return chromosome.copy();
        }
        return null;
    }

    /**
     * The PMX method
     * Can be used instead of directCopy
     */
    private Solution pmx(float totalScore) {
        int index = (int) (Math.random() * totalScore);
        int i1 = -1, i2;
        float count = 0;
        for (int i = 0; i < chromosomes.length; i++) {
            count += getScore(chromosomes[i]);
            if (count >= index) {
                i1 = i;
                break;
            }
        }

        i2 = i1;
        while (i2 == i1) {
            count=0;
            index = (int) (Math.random() * totalScore);
            for (int i = 0; i < chromosomes.length; i++) {
                count += getScore(chromosomes[i]);
                if (count >= index) {
                    i2 = i;
                    break;
                }
            }
        }

        Solution s1 = chromosomes[i1];
        Solution s2 = chromosomes[i2];
        Solution child = s1.copy();

        int startingIndex = (int) ((m_instance.getNbCities() - swathLength) * Math.random());
        ArrayList<Integer> done = new ArrayList<>();
        ArrayList<Integer> swath = new ArrayList<>();

        try {
            for (int i = startingIndex; i < startingIndex + swathLength; i++){
                swath.add(s1.getCity(i));
                done.add(s1.getCity(i));
            }

            int toInsert=-1;
            boolean inserted=true;
            for (int i = startingIndex; i < startingIndex + swathLength; i++) {
                if (done.contains(s2.getCity(i))) continue;
                if(inserted) {
                    toInsert = s2.getCity(i);
                    inserted=false;
                }
                index = s1.getCity(i);
                for (int j = 0; j < m_instance.getNbCities(); j++) {
                    if(s2.getCity(j)==index) {
                        index=j;
                        break;
                    }
                }
                if (swath.contains(s2.getCity(index))) {
                    i = index;
                    break;
                } else {
                    done.add(s2.getCity(index));
                    child.setCityPosition(toInsert, index);
                    inserted = true;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return child;
}

    /**
     * Selects the genes to mutate
     */
    private void mutation() {
        for (int j = 1; j < chromosomes.length; j++) {
            Solution chromosome = chromosomes[j];
            for (int i = 0; i < m_instance.getNbCities()-1; i++) {
                if (Math.random() > MUTATION_RATE) continue;
                int other = (int) (Math.random() * chromosome.getInstance().getNbCities());
                if (other == i) other = (other + 1) % chromosome.getInstance().getNbCities();
                chromosomes[j] = (Math.random() < 0.0f ? swap(i, other, chromosome) : twoOpt(i, other, chromosome));
            }
        }
    }

    /**
     * Swaps the i-th and the j-th genes
     */
    private Solution swap(int i, int j, Solution chromosome) {
        try {
            int tmp = chromosome.getCity(i);
            chromosome.setCityPosition(chromosome.getCity(j), i);
            chromosome.setCityPosition(tmp, j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chromosome;
    }

    /**
     * The 2-opt method
     * Can be used instead of swap
     */
    private Solution twoOpt(int i, int j, Solution chromosome) {
        Solution copy = chromosome.copy();
        try {
            for(int k=i;k<=j;k++) copy.setCityPosition(chromosome.getCity(j - k + i), k);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copy;
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

    /**
     * Evaluates each solution
     */
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
        double score = Math.pow(meanDistance * 100.0f / chromosome.getObjectiveValue(), 4);
        return score;
    }

    public long getLastObjectiveValue() {
        return lastObjectiveValue;
    }

}
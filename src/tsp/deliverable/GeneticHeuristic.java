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
 *
 * @author thomas-schillaci
 */
public class GeneticHeuristic extends AHeuristic {

    private final int POPULATION = 800;
    private final float MUTATION_RATE = 0.001f;
    private int swathLength;

    private Solution[] chromosomes;
    private long lastObjectiveValue = -1;

    public GeneticHeuristic(Instance m_instance) throws Exception {
        super(m_instance, "Genetic Heuristic");
        chromosomes = new Solution[POPULATION];
        for (int i = 0; i < POPULATION; i++) {
            AHeuristic heuristic = new RandomHeuristic(m_instance);
            heuristic.solve();
            chromosomes[i] = heuristic.getSolution();
        }
        evaluate();
        swathLength = m_instance.getNbCities() / 2;
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
            newGeneration[i] = (Math.random()<0.01f?directCopy(newGeneration, totalScore):pmx(newGeneration,totalScore));
//            newGeneration[i] = pmx(newGeneration, totalScore);

        chromosomes = newGeneration;
    }

    private Solution directCopy(Solution[] newGeneration, float totalScore) {
        int index = (int) (Math.random() * totalScore);
        float count = 0;
        for (Solution chromosome : chromosomes) {
            count += getScore(chromosome);
            if (count >= index) return chromosome.copy();
        }
        return null;
    }

    private Solution pmx(Solution[] newGeneration, float totalScore) {
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
        double score = Math.pow(m_instance.getNbCities() * m_instance.getNbCities() * 60.0f / chromosome.getObjectiveValue(), 4);
        return score;
    }

    public long getLastObjectiveValue() {
        return lastObjectiveValue;
    }

}
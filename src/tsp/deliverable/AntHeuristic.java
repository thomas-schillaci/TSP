package tsp.deliverable;

import com.sun.org.apache.bcel.internal.generic.POP;
import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AntHeuristic extends AHeuristic {

    private final int POPULATION = 10;
    private final float ALPHA=1.0f,BETA=1.0f,GAMMA=1.0f,Q=1.0f, RHO=0.25f;
    private float[][] intensities;

    public AntHeuristic(Instance instance) throws Exception {
        super(instance, "Ant Heuristic");
        intensities = new float[m_instance.getNbCities()][m_instance.getNbCities()];
    }

    @Override
    public void solve() throws Exception {
        Solution[] ants = new Solution[POPULATION];
        List<Integer>[] toVisit = new ArrayList[POPULATION];
        for (int k = 0; k < POPULATION; k++) {
            ants[k] = new Solution(m_instance);
            ants[k].setCityPosition(0, 0);
            toVisit[k] = new ArrayList<>(m_instance.getNbCities()-1);
            for (int j = 1; j < m_instance.getNbCities(); j++) toVisit[k].add(j);
        }
        for (int i = 1; i < m_instance.getNbCities(); i++) {
            for (int k = 0; k < POPULATION; k++) {
                float weights[] = new float[toVisit[k].size()];
                float totalWeight=0;
                int c1 = ants[k].getCity(i-1);
                for (int j = 0; j < toVisit[k].size(); j++) {
                    int c2 = toVisit[k].get(j);
                    float tau = intensities[c1][c2];
                    float n = 1.0f / m_instance.getDistances(c1, c2);
                    weights[j]= GAMMA + (float) ((tau!=0?Math.pow(tau,ALPHA):1.0f)*Math.pow(n,BETA));
                    totalWeight += weights[j];
                }
                float weight = (float) (Math.random()*totalWeight);
                int index=-1;
                float sum=0;
                for (int j = 0; j < toVisit[k].size(); j++) {
                    sum+=weights[j];
                    if(sum>=weight) {
                        index=j;
                        break;
                    }
                }
                ants[k].setCityPosition(toVisit[k].get(index),i);
                toVisit[k].remove(index);
            }
        }

        try {
            for (Solution solution : ants) {
                solution.setCityPosition(0, m_instance.getNbCities());
                solution.evaluate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int k = 0; k < POPULATION; k++) {
            long length = ants[k].getObjectiveValue();
            for (int i = 1; i < m_instance.getNbCities(); i++) {
                int c1 = ants[k].getCity(i-1), c2=ants[k].getCity(i);
                intensities[c1][c2]+=Q/length;
                intensities[c2][c1]+=Q/length;
            }
        }

        for (int i = 0; i < m_instance.getNbCities(); i++) {
            for (int j = 0; j < m_instance.getNbCities(); j++) {
                intensities[i][j]*=1.0f-RHO;
            }
        }

        m_solution = ants[0];
        for (int i = 1; i < ants.length; i++)
            if (ants[i].getObjectiveValue() < m_solution.getObjectiveValue()) m_solution = ants[i];
    }

}

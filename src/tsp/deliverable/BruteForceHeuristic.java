package tsp.deliverable;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteForceHeuristic extends AHeuristic {

    public BruteForceHeuristic(Instance instance) throws Exception {
        super(instance, "Brute force Heuristic");
    }

    @Override
    public void solve() throws Exception {
        if(fact(m_instance.getNbCities())<0) throw new Exception("Too many cities to evaluate by brute force");
        m_solution.setObjectiveValue(Long.MAX_VALUE);

        for (int[] ints : heap(m_instance.getNbCities())) {
            Solution solution = new Solution(m_instance);
            for (int i = 0; i < ints.length; i++) solution.setCityPosition(i, ints[i]);
            solution.setCityPosition(solution.getCity(0), m_instance.getNbCities());
            solution.evaluate();
            if(solution.getObjectiveValue()<m_solution.getObjectiveValue()) m_solution = solution.copy();
        }
    }

    private ArrayList<int[]> heap(int size) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++) a[i] = i;
        ArrayList<int[]> res = new ArrayList<>();
        heap(a, size, res);
        return res;
    }

    private void heap(int[] a, int size, ArrayList<int[]> res) {
        if(size==1) res.add(Arrays.copyOf(a, a.length));
        for (int i = 0; i < size; i++) {
            heap(a, size - 1,res);
            if(size%2==1) {
                int tmp=a[0];
                a[0]=a[size-1];
                a[size-1]=tmp;
            }
            else {
                int tmp=a[i];
                a[i]=a[size-1];
                a[size-1]=tmp;
            }
        }
    }

    private int fact(int n) {
        int res=1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

}

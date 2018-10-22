package tsp.deliverable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.neighborhood.ANeighborhood;

public class LSA_ShiftNeighborhood extends ANeighborhood {
	static final int length = 5;

	public LSA_ShiftNeighborhood(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Solution> getNeighborhood(Solution sol) throws Exception {
		// TODO Auto-generated method stub
		List<Solution> solutions = new ArrayList<Solution>();
		for(int i = 1; i<super.m_instance.getNbCities()-length; i++) {
			for(int j = 1; j<i;j++) {
				solutions.add(shift(i,j,sol));
			}
			for(int k = i+1;k<super.m_instance.getNbCities()-length;k++) {
				solutions.add(shift(i,k,sol));
			}
		}
		return solutions;
	}
	
	public static Solution shift(int i, int j, Solution sol) throws Exception {
		if (i<j) {
		Solution sol2 = sol.copy();
		int communes =Math.max(0, length+i-j);
		for(int k = i; k<i+length;k++) {
			sol2.setCityPosition(sol.getCity(k), k-i+j);
		}
		for(int k = j+communes; k<j+length;k++) {
			sol2.setCityPosition(sol.getCity(k), i+(k-j-communes));
		}
		return sol2;
		}
		else {return shift(j,i,sol);}
	}
	
//	public static void main(String[] args) {
//		String filename = null;
//		long max_time = 60;
//		boolean verbose = false;
//		boolean graphical = false;
//		int typeInstance = 0;
//
//		// Parse commande line
//		for (int i = 0; i < args.length; i++) {
//			if (args[i].compareTo("-help") == 0) {
//				System.err.println("The Traveling Salesman Problem");
//				System.err.println("Program parameters:");
//				System.err.println("command: java Main [options] dataFile");
//				System.err.println("Options:");
//				System.err.println("\t-help\t: prints this parameter description");
//				System.err.println("\t-t\t\t: maximum number of seconds given to the algorithm (int)");
//				System.err.println("\t-g\t\t: graphical output of the solution");
//				System.err.println("\t-v\t\t: trace level");
//				return;
//
//			} else if (args[i].compareTo("-v") == 0) {
//				verbose = true;
//			} else if (args[i].compareTo("-g") == 0) {
//				graphical = true;
//			} else if (args[i].compareTo("-t") == 0) {
//				try {
//					max_time = Integer.parseInt(args[++i]);
//				} catch (Exception e) {
//					System.out.println("Error: The time given for -t is not a valid integer value.");
//					System.exit(1);
//				}
//			} else if (args[i].compareTo("-i") == 0) {
//				try {
//					typeInstance = Integer.parseInt(args[++i]);
//				} catch (Exception e) {
//					System.out.println("error : the type of instance is not a valid type");
//					System.exit(1);
//				}
//			} else {
//				if (filename != null) {
//					System.err.println("Error: There is a problem in the program parameters.");
//					System.err.println("Value " + args[i] + " is not a valid parameter.");
//					System.exit(1);
//				}
//				filename = args[i];
//			}
//		}
//
//		// Create and solve problem
//		try {
//			
//			// Read data
//			Instance data = new Instance(filename, typeInstance);
//		Solution sol = new Solution(data);
//        sol.setCityPosition(0, 0);
//        sol.setCityPosition(0, data.getNbCities());
//        for(int i=1; i<data.getNbCities();i++) sol.setCityPosition(i, i);
//        Solution sol2 = shift(4, 3, sol);
//        String s2="";
//        String s="";
//        for(int i=0; i<data.getNbCities();i++) {
//        	s2+=sol.getCity(i)+"-";
//        	s+=sol2.getCity(i)+"-";
//        }
//        s2+=sol.getCity(data.getNbCities());
//        s+=sol2.getCity(data.getNbCities());
//        System.out.println(s2);
//        System.out.println(s);
//		}catch (IOException e) {
//			System.err.println("Error: An error has been met when reading the input file: " + e.getMessage());
//			System.exit(1);
//		} catch (Exception e) {
//			System.err.println("Error: " + e.getMessage());
//			System.err.println();
//			e.printStackTrace(System.err);
//			System.exit(1);
//		}
//	}

}

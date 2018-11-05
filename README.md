# Travelling Salesman Problem solver in Java

## TODO
- Read D.L. Applegate, R.E. Bixby, V. 
       Chvatal
       , and W.J. Cook. 
       The Traveling Salesman 
       Problem: A Computational Study.
       Princeton 
       University
       Press
       , 2006.
- Read G. 
       Reinelt
       . 
       The Traveling Salesman: Computational Solutions for TSP 
       Applications, volume 
       840 of Lecture Notes in Computer Science (LNCS). 
       Springer
       -
       Verlag, Berlin Heidelberg, Heidelberg, 1994.
       http://www.springerlink.com/content/br49t9h4l7mm/?MUD=MP
- Study Ants Algorithm
- Bruteforce heuristic
- Nearest neighbor
- Local search heuristic
- Add condition not to exceed computation time limit
- Create a function of the number of cities deciding which heuristics to use + tables of results
- Implement multi-threading

- Read https://www.researchgate.net/profile/Hicham_El_Hassani2/publication/275966237_Comparaison_de_l'optimisation_par_colonies_de_fourmis_et_des_Algorithmes_Genetiques_pour_la_resolution_du_probleme_du_voyageur_de_commerce/links/5627e45408ae22b1702d9aa3/Comparaison-de-loptimisation-par-colonies-de-fourmis-et-des-Algorithmes-Genetiques-pour-la-resolution-du-probleme-du-voyageur-de-commerce.pdf?origin=publication_detail

- 2-opt https://web.tuke.sk/fei-cit/butka/hop/htsp.pdf 
- Local search ameliorations :https://pdfs.semanticscholar.org/ab7c/c83bb513a91b06f6c8bc3b9da7f60cbbaee5.pdf 

## Solutions considered
- local search (swapNeighborhood seems useless)
- genetic algorithm (ants algo.)
- nearest neighbor
- Branch and bound (40-60 cities)
- V-opt heuristics

## What is effective
- Nearest Neighbor for all sizes | real quick and effective considering its speed - one shot
- Local Search with varying neighborhoods (2-opt, swap, shift...) for n <=200 | quality of solutions and speed - incremental
- Brute Force for small iterations ~10 | finds the optimum
- Genetic Heuristic with varying reproductions (direct, pmx) for all sizes | always decreases the objective value

## Final heuristic
- Using of a starting heuristic (BestInsertion for n<72 and NearestNeighbor otherwise)
- Take the solution through the LocalSearchHeuristic
- Improve it with the GeneticHeuristic during the remaning time

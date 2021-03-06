package fape.core.planning.search.strategies.plans;

import fape.core.planning.states.State;

public interface Heuristic {

    /** Current cost of the partial plan */
    double g(State st);

    /** Estimation of the cost to go */
    double h(State st);

    /** Estimation of the distance to go (in terms of search) */
    double hc(State st);
}

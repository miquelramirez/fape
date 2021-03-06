package fape.core.planning.search.strategies.plans;

import fape.core.planning.states.State;

class MakespanComp extends PartialPlanComparator {
    @Override
    public String shortName() {
        return "makespan";
    }

    @Override
    public double g(State st) {
        return st.getMakespan();
    }

    @Override
    public double h(State st) {
        return 0;
    }

    @Override
    public double hc(State st) {
        return 0;
    }
}

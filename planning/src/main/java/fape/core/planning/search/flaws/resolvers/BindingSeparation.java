package fape.core.planning.search.flaws.resolvers;

import fape.core.planning.planner.Planner;
import fape.core.planning.states.State;
import planstack.anml.model.concrete.VarRef;

/**
 * Simply adds a difference constraint between the two variables.
 */
public class BindingSeparation implements Resolver {

    public final VarRef a, b;

    public BindingSeparation(VarRef a, VarRef b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean apply(State st, Planner planner, boolean isFastForwarding) {
        st.addSeparationConstraint(a, b);

        return true;
    }

    @Override
    public int compareWithSameClass(Resolver e) {
        assert e instanceof BindingSeparation;
        BindingSeparation o = (BindingSeparation) e;
        if(a != o.a)
            return a.id() - o.a.id();
        assert b != o.b : "Comparing two identical resolvers.";
        return b.id() - o.b.id();
    }
}

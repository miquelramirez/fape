package fape.core.planning.search.strategies.flaws;


import fape.core.planning.preprocessing.AbstractionHierarchy;
import fape.core.planning.search.Flaw;
import fape.core.planning.search.SupportOption;
import fape.core.planning.search.UnsupportedDatabase;
import fape.core.planning.states.State;
import fape.core.planning.temporaldatabases.TemporalDatabase;
import fape.test.ProblemGenerator;
import fape.util.Pair;
import planstack.anml.model.AnmlProblem;
import planstack.anml.model.concrete.VarRef;

import java.util.*;

/**
 * A comparator for flaws and their resolvers that uses lifted abstraction hierarchies.
 *
 * The ordering places unsupported databases first. Other flaws are left unordered
 * Unsupported databases are ordered according to their level in the abstraction hierarchy.
 *
 */
public class AbsHierarchyComp implements FlawComparator {

    private final State state;
    private AbstractionHierarchy hierarchy;

    /**
     * Map AnmlProblems to a pair (n, h) where h is the abstraction hierarchy for the nth revision
     * of the problem.
     */
    static Map<AnmlProblem, Pair<Integer, AbstractionHierarchy>> hierarchies = new HashMap<>();

    public AbsHierarchyComp(State st) {
        this.state = st;
        if(!hierarchies.containsKey(st.pb) || hierarchies.get(st.pb).value1 != st.pb.modifiers().size()) {
            hierarchies.put(st.pb, new Pair(st.pb.modifiers().size(), new AbstractionHierarchy(st.pb)));
        }
        this.hierarchy = hierarchies.get(st.pb).value2;
    }

    private int priority(Pair<Flaw, List<SupportOption>> flawAndResolvers) {
        Flaw flaw = flawAndResolvers.value1;
        List<SupportOption> options = flawAndResolvers.value2;
        int level;
        if(flaw instanceof UnsupportedDatabase) {
            TemporalDatabase consumer = ((UnsupportedDatabase) flaw).consumer;
            String predicate = consumer.stateVariable.func().name();
            List<String> argTypes = new LinkedList<>();
            for(VarRef argVar : consumer.stateVariable.jArgs()) {
                argTypes.add(state.conNet.typeOf(argVar));
            }
            String valueType = state.conNet.typeOf(consumer.GetGlobalConsumeValue());
            level = hierarchy.getLevel(predicate, argTypes, valueType);
        } else {
            level = Integer.MAX_VALUE;
        }

        return level;
    }


    @Override
    public int compare(Pair<Flaw, List<SupportOption>> o1, Pair<Flaw, List<SupportOption>> o2) {
        return priority(o1) - priority(o2);
    }

    @Override
    public String shortName() {
        return "abs";
    }
};
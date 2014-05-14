package fape.core.planning.planner;

import fape.core.planning.preprocessing.AbstractionHierarchy;
import fape.core.planning.search.Flaw;
import fape.core.planning.search.FlawSelector;
import fape.core.planning.search.SupportOption;
import fape.core.planning.states.State;
import fape.util.Pair;
import planstack.anml.parser.ParseResult;

import java.util.Comparator;
import java.util.List;

public class BaseDTGAbsHier extends BaseDTG {

    AbstractionHierarchy hierarchy = null;

    @Override
    public String shortName() {
        return "base+dtg+abs";
    }

    @Override
    public Comparator<Pair<Flaw, List<SupportOption>>> flawComparator(State st) {
        return new FlawSelector(hierarchy, st);
    }


    @Override
    public boolean ForceFact(ParseResult anml) {
        super.ForceFact(anml);
        this.hierarchy = new AbstractionHierarchy(this.pb);
        return true;
    }
}
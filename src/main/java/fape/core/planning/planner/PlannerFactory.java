package fape.core.planning.planner;

import fape.core.planning.Planner;
import fape.core.planning.states.State;
import fape.exceptions.FAPEException;
import planstack.constraints.stnu.Controllability;

public class PlannerFactory {

    public static final String defaultPlanner = "taskcond";
    public static final String[] defaultPlanSelStrategies = { "soca" };
    public static final String[] defaultFlawSelStrategies = { "hf", "ogf", "abs", "lcf", "eogf" };
    public static final Controllability defaultControllabilityStrategy = Controllability.PSEUDO_CONTROLLABILITY;

    public static PlanningOptions defaultOptions() {
        PlanningOptions options = new PlanningOptions(defaultPlanSelStrategies, defaultFlawSelStrategies);
        return options;
    }

    public static APlanner getDefaultPlanner() {
        return getPlanner(defaultPlanner, defaultOptions(), defaultControllabilityStrategy);
    }

    public static APlanner getPlanner(String name, PlanningOptions options, Controllability controllability) {
        switch (name) {
            case "htn":
            case "base+dtg":
                return new BaseDTG(controllability, options);
            case "base":
                return new Planner(controllability, options);
            case "taskcond":
                return new TaskConditionPlanner(controllability,options);
            default:
                throw new FAPEException("Unknown planner name: "+name);
        }
    }

    public static APlanner getPlannerFromInitialState(String name, State state, PlanningOptions options) {
         switch (name) {
            case "htn":
            case "base+dtg":
                return new BaseDTG(state, options);
            case "base":
                return new Planner(state, options);
            case "taskcond":
                return new TaskConditionPlanner(state, options);
            default:
                throw new FAPEException("Unknown planner name: "+name);
        }
    }



    public static APlanner getPlanner(String name) {
        return getPlanner(name, defaultOptions(), defaultControllabilityStrategy);
    }

    public static APlanner getPlanner(String name, State state) {
        return getPlannerFromInitialState(name, state, defaultOptions());
    }
}

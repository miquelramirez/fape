package fape.core.planning.planner;


/**
 * THis planner reasons on task decomposition. When an action has a subtask, instead of inserting
 * an action right away, it adds an ActionCondition to the task network.
 *
 * This results in a flaw that can be solved by making an action support this condition. This is done
 * by unifying all there arguments and time points. To reflect that, a support link from the action condition
 * to the action is added in the task network.
 */
public class TaskConditionPlanner extends BaseDTG {

    @Override
    public String shortName() {
        return "taskcond";
    }

    @Override
    public boolean useActionConditions() {
        return true;
    }
}
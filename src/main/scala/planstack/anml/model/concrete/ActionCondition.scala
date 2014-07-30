package planstack.anml.model.concrete

import planstack.anml.ANMLException

import scala.collection.JavaConversions._

import planstack.anml.model.{Context, AnmlProblem}
import planstack.anml.model.abs.{AbstractActionRef, AbstractAction}

/** An action condition states that some task with the same name, parameters and time points must
  * be present in the plan.
  *
  * An action condition has an action name, a set of parameters and two timepoints.
  * It can be fulfilled/supported by an action with the same whose parameters and
  * time points are equal to those of the action condition.
  */
class ActionCondition(val abs :AbstractAction, val args :java.util.List[VarRef], val parent:Option[Action]) extends TemporalInterval {

  override def toString = abs.name+args.toString
}


object ActionCondition {

  def apply(pb :AnmlProblem, ref :AbstractActionRef, context :Context, parentActionOpt :Option[Action]) : ActionCondition = {
    val args = seqAsJavaList(ref.args.map(context.getGlobalVar(_)))
    val abs = pb.abstractActions.find(_.name == ref.name) match {
      case Some(absAct) => absAct
      case None => throw new ANMLException("Unable to find action: "+ref.name)
    }
    val ac = new ActionCondition(abs, args, parentActionOpt)
    context.addActionCondition(ref.localId, ac)
    ac
  }
}
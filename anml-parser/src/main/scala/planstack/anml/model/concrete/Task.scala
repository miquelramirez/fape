package planstack.anml.model.concrete

import planstack.anml.model.abs.AbstractTask
import planstack.anml.model.{TMethods, TInteger, AnmlProblem, Context}

import scala.collection.JavaConversions._

/** A task states that some action with the same name, parameters and time points must
  * be present in the plan.
  *
  * An action condition has an action name, a set of parameters and two timepoints.
  * It can be fulfilled/supported by an action with the same whose parameters and
  * time points are equal to those of the action condition.
  */
class Task(val name: String, val args :java.util.List[VarRef], val parent:Option[Action], refCounter: RefCounter) extends TemporalInterval {

  def getLabel: String = System.identityHashCode(this).toString

  override val start : TPRef = new TPRef(refCounter)
  override val end : TPRef = new TPRef(refCounter)

  val groundSupportersVar = new VarRef(TInteger, refCounter, Label(getLabel,"ground-supporters"))
  val methodSupportersVar = new VarRef(TMethods, refCounter, Label(getLabel,"method-supporters"))

  override def toString = name+args.toString
}


object Task {

  def apply(pb :AnmlProblem, ref :AbstractTask, context :Context, parentActionOpt :Option[Action], refCounter: RefCounter) : Task = {
    val args = seqAsJavaList(ref.args.map(context.getGlobalVar(_)))
    val ac = new Task(ref.name, args, parentActionOpt, refCounter)
    context.addActionCondition(ref.localId, ac)
    ac
  }
}
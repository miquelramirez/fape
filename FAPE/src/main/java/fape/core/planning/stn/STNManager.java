package fape.core.planning.stn;


import planstack.anml.model.TPRef;
import planstack.anml.model.VarRef;
import planstack.constraints.stn.STN;
import planstack.constraints.stn.STNIncBellmanFord;
import fape.util.TinyLogger;
import fape.util.Pair;

import java.util.HashMap;

public class STNManager {

    public final STN stn;
    public final HashMap<TPRef, Integer> ids;

    /**
     * Creates a new empty STN
     */
    public STNManager() {
        stn =  new STNIncBellmanFord();
        ids = new HashMap<>();
    }

    public STNManager(STNManager toCopy) {
        stn = toCopy.stn.cc();
        ids = new HashMap<>(toCopy.ids);
    }

    /** Creates and records a new ID for a timepoint
     *
     * @param tp Reference of the timepoint.
     * @return ID of the timepoint in the STN.
     */
    public int recordTimePoint(TPRef tp) {
        assert !ids.containsKey(tp) : "TimePoint "+tp+" is already recorded.";
        int id = stn.addVar();
        ids.put(tp, id);
        return id;
    }

    /** Returns the id of a timepoint int the stn */
    private int id(TPRef tp) {
        assert ids.containsKey(tp) : "TimePoint not declared: "+tp;
        return ids.get(tp);
    }

    /**
     *
     * @param a
     * @param b
     */
    public final void EnforceBefore(TPRef a, TPRef b) {
        TinyLogger.LogInfo("Adding temporal constraint: "+a+" < "+b);
        stn.enforceBefore(id(a), id(b));
    }

    /**
     * Enforces that b must happens at least minDelay after a
     * @param a
     * @param b
     * @param minDelay
     */
    public final void EnforceDelay(TPRef a, TPRef b, int minDelay) {
        stn.addConstraint(id(b), id(a), -minDelay);
    }

    /**
     * Adds a temporal constraint a --[min, max]--> b
     * @param a
     * @param b
     * @param min
     * @param max
     * @return
     */
    public final boolean EnforceConstraint(TPRef a, TPRef b, int min, int max) {
        STN backup = stn.cc();
        TinyLogger.LogInfo("Adding temporal constraint: "+a+" ["+min+","+max+"] "+b);
        stn.enforceInterval(id(a), id(b), min, max);
        if(stn.consistent()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove the edge (u,v) in the constraint graph. The edge (v,u) is not removed.
     * Performs a consistency check from scratch (expensive, try to use removeConstraints if you are to remove
     * more than one constraint)
     * @param u
     * @param v
     * @return true if the STN is consistent after removal
     */
    public boolean RemoveConstraint(int u, int v) {
        return stn.removeConstraint(u, v);
    }

    /**
     * For all pairs, remove the corresponding directed edge in the constraint graph. After every pair is removed,
     * a consistency check is performed from scratch.
     * @param ps
     * @return true if the STN is consistent after removal
     */
    public boolean RemoveConstraints(Pair<TPRef, TPRef>... ps) {
        for(Pair<TPRef, TPRef> p : ps) {
            stn.removeConstraintUnsafe(id(p.value1), id(p.value2));
        }
        return stn.checkConsistencyFromScratch();
    }

    /**
     *
     * @param first
     * @param second
     * @return
     */
    public final boolean CanBeBefore(TPRef first, TPRef second) {
        boolean ret = stn.canBeBefore(id(first), id(second));
        TinyLogger.LogInfo("STN: "+first+" can occur before "+second);
        return ret;
    }

    /**
     *
     * @return
     */
    public STNManager DeepCopy() {
        return new STNManager(this);
    }

    public String Report() {
        String ret = "size: "+this.stn.size()+"\n";
        ret += stn.g().edges().mkString("\n");
        return ret;
    }

    public void AssertConsistent(){
        if(!stn.consistent()) {
            throw new RuntimeException("Inconsistent STN:");
        }
    }


    public long GetEarliestStartTime(TPRef start) {
        return stn.earliestStart(id(start));
    }

    public boolean IsConsistent() {
        return stn.consistent();
    }
}
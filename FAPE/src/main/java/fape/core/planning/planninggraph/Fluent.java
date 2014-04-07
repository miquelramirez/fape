package fape.core.planning.planninggraph;

import planstack.anml.model.Function;
import planstack.anml.model.concrete.VarRef;

import java.util.LinkedList;
import java.util.List;

public class Fluent implements PGNode {
    final Function f;
    final List<VarRef> params;
    final VarRef value;

    int hashVal;


    public Fluent(Function f, List<VarRef> params, VarRef value) {
        this.f = f;
        this.params = new LinkedList<VarRef>(params);
        this.value = value;

        int i = 0;
        hashVal = 0;
        hashVal += f.hashCode() * 42*i++;
        hashVal += value.hashCode() * 42*i++;
        for(VarRef param : params) {
            hashVal += param.hashCode() * 42*i++;
        }
    }

    @Override
    public String toString() {
        return f.name() + params.toString() +"=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Fluent))
            return false;
        Fluent of = (Fluent) o;
        if(!f.equals(of.f))
            return false;

        for(int i=0 ; i<params.size() ; i++) {
            if(!params.get(i).equals(of.params.get(i)))
                return false;
        }

        return value.equals(of.value);
    }

    @Override
    public int hashCode() {
        return hashVal;
    }

}
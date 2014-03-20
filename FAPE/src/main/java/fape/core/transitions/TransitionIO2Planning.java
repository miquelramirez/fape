///*
// * Author:  Filip Dvořák <filip.dvorak@runbox.com>
// *
// * Copyright (c) 2013 Filip Dvořák <filip.dvorak@runbox.com>, all rights reserved
// *
// * Publishing, providing further or using this program is prohibited
// * without previous written permission of the author. Publishing or providing
// * further the contents of this file is prohibited without previous written
// * permission of the author.
// */
//package fape.core.transitions;
//
//import fape.core.execution.model.*;
//import fape.core.execution.model.Action;
//import fape.core.execution.model.statements.Statement;
//import fape.core.planning.Planner;
//import fape.core.planning.constraints.ConstraintNetworkManager;
//import fape.core.planning.model.*;
//import fape.core.planning.states.State;
//import fape.core.planning.stn.TemporalVariable;
//import fape.core.planning.temporaldatabases.TemporalDatabase;
//import fape.core.planning.temporaldatabases.events.TemporalEvent;
//import fape.core.planning.temporaldatabases.events.propositional.PersistenceEvent;
//import fape.core.planning.temporaldatabases.events.propositional.TransitionEvent;
//import fape.core.planning.temporaldatabases.events.resources.ConditionEvent;
//import fape.core.planning.temporaldatabases.events.resources.ConsumeEvent;
//import fape.core.planning.temporaldatabases.events.resources.ProduceEvent;
//import fape.core.planning.temporaldatabases.events.resources.SetEvent;
//import fape.exceptions.FAPEException;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//
//
//
//
///**
// * realizes the transfers between different models
// *
// * @author FD
// */
//public class TransitionIO2Planning {
//
//    /**
//     * taking an instance of some type, decompose it into state variables
//     *
//     * @param qualifyingName
//     * @param name
//     * @param type
//     * @param types
//     * @param rootType
//     * @return
//     */
//    public static List<fape.core.planning.model.StateVariable> decomposeInstance(String qualifyingName, String name, String type, TypeManager types, String rootType, boolean firstLevel) {
//        LinkedList<fape.core.planning.model.StateVariable> ret = new LinkedList<>();
//        if (types.containsType(type)) {
//            Type tp = types.getType(type);
//            if (firstLevel) {
//                for (String nm : tp.contents.keySet()) {
//                    ret.addAll(decomposeInstance(qualifyingName + name + ".", nm, tp.contents.get(nm), types, rootType, false));
//                }
//            }
//            // we yet need to add a type of itself, if this is not the top level
//            if (!"".equals(qualifyingName)) {
//                StateVariableEnum var = new StateVariableEnum();
//                var.name = qualifyingName + name;
//                var.type = type;
//                var.typeDerivationName = rootType + qualifyingName.substring(qualifyingName.indexOf(".")) + name;
//                ret.add(var);
//            }
//
//        } else {
//            StateVariable var;
//            switch (type) {
//                case "boolean":
//                    var = new StateVariableBoolean();
//                    break;
//                case "float":
//                    var = new StateVariableFloat();
//                    break;
//                case "integer":
//                    var = new StateVariableInteger();
//                    break;
//                default:
//                    throw new FAPEException("Error: Unknown type: " + type);
//            }
//            var.name = qualifyingName + name;
//            var.type = type;
//            var.typeDerivationName = rootType + qualifyingName.substring(qualifyingName.indexOf(".")) + name;
//            ret.add(var);
//        }
//        return ret;
//    }
//
//
//    public static TemporalEvent ProduceTemporalEvent(Statement s, ParameterizedStateVariable stateVariable, VariableRef from, VariableRef to) {
//        TemporalEvent ev = null;
//
//        if (s.operator == null) {
//            throw new FAPEException(null);
//        }
//        switch (s.operator) {
//            /* TODO: We don't support resources for now
//            case ":produce":
//                ProduceEvent eve = new ProduceEvent();
//                eve.howMuch = s.GetResourceValue();
//                ev = eve;
//                break;
//            case ":consume":
//                ConsumeEvent eve2 = new ConsumeEvent();
//                eve2.howMuch = s.GetResourceValue();
//                ev = eve2;
//                break;
//                */
//            case ":=":
//                if (s.IsResourceRelated()) {
//                    throw new FAPEException("Resources are not supported"); /* TODO
//                    SetEvent eve3 = new SetEvent();
//                    eve3.howMuch = s.GetResourceValue();
//                    ev = eve3; */
//                } else {
//                    TransitionEvent eve4 = new TransitionEvent(stateVariable, new VariableRef("null", "null"), from);
//                    ev = eve4;
//                }
//                break;
//            case "==":
//                if (s.IsResourceRelated()) {
//                    throw new UnsupportedOperationException();
//                } else {
//                    if (s.from != null) {
//                        //this is a transition event
//                        TransitionEvent eve5 = new TransitionEvent(stateVariable, from, to);
//                        ev = eve5;
//                    } else {
//                        //this is a persistence event
//                        PersistenceEvent eve6 = new PersistenceEvent(stateVariable, to);
//                        ev = eve6;
//                    }
//                }
//                break;
//            /** TODO: we don't support resources for now
//            case ">=": {
//                ConditionEvent eve6 = new ConditionEvent();
//                eve6.operator = s.operator;
//                eve6.value = s.value;
//                ev = eve6;
//                break;
//            }
//            case "<=": {
//                ConditionEvent eve6 = new ConditionEvent();
//                eve6.operator = s.operator;
//                eve6.value = s.value;
//                ev = eve6;
//                break;
//            }
//            case ">": {
//                ConditionEvent eve6 = new ConditionEvent();
//                eve6.operator = s.operator;
//                eve6.value = s.value;
//                ev = eve6;
//                break;
//            }
//            case "<": {
//                ConditionEvent eve6 = new ConditionEvent();
//                eve6.operator = s.operator;
//                eve6.value = s.value;
//                ev = eve6;
//                break;
//            } */
//            default:
//                throw new UnsupportedOperationException("Unknown operator.");
//        }
//        return ev;
//    }
//
//
//
//    /**
//     * we take the statement on input and add it into the corresponding state
//     * variable
//     *
//     * @param s
//     * @param v
//     * @param st
//     */
//    public static void InsertStatementIntoState(State st, Statement s, StateVariable v) {
//        // TODO: parameter v is unused
//        if (v == null) {
//            throw new FAPEException("Unknown state variable: " + s.GetVariableName());
//        }
//
//        ParameterizedStateVariable stateVar = new ParameterizedStateVariable(s.leftRef.predicate(), st.getVariableRef(s.leftRef.variable()), st.GetType(s.leftRef));
//
//        // create a new event for the termporal database that corresponds to the
//        // statement
//        VariableRef from = null;
//        VariableRef to = null;
//        if (s.from != null) {
//            from = st.getVariableRef(s.from.GetConstantReference());
//        }
//        if (s.to != null) {
//            to = st.getVariableRef(s.to.GetConstantReference());
//        }
//        TemporalEvent ev = ProduceTemporalEvent(s, stateVar, from, to);
//        // statements at the start of the of the world
//        TemporalVariable tvs = st.tempoNet.getNewTemporalVariable();
//        ev.start = tvs;
//        TemporalVariable tve = st.tempoNet.getNewTemporalVariable();
//        ev.end = tve;
//        st.tempoNet.EnforceBefore(tvs, tve);
//        switch (s.interval.start) {
//            case "TStart":
//                break;
//            case "TEnd":
//                st.tempoNet.EnforceConstraint(tvs, tve, 0, 0);
//                break;
//            default:
//                //number
//                int time = Integer.parseInt(s.interval.start);
//                st.tempoNet.EnforceConstraint(st.tempoNet.GetGlobalStart(), tvs, time, time);
//                break;
//        }
//        switch (s.interval.end) {
//            case "TEnd":
//                break;
//            case "TStart":
//                st.tempoNet.EnforceConstraint(tvs, tve, 0, 0);
//                break;
//            default:
//                //number
//                int time = Integer.parseInt(s.interval.end);
//                st.tempoNet.EnforceConstraint(st.tempoNet.GetGlobalStart(), tve, time, time);
//                break;
//        }
//
//        // create a database containing the event
//        TemporalDatabase db = st.tdb.GetNewDatabase(ev);
//        if (db.isConsumer()) {
//            st.consumers.add(db);
//        }
//    }
//
//    /**
//     *
//     * @param a
//     * @param pb Problem in which the action is defined
//     * @return
//     */
//    public static AbstractAction TransformAction(Action a, Problem pb) {
//        AbstractAction act = new AbstractAction();
//        act.name = a.name;
//        act.params = a.params;
//
//        for (Statement s : a.statements) {
//            String varName = s.GetVariableName();
//            String varType = "";
//            varName = varName.substring(varName.indexOf("."));
//            for (StateVariable sv : pb.vars.values()) {
//                String smallDeriv = sv.typeDerivationName.substring(sv.typeDerivationName.indexOf("."));
//                if (smallDeriv.equals(varName)) {
//                    varType = sv.type;
//                }
//            }
//            if(varType.isEmpty()) {
//                throw new FAPEException("Error: did not find type for state variable of statement "+s);
//            }
//
//            VariableRef from = new VariableRef("null", "");
//            VariableRef to = new VariableRef("null", "");
//            if (s.from != null) {
//                from = new VariableRef(s.from.variable(), act.typeOf(pb, s.from.variable()));
//            }
//            if (s.to != null) {
//                to = new VariableRef(s.to.variable(), act.typeOf(pb, s.to.variable()));
//            }
//
//            VariableRef paramVar = new VariableRef(s.leftRef.variable(), act.typeOf(pb, s.leftRef.variable()));
//            ParameterizedStateVariable sv = new ParameterizedStateVariable(s.leftRef.predicate(), paramVar, varType);
//
//            TemporalEvent tEvent = TransitionIO2Planning.ProduceTemporalEvent(s, sv, from, to);
//
//            AbstractTemporalEvent ev = new AbstractTemporalEvent(tEvent, s.interval, s.leftRef, varType);
//            act.events.add(ev);
//        }
//        act.strongDecompositions = a.strongDecompositions;
//        return act;
//    }
//}
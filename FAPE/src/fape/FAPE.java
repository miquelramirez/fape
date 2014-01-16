package fape;

import fape.core.acting.Actor;
import fape.core.execution.Executor;
import fape.core.execution.Listener;
import fape.core.planning.Planner;
import fape.util.TinyLogger;

/*
 * Author:  Filip Dvořák <filip.dvorak@runbox.com>
 *
 * Copyright (c) 2013 Filip Dvořák <filip.dvorak@runbox.com>, all rights reserved
 *
 * Publishing, providing further or using this program is prohibited
 * without previous written permission of the author. Publishing or providing
 * further the contents of this file is prohibited without previous written
 * permission of the author.
 */
/**
 * starts up all the components and binds them together
 *
 * @author FD
 */
public class FAPE {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Actor a = null;
        Planner p = null;
        Executor e = null;
        Listener l = null;

        try {
            a = new Actor();
            p = new Planner();
            e = new Executor();
            // this is a hack, we do not need listener for planner scenerio testing
            
            //"name of the machine", "who am I talking to", "my name (fape)", "3300"
            //
            l = new Listener("maxc1", "PR2", "FAPE", "3300");
            
            //l.sendMessage("(test message)");

            a.bind(e, p);
            e.bind(a, l);
            l.bind(e);
        }catch(Exception ex){
            System.out.println("FAPE setup failed.");
            throw ex;
        }

        //pushing the initial event
        a.PushEvent(e.ProcessANMLfromFile("C:\\ROOT\\PROJECTS\\fape\\FAPE\\problems\\Dream4.anml"));

        p.Init();
        
        a.run();
    }
}
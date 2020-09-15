import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import bayes.CustomBayesNet;
import bayes.CustomEliminationAsk;
import utils.HeuristicsTypes;
import utils.bnparser.BifReader;

import java.util.List;


public class BNTest {

    public static void main(String[] args) {

        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.minDegree;
        Boolean showGraph = false;
        int waitTime = 100;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        CustomBayesNet net = (CustomBayesNet) BifReader.readBIF(classloader.getResourceAsStream("win95pts.xml"));
//        bayes.CustomBayesNet net = (bayes.CustomBayesNet) BNFactory.constructToothacheCavityCatchNetwork();

        List<RandomVariable> variables = net.getVariablesInTopologicalOrder();

        RandomVariable query1 = variables.get(0);
//        RandomVariable query2 = variables.get(9);
        RandomVariable[] queriesVariables = new RandomVariable[1];
        queriesVariables[0] = query1;
//        queriesVariables[0] = query2;

        RandomVariable evidence1 = variables.get(variables.size() - 2);
        RandomVariable evidence2 = variables.get(variables.size() - 1);
        System.out.println(evidence1.getDomain());
        System.out.println(evidence2.getDomain());
        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[2];
        assignmentPropositions[0] = new AssignmentProposition(evidence1, "Yes");
        assignmentPropositions[1] = new AssignmentProposition(evidence2, "No_Output");

        CustomEliminationAsk customElimination = new CustomEliminationAsk(heuristics, showGraph, waitTime);
        long startTime = System.nanoTime();
        CategoricalDistribution cd = customElimination.ask(queriesVariables, assignmentPropositions, net);
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;

        // Output
        System.out.println("Time elapsed: " + elapsedTime/1000000 + "ms\n");
        System.out.print("P (");
        for(int i = 0; i < queriesVariables.length; i++) {
            if (i > 0)
                System.out.print(" && ");
            System.out.print(queriesVariables[i].getName());
        }
        System.out.print(" | ");
        for(int i = 0; i < assignmentPropositions.length; i++) {
            if (i > 0)
                System.out.print(" && ");
            System.out.print(assignmentPropositions[i].getTermVariable().getName());
        }
        System.out.println(")");

        System.out.println("True: " + cd.getValues()[0]);
        System.out.println("False: " + cd.getValues()[1]);
    }
}

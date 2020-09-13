import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import bayes.factory.BNFactory;
import bayes.CustomBayesNet;
import bayes.CustomEliminationAsk;
import utils.HeuristicsTypes;
import utils.bnparser.BifReader;

import java.util.List;


public class BNTest {

    public static void main(String[] args) {

        // Impostazioni
        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.minFill;
        Boolean showInteractionGraph = false;
        int delay = 1000;

        // Caricamento Rete
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        CustomBayesNet net = (CustomBayesNet) BifReader.readBIF(classloader.getResourceAsStream("sat.xml"));
//        CustomBayesNet net = (CustomBayesNet) BNFactory.constructToothacheCavityCatchNetwork();

        List<RandomVariable> variables = net.getVariablesInTopologicalOrder();

        // Set variabili di query ed evidenza
        RandomVariable query = variables.get(1);
        RandomVariable[] queriesVariables = new RandomVariable[1];
        queriesVariables[0] = query;

        RandomVariable evidence1 = variables.get(variables.size() - 1);
        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[1];
        assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.TRUE);

        System.out.println("evidenza: " + evidence1.getName() + "\nquery: " + query.getName());

        // Elimination Ask
        CustomEliminationAsk customElimination = new CustomEliminationAsk(heuristics, showInteractionGraph, delay);
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

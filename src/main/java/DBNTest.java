import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import custom.CustomDynamicBayesNet;
import custom.CustomEliminationAsk;
import factory.DBNFactory;
import graph.HeuristicsTypes;

import java.util.List;

public class DBNTest {

    public static void main(String[] args) {

        CustomDynamicBayesNet net = (CustomDynamicBayesNet) DBNFactory.getInventedNet3();
        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.reverse;
        Boolean showGraph = false;
        int waitTime = 100;

        CustomEliminationAsk customEliminationAsk = new CustomEliminationAsk(heuristics, showGraph, waitTime);

        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[4];
        List<RandomVariable> variables;
        RandomVariable evidence1, evidence2, evidence3, evidence4;


        final int N = 10;

        long startTime = System.nanoTime();
        for(int i = 0; i < N; i++){
            variables = net.getVariablesInTopologicalOrder();

            evidence1 = variables.get(variables.size() - 1);
            evidence2 = variables.get(variables.size() - 2);
            evidence3 = variables.get(variables.size() - 3);
            evidence4 = variables.get(variables.size() - 4);
            assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.TRUE);
            assignmentPropositions[1] = new AssignmentProposition(evidence2, Boolean.TRUE);
            assignmentPropositions[2] = new AssignmentProposition(evidence3, Boolean.TRUE);
            assignmentPropositions[3] = new AssignmentProposition(evidence4, Boolean.TRUE);

            net.forward(assignmentPropositions, customEliminationAsk);
        }
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;

        System.out.println("Time elapsed: " + elapsedTime/1000000 + "ms\n");
    }
}

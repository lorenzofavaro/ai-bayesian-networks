import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import custom.CustomDynamicBayesNet;
import custom.CustomEliminationAsk;
import factory.DBNFactory;
import graph.HeuristicsTypes;

import java.util.List;

public class DBNTest {

    public static void main(String[] args) {

        CustomDynamicBayesNet net = (CustomDynamicBayesNet) DBNFactory.getUmbrellaWorldNetwork();
        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.minDegree;
        Boolean showGraph = false;
        int waitTime = 100;

        CustomEliminationAsk customEliminationAsk = new CustomEliminationAsk(heuristics, showGraph, waitTime);

        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[1];
        List<RandomVariable> variables;
        RandomVariable evidence1;


        final int N = 10;

        for(int i = 0; i < N; i++){
            variables = net.getVariablesInTopologicalOrder();

            evidence1 = variables.get(variables.size() - 1);
            assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.FALSE);

            net.forward(assignmentPropositions, customEliminationAsk);
        }
    }
}

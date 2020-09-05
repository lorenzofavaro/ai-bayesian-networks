import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import bayes.CustomBayesNet;
import bayes.CustomEliminationAsk;
import bayes.HeuristicsTypes;
import utils.bnparser.BifReader;

import java.util.List;

public class Begin {

    public static void main(String[] args) {
        CustomBayesNet bayesianNetwork = (CustomBayesNet) BifReader.readBIF("src/main/resources/earthquake.xml");
        List<RandomVariable> variables = bayesianNetwork.getVariablesInTopologicalOrder();

        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.reverse;
        Boolean showInteractionGraph = false;
        int delay = 3000;

        CustomEliminationAsk customElimination = new CustomEliminationAsk(heuristics, showInteractionGraph, delay);

        RandomVariable evidence1 = variables.get(2);
        RandomVariable query = variables.get(3);

        RandomVariable[] queriesVariables = new RandomVariable[1];
        queriesVariables[0] = query;

        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[1];
        assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.TRUE);

        // Pruning archi
//        bayesianNetwork.pruneEdges(assignmentPropositions);

        CategoricalDistribution cd = customElimination.ask(queriesVariables, assignmentPropositions, bayesianNetwork);

        // Output

        System.out.print("P( ");
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

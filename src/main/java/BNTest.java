import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import bayes.factory.BNFactory;
import bayes.CustomBayesNet;
import bayes.CustomEliminationAsk;
import utils.HeuristicsTypes;

import java.util.List;

public class BNTest {

    public static void main(String[] args) {
        CustomBayesNet net = (CustomBayesNet) BNFactory.constructCloudySprinklerRainWetGrassSlipperyRoadNetwork();
        List<RandomVariable> variables = net.getVariablesInTopologicalOrder();

        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.reverse;
        Boolean showInteractionGraph = false;
        int delay = 2000;

        CustomEliminationAsk customElimination = new CustomEliminationAsk(heuristics, showInteractionGraph, delay);

        RandomVariable evidence1 = variables.get(0);
        RandomVariable query = variables.get(3);

        RandomVariable[] queriesVariables = new RandomVariable[1];
        queriesVariables[0] = query;

        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[1];
        assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.FALSE);

        CategoricalDistribution cd = customElimination.ask(queriesVariables, assignmentPropositions, net);


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

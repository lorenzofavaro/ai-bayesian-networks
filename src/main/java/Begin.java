import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import bayes.CustomEliminationAsk;
import bayes.HeuristicsTypes;
import bnparser.BifReader;

import java.util.List;

public class Begin {

    public static void main(String[] args) {
        BayesianNetwork bayesianNetwork = BifReader.readBIF("src/main/resources/earthquake.xml");
        List<RandomVariable> variables = bayesianNetwork.getVariablesInTopologicalOrder();
        for(RandomVariable variable : variables){
            System.out.println(variable.getName());
        }

        HeuristicsTypes.Heuristics heuristics = HeuristicsTypes.Heuristics.minDegree;
        Boolean showMoralGraph = true;
        int delay = 1000;

        CustomEliminationAsk customElimination = new CustomEliminationAsk(heuristics, showMoralGraph, delay);

        RandomVariable query = variables.get(3);
        RandomVariable evidence1 = variables.get(2);
        System.out.println("query: " + query + "\nevidence1: " + evidence1);

        RandomVariable[] queriesVariables = new RandomVariable[1];
        queriesVariables[0] = query;

        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[1];
        assignmentPropositions[0] = new AssignmentProposition(evidence1, true);

        CategoricalDistribution cd = customElimination.eliminationAsk(queriesVariables, assignmentPropositions, bayesianNetwork);

//        MoralGraph moralGraph = new MoralGraph(bayesianNetwork, variables, heuristics);
//        moralGraph.display();
    }
}

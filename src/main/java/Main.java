import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import bayes.CustomEliminationAsk;
import bayes.Inferences;
import bnparser.BifReader;
import utils.MoralGraph;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        BayesianNetwork bayesianNetwork = BifReader.readBIF("src/main/resources/earthquake.xml");
        List<RandomVariable> variables = bayesianNetwork.getVariablesInTopologicalOrder();

        Inferences.Heuristics heuristics = Inferences.Heuristics.inverse;
        Boolean removeIrrelevantRVs = true;
        Boolean showMoralGraph = false;
        int delay = 100;

        CustomEliminationAsk inference = new CustomEliminationAsk(heuristics, removeIrrelevantRVs, showMoralGraph, delay);

        RandomVariable query = variables.get(0);
        RandomVariable evidence1 = variables.get(variables.size() - 1);

//        MoralGraph moralGraph = new MoralGraph(bayesianNetwork, variables, heuristics);
//        moralGraph.display();
    }
}

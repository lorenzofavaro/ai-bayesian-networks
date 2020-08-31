import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import bnparser.BifReader;
import utils.MoralGraph;

import java.util.List;

public class Prova {

    public static void main(String[] args) {
        BayesianNetwork bayesianNetwork = BifReader.readBIF("src/main/resources/cow.xml");
        List<RandomVariable> variables = bayesianNetwork.getVariablesInTopologicalOrder();

        MoralGraph moralGraph = new MoralGraph(bayesianNetwork, variables);
        moralGraph.display();
    }
}

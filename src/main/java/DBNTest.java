import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import bayes.CustomDynamicBayesNet;
import bayes.factory.DBNFactory;

import java.util.List;

public class DBNTest {

    public static void main(String[] args) {

        CustomDynamicBayesNet net = (CustomDynamicBayesNet) DBNFactory.getUmbrellaWorldNetwork();
        List<RandomVariable> variables = net.getVariablesInTopologicalOrder();

        RandomVariable evidence1 = variables.get(variables.size() - 1);

        AssignmentProposition[] assignmentPropositions = new AssignmentProposition[1];
        assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.FALSE);

        net.forward(assignmentPropositions);

        variables = net.getVariablesInTopologicalOrder();
        evidence1 = variables.get(variables.size() - 1);
        assignmentPropositions[0] = new AssignmentProposition(evidence1, Boolean.TRUE);

        net.forward(assignmentPropositions);
    }
}

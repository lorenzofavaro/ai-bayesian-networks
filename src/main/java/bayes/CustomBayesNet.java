package bayes;

import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.ArrayList;
import java.util.List;


public class CustomBayesNet extends BayesNet {

    public CustomBayesNet(Node... rootNodes) {
        super(rootNodes);
    }

    public void pruneEdges(AssignmentProposition[] evidences) {

        for (AssignmentProposition assignment : evidences) {

            RandomVariable assignmentVariable = assignment.getTermVariable();
            Node assignmentNode = getNode(assignmentVariable);

            for (Node n : assignmentNode.getChildren()) {

                // Calcolo nuova CPT
                Factor factor = ((FullCPTNode) n).getCPT().getFactorFor(assignment);

                // Rimozione nodo di evidenza dai parents
                List<Node> parents = new ArrayList<>();
                for(RandomVariable variable : factor.getArgumentVariables()) {
                    if (variable != n.getRandomVariable()){
                        parents.add(getNode(variable));
                    }
                }

                // Aggiornamento nodo all'interno della bn
                n = new FullCPTNode(n.getRandomVariable(), factor.getValues(), parents.toArray(new Node[0]));
                varToNodeMap.replace(n.getRandomVariable(), n);
            }
        }
    }

}

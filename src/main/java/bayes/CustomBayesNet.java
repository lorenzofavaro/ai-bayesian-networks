package bayes;

import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.*;


public class CustomBayesNet extends BayesNet {

    public CustomBayesNet(Node... rootNodes) {
        super(rootNodes);
    }

    public void pruneEdges(AssignmentProposition[] evidences) {

        for (AssignmentProposition assignment : evidences) {

            RandomVariable assignmentVariable = assignment.getTermVariable();
            CustomNode assignmentNode = (CustomNode) getNode(assignmentVariable);

            for (Node n : assignmentNode.getChildren()) {

                // Calcolo nuova CPT
                Factor factor = ((CustomNode) n).getCPT().getFactorFor(assignment);

                // Rimozione nodo di evidenza dai parents
                List<CustomNode> parents = new ArrayList<>();
                for(RandomVariable variable : factor.getArgumentVariables()) {
                    if (variable != n.getRandomVariable()){
                        parents.add((CustomNode) getNode(variable));
                    }
                }

                // Aggiornamento nodo all'interno della bn
                Set<Node> children = n.getChildren();

                n = new CustomNode(n.getRandomVariable(), factor.getValues(), parents.toArray(new Node[0]));
                ((CustomNode) n).addChildren(children);
                varToNodeMap.replace(n.getRandomVariable(), n);
            }

            assignmentNode.removeChildren();
        }
    }

    protected Map<RandomVariable, Node> getVarToNodeMap (){
        return varToNodeMap;
    }

    protected Set<Node> getRootNodes(){
        return rootNodes;
    }

    public void replaceNode(CustomNode node){
        varToNodeMap.replace(node.getRandomVariable(), node);
    }

}

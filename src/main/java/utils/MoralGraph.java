package utils;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;

import java.util.List;

public class MoralGraph extends SingleGraph {
    private final BayesianNetwork bayesianNetwork;
    private final List<RandomVariable> variables;

    public MoralGraph(BayesianNetwork bayesianNetwork, List<RandomVariable> variables) {
        super("MG", true, false);
        this.bayesianNetwork = bayesianNetwork;
        this.variables = variables;

        addAttribute("ui.stylesheet", "node { size: 10px, 15px; shape: box; fill-color: green; stroke-mode: plain; stroke-color: yellow; text-alignment: above; text-size: 25px;}");

        for (RandomVariable var : variables) {
            MoralNode moralNode = addNode(var.getName());
            moralNode.setRandomVariable(var);
            addAttribute("ui.label", moralNode.getId());
        }

        for (RandomVariable var : variables) {
            for (Node n : bayesianNetwork.getNode(var).getParents()) {
                if (variables.contains(n.getRandomVariable()) && !getNode(var).hasEdgeBetween(getNode(n.getRandomVariable()))) {
                    addEdge(var.getName() + "--" + n.getRandomVariable().getName(), getNode(var), getNode(n.getRandomVariable()), false);
                }
            }
        }
    }

    @Override
    protected <T extends org.graphstream.graph.Node> T addNode_(String sourceId, long timeId, String nodeId) {
        AbstractNode node = getNode(nodeId);
        if (node != null) {
            return (T) node;
        }
        node = new MoralNode(this, nodeId);
        addNodeCallback(node);
        return (T) node;
    }

    private MoralNode getNode(RandomVariable randomVariable) {
        return getNode(randomVariable.getName());
    }

    public static class MoralNode extends SingleNode {
        private RandomVariable randomVariable;

        protected MoralNode(AbstractGraph graph, String id) {
            super(graph, id);
        }

        public final RandomVariable getRandomVariable() {
            return this.randomVariable;
        }

        public final void setRandomVariable(RandomVariable var) {
            this.randomVariable = var;
        }

        Boolean hasEdgeBetween(MoralNode node) {
            return hasEdgeBetween(node.getId());
        }
    }
}

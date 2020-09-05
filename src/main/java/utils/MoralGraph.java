package utils;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import bayes.HeuristicsTypes;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;
import org.graphstream.ui.swingViewer.Viewer;

import java.util.*;

public class MoralGraph extends SingleGraph {

    private final BayesianNetwork bayesianNetwork;
    private final List<RandomVariable> variables;
    private final HeuristicsTypes.Heuristics heuristicsType;
    private final PriorityQueue<MoralNode> variablesQueue;


    public MoralGraph(BayesianNetwork bayesianNetwork, List<RandomVariable> variables, HeuristicsTypes.Heuristics heuristicsType) {
        super("MG", true, false);
        this.bayesianNetwork = bayesianNetwork;
        this.variables = variables;
        this.heuristicsType = heuristicsType;
        variablesQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.calculateHeuristics(this.heuristicsType)));

        addAttribute("ui.stylesheet", "node { size: 10px, 15px; fill-color: blue; stroke-mode: plain; text-alignment: above; text-size: 25px;}");

        setupGraph();
        setupQueue();

    }

    private void setupGraph() {
        for (RandomVariable var : variables) {

            System.out.println("considerando " + var.getName());
            // Aggiunta nodi
            MoralNode moralNode = addNode(var.getName());
            moralNode.setRandomVariable(var);
            moralNode.addAttribute("ui.label", moralNode.getId());

            // Aggiunta archi "parent-parent"
            HashSet<Node> parents = new HashSet<>(bayesianNetwork.getNode(var).getParents());
            parents.removeIf(v -> !variables.contains(v.getRandomVariable()));
            if (parents.size() > 1) {
                bindParents(new ArrayList<>(parents));
            }

            // Aggiunta archi "padre-figlio"
            for (Node n : parents) {
                if (!getNode(var).hasEdgeBetween(getNode(n.getRandomVariable()))) {
                    addEdge(var.getName() + "--" + n.getRandomVariable().getName(), getNode(var), getNode(n.getRandomVariable()), false);
                }
            }

        }
    }

    private void setupQueue() {
        variablesQueue.addAll(getNodeSet());
    }

    private void bindParents(List<Node> parents) {
        for (int i = 0; i < parents.size(); i++) {
            for (int j = 0; j < parents.size(); j++) {

                if (i != j) {

                    RandomVariable var1 = parents.get(i).getRandomVariable();
                    RandomVariable var2 = parents.get(j).getRandomVariable();

                    MoralNode moralNode1 = getNode(var1);
                    MoralNode moralNode2 = getNode(var2);

                    if (!moralNode1.hasEdgeBetween(moralNode2)) {
                        addEdge(var1.getName() + "--" + var2.getName(), moralNode1, moralNode2, false);
                    }

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

    public List<RandomVariable> getOrderedVariables(Boolean showMoralGraph, int delay) {
        ArrayList<RandomVariable> variables = new ArrayList<>();
        Viewer v = null;

        if (showMoralGraph) {
            v = display();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!variablesQueue.isEmpty()) {
            MoralNode head = variablesQueue.poll();
            variables.add(head.getRandomVariable());
            removeNode(head);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (showMoralGraph) {
            v.close();
        }

        return variables;
    }

    public static class MoralNode extends SingleNode {
        private int heuristicsValue = -1;
        private RandomVariable randomVariable;
        private final MoralGraph moralGraph;

        protected MoralNode(AbstractGraph graph, String id) {
            super(graph, id);
            this.moralGraph = (MoralGraph) graph;
        }

        public int calculateHeuristics(HeuristicsTypes.Heuristics heuristicsType) {
            if (heuristicsValue == -1) {
                heuristicsValue = HeuristicsTypes.calculateHeuristics(heuristicsType, this, moralGraph.variables.indexOf(getRandomVariable()));
            }
            return heuristicsValue;
        }

        public final RandomVariable getRandomVariable() {
            return this.randomVariable;
        }

        public final void setRandomVariable(RandomVariable var) {
            this.randomVariable = var;
        }

        Boolean hasEdgeBetween(MoralNode node) {
            return node != null && hasEdgeBetween(node.getId());
        }
    }
}

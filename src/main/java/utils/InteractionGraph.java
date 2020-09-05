package utils;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.FullCPTNode;
import bayes.HeuristicsTypes;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;
import org.graphstream.ui.swingViewer.Viewer;

import java.util.*;

public class InteractionGraph extends SingleGraph {

    private final BayesianNetwork bayesianNetwork;
    private final List<RandomVariable> variables;
    private final HeuristicsTypes.Heuristics heuristicsType;
    private final PriorityQueue<InteractionNode> variablesQueue;


    public InteractionGraph(BayesianNetwork bayesianNetwork, List<RandomVariable> variables, HeuristicsTypes.Heuristics heuristicsType) {
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

            // Aggiunta nodi
            InteractionNode interactionNode = addNode(var.getName());
            interactionNode.setRandomVariable(var);
//            interactionNode.addAttribute("ui.label", interactionNode.getId());


            // Aggiunta archi "padre-padre"
            HashSet<Node> parents = new HashSet<>(bayesianNetwork.getNode(var).getParents());
            parents.removeIf(v -> !variables.contains(v.getRandomVariable()));
            if (parents.size() > 1) {
                bindNodes(new ArrayList<>(parents));
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

    private void bindNodes(List<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {

                if (i != j) {

                    RandomVariable var1 = nodes.get(i).getRandomVariable();
                    RandomVariable var2 = nodes.get(j).getRandomVariable();

                    InteractionNode interactionNode1 = getNode(var1);
                    InteractionNode interactionNode2 = getNode(var2);

                    if (!interactionNode1.hasEdgeBetween(interactionNode2)) {
                        addEdge(var1.getName() + "--" + var2.getName(), interactionNode1, interactionNode2, false);
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
        node = new InteractionNode(this, nodeId);
        addNodeCallback(node);
        return (T) node;
    }

    private InteractionNode getNode(RandomVariable randomVariable) {
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
            InteractionNode head = variablesQueue.poll();
            variables.add(head.getRandomVariable());

            Iterator<InteractionNode> iterator = head.getNeighborNodeIterator();
            List<Node> neighbors = new ArrayList<>();
            while (iterator.hasNext()) {
                neighbors.add(bayesianNetwork.getNode(iterator.next().getRandomVariable()));
            }
            if (neighbors.size() > 1) {
                bindNodes(new ArrayList<>(neighbors));

                iterator = head.getNeighborNodeIterator();
                // Ricalcolo priorità
                while (iterator.hasNext()) {
                    InteractionNode node = iterator.next();
                    variablesQueue.remove(node);
                    variablesQueue.add(node);
                }
            }

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

    public static class InteractionNode extends SingleNode {
        private int heuristicsValue = -1;
        private RandomVariable randomVariable;
        private final InteractionGraph interactionGraph;

        protected InteractionNode(AbstractGraph graph, String id) {
            super(graph, id);
            this.interactionGraph = (InteractionGraph) graph;
        }

        public int calculateHeuristics(HeuristicsTypes.Heuristics heuristicsType) {
            if (heuristicsValue == -1) {
                heuristicsValue = HeuristicsTypes.calculateHeuristics(heuristicsType, this, interactionGraph.variables.indexOf(getRandomVariable()));
            }
            return heuristicsValue;
        }

        public final RandomVariable getRandomVariable() {
            return this.randomVariable;
        }

        public final void setRandomVariable(RandomVariable var) {
            this.randomVariable = var;
        }

        Boolean hasEdgeBetween(InteractionNode node) {
            return node != null && hasEdgeBetween(node.getId());
        }
    }
}

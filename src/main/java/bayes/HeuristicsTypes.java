package bayes;

import utils.InteractionGraph;

import java.util.Iterator;

public class HeuristicsTypes {
    public enum Heuristics {minFill, minDegree, reverse, topological}

    public static int calculateHeuristics(Heuristics heuristics, InteractionGraph.InteractionNode interactionNode, int position) {
        return switch (heuristics) {
            case minFill -> minFillHeuristics(interactionNode);
            case minDegree -> minDegreeHeuristics(interactionNode);
            case reverse -> -position;
            case topological -> position;
        };
    }

    private static int minFillHeuristics(InteractionGraph.InteractionNode interactionNode) {
        Iterator<InteractionGraph.InteractionNode> iterator1 = interactionNode.getNeighborNodeIterator();
        Iterator<InteractionGraph.InteractionNode> iterator2;

        int tot = 0;
        while (iterator1.hasNext()) {
            InteractionGraph.InteractionNode node1 = iterator1.next();

            iterator2 = interactionNode.getNeighborNodeIterator();
            while (iterator2.hasNext()){
                InteractionGraph.InteractionNode node2 = iterator2.next();

                if(node1 != node2 && !node1.hasEdgeBetween(node2)){
                    tot++;
                }
            }
        }

        return tot;
    }

    private static int minDegreeHeuristics(InteractionGraph.InteractionNode interactionNode) {
        return interactionNode.getEdgeSet().size();
    }
}

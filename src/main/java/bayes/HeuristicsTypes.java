package bayes;

import utils.MoralGraph;

import java.util.Iterator;

public class HeuristicsTypes {
    public enum Heuristics {minFill, minDegree, reverse}

    public static int calculateHeuristics(Heuristics heuristics, MoralGraph.MoralNode moralNode, int position) {
        return switch (heuristics) {
            case minFill -> minFillHeuristics(moralNode);
            case minDegree -> minDegreeHeuristics(moralNode);
            case reverse -> -position;
        };
    }

    private static int minFillHeuristics(MoralGraph.MoralNode moralNode) {
        Iterator<MoralGraph.MoralNode> iterator1 = moralNode.getNeighborNodeIterator();
        Iterator<MoralGraph.MoralNode> iterator2;

        int tot = 0;
        while (iterator1.hasNext()) {
            MoralGraph.MoralNode node1 = iterator1.next();

            iterator2 = moralNode.getNeighborNodeIterator();
            while (iterator2.hasNext()){
                MoralGraph.MoralNode node2 = iterator2.next();

                if(node1 != node2 && !node1.hasEdgeBetween(node2)){
                    tot++;
                }
            }
        }

        return tot;
    }

    private static int minDegreeHeuristics(MoralGraph.MoralNode moralNode) {
        return moralNode.getEdgeSet().size();
    }
}

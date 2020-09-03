package bayes;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.proposition.AssignmentProposition;
import utils.MoralGraph;

import java.util.*;

public class CustomEliminationAsk extends EliminationAsk {
    private HeuristicsTypes.Heuristics heuristics;
    private Boolean showMoralGraph;
    private int delay;

    public CustomEliminationAsk(HeuristicsTypes.Heuristics heuristics, Boolean showMoralGraph, int delay) {
        this.heuristics = heuristics;
        this.showMoralGraph = showMoralGraph;
        this.delay = delay;
    }

    @Override
    public CategoricalDistribution eliminationAsk(RandomVariable[] X, AssignmentProposition[] e, BayesianNetwork bn) {
        if (X.length == 0)
            throw new IllegalArgumentException("Query non valida");
        if (!bn.getVariablesInTopologicalOrder().containsAll(Arrays.asList(X.clone())))
            throw new IllegalArgumentException("Variabili non presenti nella rete");

        return super.eliminationAsk(X, e, bn);
    }

    @Override
    protected List<RandomVariable> order(BayesianNetwork bn, Collection<RandomVariable> vars) {
        return new MoralGraph(bn, (List<RandomVariable>) vars, heuristics).getOrderedVariables(showMoralGraph, delay);
    }

    @Override
    protected void calculateVariables(RandomVariable[] X, AssignmentProposition[] e, BayesianNetwork bn, Set<RandomVariable> hidden, Collection<RandomVariable> bnVARS) {
        super.calculateVariables(X, e, bn, hidden, bnVARS);

        List<RandomVariable> mainVariables = new ArrayList<>(Arrays.asList(X));

        for (AssignmentProposition a : e) {
            mainVariables.add(a.getTermVariable());
        }

        // Pruning nodi non antenati di {X U e}
        hidden.removeAll(notAncestorsOf(mainVariables, bn));
        bnVARS.removeAll(notAncestorsOf(mainVariables, bn));

    }

    private Set<RandomVariable> notAncestorsOf(List<RandomVariable> mainVariables, BayesianNetwork bn) {
        Set<RandomVariable> ancestors = AncestorsOf(bn, mainVariables);

        Set<RandomVariable> notAncestors = new HashSet<>(bn.getVariablesInTopologicalOrder());
        notAncestors.removeIf(ancestors::contains);

        return notAncestors;

    }

    private Set<RandomVariable> AncestorsOf(BayesianNetwork bn, List<RandomVariable> ancestors) {

        Set<RandomVariable> result = new HashSet<>(ancestors);

        for (RandomVariable var : ancestors) {

            List<RandomVariable> ancestorsVariables = new ArrayList<>();

            for (Node n: bn.getNode(var).getParents()){
                ancestorsVariables.add(n.getRandomVariable());
            }

            result.addAll(AncestorsOf(bn, ancestorsVariables));
        }

        return result;
    }

}

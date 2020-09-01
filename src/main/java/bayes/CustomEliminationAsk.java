package bayes;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.proposition.AssignmentProposition;
import utils.MoralGraph;

import java.util.Collection;
import java.util.List;

public class CustomEliminationAsk extends EliminationAsk {
    private Inferences.Heuristics heuristics;
    private Boolean removeIrrelevantRVs;
    private Boolean showMoralGraph;
    private int delay;

    public CustomEliminationAsk(Inferences.Heuristics heuristics, Boolean removeIrrelevantRVs, Boolean showMoralGraph, int delay) {
        this.heuristics = heuristics;
        this.removeIrrelevantRVs = removeIrrelevantRVs;
        this.showMoralGraph = showMoralGraph;
        this.delay = delay;
    }

    @Override
    public CategoricalDistribution eliminationAsk(RandomVariable[] X, AssignmentProposition[] e, BayesianNetwork bn) {
        return super.eliminationAsk(X, e, bn);
    }

    @Override
    protected List<RandomVariable> order(BayesianNetwork bn, Collection<RandomVariable> vars) {
        return new MoralGraph(bn, (List<RandomVariable>) vars, heuristics).getVariables(showMoralGraph, delay);
    }
}

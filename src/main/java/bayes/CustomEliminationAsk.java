package bayes;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.proposition.AssignmentProposition;
import utils.HeuristicsTypes;
import utils.InteractionGraph;

import java.util.*;

public class CustomEliminationAsk extends EliminationAsk {
    private final HeuristicsTypes.Heuristics heuristics;
    private final Boolean showInteractionGraph;
    private final int delay;

    public CustomEliminationAsk(HeuristicsTypes.Heuristics heuristics, Boolean showInteractionGraph, int delay) {
        this.heuristics = heuristics;
        this.showInteractionGraph = showInteractionGraph;
        this.delay = delay;
    }

    public CustomEliminationAsk() {
        this.heuristics = HeuristicsTypes.Heuristics.reverse;
        this.showInteractionGraph = false;
        this.delay = 0;
    }

    @Override
    public CategoricalDistribution eliminationAsk(RandomVariable[] X, AssignmentProposition[] e, BayesianNetwork bn) {
        if (X.length == 0)
            throw new IllegalArgumentException("Query non valida");
        if (!bn.getVariablesInTopologicalOrder().containsAll(Arrays.asList(X.clone())))
            throw new IllegalArgumentException("Variabili non presenti nella rete");

        // Pruning archi irrilevanti
        ((CustomBayesNet) bn).pruneEdges(e);

        return super.eliminationAsk(X, e, bn);
    }

    @Override
    public CategoricalDistribution ask(RandomVariable[] X, AssignmentProposition[] observedEvidence, BayesianNetwork bn) {
        return this.eliminationAsk(X, observedEvidence, bn);
    }

    @Override
    protected List<RandomVariable> order(BayesianNetwork bn, Collection<RandomVariable> vars) {
        return new InteractionGraph(bn, (List<RandomVariable>) vars, heuristics).getOrderedVariables(showInteractionGraph, delay);
    }

    @Override
    protected void calculateVariables(RandomVariable[] X, AssignmentProposition[] e, BayesianNetwork bn, Set<RandomVariable> hidden, Collection<RandomVariable> bnVARS) {
        super.calculateVariables(X, e, bn, hidden, bnVARS);

        List<RandomVariable> evidenceVariables = new ArrayList<>();
        for (AssignmentProposition a : e) {
            evidenceVariables.add(a.getTermVariable());
        }

        List<RandomVariable> mainVariables = new ArrayList<>();
        mainVariables.addAll(Arrays.asList(X));
        mainVariables.addAll(evidenceVariables);

        // Pruning nodi non antenati di {X U e}
        hidden.removeAll(notAncestorsOf(mainVariables, bn));

        // Pruning nodi m-separati da X tramite E
        for (RandomVariable x : X) {
            for (AssignmentProposition assignmentProposition : e) {
                RandomVariable evidence = assignmentProposition.getTermVariable();

                if (hidden.removeIf(v -> (isMSeparated(v, x, evidence, bn)))) {
                    CustomNode n = new CustomNode(evidence, getValuesForEvidence(assignmentProposition.getValue()));
                    ((CustomBayesNet) bn).replaceNode(n);
                }
            }
        }

        bnVARS.removeIf(v -> (!mainVariables.contains(v) && !hidden.contains(v)));
    }

    private double[] getValuesForEvidence(Object value) {
        return (Boolean) value ? new double[]{1.0, 0.0} : new double[]{0.0, 1.0};
    }


    private Boolean isMSeparated(RandomVariable variable, RandomVariable x, RandomVariable e, BayesianNetwork bn) {
        List<RandomVariable> query = new ArrayList<>();
        query.add(x);

        List<RandomVariable> evidence = new ArrayList<>();
        evidence.add(e);

        return AncestorsOf(query, bn).contains(e) && AncestorsOf(evidence, bn).contains(variable);
    }

    private Set<RandomVariable> notAncestorsOf(List<RandomVariable> mainVariables, BayesianNetwork bn) {
        Set<RandomVariable> ancestors = AncestorsOf(mainVariables, bn);

        Set<RandomVariable> notAncestors = new HashSet<>(bn.getVariablesInTopologicalOrder());
        notAncestors.removeIf(ancestors::contains);

        return notAncestors;

    }

    private Set<RandomVariable> AncestorsOf(List<RandomVariable> variables, BayesianNetwork bn) {

        Set<RandomVariable> result = new HashSet<>(variables);

        for (RandomVariable var : variables) {

            List<RandomVariable> ancestorsVariables = new ArrayList<>();

            for (Node n : bn.getNode(var).getParents()) {
                ancestorsVariables.add(n.getRandomVariable());
            }

            result.addAll(AncestorsOf(ancestorsVariables, bn));
        }

        return result;
    }

}

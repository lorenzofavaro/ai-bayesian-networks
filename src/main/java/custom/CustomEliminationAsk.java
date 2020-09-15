package custom;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import graph.HeuristicsTypes;
import graph.InteractionGraph;

import java.util.*;

public class CustomEliminationAsk extends EliminationAsk {
    private static final ProbabilityTable _identity = new ProbabilityTable(new double[]{1.0});
    private final HeuristicsTypes.Heuristics heuristics;
    private final Boolean showGraph;
    private final int waitTime;

    public CustomEliminationAsk(HeuristicsTypes.Heuristics heuristics, Boolean showGraph, int waitTime) {
        this.heuristics = heuristics;
        this.showGraph = showGraph;
        this.waitTime = waitTime;
    }

    @Override
    public CategoricalDistribution eliminationAsk(RandomVariable[] X, AssignmentProposition[] e, BayesianNetwork bn) {

        Set<RandomVariable> hidden = new HashSet<>();
        List<RandomVariable> VARS = new ArrayList<>();
        calculateVariables(X, e, bn, hidden, VARS);

        List<Factor> factors = new ArrayList<>();
        for (RandomVariable var : order(bn, VARS)) {
            factors.add(0, makeFactor(var, e, bn));
        }

        for (RandomVariable var : super.order(bn, hidden)) {
            factors = sumOut(var, factors);
        }
        Factor product = pointwiseProduct(factors);
        return ((ProbabilityTable) product.pointwiseProductPOS(_identity, X))
                .normalize();
    }

    private Factor makeFactor(RandomVariable var, AssignmentProposition[] e,
                              BayesianNetwork bn) {

        Node n = bn.getNode(var);
        if (!(n instanceof FiniteNode)) {
            throw new IllegalArgumentException(
                    "Elimination-Ask only works with finite Nodes.");
        }
        FiniteNode fn = (FiniteNode) n;
        List<AssignmentProposition> evidence = new ArrayList<>();
        for (AssignmentProposition ap : e) {
            if (fn.getCPT().contains(ap.getTermVariable())) {
                evidence.add(ap);
            }
        }

        return fn.getCPT().getFactorFor(
                evidence.toArray(new AssignmentProposition[0]));
    }

    private Factor pointwiseProduct(List<Factor> factors) {

        Factor product = factors.get(0);
        for (int i = 1; i < factors.size(); i++) {
            product = product.pointwiseProduct(factors.get(i));
        }

        return product;
    }

    private List<Factor> sumOut(RandomVariable var, List<Factor> factors) {
        List<Factor> summedOutFactors = new ArrayList<>();
        List<Factor> toMultiply = new ArrayList<>();
        for (Factor f : factors) {
            if (f.contains(var)) {
                toMultiply.add(f);
            } else {
                summedOutFactors.add(f);
            }
        }

        summedOutFactors.add(pointwiseProduct(toMultiply).sumOut(var));

        return summedOutFactors;
    }

    @Override
    public CategoricalDistribution ask(RandomVariable[] X, AssignmentProposition[] observedEvidence, BayesianNetwork bn) {

        // Pruning archi irrilevanti
        ((CustomBayesNet) bn).pruneEdges(observedEvidence);

        return this.eliminationAsk(X, observedEvidence, bn);
    }

    @Override
    protected List<RandomVariable> order(BayesianNetwork bn, Collection<RandomVariable> vars) {
        return new InteractionGraph(bn, (List<RandomVariable>) vars, heuristics).getOrderedVariables(showGraph, waitTime);
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

package bayes;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;
import aima.core.util.SetOps;
import com.codepoetics.protonpack.maps.MapStream;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDynamicBayesNet extends CustomBayesNet implements DynamicBayesianNetwork {

    private Set<RandomVariable> X_0 = new LinkedHashSet<>();
    private Set<RandomVariable> X_1 = new LinkedHashSet<>();
    private Set<RandomVariable> E_1 = new LinkedHashSet<>();
    private Map<RandomVariable, RandomVariable> X_0_to_X_1 = new LinkedHashMap<>();
    private Map<RandomVariable, RandomVariable> X_1_to_X_0 = new LinkedHashMap<>();
    private BayesianNetwork priorNetwork;
    private final List<RandomVariable> X_1_VariablesInTopologicalOrder = new ArrayList<>();

    public CustomDynamicBayesNet(BayesianNetwork priorNetwork,
                                 Map<RandomVariable, RandomVariable> X_0_to_X_1,
                                 Set<RandomVariable> E_1, Node... rootNodes) {
        super(rootNodes);

        for (Map.Entry<RandomVariable, RandomVariable> x0_x1 : X_0_to_X_1
                .entrySet()) {
            RandomVariable x0 = x0_x1.getKey();
            RandomVariable x1 = x0_x1.getValue();
            this.X_0.add(x0);
            this.X_1.add(x1);
            this.X_0_to_X_1.put(x0, x1);
            this.X_1_to_X_0.put(x1, x0);
        }
        this.E_1.addAll(E_1);

        Set<RandomVariable> combined = new LinkedHashSet<>();
        combined.addAll(X_0);
        combined.addAll(X_1);
        combined.addAll(E_1);
        if (SetOps.difference(varToNodeMap.keySet(), combined).size() != 0) {
            throw new IllegalArgumentException(
                    "X_0, X_1, and E_1 do not map correctly to the Nodes describing this Dynamic Bayesian Network.");
        }
        this.priorNetwork = priorNetwork;

        X_1_VariablesInTopologicalOrder
                .addAll(getVariablesInTopologicalOrder());
        X_1_VariablesInTopologicalOrder.removeAll(X_0);
        X_1_VariablesInTopologicalOrder.removeAll(E_1);
    }


    public void forward(AssignmentProposition[] propositions, CustomEliminationAsk customEliminationAsk) {
        Map<RandomVariable, Node> newBeliefNodes = new HashMap<>();
        Map<RandomVariable, Node> newStateVariables = new HashMap<>();
        Map<RandomVariable, Node> newEvidences = new HashMap<>();
        Map<RandomVariable, RandomVariable> X1_to_X2 = new HashMap<>();

        List<CustomNode> nextRootNodes = new ArrayList<>();

        // Nuovi Belief Nodes
        for (Map.Entry<RandomVariable, RandomVariable> x0_x1 : X_0_to_X_1.entrySet()) {

            RandomVariable x0 = x0_x1.getKey();
            RandomVariable x1 = x0_x1.getValue();

            CategoricalDistribution cd = customEliminationAsk.ask(new RandomVariable[]{x1}, propositions, this);
            CustomNode node = new CustomNode(x1, cd.getValues());
            newBeliefNodes.put(x1, node);

            CPT cpt = (CPT) getNode(x0).getCPD();
            double[] cptValues = getCPTValues(cpt);

            System.out.println(x0.getName() + ": " + Arrays.toString(cptValues)
                    + " --> " + x1.getName() + ": " + Arrays.toString(cd.getValues()) + "\n");

            if (getNode(x0).getParents().isEmpty()) {
                nextRootNodes.add(node);
            }
        }

        // Nuovi Nodi di Variabili di Stato
        for (RandomVariable oldVariable : X_1) {

            RandVar newVar = getNewVar(oldVariable);
            X1_to_X2.put(oldVariable, newVar);

            List<Node> newParents = new ArrayList<>();

            for (Node parent : getNode(oldVariable).getParents()) {
                RandomVariable parentVar = parent.getRandomVariable();
                newParents.add(newBeliefNodes.get(X_0_to_X_1.get(parentVar)));
            }

            addNewVariable(newStateVariables, oldVariable, newParents, newVar);
        }

        // Nuovi Nodi di Variabili di Evidenza
        for (RandomVariable oldVariable : E_1) {

            Set<Node> parents = getNode(oldVariable).getParents();
            List<Node> newParents = new ArrayList<>();

            RandVar newVar = getNewVar(oldVariable);

            for (Node parent : parents) {
                RandomVariable parentVar = parent.getRandomVariable();
                newParents.add(newStateVariables.get(X1_to_X2.get(parentVar)));
            }

            addNewVariable(newEvidences, oldVariable, newParents, newVar);
        }

        updateNet(newBeliefNodes, newStateVariables, X1_to_X2, newEvidences, nextRootNodes);
    }

    private void addNewVariable(Map<RandomVariable, Node> newVariables, RandomVariable oldVariable, List<Node> newParents, RandVar newVar) {
        CPT cpt = (CPT) getNode(oldVariable).getCPD();
        double[] cptValues = getCPTValues(cpt);

        newVariables.put(newVar, new CustomNode(newVar, cptValues, newParents.toArray(new Node[0])));
    }

    private void updateNet(Map<RandomVariable, Node> newBeliefNodes,
                           Map<RandomVariable, Node> newStateVariables,
                           Map<RandomVariable, RandomVariable> X1_to_X2,
                           Map<RandomVariable, Node> newEvidences,
                           List<CustomNode> nextRootNodes) {

        X_0 = new LinkedHashSet<>(newBeliefNodes.keySet());
        X_1 = new LinkedHashSet<>(newStateVariables.keySet());
        E_1 = newEvidences.keySet();

        X_0_to_X_1 = X1_to_X2;
        X_1_to_X_0 = MapStream.of(X1_to_X2).inverseMapping().collect();

        priorNetwork = new CustomBayesNet(nextRootNodes.toArray(new Node[0]));
        variables = priorNetwork.getVariablesInTopologicalOrder();
        rootNodes = ((CustomBayesNet) priorNetwork).getRootNodes();
        varToNodeMap = ((CustomBayesNet) priorNetwork).getVarToNodeMap();

    }

    private double[] getCPTValues(CPT cpt) {
        int nParents = cpt.getParents().size();

        List<List<Boolean>> combinationsList = getCombinations(nParents);
        Collections.reverse(combinationsList);

        List<Double> values = new ArrayList<>();

        for (List<Boolean> list : combinationsList) {

            Boolean[] parentValues = list.toArray(new Boolean[0]);
            double[] doubles = cpt.getConditioningCase((Object[]) parentValues).getValues();

            for (Double d : doubles) {
                values.add(d);
            }
        }

        return ArrayUtils.toPrimitive((values.toArray(new Double[0])));
    }

    private static List<List<Boolean>> getCombinations(int n) {
        int nComb = 1 << n;

        List<List<Boolean>> combinationsArr = new ArrayList<>(nComb);

        for (int i = 0; i < nComb; i++) {

            ArrayList<Boolean> comb = new ArrayList<>(Collections.nCopies(n, false));
            for (int j = 0; j < n; j++) {
                comb.set(n - 1 - j, ((i >> j) & 1) == 1);
            }

            combinationsArr.add(comb);
        }

        return combinationsArr;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private RandVar getNewVar(RandomVariable oldVariable) {
        Pattern p = Pattern.compile("[0-9]+$");
        String name = oldVariable.getName();

        Matcher m = p.matcher(name);
        m.find();
        int lastNum = Integer.parseInt(m.group());
        lastNum++;
        name = name.replaceAll(p.toString(), String.valueOf(lastNum));

        return new RandVar(name, oldVariable.getDomain());
    }

    @Override
    public BayesianNetwork getPriorNetwork() {
        return priorNetwork;
    }

    @Override
    public Set<RandomVariable> getX_0() {
        return X_0;
    }

    @Override
    public Set<RandomVariable> getX_1() {
        return X_1;
    }

    @Override
    public List<RandomVariable> getX_1_VariablesInTopologicalOrder() {
        return X_1_VariablesInTopologicalOrder;
    }

    @Override
    public Map<RandomVariable, RandomVariable> getX_0_to_X_1() {
        return X_0_to_X_1;
    }

    @Override
    public Map<RandomVariable, RandomVariable> getX_1_to_X_0() {
        return X_1_to_X_0;
    }

    @Override
    public Set<RandomVariable> getE_1() {
        return E_1;
    }

    @Override
    public List<RandomVariable> getVariablesInTopologicalOrder() {
        return super.getVariablesInTopologicalOrder();
    }

    @Override
    public Node getNode(RandomVariable rv) {
        return super.getNode(rv);
    }
}

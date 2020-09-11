package bayes.factory;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.DynamicBayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.util.RandVar;
import bayes.CustomBayesNet;
import bayes.CustomDynamicBayesNet;
import bayes.CustomNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DBNFactory {

    public static DynamicBayesianNetwork getUmbrellaWorldNetwork() {

        final RandVar R_0_RV = new RandVar("R_0", new BooleanDomain());
        final RandVar R_1_RV = new RandVar("R_1", new BooleanDomain());
        final RandVar S_1_RV = new RandVar("S_1", new BooleanDomain());

        FiniteNode r_0 = new CustomNode(R_0_RV, new double[]{0.5, 0.5});
        CustomBayesNet priorNetwork = new CustomBayesNet(r_0);

        // Transition Model
        FiniteNode r_1 = new CustomNode(R_1_RV, new double[]{
                0.7, 0.3,
                0.3, 0.7}, r_0);

        // Sensor Model
        FiniteNode s_1 = new CustomNode(S_1_RV, new double[]{
                0.9, 0.1,
                0.2, 0.8}, r_1);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<>();
        X_0_to_X_1.put(R_0_RV, R_1_RV);

        Set<RandomVariable> E_1 = new HashSet<>();
        E_1.add(S_1_RV);

        return new CustomDynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, r_0);
    }


    public static DynamicBayesianNetwork getComplexDynamicNetworkExample() {

        final RandVar R_0_RV = new RandVar("R_0", new BooleanDomain());
        final RandVar S_0_RV = new RandVar("S_0", new BooleanDomain());

        final RandVar R_1_RV = new RandVar("R_1", new BooleanDomain());
        final RandVar S_1_RV = new RandVar("S_1", new BooleanDomain());

        final RandVar E_1_RV = new RandVar("E_1", new BooleanDomain());

        FiniteNode r_0 = new CustomNode(R_0_RV, new double[]{0.5, 0.5});
        FiniteNode s_0 = new CustomNode(S_0_RV, new double[]{0.5, 0.5});
        CustomBayesNet priorNetwork = new CustomBayesNet(r_0, s_0);

        FiniteNode r_1 = new CustomNode(R_1_RV, new double[]{
                0.1, 0.9,
                0.15, 0.85,
                0.7, 0.3,
                0.8, 0.2}, r_0, s_0);

        FiniteNode s_1 = new CustomNode(S_1_RV, new double[]{
                0.23, 0.77,
                0.4, 0.6,
                0.57, 0.43,
                0.29, 0.71}, r_0, s_0);

        FiniteNode e_1 = new CustomNode(E_1_RV, new double[]{
                0.93, 0.07,
                0.49, 0.51,
                0.87, 0.13,
                0.01, 0.99}, r_1, s_1);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<>();
        X_0_to_X_1.put(R_0_RV, R_1_RV);
        X_0_to_X_1.put(S_0_RV, S_1_RV);

        Set<RandomVariable> E_1 = new HashSet<>();
        E_1.add(E_1_RV);

        return new CustomDynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, r_0, s_0);
    }
}

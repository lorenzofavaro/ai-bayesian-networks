package factory;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.domain.BooleanDomain;
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

        final RandVar R_1_RV = new RandVar("R_11", new BooleanDomain());

        final RandVar E_1_RV = new RandVar("E_1", new BooleanDomain());

        FiniteNode r_0 = new CustomNode(R_0_RV, new double[]{0.5, 0.5});
        CustomBayesNet priorNetwork = new CustomBayesNet(r_0);

        FiniteNode r_1 = new CustomNode(R_1_RV, new double[]{
                0.7, 0.3, 0.3, 0.7}, r_0);

        FiniteNode e_1 = new CustomNode(E_1_RV, new double[]{
                0.9, 0.1, 0.2, 0.8}, r_1);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<>();
        X_0_to_X_1.put(R_0_RV, R_1_RV);

        Set<RandomVariable> E_1 = new HashSet<>();
        E_1.add(E_1_RV);

        return new CustomDynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, r_0);
    }


    public static DynamicBayesianNetwork getInventedNet() {

        final RandVar R_0_RV = new RandVar("R_0", new BooleanDomain());
        final RandVar S_0_RV = new RandVar("S_0", new BooleanDomain());

        final RandVar R_1_RV = new RandVar("R_1", new BooleanDomain());
        final RandVar S_1_RV = new RandVar("S_1", new BooleanDomain());

        final RandVar E_1_RV = new RandVar("E_1", new BooleanDomain());

        FiniteNode r_0 = new CustomNode(R_0_RV, new double[]{0.5, 0.5});
        FiniteNode s_0 = new CustomNode(S_0_RV, new double[]{0.5, 0.5});
        CustomBayesNet priorNetwork = new CustomBayesNet(r_0, s_0);

        FiniteNode r_1 = new CustomNode(R_1_RV, new double[]{
                0.1, 0.9, 0.15, 0.85, 0.7, 0.3, 0.8, 0.2}, r_0, s_0);

        FiniteNode s_1 = new CustomNode(S_1_RV, new double[]{
                0.23, 0.77, 0.4, 0.6, 0.57, 0.43, 0.29, 0.71}, r_0, s_0);

        FiniteNode e_1 = new CustomNode(E_1_RV, new double[]{
                0.93, 0.07, 0.49, 0.51, 0.87, 0.13, 0.01, 0.99}, r_1, s_1);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<>();
        X_0_to_X_1.put(R_0_RV, R_1_RV);
        X_0_to_X_1.put(S_0_RV, S_1_RV);

        Set<RandomVariable> E_1 = new HashSet<>();
        E_1.add(E_1_RV);

        return new CustomDynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, r_0, s_0);
    }

    public static DynamicBayesianNetwork getInventedNet2() {

        final RandVar R_0_RV = new RandVar("R_0", new BooleanDomain());
        final RandVar S_0_RV = new RandVar("S_0", new BooleanDomain());
        final RandVar T_0_RV = new RandVar("T_0", new BooleanDomain());
        final RandVar U_0_RV = new RandVar("U_0", new BooleanDomain());

        final RandVar R_1_RV = new RandVar("R_1", new BooleanDomain());
        final RandVar S_1_RV = new RandVar("S_1", new BooleanDomain());
        final RandVar T_1_RV = new RandVar("T_1", new BooleanDomain());
        final RandVar U_1_RV = new RandVar("U_1", new BooleanDomain());

        final RandVar E_1_RV = new RandVar("E_1", new BooleanDomain());
        final RandVar EE_1_RV = new RandVar("EE_1", new BooleanDomain());

        FiniteNode r_0 = new CustomNode(R_0_RV, new double[]{0.5, 0.5});
        FiniteNode s_0 = new CustomNode(S_0_RV, new double[]{0.5, 0.5});
        FiniteNode t_0 = new CustomNode(T_0_RV, new double[]{0.5, 0.5});
        FiniteNode u_0 = new CustomNode(U_0_RV, new double[]{0.5, 0.5});
        CustomBayesNet priorNetwork = new CustomBayesNet(r_0, s_0, t_0, u_0);

        FiniteNode r_1 = new CustomNode(R_1_RV, new double[]{
                0.1, 0.9, 0.15, 0.85}, r_0);

        FiniteNode s_1 = new CustomNode(S_1_RV, new double[]{
                0.57, 0.43, 0.29, 0.71}, s_0);

        FiniteNode t_1 = new CustomNode(T_1_RV, new double[]{
                0.23, 0.77,
                0.4, 0.6,
                0.57, 0.43,
                0.29, 0.71,
                0.51, 0.49,
                0.97, 0.03,
                0.81, 0.19,
                0.22, 0.78}, r_0, t_0, u_0);

        FiniteNode u_1 = new CustomNode(U_1_RV, new double[]{
                0.23, 0.77, 0.4, 0.6, 0.57, 0.43, 0.29, 0.71}, t_0, u_0);

        FiniteNode e_1 = new CustomNode(E_1_RV, new double[]{
                0.93, 0.07, 0.49, 0.51}, u_1);

        FiniteNode ee_1 = new CustomNode(EE_1_RV, new double[]{
                0.87, 0.13,
                0.01, 0.99,
                0.38, 0.62,
                0.66, 0.34,
                0.12, 0.88,
                0.5, 0.5,
                0.48, 0.52,
                0.1, 0.9}, r_1, s_1, t_1);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<>();
        X_0_to_X_1.put(R_0_RV, R_1_RV);
        X_0_to_X_1.put(S_0_RV, S_1_RV);
        X_0_to_X_1.put(T_0_RV, T_1_RV);
        X_0_to_X_1.put(U_0_RV, U_1_RV);

        Set<RandomVariable> E_1 = new HashSet<>();
        E_1.add(E_1_RV);
        E_1.add(EE_1_RV);

        return new CustomDynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, r_0, s_0, t_0, u_0);
    }

    public static DynamicBayesianNetwork getInventedNet3() {

        final RandVar R_0_RV = new RandVar("R_0", new BooleanDomain());
        final RandVar S_0_RV = new RandVar("S_0", new BooleanDomain());
        final RandVar T_0_RV = new RandVar("T_0", new BooleanDomain());
        final RandVar U_0_RV = new RandVar("U_0", new BooleanDomain());
        final RandVar V_0_RV = new RandVar("V_0", new BooleanDomain());
        final RandVar W_0_RV = new RandVar("W_0", new BooleanDomain());
        final RandVar X_0_RV = new RandVar("X_0", new BooleanDomain());
        final RandVar Y_0_RV = new RandVar("Y_0", new BooleanDomain());
        final RandVar Z_0_RV = new RandVar("Z_0", new BooleanDomain());
        final RandVar A_0_RV = new RandVar("A_0", new BooleanDomain());

        final RandVar R_1_RV = new RandVar("R_1", new BooleanDomain());
        final RandVar S_1_RV = new RandVar("S_1", new BooleanDomain());
        final RandVar T_1_RV = new RandVar("T_1", new BooleanDomain());
        final RandVar U_1_RV = new RandVar("U_1", new BooleanDomain());
        final RandVar V_1_RV = new RandVar("V_1", new BooleanDomain());
        final RandVar W_1_RV = new RandVar("W_1", new BooleanDomain());
        final RandVar X_1_RV = new RandVar("X_1", new BooleanDomain());
        final RandVar Y_1_RV = new RandVar("Y_1", new BooleanDomain());
        final RandVar Z_1_RV = new RandVar("Z_1", new BooleanDomain());
        final RandVar A_1_RV = new RandVar("A_1", new BooleanDomain());

        final RandVar E_1_RV = new RandVar("E_1", new BooleanDomain());
        final RandVar EE_1_RV = new RandVar("EE_1", new BooleanDomain());
        final RandVar EEE_1_RV = new RandVar("EEE_1", new BooleanDomain());
        final RandVar EEEE_1_RV = new RandVar("EEEE_1", new BooleanDomain());

        FiniteNode r_0 = new CustomNode(R_0_RV, new double[]{0.5, 0.5});
        FiniteNode s_0 = new CustomNode(S_0_RV, new double[]{0.5, 0.5});
        FiniteNode t_0 = new CustomNode(T_0_RV, new double[]{0.5, 0.5});
        FiniteNode u_0 = new CustomNode(U_0_RV, new double[]{0.5, 0.5});
        FiniteNode v_0 = new CustomNode(V_0_RV, new double[]{0.5, 0.5});
        FiniteNode w_0 = new CustomNode(W_0_RV, new double[]{0.5, 0.5});
        FiniteNode x_0 = new CustomNode(X_0_RV, new double[]{0.5, 0.5});
        FiniteNode y_0 = new CustomNode(Y_0_RV, new double[]{0.5, 0.5});
        FiniteNode z_0 = new CustomNode(Z_0_RV, new double[]{0.5, 0.5});
        FiniteNode a_0 = new CustomNode(A_0_RV, new double[]{0.5, 0.5});

        CustomBayesNet priorNetwork = new CustomBayesNet(r_0, s_0, t_0, u_0, v_0, w_0, x_0, y_0, z_0, a_0);

        FiniteNode r_1 = new CustomNode(R_1_RV, new double[]{
                0.1, 0.9,
                0.15, 0.85,
                0.34, 0.66,
                0.18, 0.82}, r_0, s_0);

        FiniteNode s_1 = new CustomNode(S_1_RV, new double[]{
                0.57, 0.43,
                0.29, 0.71,
                0.54, 0.46,
                0.83, 0.17,
                0.55, 0.45,
                0.96, 0.04,
                0.3, 0.7,
                0.41, 0.59}, r_0, s_0, t_0);

        FiniteNode t_1 = new CustomNode(T_1_RV, new double[]{
                0.23, 0.77,
                0.4, 0.6,
                0.57, 0.43,
                0.29, 0.71,
                0.51, 0.49,
                0.97, 0.03,
                0.81, 0.19,
                0.22, 0.78}, s_0, t_0, u_0);

        FiniteNode u_1 = new CustomNode(U_1_RV, new double[]{
                0.4, 0.6,
                0.23, 0.77,
                0.29, 0.71,
                0.75, 0.25,
                0.57, 0.43,
                0.6, 0.4,
                0.11, 0.89,
                0.05, 0.95}, t_0, u_0, v_0);

        FiniteNode v_1 = new CustomNode(V_1_RV, new double[]{
                0.11, 0.89,
                0.4, 0.6,
                0.57, 0.43,
                0.75, 0.25,
                0.6, 0.4,
                0.29, 0.71,
                0.23, 0.77,
                0.15, 0.85}, u_0, v_0, w_0);

        FiniteNode w_1 = new CustomNode(W_1_RV, new double[]{
                0.72, 0.28,
                0.16, 0.84,
                0.77, 0.23,
                0.01, 0.99,
                0.11, 0.89,
                0.88, 0.12,
                0.62, 0.38,
                0.37, 0.63
                }, v_0, w_0, x_0);

        FiniteNode x_1 = new CustomNode(X_1_RV, new double[]{
                0.88, 0.12,
                0.23, 0.77,
                0.11, 0.89,
                0.16, 0.84,
                0.01, 0.99,
                0.77, 0.23,
                0.4, 0.6,
                0.05, 0.95}, w_0, x_0, y_0);

        FiniteNode y_1 = new CustomNode(Y_1_RV, new double[]{
                0.57, 0.43,
                0.29, 0.71,
                0.51, 0.49,
                0.97, 0.03,
                0.81, 0.19,
                0.01, 0.99,
                0.77, 0.23,
                0.34, 0.66}, x_0, y_0, z_0);

        FiniteNode z_1 = new CustomNode(Z_1_RV, new double[]{
                0.51, 0.49,
                0.97, 0.03,
                0.01, 0.99,
                0.11, 0.89,
                0.81, 0.19,
                0.01, 0.99,
                0.77, 0.23,
                0.34, 0.66}, y_0, z_0, a_0);

        FiniteNode a_1 = new CustomNode(A_1_RV, new double[]{
                0.97, 0.03,
                0.01, 0.99,
                0.77, 0.23,
                0.81, 0.19}, z_0, a_0);

        FiniteNode e_1 = new CustomNode(E_1_RV, new double[]{
                0.93, 0.07,
                0.49, 0.51,
                0.42, 0.58,
                0.73, 0.27,
                0.09, 0.91,
                0.32, 0.68,
                0.87, 0.13,
                0.21, 0.79}, r_1, s_1, t_1);

        FiniteNode ee_1 = new CustomNode(EE_1_RV, new double[]{
                0.87, 0.13,
                0.01, 0.99,
                0.38, 0.62,
                0.66, 0.34,
                0.12, 0.88,
                0.5, 0.5,
                0.48, 0.52,
                0.1, 0.9,
                0.97, 0.03,
                0.01, 0.99,
                0.77, 0.23,
                0.81, 0.19,
                0.11, 0.89,
                0.01, 0.99,
                0.51, 0.49,
                0.34, 0.66}, t_1, u_1, v_1, w_1);

        FiniteNode eee_1 = new CustomNode(EEE_1_RV, new double[]{
                0.01, 0.99,
                0.66, 0.34,
                0.48, 0.52,
                0.38, 0.62,
                0.87, 0.13,
                0.12, 0.88,
                0.5, 0.5,
                0.1, 0.9}, w_1, x_1, y_1);

        FiniteNode eeee_1 = new CustomNode(EEEE_1_RV, new double[]{
                0.01, 0.99,
                0.66, 0.34,
                0.48, 0.52,
                0.38, 0.62,
                0.87, 0.13,
                0.12, 0.88,
                0.5, 0.5,
                0.1, 0.9}, y_1, z_1, a_1);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<>();
        X_0_to_X_1.put(R_0_RV, R_1_RV);
        X_0_to_X_1.put(S_0_RV, S_1_RV);
        X_0_to_X_1.put(T_0_RV, T_1_RV);
        X_0_to_X_1.put(U_0_RV, U_1_RV);
        X_0_to_X_1.put(V_0_RV, V_1_RV);
        X_0_to_X_1.put(W_0_RV, W_1_RV);
        X_0_to_X_1.put(X_0_RV, X_1_RV);
        X_0_to_X_1.put(Y_0_RV, Y_1_RV);
        X_0_to_X_1.put(Z_0_RV, Z_1_RV);
        X_0_to_X_1.put(A_0_RV, A_1_RV);

        Set<RandomVariable> E_1 = new HashSet<>();
        E_1.add(E_1_RV);
        E_1.add(EE_1_RV);
        E_1.add(EEE_1_RV);
        E_1.add(EEEE_1_RV);

        return new CustomDynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, r_0, s_0, t_0, u_0, v_0, w_0, x_0, y_0, z_0, a_0);
    }
}

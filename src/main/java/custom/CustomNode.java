package custom;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.ConditionalProbabilityTable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.CPT;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CustomNode implements Node, FiniteNode {
    private final RandomVariable variable;
    private Set<Node> parents;
    private Set<Node> children;
    private final ConditionalProbabilityTable cpt;

    public CustomNode(RandomVariable var, double[] distribution) {
        this(var, distribution, (Node[]) null);
    }

    public CustomNode(RandomVariable var, double[] values, Node... parents) {

        // AbstractNode

        if (null == var) {
            throw new IllegalArgumentException(
                    "Random Variable for Node must be specified.");
        }
        this.variable = var;
        this.parents = new LinkedHashSet<>();
        if (null != parents) {
            for (Node p : parents) {
                ((CustomNode) p).addChild(this);
                this.parents.add(p);
            }
        }
        this.parents = new HashSet<>(this.parents);
        this.children = new HashSet<>();

        // FullCPTNode

        RandomVariable[] conditionedOn = new RandomVariable[getParents().size()];
        int i = 0;
        for (Node p : getParents()) {
            conditionedOn[i++] = p.getRandomVariable();
        }

        cpt = new CPT(var, values, conditionedOn);
    }

    @Override
    public RandomVariable getRandomVariable() {
        return variable;
    }

    @Override
    public boolean isRoot() {
        return 0 == getParents().size();
    }

    @Override
    public Set<Node> getParents() {
        return parents;
    }

    @Override
    public Set<Node> getChildren() {
        return children;
    }

    public void addChildren(Set<Node> cs){
        children.addAll(cs);
    }

    public void removeChildren(){
        children = new HashSet<>();
    }


    @Override
    public Set<Node> getMarkovBlanket() {
        LinkedHashSet<Node> mb = new LinkedHashSet<>();
        // Given its parents,
        mb.addAll(getParents());
        // children,
        mb.addAll(getChildren());
        // and children's parents
        for (Node cn : getChildren()) {
            mb.addAll(cn.getParents());
        }

        return mb;
    }

    @Override
    public ConditionalProbabilityDistribution getCPD() {
        return getCPT();
    }

    public ConditionalProbabilityTable getCPT() {
        return cpt;
    }

    protected void addChild(Node childNode) {
        children = new LinkedHashSet<>(children);

        children.add(childNode);

        children = Collections.unmodifiableSet(children);
    }
}

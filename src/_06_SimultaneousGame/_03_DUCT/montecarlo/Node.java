package _06_SimultaneousGame._03_DUCT.montecarlo;

import java.util.ArrayList;
import java.util.List;

import _06_SimultaneousGame._03_DUCT.SimultaneousMazeState;

public class Node {

    private static double C = 1;
    private static int EXPAND_THRESHOLD = 5;
    
    private SimultaneousMazeState state;
    private double w;
    public List<List<Node>> childNodeses;
    public double n;
    
    public Node(SimultaneousMazeState state) {
        this.state = state;
        this.w = 0;
        this.childNodeses = new ArrayList<>();
        this.n = 0;
    }
    
    public Node(Node other) {
        this.state = new SimultaneousMazeState(other.state);
        this.w = other.w;
        this.childNodeses = new ArrayList<>();
        for (var childNodes : other.childNodeses) {
            this.childNodeses.add(childNodes.stream().map(Node::new).toList());
        }
        this.n = other.n;
    }
    
    public double evaluate() {
        if (this.state.isDone()) {
            double value = 0.5;
            switch (this.state.getWinningStatus()) {
            case FRIST:
                value = 1.0;
                break;
            case SECOND:
                value = 0.0;
                break;
            default:
                break;
            }
            this.w += value;
            this.n++;
            return value;
        }
        
        if (this.childNodeses.isEmpty()) {
            var stateCopy = new SimultaneousMazeState(this.state);
            double value = DUCTAction.playout(stateCopy);
            this.w += value;
            this.n++;
            if (this.n == EXPAND_THRESHOLD) {
                this.expand();
            }
            return value;
        } else {
            double value = this.nextChildNode().evaluate();
            this.w += value;
            this.n++;
            return value;
        }
    }
    
    public void expand() {
        var legalActions0 = this.state.legalActions(0);
        var legalActions1 = this.state.legalActions(1);
        this.childNodeses.clear();
        for (int action0 : legalActions0) {
            List<Node> targetNodes = new ArrayList<>();
            this.childNodeses.add(targetNodes);
            for (int action1 : legalActions1) {
                var targetNode = new Node(new SimultaneousMazeState(this.state));
                targetNode.state.advance(action0, action1);
                targetNodes.add(targetNode);
            }
        }
    }
    
    public Node nextChildNode() {
        for (var childNodes : this.childNodeses) {
            for (var childNode : childNodes) {
                if (childNode.n==0) {
                    return childNode;
                }
            }
        }
        
        double t=0;
        for (var childNodes : this.childNodeses) {
            for (var childNode : childNodes) {
                t += childNode.n;
            }
        }
        int[] bestIs = {-1, 1};
        
        double bestValue = -DUCTAction.INF;
        for (int i = 0; i < this.childNodeses.size(); i++) {
            var childNodes = this.childNodeses.get(i);
            double w = 0;
            double n = 0;
            for (int j = 0; j < childNodes.size(); j++) {
                var childeNode = childNodes.get(j);
                w += childeNode.w;
                n += childeNode.n;
            }
            double ucb1Value = w / n + (double)C * Math.sqrt(2. * Math.log(t) / n);
            if (ucb1Value > bestValue) {
                bestIs[0] = i;
                bestValue = ucb1Value;
            }
        }
        
        bestValue = -DUCTAction.INF;
        for (int j = 0; j < this.childNodeses.get(0).size(); j++) {
            double w = 0;
            double n = 0;
            for (int i = 0; i < this.childNodeses.size(); i++) {
                var childNode = childNodeses.get(i).get(j);
                w += childNode.w;
                n += childNode.n;
            }
            w = 1. - w;
            double ucb1Value = w / n + (double)C * Math.sqrt(2. * Math.log(t) / n);
            if (ucb1Value > bestValue) {
                bestIs[1] = j;
                bestValue = ucb1Value;
            }
        }

        return this.childNodeses.get(bestIs[0]).get(bestIs[1]);
    }
    
}

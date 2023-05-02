package _06_SimultaneousGame._03_DUCT.altanate_motecalo;

import java.util.ArrayList;
import java.util.List;

import _06_SimultaneousGame._03_DUCT.AlternateMazeState;

public class Node {

    private static double C = 1;
    private static int EXPAND_THRESHOLD = 10;
    
    private AlternateMazeState state;
    private double w;
    public List<Node> childNodes;
    public double n;
    
    public Node(AlternateMazeState state) {
        this.state = state;
        this.w = 0;
        this.childNodes = new ArrayList<>();
        this.n = 0;
    }
    
    public Node(Node other) {
        this.state = new AlternateMazeState(other.state);
        this.w = other.w;
        this.childNodes = other.childNodes.stream().map(Node::new).toList();
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
        
        if (this.childNodes.isEmpty()) {
            var stateCopy = new AlternateMazeState(this.state);
            double value = MCTSAction.playout(stateCopy);
            this.w += value;
            this.n++;
            if (this.n == EXPAND_THRESHOLD) {
                this.expand();
            }
            return value;
        } else {
            double value = 1.0 - this.nextChildNode().evaluate();
            this.w += value;
            this.n++;
            return value;
        }
    }
    
    public void expand() {
        var legalActions = this.state.legalActions();
        this.childNodes.clear();
        for (int action : legalActions) {
            var childNode = new Node(new AlternateMazeState(this.state));
            childNode.state.advance(action);
            this.childNodes.add(childNode);
        }
    }
    
    public Node nextChildNode() {
        for (var childNode : this.childNodes) {
            if (childNode.n==0) {
                return childNode;
            }
        }
        
        double t=0;
        for (var childNode : this.childNodes) {
            t += childNode.n;
        }
        double bestValue = -MCTSAction.INF;
        int bestActionIndex = -1;
        for (int i = 0; i < this.childNodes.size(); i++) {
            var childNode = this.childNodes.get(i);
            double ucb1Value = 1. - childNode.w / childNode.n + (double)C * Math.sqrt(2. * Math.log(t) / childNode.n); 
            if (ucb1Value>bestValue) {
                bestActionIndex=i;
                bestValue=ucb1Value;
            }
        }
        return this.childNodes.get(bestActionIndex);
    }
    
}

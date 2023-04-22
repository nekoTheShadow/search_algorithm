package _05_AlternateGame._12_ThunderSearch.thunderSearchAction;

import java.util.ArrayList;
import java.util.List;

import _05_AlternateGame._12_ThunderSearch.State;

public class Node {
    private static int INF = 1000000000;
    public static double C = 1.;            
    public static int EXPAND_THRESHOLD = 10;

    private State state;
    private double w;
    
    public List<Node> childNodes;
    public double n;
    
    public Node(State state) {
        this.state = state;
        this.w = 0;
        this.childNodes = new ArrayList<>();
        this.n = 0;
    }
    
    public double evaluate() {
        if (this.state.isDone()) {
            double value = 0.5;
            switch(this.state.getWinningStatus()) {
            case WIN:
                value = 1.0;
                break;
            case LOSE:
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
            double value = this.state.getScoreRate();
            this.w += value;
            this.n++;
            this.expand();
            return value;
        } else {
            double value = 1.0 - this.nextChildNode().evaluate();
            this.w += value;
            this.n++;
            return value;
        }
    }
    
    public void expand() {
        List<Integer> legalActions = this.state.legalActions();
        this.childNodes.clear();
        for (int action : legalActions) {
            this.childNodes.add(new Node(new State(state)));
            this.childNodes.get(this.childNodes.size()-1).state.advance(action);
        }
    }
    
    public Node nextChildNode() {
        for (Node childNode : this.childNodes) {
            if (childNode.n == 0) {
                return childNode;
            }
        }
        
        
        double t = 0;
        for (Node childNode : this.childNodes) {
            t += childNode.n;
        }
        double bestValue = -INF;
        int bestActionIndex = -1;
        for (int i = 0; i < this.childNodes.size(); i++) {
            Node childNode = this.childNodes.get(i);
            double thunderValue = 1.0 - childNode.w/childNode.n;
            if (thunderValue > bestValue) {
                bestActionIndex = i;
                bestValue = thunderValue;
            }
        }
        return this.childNodes.get(bestActionIndex);
    }
    

}

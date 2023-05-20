package _08_Actual._02_BitBoard.montecarlo_bit;

import java.util.ArrayList;
import java.util.List;


public class Node {
    private static int INF = 1000000000;
    public static double C = 1.;            
    public static int EXPAND_THRESHOLD = 10;

    private ConnectFourStateByBitSet state;
    private double w;
    
    public List<Node> childNodes;
    public double n;
    
    public Node(ConnectFourStateByBitSet state) {
        this.state = new ConnectFourStateByBitSet(state);
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
            ConnectFourStateByBitSet state = new ConnectFourStateByBitSet(this.state);
            double value = playout(state);
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
       int[] legalActions = this.state.legalActions();
        this.childNodes.clear();
        for (int action : legalActions) {
            this.childNodes.add(new Node(new ConnectFourStateByBitSet(state)));
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
            double ucb1Value = 1.0 - childNode.w/childNode.n + (double)C * Math.sqrt(2. * Math.log(t) / childNode.n);
            if (ucb1Value > bestValue) {
                bestActionIndex = i;
                bestValue = ucb1Value;
            }
        }
        return this.childNodes.get(bestActionIndex);
    }
    
    public static double playout(ConnectFourStateByBitSet state) {
        switch(state.getWinningStatus()) {
        case WIN:
            return 1.0;
        case LOSE:
            return 0.0;
        case DRAW:
            return 0.5;
        default:
            state.advance(MontecarloBitAction.randomAction(state));
            return 1.0 - playout(state);
        }
    }
}

package _05_AlternateGame._13_ThunderSearchWithTime.mctsAction;

import java.util.List;

import _05_AlternateGame._12_ThunderSearch.TimeKeeper;
import _05_AlternateGame._13_ThunderSearchWithTime.State;

public class MctAction {
    public static int mctsAction(State state, int playoutNumber) {
        Node rootNode = new Node(state);
        rootNode.expand();
        for (int i = 0; i < playoutNumber; i++) {
            rootNode.evaluate();
        }
        
        List<Integer> legalActions = state.legalActions();
        int bestActionSearchedNumber = -1;
        int bestActionIndex = -1;
        for (int i = 0; i < legalActions.size(); i++) {
            int n = (int)rootNode.childNodes.get(i).n;
            if (n > bestActionSearchedNumber) {
                bestActionIndex = i;
                bestActionSearchedNumber = n;
            }
        }
        return legalActions.get(bestActionIndex);
    }
    
    public static int mctsActionWithTimeThreshold(State state, long timeThreshold) {
        Node rootNode = new Node(state);
        rootNode.expand();
        TimeKeeper timeKeeper = new TimeKeeper(timeThreshold);
        for (int cnt = 0;; cnt++) {
            if (timeKeeper.isTimeOver()) {
                break;
            }
            rootNode.evaluate();
        }
        
        List<Integer> legalActions = state.legalActions();
        int bestActionSearchedNumber = -1;
        int bestActionIndex = -1;
        for (int i = 0; i < legalActions.size(); i++) {
            int n = (int)rootNode.childNodes.get(i).n;
            if (n > bestActionSearchedNumber) {
                bestActionIndex = i;
                bestActionSearchedNumber = n;
            }
        }
        return legalActions.get(bestActionIndex);
    }
}

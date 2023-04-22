package _05_AlternateGame._12_ThunderSearch.mctsAction;

import java.util.List;

import _05_AlternateGame._12_ThunderSearch.State;

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
}

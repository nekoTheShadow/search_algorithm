package _08_Actual._00_ConnectFour;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        playGame();
    }
    
    private static Random mtForAction = new Random(0);

    public static void playGame() {
        ConnectFourState state = new ConnectFourState();
        System.out.println(state.toString());
        while (!state.isDone()) {
            {
                System.out.println("1p ------------------------------------");
                int action = randomAction(state);
                System.out.println("action " + action);
                state.advance(action); 
                System.out.println(state.toString());
                if (state.isDone()) {

                    switch (state.getWinningStatus()){
                    case WIN:
                        System.out.println("winner: 2p");
                        break;
                    case LOSE:
                        System.out.println("winner: 1p");
                        break;
                    default:
                        System.out.println("DRAW");
                        break;
                    }
                    break;
                }
            }
            // 2p
            {
                System.out.println("2p ------------------------------------");
                int action = randomAction(state);
                System.out.println("action " + action);
                state.advance(action); 
                System.out.println(state.toString());
                if (state.isDone()) {

                    switch (state.getWinningStatus()){
                    case WIN:
                        System.out.println("winner: 1p");
                        break;
                    case LOSE:
                        System.out.println("winner: 2p");
                        break;
                    default:
                        System.out.println("DRAW");
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public static int randomAction(ConnectFourState state) {
        int[] legalActions = state.legalActions();
        return legalActions[mtForAction.nextInt(legalActions.length)];
    }

}

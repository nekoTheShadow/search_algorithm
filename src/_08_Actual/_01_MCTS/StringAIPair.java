package _08_Actual._01_MCTS;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<ConnectFourState, Integer> second;
    
    public StringAIPair(String first, Function<ConnectFourState, Integer> second) {
        this.first = first;
        this.second = second;
    }
}

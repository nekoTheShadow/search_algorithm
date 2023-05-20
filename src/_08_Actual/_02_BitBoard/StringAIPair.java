package _08_Actual._02_BitBoard;

import java.util.function.Function;

import _08_Actual._02_BitBoard.montecarlo.ConnectFourState;

public class StringAIPair {
    public String first;
    public Function<ConnectFourState, Integer> second;
    
    public StringAIPair(String first, Function<ConnectFourState, Integer> second) {
        this.first = first;
        this.second = second;
    }
}

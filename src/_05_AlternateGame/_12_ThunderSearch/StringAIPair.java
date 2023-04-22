package _05_AlternateGame._12_ThunderSearch;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<State, Integer> second;
    
    public StringAIPair(String first, Function<State, Integer> second) {
        this.first = first;
        this.second = second;
    }
}

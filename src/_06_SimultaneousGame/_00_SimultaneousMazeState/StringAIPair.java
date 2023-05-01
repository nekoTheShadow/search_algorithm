package _06_SimultaneousGame._00_SimultaneousMazeState;

import java.util.function.BiFunction;

public class StringAIPair {
    public String first;
    public BiFunction<SimultaneousMazeState, Integer, Integer> second;
    
    public StringAIPair(String first, BiFunction<SimultaneousMazeState, Integer, Integer> second) {
        this.first = first;
        this.second = second;
    }
}

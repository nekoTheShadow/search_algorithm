package _08_Actual._00_ConnectFour;

public class Pair {
    public int first;
    public int second;
    
    public static Pair of(int first, int second) {
        Pair pair = new Pair();
        pair.first = first;
        pair.second = second;
        return pair;
    }
    
    private Pair() {
        
    }
}

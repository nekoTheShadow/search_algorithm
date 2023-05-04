package _07_Advanced._05_MultiBitSet;

public class DistanceCoord {
    public int y;
    public int x;
    public int distance;
    
    public DistanceCoord(Coord coord) {
        this.y = coord.y;
        this.x = coord.x;
        this.distance = 0;
    }
    
    public DistanceCoord(int y, int x, int distance) {
        this.y = y;
        this.x = x;
        this.distance = distance;
    }
}

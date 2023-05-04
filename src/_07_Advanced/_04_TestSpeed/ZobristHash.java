package _07_Advanced._04_TestSpeed;

import java.util.Random;

public class ZobristHash {
    public static long[][][] points;
    public static long[][] character;
    
    static {
        points = new long[WallMazeState.H][WallMazeState.W][9+1];
        character = new long[WallMazeState.H][WallMazeState.W];
        
        Random mtInitHash = new Random(0);
        for (int y = 0; y < WallMazeState.H; y++) {
            for (int x = 0; x < WallMazeState.W; x++) {
                for (int p = 1; p < 9+1; p++) {
                    points[y][x][p] = mtInitHash.nextLong(0, Long.MAX_VALUE);
                }
                character[y][x] = mtInitHash.nextLong(0, Long.MAX_VALUE);
            }
        }
        
    }
}

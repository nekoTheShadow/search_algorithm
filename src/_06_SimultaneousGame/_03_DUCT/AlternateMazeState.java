package _06_SimultaneousGame._03_DUCT;

import java.util.ArrayList;
import java.util.List;

public class AlternateMazeState {
    
    public static int END_TURN = SimultaneousMazeState.END_TURN*2;
    private static int[] dx = {1, -1, 0, 0};
    private static int[] dy = {0, 0, 1, -1};
    
    private int[][] points;
    private int turn;
    private Character[] characters;
    
    public AlternateMazeState(SimultaneousMazeState baseState, int playerId) {
        this.points = baseState.getPoints();
        this.turn = baseState.getTurn()*2;
        if (playerId==0) {
            this.characters = baseState.getCharacters();
        } else {
            var characters = baseState.getCharacters();
            this.characters = new Character[]{characters[1], characters[1]};
        }
    }
    
    public AlternateMazeState(AlternateMazeState other) {
        int h = other.points.length;
        int w = other.points[0].length;
        this.points = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                this.points[i][j] = other.points[i][j];
            }
        }
        this.turn = other.turn;
        this.characters = new Character[] {new Character(other.characters[0]), new Character(other.characters[1])};
    }
    
    public WinningStatus getWinningStatus() {
        if (this.isDone()) {
            if (characters[0].gameScore>characters[1].gameScore) {
                return WinningStatus.FRIST;
            } else if (characters[0].gameScore<characters[1].gameScore) {
                return WinningStatus.SECOND;
            } else {
                return WinningStatus.DRAW;
            }
        } else {
            return WinningStatus.NONE;
        }
    }
    
    public void advance(int action) {
        var character = this.characters[0];
        character.x += dx[action];
        character.y += dy[action];
        var point = this.points[character.y][character.x];
        if (point>0) {
            character.gameScore += point;
            this.points[character.y][character.x] = 0;
        }
        this.turn++;
        
        var temp = this.characters[0];
        this.characters[0] = this.characters[1];
        this.characters[1] = temp;
    }
    
    public List<Integer> legalActions() {
        List<Integer> actions = new ArrayList<>();
        int playerId = 0;
        var character = this.characters[playerId];
        for (int action = 0; action < 4; action++) {
            int ty = character.y + dy[action];
            int tx = character.x + dx[action];
            if (ty>=0 && ty<SimultaneousMazeState.H && tx>=0 && tx<SimultaneousMazeState.W) {
                actions.add(action);
            }
        }
        return actions;   
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
    }
}

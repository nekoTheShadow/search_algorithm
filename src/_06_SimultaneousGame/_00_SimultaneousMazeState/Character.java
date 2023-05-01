package _06_SimultaneousGame._00_SimultaneousMazeState;

public class Character {
    public int y;
    public int x;
    public int gameScore;
    
    public Character(int y, int x) {
        this.y = y;
        this.x = x;
        this.gameScore = 0;
    }
    
    public Character(Character other) {
        this.y = other.y;
        this.x = other.x;
        this.gameScore = other.gameScore;
    }
}

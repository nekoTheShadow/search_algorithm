package _06_SimultaneousGame._03_DUCT;

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

    @Override
    public String toString() {
        return "Character [y=" + y + ", x=" + x + ", gameScore=" + gameScore + "]";
    }
}

package _05_AlternateGame._13_ThunderSearchWithTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class State {
    public static int H = 10;
    public static int W = 10;
    public static int END_TURN = 50;
    
    private static int[] dx = {1, -1, 0, 0};
    private static int[] dy = {0, 0, 1, -1};
    
    private int[][] points;
    private int turn;
    private Character[] characters;
    
    public State(long seed) {
        this.points = new int[H][W];
        this.turn = 0;
        this.characters = new Character[] {
            new Character(H/2, (W/2)-1),
            new Character(H/2, (W/2)+1)
        };
        
        Random mtForConstruct = new Random(seed);
        for (int y=0; y<H; y++) {
            for (int x=0; x<W; x++) {
                int point = mtForConstruct.nextInt(10);
                if (characters[0].y==y && characters[0].x==x) {
                    continue;
                }
                if (characters[1].y==y && characters[1].x==x) {
                    continue;
                }
                this.points[y][x] = point;
            }
        }
    }
    
    public State(State other) {
        this.points = new int[H][W];
        for (int y=0; y<H; y++) {
            for (int x=0; x<W; x++) {
                this.points[y][x] = other.points[y][x];
            }
        }
        
        this.turn = other.turn;
        this.characters = new Character[] {
            new Character(other.characters[0]), 
            new Character(other.characters[1])
        };
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
    }
    
    public void advance(int action) {
        Character character = this.characters[0];
        character.x += dx[action];
        character.y += dy[action];
        int point = this.points[character.y][character.x];
        if (point>0) {
            character.gameScore += point;
            this.points[character.y][character.x] = 0;
        }
        this.turn++;
        
        Character tmp = this.characters[0];
        this.characters[0] = this.characters[1];
        this.characters[1] = tmp;
    }
    
    public List<Integer> legalActions() {
        List<Integer> actions = new ArrayList<>();
        Character character = this.characters[0];
        for (int action=0; action<4; action++) {
            int ty = character.y + dy[action];
            int tx = character.x + dx[action];
            if (0<=ty && ty<H && 0<=tx && tx<W) {
                actions.add(action);
            }
        }
        return actions;
    }
    
    public WinningStatus getWinningStatus() {
        if (isDone()) {
            if (characters[0].gameScore > characters[1].gameScore) {
                return WinningStatus.WIN;
            } else if (characters[0].gameScore < characters[1].gameScore) {
                return WinningStatus.LOSE;
            } else {
                return WinningStatus.DRAW;
            }
        } else {
            return WinningStatus.NONE;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("turn:\t%d%n".formatted(this.turn));
        for (int playerId=0; playerId<this.characters.length; playerId++) {
            int actualPlayerId = playerId;
            if (this.turn%2==1) {
                actualPlayerId=(playerId+1)%2;
            }
            
            Character character = this.characters[actualPlayerId];
            ss.append("score(%d):\t%d\t y:%d\t x:%d%n".formatted(actualPlayerId, character.gameScore, character.y, character.x));
        }
        
        for (int h=0; h<H; h++) {
            for (int w=0; w<W; w++) {
                boolean isWritten = false;
                for (int playerId=0; playerId<this.characters.length; playerId++) {
                    int actualPlayerId = playerId;
                    if (this.turn%2==1) {
                        actualPlayerId = (playerId+1)%2;
                    }
                    
                    Character character = this.characters[playerId];
                    if (character.y==h && character.x==w) {
                        if (actualPlayerId==0) {
                            ss.append("A");
                        } else {
                            ss.append("B");
                        }
                        isWritten = true;
                    }
                }
                
                if (!isWritten) {
                    if (this.points[h][w]>0) {
                        ss.append(this.points[h][w]);
                    } else {
                        ss.append(".");
                    }
                }
            }
            ss.append(System.lineSeparator());
        }
        return ss.toString();
    }
    
    public int getScore() {
        return characters[0].gameScore - characters[1].gameScore;
    }
    
    public boolean isFirstPlayer() {
        return this.turn%2==0;
    }
    
    public double getFristPlayerScoreForWinRate() {
        switch(this.getWinningStatus()) {
        case WIN:
            if (this.isFirstPlayer()) {
                return 1.0;
            } else {
                return 0.0;
            }
        case LOSE:
            if (this.isFirstPlayer()) {
                return 0.0;
            } else {
                return 1.0;
            }
        default:
            return 0.5;
        }
    }
    
    public double getScoreRate() {
        if (characters[0].gameScore+characters[1].gameScore==0) {
            return 0.0;
        } else {
            return ((double)characters[0].gameScore)/(double)(characters[0].gameScore+characters[1].gameScore);
        }
    }
}

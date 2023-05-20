package _08_Actual._02_BitBoard.montecarlo_bit;

import java.util.ArrayList;
import java.util.List;

import _08_Actual._02_BitBoard.WinningStatus;
import _08_Actual._02_BitBoard.montecarlo.ConnectFourState;

public class ConnectFourStateByBitSet {
    private boolean isFirst;
    private long myBoard;
    private long allBoard;
    private WinningStatus winningStatus;
    
    public ConnectFourStateByBitSet(ConnectFourStateByBitSet other) {
        this.isFirst = other.isFirst;
        this.myBoard = other.myBoard;
        this.allBoard = other.allBoard;
        this.winningStatus = other.winningStatus;
    }
    
    public ConnectFourStateByBitSet(ConnectFourState state) {
        this.isFirst = state.isFirst;
        this.myBoard = 0L;
        this.allBoard = 0L;
        for (int y = 0; y < ConnectFourState.H; y++) {
            for (int x = 0; x < ConnectFourState.W; x++) {
                int index = x*(ConnectFourState.H+1)+y;
                if (state.myBoard[y][x]==1) {
                    this.myBoard |= 1L << index;
                }
                if (state.myBoard[y][x]==1 || state.enemyBoard[y][x]==1) {
                    this.allBoard |= 1L << index;
                }
            }
        }
        this.winningStatus = WinningStatus.NONE;
    }
    
    public int[] legalActions() {
        List<Integer> actions = new ArrayList<>();
        long possible = this.allBoard + 0b0000001000000100000010000001000000100000010000001L;
        long filter = 0b0111111;
        for (int x = 0; x < ConnectFourState.W; x++){
            if ((filter & possible) != 0) {
                actions.add(x);
            }
            filter <<= 7L;
        }
        return actions.stream().mapToInt(Integer::intValue).toArray();
    }
    
    public void advance(int action) {
        this.myBoard ^= this.allBoard;
        isFirst = !isFirst;
        long newAllBoard = this.allBoard | (this.allBoard + (1L << (action * 7L)));
        this.allBoard = newAllBoard;
        long filled = 0b0111111011111101111110111111011111101111110111111L;

        if (isWinner(this.myBoard ^ this.allBoard)){
            this.winningStatus = WinningStatus.LOSE;
        } else if (this.allBoard == filled) {
            this.winningStatus = WinningStatus.DRAW;
        }
    }
    

    private boolean isWinner(long board) {
        long tmpBoard = board & (board >> 7L);
        if ((tmpBoard & (tmpBoard >> 14L)) != 0) {
            return true;
        }
        
        // "\"方向の連結判定
        tmpBoard = board & (board >> 6L);
        if ((tmpBoard & (tmpBoard >> 12L)) != 0){
            return true;
        }
        
        // "／"方向の連結判定
        tmpBoard = board & (board >> 8L);
        if ((tmpBoard & (tmpBoard >> 16L)) != 0) {
            return true;
        }
        
        // 縦方向の連結判定
        tmpBoard = board & (board >> 1L);
        if ((tmpBoard & (tmpBoard >> 2L)) != 0) {
            return true;
        }

        return false;
    }
    
    public boolean isDone() {
        return this.winningStatus != WinningStatus.NONE;
    }
    
    
    public WinningStatus getWinningStatus() {
        return this.winningStatus;
    }
    
    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("is_first:\t").append(isFirst).append(System.lineSeparator());
        for (int y = ConnectFourState.H - 1; y >= 0; y--) {
            for (int x = 0; x < ConnectFourState.W; x++) {
                int index = x * (ConnectFourState.H + 1) + y;
                char c = '.';
                if (((myBoard >> index) & 1L) != 0) {
                    c = (isFirst ? 'x' : 'o');
                } else if ((((allBoard ^ myBoard) >> index) & 1L) != 0) {
                    c = (isFirst ? 'o' : 'x');
                }
                ss.append(c);
            }
            ss.append(System.lineSeparator());
        }

        return ss.toString();
    }
}

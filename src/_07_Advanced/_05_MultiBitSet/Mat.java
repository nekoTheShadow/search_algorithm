package _07_Advanced._05_MultiBitSet;

public class Mat {
    private long[] bits;
    
    public Mat() {
        this.bits = new long[WallMazeState.H];
        for (int i = 0; i < WallMazeState.H; i++) {
            this.bits[i] = 0;
        }
    }
    
    public Mat(Mat other) {
        int n = other.bits.length;
        this.bits = new long[n];
        for (int i = 0; i < n; i++) {
            this.bits[i] = other.bits[i];
        }
    }
    
    public boolean get(int y, int x) {
        return (this.bits[y] & (1<<x)) > 0;
    }
    
    public void set(int y, int x) {
        this.bits[y] = this.bits[y] | (1<<x);
    }
    
    public void del(int y, int x) {
        this.bits[y] = this.bits[y] & ~(1<<x);
    }
    
    private Mat upMat() {
        Mat retMat = new Mat(this);
        for (int y = 0; y < WallMazeState.H-1; y++) {
            retMat.bits[y] |= retMat.bits[y+1];
        }
        return retMat;
    }
    
    private Mat downMat() {
        Mat retMat = new Mat(this);
        for (int y=WallMazeState.H-1; y>=1; y--) {
            retMat.bits[y] |= retMat.bits[y-1];
        }
        return retMat;
    }
    
    private Mat leftMat() {
        Mat retMat = new Mat(this);
        for (int y = 0; y < WallMazeState.H; y++) {
            retMat.bits[y] >>= 1;
        }
        return retMat;
    }
    
    private Mat rightMat() {
        Mat retMat = new Mat(this);
        for (int y = 0; y < WallMazeState.H; y++) {
            retMat.bits[y] <<= 1;
        }
        return retMat;
    }
    
    public void expand() {
        Mat up = upMat();
        Mat down = downMat();
        Mat left = leftMat();
        Mat right = rightMat();
        for (int y = 0; y < WallMazeState.H; y++) {
            this.bits[y] |= up.bits[y];
            this.bits[y] |= down.bits[y];
            this.bits[y] |= left.bits[y];
            this.bits[y] |= right.bits[y];
        }
    }
    
    public void andeqNot(Mat mat) {
        for (int y = 0; y < WallMazeState.H; y++) {
            this.bits[y] &= ~mat.bits[y];
        }
    }
    
    public boolean isEqual(Mat mat) {
        for (int y = 0; y < WallMazeState.H; y++) {
            if (this.bits[y] != mat.bits[y]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isAnyEqual(Mat mat) {
        for (int y = 0; y < WallMazeState.H; y++) {
            if (Long.bitCount(this.bits[y] & mat.bits[y])>0) {
                return true;
            }
        }
        return false;
    }
}

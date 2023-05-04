package _07_Advanced._06_SingleBitSet;

public class Mat {
    private static long leftMask;
    private static long rightMask;
    
    static {
        leftMask = initLeftMask();
        rightMask = initRightMask();
    }
    
    private static long initLeftMask() {
        long mask = 0L;
        for (int y = 0; y < WallMazeState.H; y++) {
            mask |= 1L << (y*WallMazeState.W);
        }
        mask = ~mask;
        return mask;
    }
    
    private static long initRightMask() {
        long mask = 0L;
        for (int y = 0; y < WallMazeState.H; y++) {
            mask |= 1L << (y * WallMazeState.W + WallMazeState.W - 1L);
        }
        mask = ~mask;
        return mask;
    }
    
    private long bits;

    
    public Mat() {
        this.bits = 0L;
    }
    
    public Mat(Mat other) {
        this.bits = other.bits;
    }
    
    public boolean get(int y, int x) {
        return (this.bits & (1L<<(y * WallMazeState.W + x))) > 0;
    }
    
    public void set(int y, int x) {
        this.bits = this.bits | (1L<<(y * WallMazeState.W + x));
    }
    
    public void del(int y, int x) {
        this.bits = this.bits & ~(1L<<(y * WallMazeState.W + x));
    }
    
    private Mat upMat() {
        Mat retMat = new Mat(this);
        retMat.bits >>= WallMazeState.W;
        return retMat;
    }
    
    private Mat downMat() {
        Mat retMat = new Mat(this);
        retMat.bits <<= WallMazeState.W;
        return retMat;
    }
    
    private Mat leftMat() {
        Mat retMat = new Mat(this);
        retMat.bits |= (retMat.bits & leftMask) >> 1L;
        return retMat;
    }
    
    private Mat rightMat() {
        Mat retMat = new Mat(this);
        retMat.bits |= (retMat.bits & rightMask) << 1L;
        return retMat;
    }
    
    public void expand() {
        Mat up = upMat();
        Mat down = downMat();
        Mat left = leftMat();
        Mat right = rightMat();
        this.bits |= up.bits;
        this.bits |= down.bits;
        this.bits |= left.bits;
        this.bits |= right.bits;
    }
    
    public void andeqNot(Mat mat) {
        this.bits &= ~mat.bits;
    }
    
    public boolean isEqual(Mat mat) {
        return this.bits == mat.bits;
    }
    
    public boolean isAnyEqual(Mat mat) {
        return Long.bitCount(this.bits & mat.bits)>0;
    }
}

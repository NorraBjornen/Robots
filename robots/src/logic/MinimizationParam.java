package logic;

public class MinimizationParam {
    private boolean isLogWindowMinimized, isGameWindowMinimized;

    public MinimizationParam(boolean isLogWindowMinimized, boolean isGameWindowMinimized){
        this.isLogWindowMinimized = isLogWindowMinimized;
        this.isGameWindowMinimized = isGameWindowMinimized;
    }

    public boolean isLogWindowMinimized() {
        return isLogWindowMinimized;
    }

    public boolean isGameWindowMinimized() {
        return isGameWindowMinimized;
    }
}

package backend.academy.rendering;

/**
 * Структура данных для хранения результатов накопления.
 */
public class FlameData {
    public final int[][] density;
    public final float[][][] colorMap;
    public final float maxDensity;

    public FlameData(int[][] density, float[][][] colorMap, float maxDensity) {
        this.density = density;
        this.colorMap = colorMap;
        this.maxDensity = maxDensity;
    }
}

package backend.academy.rendering;

import backend.academy.models.Point;
import java.awt.Color;
import java.util.List;

/**
 * Класс для накопления данных плотности и цвета по набору точек.
 * Он не знает о нормализации, гамме и прочих деталях.
 */
@SuppressWarnings("MagicNumber")
public class FlameDataAccumulator {
    public FlameData accumulate(List<Point> points,
        int width,
        int height,
        double xMin,
        double xMax,
        double yMin,
        double yMax) {
        int[][] density = new int[height][width];
        float[][][] colorMap = new float[height][width][3];

        for (Point p : points) {
            int px = (int) ((p.x() - xMin) / (xMax - xMin) * (width - 1));
            int py = (int) ((p.y() - yMin) / (yMax - yMin) * (height - 1));
            if (px >= 0 && px < width && py >= 0 && py < height) {
                density[py][px] += 1;
                Color c = p.color();
                colorMap[py][px][0] += c.getRed();
                colorMap[py][px][1] += c.getGreen();
                colorMap[py][px][2] += c.getBlue();
            }
        }

        float maxDensity = 0.0f;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (density[y][x] > maxDensity) {
                    maxDensity = density[y][x];
                }
            }
        }

        return new FlameData(density, colorMap, maxDensity);
    }
}

package backend.academy.rendering;

import backend.academy.models.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class Renderer {
    private final int width;
    private final int height;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    private final FlameDataAccumulator accumulator;
    private final FlameNormalizer normalizer;

    public Renderer(int width, int height, double xMin, double xMax, double yMin, double yMax) {
        this.width = width;
        this.height = height;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.accumulator = new FlameDataAccumulator();
        this.normalizer = new FlameNormalizer();
    }

    public BufferedImage render(List<Point> points) {
        FlameData data = accumulator.accumulate(points, width, height, xMin, xMax, yMin, yMax);
        return normalizer.normalizeAndCreateImage(data, width, height);
    }

    public void saveImage(BufferedImage image, String filename) throws IOException {
        ImageIO.write(image, "png", new File(filename));
    }
}

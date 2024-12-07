
import backend.academy.rendering.FlameData;
import backend.academy.rendering.FlameNormalizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;

public class FlameNormalizerTest {

    @Test
    public void testNormalizeAndCreateImage() {
        FlameNormalizer normalizer = new FlameNormalizer();

        int[][] density = new int[2][2];
        density[0][0] = 10;
        density[0][1] = 5;
        density[1][0] = 0;
        density[1][1] = 20;

        float[][][] colorMap = new float[2][2][3];
        colorMap[0][0][0] = 255 * 10;
        FlameData data = new FlameData(density,colorMap,20);

        BufferedImage image = normalizer.normalizeAndCreateImage(data,2,2);
        Assertions.assertNotNull(image);
        int rgb = image.getRGB(0,0);
        Assertions.assertTrue((rgb & 0xFFFFFF) != 0);
    }
}

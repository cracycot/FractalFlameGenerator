package backend.academy.rendering;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Класс для нормализации данных и (опционально) гамма-коррекции.
 * Он получает FlameData и генерирует итоговое изображение.
 */
@SuppressWarnings("MagicNumber")
public class FlameNormalizer {
    private final float gamma = 2.2f;

    public BufferedImage normalizeAndCreateImage(FlameData data, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        float maxDensity = data.maxDensity;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int d = data.density[y][x];
                if (d > 0) {
                    float normDensity = (float) Math.log1p(d);
                    normDensity /= (float) Math.log1p(maxDensity);

                    float r = (data.colorMap[y][x][0] / d) / 255.0f;
                    float g = (data.colorMap[y][x][1] / d) / 255.0f;
                    float b = (data.colorMap[y][x][2] / d) / 255.0f;

                    // Применение гамма-коррекции (по желанию)
                    r = (float) Math.pow(r * normDensity, 1.0 / gamma);
                    g = (float) Math.pow(g * normDensity, 1.0 / gamma);
                    b = (float) Math.pow(b * normDensity, 1.0 / gamma);

                    int red = Math.min(255, (int) (r * 255));
                    int green = Math.min(255, (int) (g * 255));
                    int blue = Math.min(255, (int) (b * 255));

                    Color color = new Color(red, green, blue);
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }

        return image;
    }
}

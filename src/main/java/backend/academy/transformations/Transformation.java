package backend.academy.transformations;

import backend.academy.models.Point;
import java.util.function.BiFunction;
import lombok.Getter;

public class Transformation {
    private final double[] affineCoefficients;
    private static final int MAX_LENGTH = 6;
    @Getter
    private final double weight;
    private final BiFunction<Double, Double, Point> transformationFunction;

    public Transformation(double[] affineCoefficients,
        double weight,
        BiFunction<Double, Double, Point> transformationFunction) {
        if (affineCoefficients.length != MAX_LENGTH) {
            throw new IllegalArgumentException("Affine coefficients array must have exactly 6 elements.");
        }
        this.affineCoefficients = affineCoefficients;
        this.weight = weight;
        this.transformationFunction = transformationFunction;
    }

    @SuppressWarnings("MagicNumber")
    public Point apply(double x, double y) {
        double a = affineCoefficients[0];
        double b = affineCoefficients[1];
        double c = affineCoefficients[2];
        double d = affineCoefficients[3];
        double e = affineCoefficients[4];
        double f = affineCoefficients[5];

        double newX = a * x + b * y + e;
        double newY = c * x + d * y + f;

        return transformationFunction.apply(newX, newY);
    }
}

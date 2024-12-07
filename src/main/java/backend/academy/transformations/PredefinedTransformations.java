package backend.academy.transformations;

import backend.academy.models.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class PredefinedTransformations {
    private static final int PARAMS_SIZE = 6;

    public List<Transformation> getDefaultTransformations(Random random) {
        List<Transformation> transformations = new ArrayList<>();
        transformations.add(createSinusoidalTransformation(random));
        transformations.add(createSphericalTransformation(random));
        transformations.add(createPolarTransformation(random));
        transformations.add(createHeartTransformation(random));
        transformations.add(createDiskTransformation(random));
        transformations.add(createLogarithmicTransformation(random));

        return transformations;
    }

    private Transformation createSinusoidalTransformation(Random random) {
        BiFunction<Double, Double, Point> sinusoidal = (x, y) -> {
            double newX = Math.sin(x);
            double newY = Math.sin(y);
            return new Point(newX, newY, Color.RED);
        };

        double[] affineCoefficients = randomAffineCoefficients(random);
        double weight = random.nextDouble();

        return new Transformation(affineCoefficients, weight, sinusoidal);
    }

    private Transformation createSphericalTransformation(Random random) {
        BiFunction<Double, Double, Point> spherical = (x, y) -> {
            double r2 = x * x + y * y;
            double newX = x / r2;
            double newY = y / r2;
            return new Point(newX, newY, Color.GREEN);
        };

        double[] affineCoefficients = randomAffineCoefficients(random);
        double weight = random.nextDouble();

        return new Transformation(affineCoefficients, weight, spherical);
    }

    private Transformation createPolarTransformation(Random random) {
        BiFunction<Double, Double, Point> polar = (x, y) -> {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = theta / Math.PI;
            double newY = r - 1;
            return new Point(newX, newY, Color.BLUE);
        };

        double[] affineCoefficients = randomAffineCoefficients(random);
        double weight = random.nextDouble();

        return new Transformation(affineCoefficients, weight, polar);
    }

    private Transformation createHeartTransformation(Random random) {
        BiFunction<Double, Double, Point> heart = (x, y) -> {
            double newX = x * Math.sin(Math.PI * y);
            double newY = -y * Math.cos(Math.PI * x);
            return new Point(newX, newY, Color.MAGENTA);
        };

        double[] affineCoefficients = randomAffineCoefficients(random);
        double weight = random.nextDouble();

        return new Transformation(affineCoefficients, weight, heart);
    }

    private Transformation createDiskTransformation(Random random) {
        BiFunction<Double, Double, Point> disk = (x, y) -> {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = theta / Math.PI * Math.sin(Math.PI * r);
            double newY = theta / Math.PI * Math.cos(Math.PI * r);
            return new Point(newX, newY, Color.ORANGE);
        };

        double[] affineCoefficients = randomAffineCoefficients(random);
        double weight = random.nextDouble();

        return new Transformation(affineCoefficients, weight, disk);
    }

    private Transformation createLogarithmicTransformation(Random random) {
        BiFunction<Double, Double, Point> logarithmic = (x, y) -> {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = Math.log(r);
            return new Point(newX, theta, Color.CYAN);
        };

        double[] affineCoefficients = randomAffineCoefficients(random);
        double weight = random.nextDouble();

        return new Transformation(affineCoefficients, weight, logarithmic);
    }

    private double[] randomAffineCoefficients(Random random) {
        double[] coefficients = new double[PARAMS_SIZE];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = random.nextDouble() * 2 - 1;
        }
        return coefficients;
    }
}

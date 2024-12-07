package backend.academy.rendering;

import backend.academy.models.Point;
import backend.academy.transformations.Transformation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class PointGenerator implements Callable<List<Point>> {
    private final long numPoints;
    private final int skipPoints;
    private final List<Transformation> transformations;

    public PointGenerator(long numPoints, int skipPoints, List<Transformation> transformations) {
        this.numPoints = numPoints;
        this.skipPoints = skipPoints;
        this.transformations = transformations;
    }

    @Override
    public List<Point> call() {
        List<Point> generatedPoints = new ArrayList<>();
        double x = 0.0;
        double y = 0.0;

        for (int i = 0; i < skipPoints; i++) {
            Transformation t = selectRandomTransformation(transformations);
            Point p = t.apply(x, y);
            x = p.x();
            y = p.y();
        }

        for (int i = 0; i < numPoints; i++) {
            Transformation t = selectRandomTransformation(transformations);
            Point p = t.apply(x, y);
            generatedPoints.add(p);
            x = p.x();
            y = p.y();
        }
        return generatedPoints;
    }

    private Transformation selectRandomTransformation(List<Transformation> transformations) {
        double rand = ThreadLocalRandom.current().nextDouble();
        double cumulativeWeight = 0.0;
        for (Transformation t : transformations) {
            cumulativeWeight += t.weight();
            if (rand <= cumulativeWeight) {
                return t;
            }
        }
        return transformations.get(transformations.size() - 1);
    }
}

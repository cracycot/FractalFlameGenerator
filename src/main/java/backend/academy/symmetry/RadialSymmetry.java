package backend.academy.symmetry;

import backend.academy.models.Point;
import java.util.ArrayList;
import java.util.List;

public class RadialSymmetry implements Symmetry {
    private final int numAxes;

    public RadialSymmetry(int numAxes) {
        this.numAxes = numAxes;
    }

    @Override
    public List<Point> apply(List<Point> points) {
        List<Point> symmetricPoints = new ArrayList<>();
        double angleIncrement = 2 * Math.PI / numAxes;
        for (Point p : points) {
            for (int i = 1; i < numAxes; i++) {
                double angle = angleIncrement * i;
                double symX = p.x() * Math.cos(angle) - p.y() * Math.sin(angle);
                double symY = p.x() * Math.sin(angle) + p.y() * Math.cos(angle);
                symmetricPoints.add(new Point(symX, symY, p.color()));
            }
        }
        List<Point> allPoints = new ArrayList<>(points);
        allPoints.addAll(symmetricPoints);
        return allPoints;
    }
}

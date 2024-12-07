package backend.academy.symmetry;

import backend.academy.models.Point;
import java.util.ArrayList;
import java.util.List;

public class VerticalSymmetry implements Symmetry {
    @Override
    public List<Point> apply(List<Point> points) {
        List<Point> symmetricPoints = new ArrayList<>();
        for (Point p : points) {
            symmetricPoints.add(new Point(-p.x(), p.y(), p.color()));
        }
        List<Point> allPoints = new ArrayList<>(points);
        allPoints.addAll(symmetricPoints);
        return allPoints;
    }
}

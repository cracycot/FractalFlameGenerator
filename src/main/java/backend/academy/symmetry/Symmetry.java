package backend.academy.symmetry;

import backend.academy.models.Point;
import java.util.List;

public interface Symmetry {
    List<Point> apply(List<Point> points);
}

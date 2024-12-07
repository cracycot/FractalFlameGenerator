import backend.academy.models.Point;
import backend.academy.rendering.PointGenerator;
import backend.academy.transformations.Transformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PointGeneratorTest {
    @Test
    public void testGeneratePointsSingleTransformation() throws Exception {
        Transformation transformation = new Transformation(
            new double[]{1,0,0,1,1,1},
            1.0,
            (x,y) -> new Point(x, y, Color.RED)
        );

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(transformation);

        PointGenerator generator = new PointGenerator(10, 0, transformations);
        List<Point> points = generator.call();

        Assertions.assertEquals(10, points.size());
        Assertions.assertEquals(1.0, points.get(0).x(), 1e-9);
        Assertions.assertEquals(1.0, points.get(0).y(), 1e-9);
        Assertions.assertEquals(Color.RED, points.get(0).color());
    }
}

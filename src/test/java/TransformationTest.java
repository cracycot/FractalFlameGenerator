import backend.academy.models.Point;
import backend.academy.transformations.Transformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.util.function.BiFunction;

public class TransformationTest {

    @Test
    public void testApply() {
        double[] coefficients = {1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
        BiFunction<Double, Double, Point> identity = (x, y) -> new Point(x, y, Color.WHITE);
        Transformation transformation = new Transformation(coefficients, 1.0, identity);

        Point p = transformation.apply(2.0, 3.0);
        Assertions.assertEquals(2.0, p.x(), 1e-9);
        Assertions.assertEquals(3.0, p.y(), 1e-9);
        Assertions.assertEquals(Color.WHITE, p.color());
    }

    @Test
    public void testWeight() {
        double[] coefficients = {1,0,0,1,0,0};
        Transformation t = new Transformation(coefficients, 0.5, (x,y)->new Point(x,y,Color.RED));
        Assertions.assertEquals(0.5, t.weight(), 1e-9);
    }
}

import backend.academy.models.Point;
import backend.academy.rendering.FlameData;
import backend.academy.rendering.FlameDataAccumulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.util.List;

public class FlameDataAccumulatorTest {

    @Test
    public void testAccumulate() {
        FlameDataAccumulator accumulator = new FlameDataAccumulator();
        List<Point> points = List.of(
            new Point(0.0,0.0, Color.RED),
            new Point(0.5,0.5, Color.BLUE)
        );

        FlameData data = accumulator.accumulate(points,100,100,0,1,0,1);

        Assertions.assertEquals(1, data.density[0][0]);

        int px = (int)((0.5 -0)/(1-0)*(100-1));
        int py = (int)((0.5 -0)/(1-0)*(100-1));
        Assertions.assertEquals(1, data.density[py][px]);

        Assertions.assertEquals(1.0f, data.maxDensity,1e-9);
    }
}

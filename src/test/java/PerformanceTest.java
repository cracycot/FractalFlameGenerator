import backend.academy.FractalFlameFacade;
import backend.academy.symmetry.Symmetry;
import backend.academy.transformations.PredefinedTransformations;
import backend.academy.transformations.Transformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;

public class PerformanceTest {

    @Test
    public void compareSingleThreadAndMultiThreadPerformance() throws Exception {
        int width = 1600;
        int height = 1600;
        double xMin = -1.7, xMax = 1.7, yMin = -1.7, yMax = 1.7;
        long totalPoints = 10_000_000;
        int skipPoints = 10000;
        int numThreads = 3;
        Random random = new Random();
        PredefinedTransformations transformation = new PredefinedTransformations();
        List<Transformation> transformations = transformation.getDefaultTransformations(random);
        Symmetry symmetry = null;

        FractalFlameFacade fractalFlameSingle = new FractalFlameFacade();
        fractalFlameSingle.width(width);
        fractalFlameSingle.height(height);
        fractalFlameSingle.xMin(xMin);
        fractalFlameSingle.xMax(xMax);
        fractalFlameSingle.yMin(yMin);
        fractalFlameSingle.yMax(yMax);
        fractalFlameSingle.totalPoints(totalPoints);
        fractalFlameSingle.skipPoints(skipPoints);
        fractalFlameSingle.numThreads(numThreads);
        fractalFlameSingle.symmetry(symmetry);
        fractalFlameSingle.transformations(transformations);

        FractalFlameFacade fractalFlameMulti = fractalFlameSingle;

        // Прогрев (необязательный шаг): можно несколько раз вызвать одну из генераций, чтобы JVM прогрелась.
        // fractalFlameSingle.generateSingleThread();
        // fractalFlameMulti.generateMultyThreads();

        // Замер однопоточной версии
        long startSingle = System.nanoTime();
        fractalFlameSingle.generateSingleThread();
        long endSingle = System.nanoTime();
        long durationSingle = endSingle - startSingle;

        // Замер многопоточной версии
        long startMulti = System.nanoTime();
        fractalFlameMulti.generateMultyThreads();
        long endMulti = System.nanoTime();
        long durationMulti = endMulti - startMulti;

        System.out.println("Single-thread duration (ns): " + durationSingle);
        System.out.println("Multi-thread duration (ns): " + durationMulti);

        Assertions.assertTrue(durationMulti < durationSingle * 1.1,
            "Многопоточная версия не значительно быстрее однопоточной, возможно требуется оптимизация или чистые условия теста.");
    }
}

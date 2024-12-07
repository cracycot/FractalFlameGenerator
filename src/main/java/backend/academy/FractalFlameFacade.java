package backend.academy;

import backend.academy.models.Point;
import backend.academy.rendering.PointGenerator;
import backend.academy.rendering.Renderer;
import backend.academy.symmetry.HorizontalSymmetry;
import backend.academy.symmetry.RadialSymmetry;
import backend.academy.symmetry.Symmetry;
import backend.academy.symmetry.VerticalSymmetry;
import backend.academy.transformations.PredefinedTransformations;
import backend.academy.transformations.Transformation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
@Setter
@Slf4j
@SuppressWarnings({"RegexpSinglelineJava", "MultipleStringLiterals"})
public class FractalFlameFacade {
    private List<Transformation> transformations;
    private List<Point> points;
    private long totalPoints;
    private int skipPoints;
    private Symmetry symmetry;
    private int numThreads;
    private int width;
    private int height;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private Random random;

    public FractalFlameFacade() {
        this.points = new ArrayList<>();
        this.random = new Random();
    }

    @SuppressWarnings("MagicNumber")
    public void startProgramm() {
        Scanner scanner = new Scanner(System.in);

        try {
            log.info("Начало генерации фрактального пламени.");

            // Ввод данных от пользователя
            log.info("Запрос ширины изображения.");
            System.out.println("Введите ширину изображения (например, 2000):");
            width = readInt(scanner);
            log.debug("Ширина изображения: {}", width);

            log.info("Запрос высоты изображения.");
            System.out.println("Введите высоту изображения (например, 2000):");
            height = readInt(scanner);
            log.debug("Высота изображения: {}", height);

            log.info("Запрос минимального значения X.");
            System.out.println("Введите минимальное значение X (например, -1.7):");
            xMin = readDouble(scanner);
            log.debug("Минимальное значение X: {}", xMin);

            log.info("Запрос максимального значения X.");
            System.out.println("Введите максимальное значение X (например, 1.7):");
            xMax = readDouble(scanner);
            log.debug("Максимальное значение X: {}", xMax);

            log.info("Запрос минимального значения Y.");
            System.out.println("Введите минимальное значение Y (например, -1.7):");
            yMin = readDouble(scanner);
            log.debug("Минимальное значение Y: {}", yMin);

            log.info("Запрос максимального значения Y.");
            System.out.println("Введите максимальное значение Y (например, 1.7):");
            yMax = readDouble(scanner);
            log.debug("Максимальное значение Y: {}", yMax);

            log.info("Запрос количества итераций (точек).");
            System.out.println("Введите количество итераций (количество точек), например 10000000:");
            totalPoints = readLong(scanner);
            log.debug("Количество итераций: {}", totalPoints);

            log.info("Запрос количества пропускаемых начальных точек.");
            System.out.println("Введите количество пропускаемых начальных точек (skipPoints), например 0:");
            skipPoints = readInt(scanner);
            log.debug("Количество пропускаемых точек: {}", skipPoints);

            log.info("Запрос количества потоков для параллелизации.");
            System.out.println("Введите количество потоков для параллелизации (например, 4 или 10):");
            numThreads = readInt(scanner);
            log.debug("Количество потоков: {}", numThreads);

            // Выбор типа симметрии
            log.info("Выбор типа симметрии.");
            System.out.println("Выберите тип симметрии:");
            System.out.println("0 - без симметрии");
            System.out.println("1 - радиальная симметрия (например, 6 осей)");
            System.out.println("2 - вертикальная симметрия");
            System.out.println("3 - горизонтальная симметрия");
            int symmetryChoice = readInt(scanner);
            log.debug("Выбор симметрии: {}", symmetryChoice);

            switch (symmetryChoice) {
                case 1:
                    System.out.println("Введите количество осей для радиальной симметрии:");
                    int axes = readInt(scanner);
                    symmetry = new RadialSymmetry(axes);
                    log.debug("Радиальная симметрия с {} осями.", axes);
                    break;
                case 2:
                    symmetry = new VerticalSymmetry();
                    log.debug("Вертикальная симметрия.");
                    break;
                case 3:
                    symmetry = new HorizontalSymmetry();
                    log.debug("Горизонтальная симметрия.");
                    break;
                default:
                    symmetry = null;
                    log.debug("Без симметрии.");
            }

            // Выбор трансформаций
            log.info("Запрос использования предопределённого набора трансформаций.");
            System.out.println("Использовать предопределённый набор трансформаций? (y/n)");
            String usePredefined = scanner.nextLine().trim().toLowerCase();
            if (usePredefined.isEmpty()) {
                usePredefined = scanner.nextLine().trim().toLowerCase();
            }

            PredefinedTransformations predefinedTransformations = new PredefinedTransformations();
            if (usePredefined.equals("y")) {
                transformations = predefinedTransformations.getDefaultTransformations(random);
                log.info("Используется предопределённый набор трансформаций.");
            } else {
                log.warn("Пользователь отказался от использования предопределённых трансформаций.");
                System.out.println("Пока реализована только опция использования предопределенных трансформаций,"
                    + " будет использован предопределенный набор.");
                transformations = predefinedTransformations.getDefaultTransformations(random);
            }

            // Выбор режима генерации
            log.info("Запрос использования многопоточной генерации.");
            System.out.println("Вы хотите использовать многопоточную генерацию? (y/n)");
            String multithreadingChoice = scanner.nextLine().trim().toLowerCase();
            if (multithreadingChoice.isEmpty()) {
                multithreadingChoice = scanner.nextLine().trim().toLowerCase();
            }

            if (multithreadingChoice.equals("y")) {
                log.info("Выбран многопоточный режим генерации.");
                generateMultyThreads();
            } else {
                log.info("Выбран однопоточный режим генерации.");
                generateSingleThread();
            }

            // Рендеринг изображения
            Renderer renderer = new Renderer(width, height, xMin, xMax, yMin, yMax);
            log.info("Начало рендеринга изображения.");
            BufferedImage image = renderer.render(points);
            log.info("Рендеринг завершён.");

            // Сохранение изображения
            System.out.println("Введите имя файла для сохранения изображения (например, fractal_flame.png):");
            String filename = scanner.nextLine().trim();
            if (filename.isEmpty()) {
                filename = "fractal_flame.png";
            }
            log.info("Сохранение изображения в файл: {}", filename);
            renderer.saveImage(image, filename);
            log.info("Фрактальное пламя успешно сгенерировано и сохранено как {}", filename);
        } catch (Exception e) {
            log.error("Произошла ошибка при генерации фрактального пламени.", e);
        } finally {
            scanner.close();
            log.info("Завершение программы.");
        }
    }

    public void generateMultyThreads() throws InterruptedException, ExecutionException {
        log.info("Запуск многопоточной генерации точек с {} потоками.", numThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<List<Point>>> futures = new ArrayList<>();

        long pointsPerThread = totalPoints / numThreads;
        log.debug("Точек на поток: {}", pointsPerThread);

        for (int i = 0; i < numThreads; i++) {
            futures.add(executor.submit(new PointGenerator(pointsPerThread, skipPoints, transformations)));
        }

        for (Future<List<Point>> future : futures) {
            points.addAll(future.get());
        }

        executor.shutdown();
        log.info("Многопоточная генерация точек завершена.");

        if (symmetry != null) {
            log.info("Применение симметрии: {}", symmetry.getClass().getSimpleName());
            points = symmetry.apply(points);
            log.debug("Количество точек после применения симметрии: {}", points.size());
        }
    }

    public void generateSingleThread() {
        log.info("Запуск однопоточной генерации точек.");
        PointGenerator pointGenerator = new PointGenerator(totalPoints, skipPoints, transformations);
        points = pointGenerator.call();
        log.info("Однопоточная генерация точек завершена. Количество точек: {}", points.size());

        if (symmetry != null) {
            log.info("Применение симметрии: {}", symmetry.getClass().getSimpleName());
            points = symmetry.apply(points);
            log.debug("Количество точек после применения симметрии: {}", points.size());
        }
    }

    private int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                log.warn("Неверный формат числа: '{}'. Требуется целое число.", line);
                System.out.println("Неверный формат числа, попробуйте ещё раз:");
            }
        }
    }

    private long readLong(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Long.parseLong(line);
            } catch (NumberFormatException e) {
                log.warn("Неверный формат числа: '{}'. Требуется целое число.", line);
                System.out.println("Неверный формат числа, попробуйте ещё раз:");
            }
        }
    }

    private double readDouble(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line.replace(',', '.'));
            } catch (NumberFormatException e) {
                log.warn("Неверный формат числа: '{}'. Требуется число с плавающей точкой.", line);
                System.out.println("Неверный формат числа, попробуйте ещё раз:");
            }
        }
    }
}

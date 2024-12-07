import backend.academy.models.Point;
import backend.academy.rendering.Renderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

public class RendererTest {
    @Test
    public void testRenderAndSaveImage() throws Exception {
        Renderer renderer = new Renderer(100,100,0,1,0,1);
        List<Point> points = List.of(new Point(0.5,0.5, Color.RED));
        BufferedImage image = renderer.render(points);
        Assertions.assertNotNull(image);

        boolean foundNonBlack = false;
        for (int y=0; y<100; y++){
            for (int x=0; x<100; x++){
                if ((image.getRGB(x,y)&0xFFFFFF)!=0){
                    foundNonBlack = true;
                    break;
                }
            }
            if (foundNonBlack) break;
        }

        Assertions.assertTrue(foundNonBlack);

        renderer.saveImage(image,"test_output.png");
    }
}

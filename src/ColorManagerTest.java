import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ColorManagerTest {
    private ColorManager colorManager;

    @BeforeEach
    public void Setup() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);

        int[] colorBuffer = new int[9];
        for (int i = 0; i < 9; i++) {
            colorBuffer[i] = new Color(i, i, i, 255).getRGB();
        }

        image.setRGB(0, 0, 3, 3, colorBuffer, 0, 3);

        colorManager = new ColorManager(image);
    }

    @Test
    public void TestConstructor() {
        for (int i = 0; i < 9; i++) {
            Color color = new Color(colorManager.Red[i] & 0xFF, colorManager.Green[i] & 0xFF, colorManager.Blue[i] & 0xFF);

            assertEquals(new Color(i ,i, i).getRGB(), color.getRGB());
        }

        assertEquals(3, colorManager.Width);
        assertEquals(3, colorManager.Height);
    }

    @Test
    public void TestShrink() {
        colorManager.Shrink(1, 1);

        assertEquals(new Color(4, 4, 4).getRGB(), new Color(colorManager.Red[0] & 0xFF, colorManager.Green[0] & 0xFF, colorManager.Blue[0] & 0xFF).getRGB());
    }
}
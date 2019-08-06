import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ASCIIArtGeneratorTest {
    @Test
    public void TestHTML() {
        byte[] red = new byte[9];
        byte[] green = new byte[9];
        byte[] blue = new byte[9];

        for (int i = 0; i < 9; i++) {
            red[i] = (byte)i;
            green[i] = (byte)i;
            blue[i] = (byte)i;
        }

        String html = "<!DOCTYPE html><head><style>p {display: inline; margin: 0;font-size: 10px;}body {background-color: black; line-height: 10px;}</style></head><body><p style='color: #000000;'>#</p><p style='color: #010101;'>#</p><p style='color: #020202;'>#</p><br><p style='color: #030303;'>#</p><p style='color: #040404;'>#</p><p style='color: #050505;'>#</p><br><p style='color: #060606;'>#</p><p style='color: #070707;'>#</p><p style='color: #080808;'>#</p><br></body>";

        assertEquals(html, ASCIIArtGenerator.HTML(3, 3, red, green, blue));
    }

    @Test
    public void TestBufferedImage() {
        byte[] red = new byte[9];
        byte[] green = new byte[9];
        byte[] blue = new byte[9];

        for (int i = 0; i < 9; i++) {
            red[i] = (byte)i;
            green[i] = (byte)i;
            blue[i] = (byte)i;
        }

        String html = "<!DOCTYPE html><head><style>p {display: inline; margin: 0;font-size: 10px;}body {background-color: black; line-height: 10px;}</style></head><body><p style='color: #000000;'>#</p><p style='color: #010101;'>#</p><p style='color: #020202;'>#</p><br><p style='color: #030303;'>#</p><p style='color: #040404;'>#</p><p style='color: #050505;'>#</p><br><p style='color: #060606;'>#</p><p style='color: #070707;'>#</p><p style='color: #080808;'>#</p><br></body>";

        BufferedImage image = ASCIIArtGenerator.BufferedImage(html);

        for (int i = 0; i < 9; i++) {
            assertEquals(new Color(red[i] & 0xFF, green[i] & 0xFF, blue[i] & 0xFF).getRGB(), image.getRGB(i % 3, i / 3));
        }
    }
}
import java.awt.*;
import java.awt.image.BufferedImage;

public class ASCIIArtGenerator {
    // Converts color channel arrays into an HTML string that displays the ascii art
    public static String HTML(int width, int height, byte[] red, byte[] green, byte[] blue) {
        String html = "<!DOCTYPE html><head><style>p {display: inline; margin: 0;font-size: 10px;}body {background-color: black; line-height: 10px;}</style></head><body>";
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                String r = Integer.toHexString(red[index] & 0xFF).length() == 1 ? "0" + Integer.toHexString(red[index] & 0xFF) : Integer.toHexString(red[index] & 0xFF);
                String g = Integer.toHexString(green[index] & 0xFF).length() == 1 ? "0" + Integer.toHexString(green[index] & 0xFF) : Integer.toHexString(green[index] & 0xFF);
                String b = Integer.toHexString(blue[index] & 0xFF).length() == 1 ? "0" + Integer.toHexString(blue[index] & 0xFF) : Integer.toHexString(blue[index] & 0xFF);
                html += "<p style='color: #" + r + g + b + ";'>#</p>";
            }
            html += "<br>";
        }
        html += "</body>";

        return html;
    }

    // Converts a previously generated image into a buffered image
    public static BufferedImage BufferedImage(String html) {
        html = html.replace("<!DOCTYPE html><head><style>p {display: inline; margin: 0;font-size: 10px;}body {background-color: black; line-height: 10px;}</style></head><body>", "");

        int height = 0;
        int width = 0;
        int lastIndex = 0;

        while (lastIndex != -1) {
            lastIndex = html.indexOf("<br>", lastIndex);

            if (lastIndex != -1) {
                height++;
                lastIndex += "<br>".length();
            }
        }

        html = html.replace("<br>", "").replace("<p style='color: #", "").replace(";'>#</p>", "").replace("</body>", "").replace(";'", "");

        width = html.length() / 6 / height;

        int[] colorBuffer = new int[html.length()/6];

        for (int i = 0; i < html.length(); i += 6) {
            int red = Integer.parseInt(html.substring(i, i + 2), 16);
            int green = Integer.parseInt(html.substring(i + 2, i + 4), 16);
            int blue = Integer.parseInt(html.substring(i + 4, i + 6), 16);

            colorBuffer[i/6] = new Color(red, green, blue, 255).getRGB();
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        image.setRGB(0, 0, width, height, colorBuffer, 0, width);

        return image;
    }
}
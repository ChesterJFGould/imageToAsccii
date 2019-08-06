import java.awt.image.BufferedImage;
import java.awt.Color;

public class ColorManager {
    // Loads values taken from image into class
    public ColorManager(BufferedImage image) {
        Height = image.getHeight();
        Width = image.getWidth();

        int imageLength = Height * Width;
        Red = new byte[imageLength];
        Green = new byte[imageLength];
        Blue = new byte[imageLength];

        for (int i = 0; i < Height * Width; i++) {
            Color color = new Color(image.getRGB(i % Width, i / Width));
            Red[i] = (byte)color.getRed();
            Green[i] = (byte)color.getGreen();
            Blue[i] = (byte)color.getBlue();
        }
    }

    public int Height;
    public int Width;

    // Stores each color channel as a byte array to increase cpu cache efficiency and speed up time that shrink takes

    public byte[] Red;
    public byte[] Green;
    public byte[] Blue;

    // Shrinks the loaded image to the given size by taking the average color of determined areas
    public void Shrink(int width, int height) {
        if (width >= Width && height >= Height) {
            return;
        }

        // Performs a kinda abstract image convolution

        int convolutionWidth = (int)((float)Width / (float)width);
        int convolutionHeight = (int)((float)Height / (float)height);
        int convolutionLength = convolutionHeight * convolutionWidth;

        int newLength = width * height;

        byte[] newRed = new byte[newLength];
        byte[] newGreen = new byte[newLength];
        byte[] newBlue = new byte[newLength];

        // Somewhat complicated iteration where each for loop is doing the work of two for efficiency
        // Calculates average color for each determined area

        for (int i = 0; i < newLength; i++) {
            int buf = 0;
            for (int j = 0; j < convolutionLength; j++) {
                // So this goes [ y coordinate on old image + x coordinate on old image + y coordinate of current convolution step + x coordinate of current convolution step ]
                buf += Red[((Width * convolutionHeight) * (i / width)) + (convolutionWidth * (i % width)) + (Width * (j / convolutionWidth)) + (j % convolutionWidth)];
            }
            newRed[i] = (byte)(buf / convolutionLength);
        }

        for (int i = 0; i < newLength; i++) {
            int buf = 0;
            for (int j = 0; j < convolutionLength; j++) {
                buf += Green[((Width * convolutionHeight) * (i / width)) + (convolutionWidth * (i % width)) + (Width * (j / convolutionWidth)) + (j % convolutionWidth)];
            }
            newGreen[i] = (byte)(buf / convolutionLength);
        }

        for (int i = 0; i < newLength; i++) {
            int buf = 0;
            for (int j = 0; j < convolutionLength; j++) {
                buf += Blue[((Width * convolutionHeight) * (i / width)) + (convolutionWidth * (i % width)) + (Width * (j / convolutionWidth)) + (j % convolutionWidth)];
            }
            newBlue[i] = (byte)(buf / convolutionLength);
        }
        
        Width = width;
        Height = height;

        Red = newRed;
        Green = newGreen;
        Blue = newBlue;
    }

    // Converts the color channel arrays into a buffered image
    public BufferedImage GetImage() {
        int imageLength = Width * Height;

        int[] colorBuffer = new int[imageLength];

        for (int i = 0; i < imageLength; i++) {
            // Byte & 0xFF turns byte into positive number as bytes are signed in java for some reason?
            colorBuffer[i] = new Color(Red[i] & 0xFF, Green[i] & 0xFF, Blue[i] & 0xFF, 255).getRGB();
        }

        BufferedImage image = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_ARGB);

        image.setRGB(0, 0, Width, Height, colorBuffer, 0, Width);

        return image;
    }
}
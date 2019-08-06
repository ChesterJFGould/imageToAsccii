import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Controller {
    @FXML
    public void initialize() {
        // Adds text property listeners so the user can't generate an Html image until they've entered a valid int into both inputs
        newWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newWidth.getText().isEmpty() || newHeight.getText().isEmpty()) {
                return;
            }

            int width;
            int height;

            try {
                width = Integer.parseInt(newWidth.getText());
                height = Integer.parseInt(newHeight.getText());
            } catch (NumberFormatException e) {
                return;
            }

            if (width > colorManager.Width || height > colorManager.Height) {
                return;
            }

            generate.setDisable(false);
        });

        newHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newWidth.getText().isEmpty() || newHeight.getText().isEmpty()) {
                return;
            }

            int width;
            int height;

            try {
                width = Integer.parseInt(newWidth.getText());
                height = Integer.parseInt(newHeight.getText());
            } catch (NumberFormatException e) {
                return;
            }

            if (width > colorManager.Width || height > colorManager.Height) {
                return;
            }

            generate.setDisable(false);
        });
    }

    private ColorManager colorManager;
    private ASCIIArtGenerator artGenerator;

    // Asks user to choose an image then loads it into the program
    @FXML
    private void openImageFromDialog() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.jpg", "*.jpeg", "*.png")
        );

        File file = fileChooser.showOpenDialog(webView.getScene().getWindow());

        if (file == null) {
            return;
        }

        colorManager = new ColorManager(ImageIO.read(file));

        Image image = new Image(file.toURI().toString());

        imageView.setImage(image);

        imageWidth.setText(Double.toString(image.getWidth()));
        imageHeight.setText(Double.toString(image.getHeight()));
    }

    // Generates html from the loaded image and loads it into the web view
    @FXML
    private void generateHtml() {
           if (colorManager == null) {
               return;
           }
           saveHtmlMenuItem.setDisable(false);
           webView.getEngine().loadContent("");

           int width = Integer.parseInt(newWidth.getText());
           int height = Integer.parseInt(newHeight.getText());

           colorManager.Shrink(width, height);

           String html = ASCIIArtGenerator.HTML(colorManager.Width, colorManager.Height, colorManager.Red, colorManager.Green, colorManager.Blue);

           webView.getEngine().loadContent(html);

            // Sets zoom to try to fit the full ascii art into the image, doesn't work all the time, but it's trying its best
           double webViewHeight = webView.getHeight();
           double webViewWidth = webView.getWidth();

           double zoomWidth = webViewWidth / (colorManager.Width * 8);
           double zoomHeight = webViewHeight / (colorManager.Height * 8);

           webView.setZoom(zoomWidth > zoomHeight ? zoomHeight : zoomWidth);
    }

    // Saves the loaded html into a file
    @FXML
    private void saveHtml() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File To Save HTML To");

        File file = fileChooser.showSaveDialog(webView.getScene().getWindow());

        if (file == null) {
            return;
        }

        PrintWriter writer = new PrintWriter(file);
        writer.print(ASCIIArtGenerator.HTML(colorManager.Width, colorManager.Height, colorManager.Red, colorManager.Green, colorManager.Blue));
        writer.close();
    }

    // Loads saved html into the weview, generates an image based on the html and loads that into the image view
    @FXML
    private void loadHtml() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Previously Generated HTML File");

        File file = fileChooser.showOpenDialog(webView.getScene().getWindow());

        if (file == null) {
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String html = "";

        String line;

        while ((line = reader.readLine()) != null) {
            html += line;
        }

        webView.getEngine().loadContent(html);

        BufferedImage image = ASCIIArtGenerator.BufferedImage(html);

        colorManager = new ColorManager(image);

        file = File.createTempFile("image", "png");
        ImageIO.write(image, "png", file);

        Image img = new Image(file.toURI().toString());

        imageView.setImage(img);

        // Sets zoom to try to fit the full ascii art into the image, doesn't work all the time, but it's trying its best
        double webViewHeight = webView.getHeight();
        double webViewWidth = webView.getWidth();

        double zoomWidth = webViewWidth / (colorManager.Width * 8);
        double zoomHeight = webViewHeight / (colorManager.Height * 8);

        webView.setZoom(zoomWidth > zoomHeight ? zoomHeight : zoomWidth);
    }

    @FXML
    public MenuItem openImageMenuItem;

    @FXML
    public MenuItem saveHtmlMenuItem;

    @FXML
    public ImageView imageView;

    @FXML
    public WebView webView;

    @FXML
    public Label imageWidth;

    @FXML
    public Label imageHeight;

    @FXML
    public TextField newWidth;

    @FXML
    public TextField newHeight;

    @FXML
    public Button generate;
}

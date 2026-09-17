package space.bl1nd.svdcompression.image;

import space.bl1nd.svdcompression.exception.ImageProcessingException;
import space.bl1nd.svdcompression.validation.InputValidator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 * Reads a validated image through Java ImageIO.
 */
public final class ImageReader {
    private final InputValidator validator;

    public ImageReader(InputValidator validator) {
        this.validator = validator;
    }

    public ImageData read(Path path) {
        validator.validateInput(path);
        try {
            BufferedImage image = ImageIO.read(path.toFile());
            if (image == null) throw new ImageProcessingException("The file is not a readable image: " + path);
            return new ImageData(image, path);
        } catch (IOException e) {
            throw new ImageProcessingException("Could not read image: " + path, e);
        }
    }
}

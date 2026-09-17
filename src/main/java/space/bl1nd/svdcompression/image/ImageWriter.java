package space.bl1nd.svdcompression.image;

import space.bl1nd.svdcompression.exception.ImageProcessingException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 * Writes an image, creating explicitly requested parent directories.
 */
public final class ImageWriter {
    public void write(BufferedImage image, Path output, String format) {
        try {
            if (output.getParent() != null) Files.createDirectories(output.getParent());
            if (!ImageIO.write(image, format, output.toFile()))
                throw new ImageProcessingException("No writer is available for format: " + format);
        } catch (IOException e) {
            throw new ImageProcessingException("Could not write output image: " + output, e);
        }
    }
}

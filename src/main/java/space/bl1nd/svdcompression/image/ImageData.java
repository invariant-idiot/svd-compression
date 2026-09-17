package space.bl1nd.svdcompression.image;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

/**
 * Loaded image and its source path.
 */
public record ImageData(BufferedImage image, Path source) {
}

package space.bl1nd.svdcompression.compression;

import java.awt.image.BufferedImage;

/**
 * Reconstructed image and statistics emitted by the compression service.
 */
public record CompressionResult(BufferedImage image, CompressionStatistics statistics) {
}

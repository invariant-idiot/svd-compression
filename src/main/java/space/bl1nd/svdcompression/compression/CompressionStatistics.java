package space.bl1nd.svdcompression.compression;

import space.bl1nd.svdcompression.config.ColorMode;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Measured result details; encoded size is deliberately separate from SVD quality.
 */
public record CompressionStatistics(int width, int height, ColorMode mode, int rank, int maximumRank,
                                    long originalBytes, long outputBytes, long elapsedMillis, double mse, double psnr) {
    public static CompressionStatistics create(int width, int height, ColorMode mode, int rank, int max, Path input, Path output, long elapsed, double mse, double psnr) {
        try {
            return new CompressionStatistics(width, height, mode, rank, max, Files.size(input), Files.size(output), elapsed, mse, psnr);
        } catch (Exception e) {
            throw new IllegalStateException("Could not measure file sizes.", e);
        }
    }

    public double ratio() {
        return outputBytes == 0 ? Double.POSITIVE_INFINITY : (double) originalBytes / outputBytes;
    }

    public double reduction() {
        return originalBytes == 0 ? 0 : 100.0 * (originalBytes - outputBytes) / originalBytes;
    }
}

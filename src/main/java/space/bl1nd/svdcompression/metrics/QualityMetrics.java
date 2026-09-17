package space.bl1nd.svdcompression.metrics;

import java.awt.image.BufferedImage;

/**
 * Calculates reconstruction error in the RGB sample space.
 */
public final class QualityMetrics {
    public double mse(BufferedImage original, BufferedImage reconstructed) {
        if (original.getWidth() != reconstructed.getWidth() || original.getHeight() != reconstructed.getHeight())
            throw new IllegalArgumentException("Images must have the same dimensions.");
        double error = 0;
        long count = (long) original.getWidth() * original.getHeight() * 3;
        for (int y = 0; y < original.getHeight(); y++)
            for (int x = 0; x < original.getWidth(); x++) {
                int a = original.getRGB(x, y), b = reconstructed.getRGB(x, y);
                for (int shift : new int[]{16, 8, 0}) {
                    double d = ((a >> shift) & 255) - ((b >> shift) & 255);
                    error += d * d;
                }
            }
        return error / count;
    }

    public double psnr(double mse) {
        return mse == 0.0 ? Double.POSITIVE_INFINITY : 10 * Math.log10((255.0 * 255.0) / mse);
    }
}

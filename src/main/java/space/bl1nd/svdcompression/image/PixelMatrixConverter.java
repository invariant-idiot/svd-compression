package space.bl1nd.svdcompression.image;

import space.bl1nd.svdcompression.config.ColorMode;

import java.awt.image.BufferedImage;

/**
 * Converts pixels to numerical channel matrices and reconstructs clamped images.
 */
public final class PixelMatrixConverter {
    public PixelMatrices toMatrices(BufferedImage image, ColorMode requested) {
        boolean gray = requested == ColorMode.GRAYSCALE || (requested == ColorMode.AUTO && image.getColorModel().getNumColorComponents() == 1);
        int h = image.getHeight(), w = image.getWidth();
        double[][] r = new double[h][w], g = gray ? null : new double[h][w], b = gray ? null : new double[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int p = image.getRGB(x, y);
                int rv = (p >> 16) & 255, gv = (p >> 8) & 255, bv = p & 255;
                if (gray) r[y][x] = 0.299 * rv + 0.587 * gv + 0.114 * bv;
                else {
                    r[y][x] = rv;
                    g[y][x] = gv;
                    b[y][x] = bv;
                }
            }
        return new PixelMatrices(r, g, b, gray);
    }

    public BufferedImage toImage(PixelMatrices matrices) {
        int h = matrices.red().length, w = matrices.red()[0].length;
        BufferedImage image = new BufferedImage(w, h, matrices.grayscale() ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int r = clamp(matrices.red()[y][x]);
                if (matrices.grayscale()) image.setRGB(x, y, (r << 16) | (r << 8) | r);
                else
                    image.setRGB(x, y, (clamp(matrices.red()[y][x]) << 16) | (clamp(matrices.green()[y][x]) << 8) | clamp(matrices.blue()[y][x]));
            }
        return image;
    }

    public static int clamp(double value) {
        return (int) Math.max(0, Math.min(255, Math.round(value)));
    }
}

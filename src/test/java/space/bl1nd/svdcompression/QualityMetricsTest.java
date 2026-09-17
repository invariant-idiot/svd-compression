package space.bl1nd.svdcompression;

import space.bl1nd.svdcompression.metrics.QualityMetrics;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QualityMetricsTest {
    @Test
    void identicalImagesHaveZeroMseAndInfinitePsnr() {
        BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        QualityMetrics m = new QualityMetrics();
        assertEquals(0, m.mse(i, i));
        assertTrue(Double.isInfinite(m.psnr(0)));
    }

    @Test
    void computesRgbMse() {
        BufferedImage a = new BufferedImage(1, 1, 1), b = new BufferedImage(1, 1, 1);
        b.setRGB(0, 0, 0x030000);
        assertEquals(3, mse(a, b), 1e-9);
    }

    private double mse(BufferedImage a, BufferedImage b) {
        return new QualityMetrics().mse(a, b);
    }
}

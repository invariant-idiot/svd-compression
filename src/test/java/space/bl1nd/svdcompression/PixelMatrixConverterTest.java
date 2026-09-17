package space.bl1nd.svdcompression;

import space.bl1nd.svdcompression.config.ColorMode;
import space.bl1nd.svdcompression.image.*;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import space.bl1nd.svdcompression.image.PixelMatrices;
import space.bl1nd.svdcompression.image.PixelMatrixConverter;

import static org.junit.jupiter.api.Assertions.*;

class PixelMatrixConverterTest {
    @Test
    void roundTripsRgbAndClamps() {
        BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        i.setRGB(0, 0, 0x0A141E);
        PixelMatrixConverter c = new PixelMatrixConverter();
        PixelMatrices p = c.toMatrices(i, ColorMode.RGB);
        assertEquals(10, p.red()[0][0]);
        BufferedImage out = c.toImage(new PixelMatrices(new double[][]{{-2}}, new double[][]{{42.4}}, new double[][]{{300}}, false));
        assertEquals(0, (out.getRGB(0, 0) >> 16) & 255);
        assertEquals(42, (out.getRGB(0, 0) >> 8) & 255);
        assertEquals(255, out.getRGB(0, 0) & 255);
    }

    @Test
    void convertsGrayscale() {
        BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        i.setRGB(0, 0, 0xFF0000);
        assertTrue(new PixelMatrixConverter().toMatrices(i, ColorMode.GRAYSCALE).grayscale());
    }
}

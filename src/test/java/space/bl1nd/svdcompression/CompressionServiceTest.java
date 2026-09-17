package space.bl1nd.svdcompression;

import space.bl1nd.svdcompression.config.ColorMode;
import space.bl1nd.svdcompression.config.CompressionConfig;
import space.bl1nd.svdcompression.service.CompressionService;

import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompressionServiceTest {
    @Test
    void performsEndToEndCompression() throws Exception {
        Path d = Files.createTempDirectory("svd-test");
        Path in = d.resolve("in.png"), out = d.resolve("out.png");
        BufferedImage image = new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++)
            for (int x = 0; x < 3; x++) image.setRGB(x, y, (x * 90 << 16) | (y * 100 << 8) | 50);
        ImageIO.write(image, "png", in.toFile());
        var c = new CompressionConfig(in, out, 1, null, null, ColorMode.RGB, "png", false, false, true);
        var result = new CompressionService().compress(c, message -> {
        });
        assertTrue(Files.exists(out));
        assertEquals(3, ImageIO.read(out.toFile()).getWidth());
        assertEquals(1, result.statistics().rank());
    }
}

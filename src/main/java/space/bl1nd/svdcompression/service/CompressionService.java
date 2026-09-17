package space.bl1nd.svdcompression.service;

import space.bl1nd.svdcompression.compression.CompressionResult;
import space.bl1nd.svdcompression.compression.CompressionStatistics;
import space.bl1nd.svdcompression.compression.SvdCompressor;
import space.bl1nd.svdcompression.config.ColorMode;
import space.bl1nd.svdcompression.config.CompressionConfig;
import space.bl1nd.svdcompression.image.*;
import space.bl1nd.svdcompression.metrics.QualityMetrics;
import space.bl1nd.svdcompression.validation.InputValidator;

import java.awt.image.BufferedImage;

public final class CompressionService {
    private final ImageReader reader;
    private final ImageWriter writer;
    private final PixelMatrixConverter converter;
    private final SvdCompressor compressor;
    private final QualityMetrics metrics;
    private final InputValidator validator;

    public CompressionService() {
        validator = new InputValidator();
        reader = new ImageReader(validator);
        writer = new ImageWriter();
        converter = new PixelMatrixConverter();
        compressor = new SvdCompressor();
        metrics = new QualityMetrics();
    }

    public CompressionResult compress(CompressionConfig config, Progress progress) {
        long start = System.nanoTime();
        ImageData source = reader.read(config.input());
        BufferedImage original = source.image();
        if ((long) original.getWidth() * original.getHeight() > 16_000_000L)
            progress.message("Warning: this large image may require substantial memory and time.");
        String format = config.format();
        validator.validateOutput(config.input(), config.output(), format, config.overwrite());
        PixelMatrices input = converter.toMatrices(original, config.mode());
        int maximum = Math.min(original.getWidth(), original.getHeight());
        int rank = config.requestedRank(maximum).orElseThrow();
        validator.validateRank(rank, maximum);
        progress.message(input.grayscale() ? "[1/1] Compressing grayscale channel" : "[1/3] Compressing red channel");
        double[][] r = compressor.compress(input.red(), rank);
        double[][] g = null, b = null;
        if (!input.grayscale()) {
            progress.message("[2/3] Compressing green channel");
            g = compressor.compress(input.green(), rank);
            progress.message("[3/3] Compressing blue channel");
            b = compressor.compress(input.blue(), rank);
        }
        BufferedImage output = converter.toImage(new PixelMatrices(r, g, b, input.grayscale()));
        progress.message("Calculating quality metrics and saving output...");
        writer.write(output, config.output(), format);
        double mse = metrics.mse(original, output);
        double psnr = metrics.psnr(mse);
        ColorMode actual = input.grayscale() ? ColorMode.GRAYSCALE : ColorMode.RGB;
        return new CompressionResult(output, CompressionStatistics.create(original.getWidth(), original.getHeight(), actual, rank, maximum, config.input(), config.output(), (System.nanoTime() - start) / 1_000_000, mse, psnr));
    }

    @FunctionalInterface
    public interface Progress {
        void message(String text);
    }
}

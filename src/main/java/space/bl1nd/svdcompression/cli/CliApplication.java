package space.bl1nd.svdcompression.cli;

import space.bl1nd.svdcompression.compression.*;
import space.bl1nd.svdcompression.compression.CompressionResult;
import space.bl1nd.svdcompression.compression.CompressionStatistics;
import space.bl1nd.svdcompression.config.CompressionConfig;
import space.bl1nd.svdcompression.service.CompressionService;

/**
 * CLI boundary that turns normal failures into concise user-facing messages.
 */
public final class CliApplication {
    public void run(String[] args) {
        try {
            if (args.length == 1 && args[0].equals("--help")) {
                new HelpPrinter().print();
                return;
            }
            if (args.length == 1 && args[0].equals("--version")) {
                System.out.println("SVD Image Compressor 1.0.0");
                return;
            }
            CompressionConfig config = new CliParser().parse(args.length == 0 ? new InteractiveCli().prompt() : args);
            CompressionResult result = new CompressionService().compress(config, text -> {
                if (!config.quiet()) System.out.println(text);
            });
            print(result.statistics());
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (OutOfMemoryError e) {
            System.err.println("Error: insufficient memory to process this image.");
        }
    }

    private void print(CompressionStatistics s) {
        System.out.printf("%nCompression Summary%n-------------------%nDimensions: %d x %d%nMode: %s%nRank: %d / %d%nOriginal size: %d bytes%nOutput size: %d bytes%nCompression ratio: %.2f : 1%nSize reduction: %.1f%%%nMSE: %.4f%nPSNR: %s%nProcessing time: %.3f seconds%n", s.width(), s.height(), s.mode(), s.rank(), s.maximumRank(), s.originalBytes(), s.outputBytes(), s.ratio(), s.reduction(), s.mse(), Double.isInfinite(s.psnr()) ? "Infinity dB" : String.format("%.4f dB", s.psnr()), s.elapsedMillis() / 1000.0);
    }
}

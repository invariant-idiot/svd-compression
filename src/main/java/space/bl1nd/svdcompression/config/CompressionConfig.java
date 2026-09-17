package space.bl1nd.svdcompression.config;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Immutable command configuration. Compression is the percent of SVD components retained.
 */
public record CompressionConfig(Path input, Path output, Integer rank, Integer compression,
                                Quality quality, ColorMode mode, String format,
                                boolean overwrite, boolean verbose, boolean quiet) {
    public Optional<Integer> requestedRank(int maximumRank) {
        if (rank != null) return Optional.of(rank);
        int percent = compression != null ? compression : quality != null ? quality.retainedPercent() : Quality.MEDIUM.retainedPercent();
        return Optional.of(Math.max(1, (int) Math.ceil(maximumRank * percent / 100.0)));
    }
}

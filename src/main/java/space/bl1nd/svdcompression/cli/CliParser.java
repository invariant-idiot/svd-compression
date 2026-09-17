package space.bl1nd.svdcompression.cli;

import space.bl1nd.svdcompression.config.ColorMode;
import space.bl1nd.svdcompression.config.CompressionConfig;
import space.bl1nd.svdcompression.config.Quality;
import space.bl1nd.svdcompression.exception.UserInputException;
import space.bl1nd.svdcompression.validation.InputValidator;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

/**
 * Parses and validates command-line arguments for the SVD compressor.
 */
public final class CliParser {

    private static final String OUTPUT_FORMAT = "png";

    public CompressionConfig parse(String[] args) {
        ParsedArguments parsed = parseArguments(args);

        validate(parsed);

        Path output = resolveOutput(parsed.input(), parsed.output());

        return new CompressionConfig(
                parsed.input(),
                output,
                parsed.rank(),
                parsed.compression(),
                parsed.quality(),
                parsed.mode(),
                OUTPUT_FORMAT,
                parsed.overwrite(),
                parsed.verbose(),
                parsed.quiet()
        );
    }

    private static ParsedArguments parseArguments(String[] args) {
        Path input = null;
        Path output = null;
        Integer rank = null;
        Integer compression = null;
        Quality quality = null;
        ColorMode mode = ColorMode.AUTO;

        boolean overwrite = false;
        boolean verbose = false;
        boolean quiet = false;

        for (int i = 0; i < args.length; i++) {
            String option = args[i];

            switch (option) {
                case "--input" -> input = Path.of(nextValue(args, ++i, option));

                case "--output" -> output = Path.of(nextValue(args, ++i, option));

                case "--rank" -> rank = parseInteger(
                        nextValue(args, ++i, option),
                        "rank"
                );

                case "--compression" -> compression = parseInteger(
                        nextValue(args, ++i, option),
                        "compression percentage"
                );

                case "--quality" -> quality = parseEnum(
                        Quality.class,
                        nextValue(args, ++i, option),
                        "quality"
                );

                case "--mode" -> mode = parseEnum(
                        ColorMode.class,
                        nextValue(args, ++i, option),
                        "mode"
                );

                case "--overwrite" -> overwrite = true;
                case "--verbose" -> verbose = true;
                case "--quiet" -> quiet = true;

                default -> throw new UserInputException(
                        "Unknown option: " + option +
                                ". Use --help for usage."
                );
            }
        }

        return new ParsedArguments(
                input,
                output,
                rank,
                compression,
                quality,
                mode,
                overwrite,
                verbose,
                quiet
        );
    }

    private static void validate(ParsedArguments args) {
        validateInput(args.input());
        validateInputFormat(args.input());
        validateCompressionOptions(args);
        validateCompressionPercentage(args.compression());
        validateVerbosity(args);
    }

    private static void validateInput(Path input) {
        if (input == null) {
            throw new UserInputException(
                    "Missing required option --input. Use --help for usage."
            );
        }
    }

    private static void validateInputFormat(Path input) {
        String extension = InputValidator.extension(input)
                .toLowerCase(Locale.ROOT);

        if (!extension.equals("png") && !extension.equals("bmp")) {
            throw new UserInputException(
                    "Unsupported input format: " + extension +
                            ". Supported input formats: png, bmp."
            );
        }
    }

    private static void validateCompressionOptions(ParsedArguments args) {
        int selectedOptions = countNonNull(
                args.rank(),
                args.compression(),
                args.quality()
        );

        if (selectedOptions > 1) {
            throw new UserInputException(
                    "Use only one of --rank, --compression, or --quality."
            );
        }
    }

    private static void validateCompressionPercentage(Integer compression) {
        if (compression != null && (compression < 1 || compression > 100)) {
            throw new UserInputException(
                    "Compression percentage must be between 1 and 100 " +
                            "(retained SVD components)."
            );
        }
    }

    private static void validateVerbosity(ParsedArguments args) {
        if (args.verbose() && args.quiet()) {
            throw new UserInputException(
                    "Use either --verbose or --quiet, not both."
            );
        }
    }

    private static Path resolveOutput(Path input, Path output) {
        if (output != null) {
            return output;
        }

        return defaultOutput(input);
    }

    private static Path defaultOutput(Path input) {
        String fileName = input.getFileName().toString();
        String stem = removeExtension(fileName);

        Path parent = input.toAbsolutePath().getParent();

        if (parent == null) {
            parent = Path.of("");
        }

        return parent.resolve(stem + "_compressed." + OUTPUT_FORMAT);
    }

    private static String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        return dotIndex < 0
                ? fileName
                : fileName.substring(0, dotIndex);
    }

    private static String nextValue(
            String[] args,
            int index,
            String option
    ) {
        if (index >= args.length || args[index].startsWith("--")) {
            throw new UserInputException(
                    "Missing value for " + option + "."
            );
        }

        return args[index];
    }

    private static int parseInteger(String value, String label) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new UserInputException(
                    "Invalid " + label + ": " + value
            );
        }
    }

    private static <T extends Enum<T>> T parseEnum(
            Class<T> enumType,
            String value,
            String label
    ) {
        try {
            return Enum.valueOf(
                    enumType,
                    value.toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException e) {
            throw new UserInputException(
                    "Invalid " + label + ": " + value +
                            ". Allowed values: " +
                            Arrays.toString(enumType.getEnumConstants())
            );
        }
    }

    private static int countNonNull(Object... values) {
        int count = 0;

        for (Object value : values) {
            if (value != null) {
                count++;
            }
        }

        return count;
    }

    private record ParsedArguments(
            Path input,
            Path output,
            Integer rank,
            Integer compression,
            Quality quality,
            ColorMode mode,
            boolean overwrite,
            boolean verbose,
            boolean quiet
    ) {
    }
}
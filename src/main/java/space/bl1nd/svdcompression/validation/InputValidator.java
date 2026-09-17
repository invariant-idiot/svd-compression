package space.bl1nd.svdcompression.validation;

import space.bl1nd.svdcompression.exception.UserInputException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

/**
 * Validates local image paths and compression settings.
 */
public final class InputValidator {
    private static final Set<String> FORMATS = Set.of("png", "jpg", "jpeg", "bmp");

    public void validateInput(Path path) {
        if (path == null || !Files.exists(path)) throw new UserInputException("Input image does not exist: " + path);
        if (!Files.isRegularFile(path) || !Files.isReadable(path))
            throw new UserInputException("Input path must be a readable regular file: " + path);
        if (!FORMATS.contains(extension(path)))
            throw new UserInputException("Unsupported input format. Supported formats: PNG, JPG, JPEG, BMP.");
    }

    public void validateOutput(Path input, Path output, String format, boolean overwrite) {
        if (output == null) throw new UserInputException("An output path is required in non-interactive mode.");
        if (input.toAbsolutePath().normalize().equals(output.toAbsolutePath().normalize()))
            throw new UserInputException("Refusing to overwrite the input image. Choose a different output path.");
        if (!FORMATS.contains(format.toLowerCase(Locale.ROOT)))
            throw new UserInputException("Unsupported output format: " + format);
        if (Files.exists(output) && !overwrite)
            throw new UserInputException("Output already exists: " + output + ". Use --overwrite to replace it.");
    }

    public void validateRank(int rank, int maximum) {
        if (rank < 1 || rank > maximum)
            throw new UserInputException("Invalid rank: " + rank + ". Choose a rank between 1 and " + maximum + ".");
    }

    public static String extension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}

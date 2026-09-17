package space.bl1nd.svdcompression.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Collects command-line options interactively.
 *
 * <p>Supported input formats are PNG and BMP.
 * The output format is always PNG.
 */
public final class InteractiveCli {

    private final Scanner scanner;

    public InteractiveCli() {
        this(new Scanner(System.in));
    }

    InteractiveCli(Scanner scanner) {
        this.scanner = scanner;
    }

    public String[] prompt() {
        printHeader();

        String input = ask("Enter image path: ");
        String setting = ask(
                "Choose rank, compression %, or quality " +
                        "[default quality medium]: "
        );
        String mode = ask(
                "Mode [rgb|grayscale|auto, default auto]: "
        );
        String output = ask(
                "Output path [blank for default PNG]: "
        );

        List<String> arguments = new ArrayList<>();

        addRequiredArgument(arguments, input);
        addCompressionArgument(arguments, setting);
        addOptionalArgument(arguments, "--mode", mode);
        addOptionalArgument(arguments, "--output", output);

        return arguments.toArray(String[]::new);
    }

    private void printHeader() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("        SVD IMAGE COMPRESSOR");
        System.out.println("========================================");
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void addRequiredArgument(
            List<String> arguments,
            String value
    ) {
        arguments.add("--input");
        arguments.add(value);
    }

    private static void addOptionalArgument(
            List<String> arguments,
            String option,
            String value
    ) {
        if (!value.isBlank()) {
            arguments.add(option);
            arguments.add(value);
        }
    }

    private static void addCompressionArgument(
            List<String> arguments,
            String setting
    ) {
        if (setting.isBlank()) {
            return;
        }

        String[] parts = setting.split("\\s+", 2);
        String type = parts[0].toLowerCase();

        if (isCompressionOption(type) && parts.length == 2) {
            arguments.add("--" + type);
            arguments.add(parts[1]);
            return;
        }

        // A plain value is treated as a quality value.
        arguments.add("--quality");
        arguments.add(setting);
    }

    private static boolean isCompressionOption(String value) {
        return value.equals("rank")
                || value.equals("compression")
                || value.equals("quality");
    }
}


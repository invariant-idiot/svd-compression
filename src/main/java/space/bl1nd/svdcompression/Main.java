package space.bl1nd.svdcompression;

import space.bl1nd.svdcompression.cli.CliApplication;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        new CliApplication().run(args);
    }
}

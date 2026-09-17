package space.bl1nd.svdcompression.config;

/**
 * Named retained-component presets.
 */
public enum Quality {
    LOW(15), MEDIUM(35), HIGH(60);
    private final int retainedPercent;

    Quality(int retainedPercent) {
        this.retainedPercent = retainedPercent;
    }

    public int retainedPercent() {
        return retainedPercent;
    }
}

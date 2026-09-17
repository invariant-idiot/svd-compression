package space.bl1nd.svdcompression.cli;

/**
 * Prints usage without depending on a CLI framework.
 */
public final class HelpPrinter {
    public void print() {
        System.out.println("""
                SVD Image Compressor 1.0.0
                Uses truncated singular value decomposition (A_k = U_k Sigma_k V_k^T) to reconstruct a local image.
                
                Usage: java -jar target/svd-image-compressor.jar --input <file> [options]
                
                Options:
                  --input <path>          PNG, JPG/JPEG, or BMP input image (required outside interactive mode)
                  --output <path>         Output path (default: input name plus _compressed)
                  --rank <number>         Retain exactly this many singular components
                  --compression <1-100>   Retain this percentage of maximum SVD components
                  --quality <low|medium|high>  Retain 15%, 35%, or 60% respectively
                  --mode <rgb|grayscale|auto>  Channel processing mode (default: auto)
                  --format <png|jpg|bmp>  Output encoding format (default: output extension or PNG)
                  --overwrite             Allow replacement of an existing output (never the input)
                  --verbose | --quiet     Show detailed stages or suppress normal stages
                  --help                  Show this help
                  --version               Show version
                
                Examples:
                  java -jar target/svd-image-compressor.jar --input photo.png --rank 100 --mode rgb
                  java -jar target/svd-image-compressor.jar --input photo.jpg --compression 30 --format png
                
                The compression percentage describes retained SVD components, not a guaranteed output-file size reduction.
                """);
    }
}

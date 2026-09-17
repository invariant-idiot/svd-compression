package space.bl1nd.svdcompression;

import space.bl1nd.svdcompression.compression.SvdCompressor;
import space.bl1nd.svdcompression.exception.UserInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SvdCompressorTest {
    @Test
    void fullRankRebuildsMatrix() {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = new SvdCompressor().compress(a, 2);
        assertEquals(1, b[0][0], 1e-8);
        assertEquals(4, b[1][1], 1e-8);
    }

    @Test
    void rankOneHasCorrectDimensionsAndIsApproximation() {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = new SvdCompressor().compress(a, 1);
        assertEquals(2, b.length);
        assertEquals(2, b[0].length);
        assertNotEquals(4, b[1][1], 1e-5);
    }

    @Test
    void rejectsInvalidRank() {
        assertThrows(UserInputException.class, () -> new SvdCompressor().compress(new double[][]{{1, 2}}, 2));
    }
}

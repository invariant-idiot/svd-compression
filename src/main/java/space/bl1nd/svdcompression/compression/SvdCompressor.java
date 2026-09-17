package space.bl1nd.svdcompression.compression;

import space.bl1nd.svdcompression.exception.UserInputException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;

/**
 * Performs truncated singular-value decomposition of one image channel.
 */
public final class SvdCompressor {
    /**
     * Reconstructs A_k = U_k Sigma_k V_k^T using exactly the requested leading components.
     */
    public double[][] compress(double[][] values, int rank) {
        int rows = values.length, columns = values[0].length, maximum = Math.min(rows, columns);
        if (rank < 1 || rank > maximum)
            throw new UserInputException("Invalid rank: " + rank + ". Choose a rank between 1 and " + maximum + ".");
        SingularValueDecomposition svd = new SingularValueDecomposition(MatrixUtils.createRealMatrix(values));
        RealMatrix u = svd.getU(), vt = svd.getVT();
        double[] singular = svd.getSingularValues();
        double[][] rebuilt = new double[rows][columns];
        for (int component = 0; component < rank; component++)
            for (int row = 0; row < rows; row++) {
                double scaled = u.getEntry(row, component) * singular[component];
                for (int col = 0; col < columns; col++) rebuilt[row][col] += scaled * vt.getEntry(component, col);
            }
        return rebuilt;
    }
}

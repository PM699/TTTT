package common;

import java.util.Random;

public class Matrix {
    private final int numRows;
    private final int numCols;
    public double[][] data;

    public Matrix(int size) {
        this.numRows = size;
        this.numCols = size;
        this.data = new double[numRows][numCols];
    }

    public Matrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.data = new double[numRows][numCols];
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }

    public Matrix transpose() {
        final Matrix transposed = new Matrix(numCols, numRows);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposed.data[j][i] = data[i][j];
            }
        }
        return transposed;
    }

    public void generateRandomMatrix(double minVal, double maxVal) {
        Random random = new Random();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                data[i][j] = Math.floor(minVal + (maxVal - minVal) * random.nextDouble());
            }
        }
    }

    public double[][] getRows(int offset, int numRows) {
        final double[][] result = new double[numRows][this.numCols];
        System.arraycopy(this.data, offset, result, 0, numRows);
        return result;
    }

    public Matrix multiply(Matrix other) {
        if (this.numCols != other.numRows) {
            throw new IllegalArgumentException("Number of columns in first matrix must match number of rows in second matrix.");
        }

        Matrix result = new Matrix(this.numRows, other.numCols);
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < other.numCols; j++) {
                for (int k = 0; k < this.numCols; k++) {
                    result.data[i][j] += this.data[i][k] * other.data[k][j];
                }
            }
        }
        return result;
    }

    public boolean equals(Matrix B) {
        if (numRows != B.numRows || numCols != B.numCols) {
            return false;
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (data[i][j] != B.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private double[][] getSquareBlock(int startRow, int startCol, int size) {
        double[][] result = new double[size][size];
        for (int i = 0; i < size && (i + startRow < numRows); i++) {
            for (int j = 0; j < size && (j + startCol < numCols); j++) {
                result[i][j] = this.data[i + startRow][j + startCol];
            }
        }
        return result;
    }

    public double[][][][] getFoxBlockSplit(int blocksNumSqrt) {
        double[][][][] matrixBlocks = new double[blocksNumSqrt][blocksNumSqrt][this.numRows][this.numCols];
        final int blockSize = Math.ceilDiv(this.numRows, blocksNumSqrt);
        for (int i = 0; i < blocksNumSqrt; i++) {
            for (int j = 0; j < blocksNumSqrt; j++) {
                matrixBlocks[i][j] = getSquareBlock(i * blockSize, j * blockSize, blockSize);
            }
        }
        return matrixBlocks;
    }

    public double[][][][] getFoxBlockSplit(int blocksNumSqrt, int blockSize) {
        double[][][][] matrixBlocks = new double[blocksNumSqrt][blocksNumSqrt][this.numRows][this.numCols];
        for (int i = 0; i < blocksNumSqrt; i++) {
            for (int j = 0; j < blocksNumSqrt; j++) {
                matrixBlocks[i][j] = getSquareBlock(i * blockSize, j * blockSize, blockSize);
            }
        }
        return matrixBlocks;
    }
}

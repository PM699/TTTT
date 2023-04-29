package common;

public class Result extends Matrix {
    public Result(int numRows, int numCols) {
        super(numRows, numCols);
    }

    static public Result joinFoxBlockSplit(int numRows, int numCols, double[][][][] cBlocks) {
        Result result = new Result(numRows, numCols);
        final int blockSize = cBlocks[0][0].length;
        for (int i = 0; i < cBlocks.length; i++) {
            for (int j = 0; j < cBlocks.length; j++) {
                double[][] subMatrix = cBlocks[i][j];
                int subMatrixStartRow = i * blockSize;
                int subMatrixStartCol = j * blockSize;
                for (int k = 0; k < blockSize && k + subMatrixStartRow < numRows; k++) {
                    for (int l = 0; l < blockSize && l + subMatrixStartCol < numCols; l++) {
                        result.data[k + subMatrixStartRow][l + subMatrixStartCol] = subMatrix[k][l];
                    }
                }
            }
        }
        return result;
    }
}

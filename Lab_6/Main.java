import  mpi.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    static final int N = 40;
    static final int MASTER = 0;
    static final int FROM_MASTER = 1;
    static final int FROM_WORKER = 5;

    public static void main(String[] args) {
   /*     {
            MPI.Init(args);

            int currentProcess = MPI.COMM_WORLD.Rank();
            int processesCount = MPI.COMM_WORLD.Size();

            int workersCount = processesCount - 1;
            if(N % workersCount != 0) {
                if(currentProcess == MASTER){
                    System.out.println("It is not possible to allocate the specified number of columns to " + processesCount + " processors!");
                }

                MPI.Finalize();
                return;
            }
            int rowsPerProcess = N / workersCount;

            double[][] matrixB = new double[N][N];

            if(currentProcess == MASTER){
                double[][] matrixA = new double[N][N];
                double[][] matrixC = new double[N][N];

                long startTime = System.currentTimeMillis();

                ArrayHelper.fill2DArray(matrixA, 1);
                ArrayHelper.fill2DArray(matrixB, 1);
                double[] matrixB1D = ArrayHelper.convert2DTo1D(matrixB);

                for (int dest = 1; dest <= workersCount; dest++) {
                    int offSet = (dest - 1);
                    int startRow = offSet * rowsPerProcess;
                    int endRow = startRow + rowsPerProcess;

                    double[] subMatrixA1D = ArrayHelper.convert2DTo1D(Arrays.copyOfRange(matrixA, startRow, endRow));

                    MPI.COMM_WORLD.Send(new int[] {offSet}, 0, 1, MPI.INT, dest, FROM_MASTER);
                    MPI.COMM_WORLD.Send(subMatrixA1D, 0, rowsPerProcess * N, MPI.DOUBLE, dest, FROM_MASTER + 1);
                    MPI.COMM_WORLD.Send(matrixB1D, 0, N * N, MPI.DOUBLE, dest, FROM_MASTER + 2);
                }

                for (int source = 1; source <= workersCount; source++) {
                    int[] offset = new int[1];
                    MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, source, FROM_WORKER);

                    double[] resultMatrix1D = new double[rowsPerProcess * N];
                    MPI.COMM_WORLD.Recv(resultMatrix1D, 0, rowsPerProcess * N, MPI.DOUBLE, source, FROM_WORKER + 1);

                    ArrayHelper.fillMatrixWithOffset(matrixC, resultMatrix1D, offset[0] * rowsPerProcess);
                }

                System.out.println("Execution time of blocking: " + (System.currentTimeMillis() - startTime) + " ms");
                //ArrayHelper.printMatrix(matrixC);
                //System.out.println(ArrayHelper.convert2DTo1D(matrixC).length);
            }
            else {
                int[] offset = new int[1];
                MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, MASTER, FROM_MASTER);

                double[] subMatrixA1D = new double[rowsPerProcess * N];
                MPI.COMM_WORLD.Recv(subMatrixA1D, 0, rowsPerProcess * N, MPI.DOUBLE, MASTER, FROM_MASTER + 1);
                double[][] subMatrixA = ArrayHelper.convert1DTo2D(subMatrixA1D, rowsPerProcess, N);

                double[] matrixB1D = new double[N * N];
                MPI.COMM_WORLD.Recv(matrixB1D, 0, N * N, MPI.DOUBLE, MASTER, FROM_MASTER + 2);
                matrixB = ArrayHelper.convert1DTo2D(matrixB1D, N, N);

                double[][] resultMatrix = new double[rowsPerProcess][N];
                for (int i = 0; i < rowsPerProcess; i++) {
                    for (int j = 0; j < N; j++) {
                        for (int k = 0; k < N; k++) {
                            resultMatrix[i][j] += subMatrixA[i][k] * matrixB[k][j];
                        }
                    }
                }

                MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, MASTER, FROM_WORKER);
                MPI.COMM_WORLD.Send(ArrayHelper.convert2DTo1D(resultMatrix), 0, rowsPerProcess * N, MPI.DOUBLE, MASTER, FROM_WORKER + 1);
            }

            MPI.Finalize();
        }*/
        {
            MPI.Init(args);

            int currentProcess = MPI.COMM_WORLD.Rank();
            int processesCount = MPI.COMM_WORLD.Size();

            int workersCount = processesCount - 1;
            if (N % workersCount != 0) {
                if (currentProcess == MASTER) {
                    System.out.println("It is not possible to allocate the specified number of columns to " + processesCount + " processors!");
                }

                MPI.Finalize();
                return;
            }
            int rowsPerProcess = N / workersCount;

            double[][] matrixB = new double[N][N];

            if (currentProcess == MASTER) {
                double[][] matrixA = new double[N][N];
                double[][] matrixC = new double[N][N];

                long startTime = System.currentTimeMillis();

                ArrayHelper.fill2DArray(matrixA, 1);
                ArrayHelper.fill2DArray(matrixB, 1);
                double[] matrixB1D = ArrayHelper.convert2DTo1D(matrixB);

                Request[] sendRequests= new Request[workersCount * 3];
                int index = 0;
                for (int dest = 1; dest <= workersCount; dest++) {
                    int offSet = (dest - 1);
                    int startRow = offSet * rowsPerProcess;
                    int endRow = startRow + rowsPerProcess;

                    double[] subMatrixA1D = ArrayHelper.convert2DTo1D(Arrays.copyOfRange(matrixA, startRow, endRow));

                    sendRequests[index++] = MPI.COMM_WORLD.Isend(new int[]{offSet}, 0, 1, MPI.INT, dest, FROM_MASTER);
                    sendRequests[index++] = MPI.COMM_WORLD.Isend(subMatrixA1D, 0, rowsPerProcess * N, MPI.DOUBLE, dest, FROM_MASTER + 1);
                    sendRequests[index++] = MPI.COMM_WORLD.Isend(matrixB1D, 0, N * N, MPI.DOUBLE, dest, FROM_MASTER + 2);
                }

                Request.Waitall(sendRequests);

                for (int source = 1; source <= workersCount; source++) {
                    int[] offset = new int[1];
                    double[] resultMatrix1D = new double[rowsPerProcess * N];

                    var offsetRequest = MPI.COMM_WORLD.Irecv(offset, 0, 1, MPI.INT, source, FROM_WORKER);
                    var resultMatrixRequest = MPI.COMM_WORLD.Irecv(resultMatrix1D, 0, rowsPerProcess * N, MPI.DOUBLE, source, FROM_WORKER + 1);

                    offsetRequest.Wait();
                    resultMatrixRequest.Wait();

                    ArrayHelper.fillMatrixWithOffset(matrixC, resultMatrix1D, offset[0] * rowsPerProcess);
                }

                System.out.println("Execution time of non-blocking: " + (System.currentTimeMillis() - startTime) + " ms");
                ArrayHelper.printMatrix(matrixC);
                System.out.println(ArrayHelper.convert2DTo1D(matrixC).length);
            } else {
                int[] offset = new int[1];
                MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, MASTER, FROM_MASTER);

                double[] subMatrixA1D = new double[rowsPerProcess * N];
                var subMatrixRequest = MPI.COMM_WORLD.Irecv(subMatrixA1D, 0, rowsPerProcess * N, MPI.DOUBLE, MASTER, FROM_MASTER + 1);

                double[] matrixB1D = new double[N * N];
                var matrixBRequest = MPI.COMM_WORLD.Irecv(matrixB1D, 0, N * N, MPI.DOUBLE, MASTER, FROM_MASTER + 2);

                subMatrixRequest.Wait();
                matrixBRequest.Wait();

                double[][] subMatrixA = ArrayHelper.convert1DTo2D(subMatrixA1D, rowsPerProcess, N);
                matrixB = ArrayHelper.convert1DTo2D(matrixB1D, N, N);

                double[][] resultMatrix = new double[rowsPerProcess][N];
                for (int i = 0; i < rowsPerProcess; i++) {
                    for (int j = 0; j < N; j++) {
                        for (int k = 0; k < N; k++) {
                            resultMatrix[i][j] += subMatrixA[i][k] * matrixB[k][j];
                        }
                    }
                }

                Request[] sendRequests = new Request[2];
                MPI.COMM_WORLD.Isend(offset, 0, 1, MPI.INT, MASTER, FROM_WORKER);
                MPI.COMM_WORLD.Isend(ArrayHelper.convert2DTo1D(resultMatrix), 0, rowsPerProcess * N, MPI.DOUBLE, MASTER, FROM_WORKER + 1);
            }

            MPI.Finalize();
        }
    }
}
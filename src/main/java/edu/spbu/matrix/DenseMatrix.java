package edu.spbu.matrix;
import java.io.*;
import java.util.Scanner;
/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
    public static final int ARRAY_SIZE = 3000; /* максимальный размер массива */
    public int rows = 0;
    public int columns = 0;
    public double[][] dMatrix;  /* массив матрицы */

    @Override
    public int numberOfColumns() {
        return columns;
    }

    @Override
    public int numberOfRows() {
        return rows;
    }
    @Override
    public double getCell(int row, int column) {
        return dMatrix[row][column];
    }

    public SparseMatrix toSparse(){
        SparseMatrix result = new SparseMatrix(rows, columns);
        int key = 0;
        for (int i = 0; i<rows; i++)
            for (int j=0; j<columns; j++)
                if (dMatrix[i][j] != 0)
                {
                    result.value.put(key, dMatrix[i][j]);
                    result.row.put(key, i);
                    result.column.put(key, j);
                    key++;
                }
        key++;
        return result;
    }
    /**
     * загружает матрицу из файла
     * @param fileName
     */

    public DenseMatrix(String fileName) throws IOException{
        if(fileName.trim().length()==0) // если файл не существует
            return;

        int j;
        String[] currentRowArray;
        Scanner input = new Scanner(new File(fileName));
        String currentRow="1";

        while(input.hasNextLine() && currentRow.trim().length()!=0) /* пока есть непустые строки */
        {
            ++rows;
            currentRow = input.nextLine();
            currentRowArray = currentRow.split(" ");
            if(rows == 1)
            {
                columns = currentRowArray.length;
                dMatrix = new double[ARRAY_SIZE][columns];
            }

            for(j=0; j<columns; ++j)
            {
                dMatrix[rows-1][j]=Double.parseDouble(currentRowArray[j]);
            }
        }
        input.close();
    }
    public DenseMatrix(int x, int y){
        rows = x;
        columns = y;
        dMatrix = new double[rows][columns];
    }

    /**
   * однопоточное умножение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
    @Override public Matrix mul(Matrix o) throws IOException {
        if (o instanceof DenseMatrix) {

            int r1 = rows;
            int c1 = columns;
            int r2 = o.numberOfRows();
            int c2 = o.numberOfColumns();

            DenseMatrix result = new DenseMatrix(rows, o.numberOfColumns());

            for (int i = 0; i < r1; i++)
                for (int j = 0; j < c2; j++)
                    for (int k = 0; k < c1; k++)
                        result.dMatrix[i][j] += dMatrix[i][k] * o.getCell(k, j);
            return result;
        }
        if (o instanceof SparseMatrix)
        {
            return this.toSparse().mul(o);
        }
        return null;
    }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
      DenseMatrix Result = new DenseMatrix(rows, o.numberOfColumns());

      class Task implements Runnable {
          int firstRow;
          int lastRow;
          public Task(int x, int y)
          {
              firstRow = x;
              lastRow = y;
          }
          public void run()
          {
              int i, j, k;
              for(i=firstRow;i<=lastRow;++i)
                  for(j=0;j<o.numberOfColumns(); j++)
                      for (k = 0; k < o.numberOfRows(); ++k)
                          Result.dMatrix[i][j] += getCell(i,k) * o.getCell(k,j);
          }
      }

      int threadsCount = 4;
      int m = rows;
      if (threadsCount > m) {
          threadsCount = m;
      }
      /*посчитаем сколько строк результирующей матрицы будет считать каждый поток*/
      int count = m / threadsCount;
      int additional = m % threadsCount; //если не делится на threadsCount, то добавим к первому потоку
      //создаем и запускаем потоки
      Thread[] threads = new Thread[threadsCount];
      int start = 0; int cnt;
      for (int i = 0; i < threadsCount; i++) {
          //int cnt = ((i == 0) ? count + additional : count);
          if (i == 0) cnt = count + additional;
          else cnt = count;
          threads[i] = new Thread(new Task(start, start + cnt - 1));
          start += cnt;
          threads[i].start();
      }
      //ждем завершения
      try {
          for (Thread thread : threads) {
              thread.join();
          }
      } catch (InterruptedException e) {
          System.out.println("Interrupted");
      }
      return Result;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */

  @Override
  public boolean equals(Object o) {
      if(!(o instanceof Matrix))
          return false;

      Matrix Q = ((Matrix)o);
      if(Q.numberOfRows() != rows || Q.numberOfColumns() != columns)
          return false;

      int i, j;
      for(i = 0; i < rows; ++i)
          for(j=0; j< columns; ++j)
              if(dMatrix[i][j] != Q.getCell(i,j))
                  return false;
      return true;
  }

  public void printDenseMatrix(){
      for (int i = 0; i<rows; i++){
          for (int j=0; j<columns; j++)
              System.out.print(dMatrix[i][j]+ " ");
          System.out.println(" ");
      }
  }
}

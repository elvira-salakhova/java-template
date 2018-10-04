package edu.spbu.matrix;

import java.io.*;
import java.util.Scanner;
import java.util.*;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
  public int rows = 0;
  public int columns = 0;
  public double[][] sMatrix;  /* массив матрицы */
  public static final int ARRAY_SIZE = 1000; /* максимальный размер массива */
  public Map<Integer, Map<Integer, Double>> hashTable = new HashMap<>();

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
    return sMatrix[row][column];
  }

  /**
   * Строится hash-таблица
   */

  public Map<Integer, Map<Integer, Double>> getHashMap()
  {
    int i, j;
    for( i = 0; i < rows; ++i)
      for(j=0; j < columns; ++j)
        if(sMatrix[i][j] != 0)
        {
          if(!hashTable.containsKey(i))
            hashTable.put(i, new HashMap<>());
          hashTable.get(i).put(j, sMatrix[i][j]);
        }
    return hashTable;
  }

  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) throws FileNotFoundException {
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
              sMatrix = new double[ARRAY_SIZE][columns];
          }

          for(j=0; j<columns; ++j)
          {
              sMatrix[rows-1][j]=Double.parseDouble(currentRowArray[j]);
          }
      }
      input.close();
      getHashMap();
  }

    public SparseMatrix(int x, int y){
        rows = x;
        columns = y;
        sMatrix = new double[rows][columns];
        getHashMap();
    }

  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o) throws FileNotFoundException {
    if(o instanceof SparseMatrix)
    {
      SparseMatrix currentMatrix = new SparseMatrix(rows,o.numberOfColumns());
      Map<Integer, Map<Integer, Double>> currentHashTable = ((SparseMatrix) o).getHashMap();
      for (int i : hashTable.keySet()) {
        for (int j : hashTable.get(i).keySet()) {
          if (!currentHashTable.containsKey(j)) {
            continue;
          }
          for (int k : currentHashTable.get(j).keySet()) {
            currentMatrix.sMatrix[i][k] += hashTable.get(i).get(j) * currentHashTable.get(j).get(k);
          }
        }
      }
      return currentMatrix;
    }
    else
    {
      DenseMatrix currentMatrix = new DenseMatrix(rows,o.numberOfColumns());
        for (int i = 0; i < rows; i++)
          for (int j = 0; j < o.numberOfColumns(); j++)
            for (int k = 0; k < o.numberOfRows(); k++)
              currentMatrix.dMatrix[i][j] += sMatrix[i][k] * o.getCell(k,j);
      return currentMatrix;
    }
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */

  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */


  @Override public boolean equals(Object o) {
    if(!(o instanceof Matrix))
      return false;
    Matrix Q = ((Matrix)o);
    if(rows != Q.numberOfRows() || columns != Q.numberOfColumns())
    {
      return false;
    }
    for(int i=0;i<rows;++i)
      for(int j=0;j<columns;++j)
        if(sMatrix[i][j]!=Q.getCell(i,j))
          return false;
    return true;
  }

/* public void printmatrix(String fileName) throws IOException {
    FileWriter writer = new FileWriter(fileName);
    if(rows==0 && columns==0)
    {
      writer.write("Empty matrix");
      return;
    }
    int i, j;
    String x;
    for(i=0;i<rows;++i)
    {
      for(j=0;j<columns;++j)
      {
        x = Double.toString(sMatrix[i][j]);
        writer.write(x+" ");
      }
      writer.write(System.getProperty( "line.separator" ));
    }
    writer.close();
  }
*/

}

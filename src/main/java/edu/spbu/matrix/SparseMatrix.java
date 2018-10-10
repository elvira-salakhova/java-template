package edu.spbu.matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
  public int rows = 0;
  public int columns = 0;
  HashMap<Integer,Double> value = new HashMap<>(); /* массив ненулевых значений */
  HashMap<Integer,Integer> column = new HashMap<>();
  HashMap<Integer,Integer> row = new HashMap<>();


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

    return 0;

  }
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) throws FileNotFoundException {
      if(fileName.trim().length()==0) // если файл не существует
          return;

      String[] currentRowArray;
      Scanner input = new Scanner(new File(fileName));
      String currentRow="1";
      double temp;
      int j = 0;
      int k = 0;


      while(input.hasNextLine() && currentRow.trim().length()!=0) /* пока есть непустые строки */
      {
          ++rows;
          currentRow = input.nextLine();
          currentRowArray = currentRow.split(" ");
          /*Обработка текущей строки*/
          for (int i = 0; i < currentRowArray.length; ++i){
            temp = Double.parseDouble(currentRowArray[i]);
            if (temp != 0.0) {
              row.put(j,rows-1);
              value.put(j, temp);
              column.put(j,i);
              j++;
            }
          }

        if(rows == 1)
          {
              columns = currentRowArray.length;
          }
      }
      input.close();
  }

  public SparseMatrix(int x, int y){
    rows = x;
    columns = y;
  }

  public SparseMatrix SparseMatrixTranspose() {
    SparseMatrix result = new SparseMatrix(columns, rows);
    int count[] = new int[columns];
    for (int i = 0; i < value.size(); i++)
      count[column.get(i)]++;
    int[] index = new int[columns];

    index[0] = 0;

    for (int i = 1; i< columns; i++)
      index[i] = index[i-1] + count[i-1];
    for (int i = 0; i < value.size(); i++){
      int rpos = index[column.get(i)]++;

      result.row.put(rpos, column.get(i));
      result.column.put(rpos, row.get(i));
      result.value.put(rpos, value.get(i));
    }
    return result;
  }

  public SparseMatrix toSparse(DenseMatrix matrix){
    SparseMatrix result = new SparseMatrix(matrix.rows, matrix.columns);
    int key = 0;
    for (int i = 0; i<matrix.rows; i++)
      for (int j=0; j<matrix.columns; j++)
        if (matrix.dMatrix[i][j] != 0)
        {
          result.value.put(key, matrix.dMatrix[i][j]);
          result.row.put(key, i);
          result.column.put(key, j);
          key++;
        }
    key++;
    return result;
  }

  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public SparseMatrix mul(Matrix o) throws FileNotFoundException {
    if (o instanceof SparseMatrix)
    {
      if (columns != ((SparseMatrix) o).rows) {
        System.out.println("Cannot multiply");
        return null;
      }
      o = ((SparseMatrix) o).SparseMatrixTranspose();
      SparseMatrix result = new SparseMatrix(rows, ((SparseMatrix) o).rows);
      for (int i = 0; i< value.size();){
        int currentRowResult = row.get(i);
        for (int j = 0; j<((SparseMatrix) o).value.size();) {
          int currentColumnResult = ((SparseMatrix) o).row.get(j);

          int ti = i;
          int tj = j;

          double sum = 0;

          while (ti < value.size() && row.get(ti) == currentRowResult
          && tj < ((SparseMatrix) o).value.size() && ((SparseMatrix) o).row.get(tj) == currentColumnResult) {
            if (column.get(ti)<((SparseMatrix) o).column.get(tj))
              ti++;
            else if (column.get(ti) > ((SparseMatrix) o).column.get(tj))
              tj++;
            else
              sum += value.get(ti++)*((SparseMatrix) o).value.get(tj++);
          }
          if (sum != 0) {
            int key = result.value.size();
            result.row.put(key, currentRowResult);
            result.column.put(key, currentColumnResult);
            result.value.put(key, sum);
          }

          while (j < ((SparseMatrix) o).value.size() && ((SparseMatrix) o).row.get(j) == currentColumnResult)
            j++;
        }
        while (i < value.size() && row.get(i) == currentRowResult)
          i++;
      }
      return result;
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

    SparseMatrix Q = ((SparseMatrix)o);
    if(Q.numberOfRows() != rows || Q.numberOfColumns() != columns)
      return false;
    for (int i = 0; i<value.size(); i++)
      if (value.get(i)-Q.value.get(i) != 0 || row.get(i)-Q.row.get(i) != 0
      || column.get(i)-Q.column.get(i) != 0)
        return false;
    return true;
  }

  public void printSparseMatrix() {
    double[][] array = new double[rows][columns];
    for (int i = 0; i < value.size(); ++i) {
      array[row.get(i)][column.get(i)] = value.get(i);
    }

    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < columns; ++j)
        System.out.print(array[i][j] + " ");
      System.out.println(" ");
    }
  }
}

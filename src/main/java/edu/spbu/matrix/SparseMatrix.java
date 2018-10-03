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
  public double[][] sMatrix;  /* массив матрицы */
  public static final int ARRAY_SIZE = 3000; /* максимальный размер массива */
  public Map<Integer, Map<Integer, Double>> table = new HashMap<>();

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
   * построить ХЭШ-таблицу
   */

  public Map<Integer, Map<Integer, Double>> getHashMap()
  {
    int i, j;
    for(i=0;i<rows;++i)
      for(j=0;j<columns;++j)
        if(sMatrix[i][j]!=0)
        {
          if(!table.containsKey(i))
            table.put(i, new HashMap<>());
          table.get(i).put(j, sMatrix[i][j]);
        }
    return table;
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
      SparseMatrix R = new SparseMatrix(rows,o.numberOfColumns());
      Map<Integer, Map<Integer, Double>> T = ((SparseMatrix) o).getHashMap();
      for (int i : table.keySet()) {
        for (int j : table.get(i).keySet()) {
          if (!T.containsKey(j)) {
            continue;
          }
          for (int k : T.get(j).keySet()) {
            R.sMatrix[i][k] += table.get(i).get(j) * T.get(j).get(k);
          }
        }
      }
      return R;
    }
    else
    {
      DenseMatrix R = new DenseMatrix(rows,o.numberOfColumns());


        for (int i = 0; i < rows; i++)
          for (int j = 0; j < o.numberOfColumns(); j++)
            for (int k = 0; k < o.numberOfRows(); k++)
              R.dMatrix[i][j] += sMatrix[i][k] * o.getCell(k,j);

      return R;
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

 public void printmatrix(String fileName) throws IOException {
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


}

package edu.spbu.matrix;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{



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

  /*Изменяет размер матрицы, чтобы можно было выполнять умножение*/
  @Override
  public void resizeMatrix(int x, int y)
  {
    rows = x;
    columns = y;
    this.dMatrix = new double[rows][columns];
  }

  @Override
  public double getCell(int row, int column) {
    return dMatrix[row][column];
  }

  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) {

  }
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o)
  {
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
  @Override public boolean equals(Object o) {
    return false;
  }
}

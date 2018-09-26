package edu.spbu.matrix;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 */

public interface Matrix
{
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   * @param o
   * @return
   */
  Matrix mul(Matrix o) throws IOException;
  /**
   * многопоточное умножение матриц
   * @param o
   * @return
   */
  Matrix dmul(Matrix o);

  double getCell(int row, int column);
  int numberOfColumns();
  int numberOfRows();
  void resizeMatrix(int x, int y);
  boolean equals(Object o);


}

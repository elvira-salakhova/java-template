package edu.spbu.matrix;

import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MatrixTest
{
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void readDM() throws IOException {
    /**
    В файл записывается матрица First размера n на n.
    Затем она считывается в матрицу Second.
    Матрицы поэлементно сравниваются.
    */
    BufferedWriter checkArr = new BufferedWriter (new FileWriter("src/checkArray.txt"));
    int n = 6;

    DenseMatrix First = new DenseMatrix("");
    First.resizeMatrix(n,n);

    for (int i = 0; i<n; i++){
      for (int j = 0; j<n; j++)
      {
        First.dMatrix[i][j] = (i+2)*(j+1);
        checkArr.write(Double.toString(First.dMatrix[i][j])+" ");
      }
      checkArr.newLine();
    }
    checkArr.flush();
    checkArr.close();

    /*прочтение*/
      DenseMatrix Second = new DenseMatrix("src/checkArray.txt");
    /*проверка*/

      assertEquals(First, Second);



  }


  @Test
    public void mulDD() throws IOException {
        Matrix m1 = new DenseMatrix("src/m1.txt");
        Matrix m2 = new DenseMatrix("src/m2.txt");
        Matrix expected = new DenseMatrix("src/result.txt");
        assertEquals(expected, m1.mul(m2));
    }



  /*

    @Test
  public void getCellTest() throws FileNotFoundException {
      Matrix m1 = new DenseMatrix("src/m1.txt");
      Assert.assertEquals(m1.getCell(2,2), 1.0, 0);
    }
    @Test
  public void mulDD() throws FileNotFoundException {
    Matrix m1 = new DenseMatrix("src/m1.txt");
    Matrix m2 = new DenseMatrix("src/m2.txt");
    Matrix expected = new DenseMatrix("src/result.txt");
    assertEquals(expected, m1.mul(m2));
  }
  */
}

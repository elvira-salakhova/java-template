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
  public void SparseMulTest() throws FileNotFoundException {
    SparseMatrix m1 = new SparseMatrix("src/test/resources/m1.txt");
    SparseMatrix m2 = new SparseMatrix("src/test/resources/m2.txt");
    //SparseMatrix expected = new SparseMatrix("src/test/resources/result.txt");
    SparseMatrix res = new SparseMatrix(m1.rows, m2.columns);
    res = m1.mul(m2);
    res.printSparseMatrix();
    System.out.println("Sparse: ");
    res.printSparseMatrix();
    System.out.println(res.value);
    System.out.println(res.row);
    System.out.println(res.column);
    //assertEquals(expected, res);
  }

  @Test
  public void SparseMatrixTest () throws FileNotFoundException {
    SparseMatrix m1 = new SparseMatrix("src/test/resources/m2.txt");
    System.out.println(m1.rows);
    System.out.println(m1.columns);
    System.out.println(m1.value);
    System.out.println(m1.row);
    System.out.println(m1.column);
    System.out.println("Transposed:");
    SparseMatrix m1copy = new SparseMatrix(m1.columns, m1.rows);
    m1copy = m1;
    m1.SparseMatrixTranspose().printSparseMatrix();
    System.out.println(m1.SparseMatrixTranspose().rows);
    System.out.println(m1.SparseMatrixTranspose().columns);
    System.out.println(m1.SparseMatrixTranspose().row);
    System.out.println(m1.SparseMatrixTranspose().column);
  }

  @Test
  public void printSparseMatrixTest() throws FileNotFoundException {
    SparseMatrix m1 = new SparseMatrix("src/test/resources/m1.txt");
    m1.printSparseMatrix();
    m1.transposeSparseMatrix();
    System.out.println("Transposed:");
    m1.printSparseMatrix();
  }
  @Test
  public void numOfRowsAndColsTest () throws IOException {
    DenseMatrix m = new DenseMatrix(2,4);
    assertEquals(m.rows, 2);
    assertEquals(m.columns, 4);
  }


  @Test
  public void readDM() throws IOException {
    /**
    В файл записывается матрица First размера n на n.
    Затем она считывается в матрицу Second.
    Матрицы поэлементно сравниваются.
    */
    BufferedWriter checkArr = new BufferedWriter (new FileWriter("src/checkArray.txt"));
    int n = 6;

    DenseMatrix First = new DenseMatrix(n,n);
    for (int i = 0; i<n; i++)
    {
      for (int j = 0; j<n; j++)
      {
        First.dMatrix[i][j] = (i+2)*(j+1);
        checkArr.write(Double.toString(First.dMatrix[i][j])+" ");
      }
      checkArr.newLine();
    }
    checkArr.close();
    DenseMatrix Second = new DenseMatrix("src/checkArray.txt");
    assertEquals(First, Second);
  }


  @Test
  public void mulDD() throws IOException
  {
      Matrix m1 = new DenseMatrix("src/m1.txt");
      Matrix m2 = new DenseMatrix("src/m2.txt");
      Matrix expected = new DenseMatrix("src/result.txt");
      assertEquals(expected, m1.mul(m2));
  }

 @Test
 public void mulSS()  throws IOException
 {
   Matrix m1 = new SparseMatrix("src/m1.txt");
   Matrix m2 = new SparseMatrix("src/m2.txt");
   Matrix expected = new SparseMatrix("src/result.txt");
   assertEquals(expected, m1.mul(m2));
 }

  @Test
  public void mulSD()  throws IOException
  {
    Matrix m1 = new SparseMatrix("src/m1.txt");
    Matrix m2 = new DenseMatrix("src/m2.txt");
    Matrix expected = new DenseMatrix("src/result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDS()  throws IOException
  {
    Matrix m1 = new DenseMatrix("src/m1.txt");
    Matrix m2 = new SparseMatrix("src/m2.txt");
    Matrix expected = new DenseMatrix("src/result.txt");
    assertEquals(expected, m1.mul(m2));
  }
}

package edu.spbu.matrix;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

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
      Matrix m1 = new DenseMatrix("src/test/resources/m1.txt");
      Matrix m2 = new DenseMatrix("src/test/resources/m2.txt");
      Matrix expected = new DenseMatrix("src/test/resources/result.txt");
      assertEquals(expected, m1.dmul(m2));

  }

  @Test
  public void dmulDD() throws IOException {
    Matrix m1 = new DenseMatrix("src/test/resources/dm1.txt");
    Matrix m2 = new DenseMatrix("src/test/resources/dm2.txt");
    long t1 = System.currentTimeMillis();
    Matrix R = m1.dmul(m2);
    long t2 = System.currentTimeMillis();
    long t3 = System.currentTimeMillis();
    Matrix R2 = m1.mul(m2);
    long t4 = System.currentTimeMillis();
    assertEquals(R,R2);
    System.out.println("Время парралельного перемножения матриц размерами 2000 X 2000: "
            +(t2-t1));
    System.out.println("Время обычного перемножения матриц размерами 2000 X 2000: "
            +(t4-t3));
  }

  @Test
  public void SparseMulTest() throws FileNotFoundException {
    System.out.println("SPARSE MULTIPLICATION");
    SparseMatrix m1 = new SparseMatrix("src/test/resources/m1.txt");
    SparseMatrix m2 = new SparseMatrix("src/test/resources/m2.txt");
    SparseMatrix expected = new SparseMatrix("src/test/resources/result.txt");
    SparseMatrix res = new SparseMatrix(m1.rows, m2.columns);
    res = m1.mul(m2);
    System.out.println("Calculated result: ");
    res.printSparseMatrix();
    System.out.println("Compressed form:");
    System.out.println("value  >> " +res.value);
    System.out.println("row    >> " +res.row);
    System.out.println("column >> " +res.column);

    System.out.println("Expected (from result.txt): ");
    expected.printSparseMatrix();
    System.out.println("Compressed form:");
    System.out.println("value  >> " +expected.value);
    System.out.println("row    >> " +expected.row);
    System.out.println("column >> " +expected.column);
    System.out.println("-----------------------------");
    assertEquals(expected, res);
  }

  @Test
  public void TransposeSparseMatrixTest () throws FileNotFoundException {
    System.out.println("SPARSE TRANSPOSE");
    SparseMatrix m1 = new SparseMatrix("src/test/resources/m2.txt");
    System.out.println("Given matrix:");
    m1.printSparseMatrix();
    System.out.println("Compressed form:");
    System.out.println("value  >> " + m1.value);
    System.out.println("row    >> " + m1.row);
    System.out.println("column >> " +m1.column);
    System.out.println("Transposed:");
    SparseMatrix res = new SparseMatrix(m1.columns, m1.rows);
    res = m1.SparseMatrixTranspose();
    res.printSparseMatrix();
    System.out.println("Compressed form:");
    System.out.println("value  >> " +res.value);
    System.out.println("row    >> " +res.row);
    System.out.println("column >> " +res.column);
    System.out.println("-------------------------------");
  }

 @Test
 public void mulSS()  throws IOException
 {
   Matrix m1 = new SparseMatrix("src/test/resources/m1.txt");
   Matrix m2 = new SparseMatrix("src/test/resources/m2.txt");
   Matrix expected = new SparseMatrix("src/test/resources/result.txt");
   assertEquals(expected, m1.mul(m2));
 }

  @Test
  public void toSparseTest() throws IOException {
    System.out.println("DENSE TO SPARSE");
    DenseMatrix m1 = new DenseMatrix("src/test/resources/m1.txt");
    m1.printDenseMatrix();
    SparseMatrix m2 = new SparseMatrix(((DenseMatrix) m1).rows,((DenseMatrix) m1).columns);
    m2 = m2.toSparse(m1);
    System.out.println("Compressed form:");
    System.out.println("value  >> " +m2.value);
    System.out.println("row    >> " +m2.row);
    System.out.println("column >> " +m2.column);
    System.out.println("------------------------------");
  }

  @Test
  public void mulDS()  throws IOException
  {
    DenseMatrix m1 = new DenseMatrix("src/test/resources/m1.txt");
    SparseMatrix m2 = new SparseMatrix("src/test/resources/m2.txt");
    //SparseMatrix m3 = new SparseMatrix(((DenseMatrix) m1).rows,((DenseMatrix) m1).columns);
    //m3 = m3.toSparse(m1);
    SparseMatrix expected = new SparseMatrix("src/test/resources/result.txt");
    //m1.mul(m2).printSparseMatrix();
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulSD()  throws IOException
  {
    DenseMatrix m1 = new DenseMatrix("src/test/resources/m2.txt");
    SparseMatrix m2 = new SparseMatrix("src/test/resources/m1.txt");
    SparseMatrix m3 = new SparseMatrix(((DenseMatrix) m1).rows,((DenseMatrix) m1).columns);
    m3 = m3.toSparse(m1);
    SparseMatrix expected = new SparseMatrix("src/test/resources/result.txt");
    assertEquals(expected, m2.mul(m3));
  }
}

package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {
  public static void sort (int array[]) {
    for (int i=0; i < array.length; i++)
      for (int j=array.length-1; j > i; j--)
        if (array[j-1] > array[j]){
          int tmp = array[j-1];
          array[j-1] = array[j];
          array[j] = tmp;
        }
  }

  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }
}
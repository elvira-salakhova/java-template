package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {
  public static void sort (int array[], int left, int right) {
      if (left >= right)
          return;
    int currentLeft = left;
    int currentRight = right;
    int pivot = array[left+(right-left)/2];
    while (currentLeft <= currentRight) {
        while (array[currentLeft] < pivot)
            currentLeft++;
        while (array[currentRight] > pivot)
            currentRight--;
        if (currentLeft <= currentRight) {
            int tmp = array[currentLeft];
            array[currentLeft] = array[currentRight];
            array[currentRight] = tmp;
            currentLeft++;
            currentRight--;
        }
    }
    if (left < currentRight)
        sort(array, left, currentRight);
    if (currentLeft < right)
        sort(array, currentLeft, right);
  }

public static void sort (List<Integer> list) {
        Collections.sort(list);
    }
}
package com.example.testspringboot.sortingAlgorithms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/6 15:48
 */
public class Tests {

    public static void main(String[] args) {
        int[] arr1 = {7, 8, 2, 4, 3, 9, 1, 5, 6};
        System.out.println("冒泡排序前：" + Arrays.toString(arr1));
        bubbleSort(arr1);
        System.out.println("冒泡排序后：" + Arrays.toString(arr1));
        System.out.println("------------------------------分割线------------------------------");

        int[] arr2 = {2, 8, 5, 7, 1, 4, 9, 6, 3};
        System.out.println("选择排序前：" + Arrays.toString(arr2));
        selectionSort(arr2);
        System.out.println("选择排序后：" + Arrays.toString(arr2));
        System.out.println("------------------------------分割线------------------------------");

        int[] arr3 = {8, 7, 4, 1, 3, 5, 2, 9, 6};
        System.out.println("插入排序前：" + Arrays.toString(arr3));
        insertionSort(arr3);
        System.out.println("插入排序后：" + Arrays.toString(arr3));
        System.out.println("------------------------------分割线------------------------------");

        int[] arr4 = {5, 1, 3, 8, 4, 9, 6, 7, 2};
        System.out.println("归并排序前：" + Arrays.toString(arr4));
        mergeSort(arr4, 0, arr4.length - 1);
        System.out.println("归并排序后：" + Arrays.toString(arr4));
        System.out.println("------------------------------分割线------------------------------");

        int[] arr5 = {9, 7, 5, 11, 12, 2, 14, 3, 10, 6};
        System.out.println("快速排序前：" + Arrays.toString(arr5));
        quickSort(arr5, 0, arr5.length - 1);
        System.out.println("快速排序后：" + Arrays.toString(arr5));
    }

    // 冒泡排序
    public static void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    // 选择排序
    public static void selectionSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
    }

    // 插入排序
    public static void insertionSort(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; i++) {
            int temp = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > temp) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = temp;
        }
    }

    // 归并排序
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }
    public static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        while (j <= right) {
            temp[k++] = arr[j++];
        }

        for (int p = 0; p < temp.length; p++) {
            arr[left + p] = temp[p];
        }
    }

    // 快速排序
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }
    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    // 递归
    @Test
    public void recursion() {
        List<String> list1 = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        List<String> list2 = Arrays.asList("a", "b", "c", "d", "e", "f");
        List<String> list23 = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<List<String>> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        list.add(list23);
        List<List<String>> result = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        select(0, list, result, list3);
        System.out.println(result);
        System.out.println(result.size());
    }
    private static void select(Integer depth,  List<List<String>> list, List<List<String>> result, List<String> strings) {
        if (depth == list.size()) {
            List<String> strings1 = new ArrayList<>(strings);
            result.add(strings1);
            return;
        }
        List<String> list1 = list.get(depth);
        depth = depth + 1;
        for (String s : list1) {
            strings.add(s);
            select(depth, list, result, strings);
            strings.remove(strings.size() - 1);
        }
    }
}

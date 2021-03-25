import java.util.Arrays;

/**
 * @author ykm
 * @date 2021/3/25 7:19 下午
 */
public class NumSort {

    static class BubbleSort {
        public static void sort(int[] a) {
            int temp;
            for (int i = 0; i < a.length - 1; i++) {
                for (int j = 0; j < a.length - 1; j++) {
                    if (a[j + 1] < a[j]) {
                        temp = a[j];
                        a[j] = a[j + 1];
                        a[j + 1] = temp;
                    }
                }
            }
        }

        public static void main(String[] args) {
            int[] a = {3, 1, 6, 4, 2, 5};
            sort(a);
            Arrays.stream(a).forEach(System.out::println);
        }
    }

    /**
     * 时间复杂度：O(nlogn)
     */
    static class QuickSort {

        public static void main(String[] args) {
            int[] a = {3, 1, 6, 4, 2, 5};
            sort(a, 0, a.length - 1);
            Arrays.stream(a).forEach(System.out::println);
        }

        public static void sort(int[] a, int low, int high) {
            if (low > high) {
                return;
            }
            int i = low;
            int j = high;
            int key = a[i];
            while (i < j) {
                while (a[j] > key && i < j) {
                    j--;
                }
                if (a[j] < key) {
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
                while (a[i] <= key && i < j) {
                    i++;
                }
                if (a[i] > key) {
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
            sort(a, low, i-1);
            sort(a, i+1, high);
        }
    }

}

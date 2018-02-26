package ind.xwm.imooc.algorithms.sort;


public class NormalSort {

    public static void main(String[] args) {

    }

    /**
     * 冒泡排序
     *
     * @param a 待排序数组
     */
    public static void bubbleSort(int[] a) {
        int i, j, temp;
        for (i = 0; i < a.length; i++) {
            for (j = a.length - 1; j > i; j--) {
                if (a[j] < a[j - 1]) { // 从小到大排序
                    temp = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = temp;
                }
            }
        }
    }

    /**
     * 选择排序 相比冒泡排序减低了数组值交换的次数
     *
     * @param a 待排序数组
     */
    public static void selectionSort(int[] a) {
        int i, j, k, temp;
        for (i = 0; i < a.length; i++) {
            k = i;
            for (j = i + 1; j < a.length; j++) {  // 查找序列当中最小的值
                if (a[k] > a[j]) k = j;
            }
            if (k != i) { // 如果最大的值不是第一个选中的值，则交换
                temp = a[i];
                a[i] = a[k];
                a[k] = temp;
            }
        }
    }

    /**
     * 插入排序
     *
     * @param a 待排序数组
     */
    public void insertSort(int[] a) {
        int n = a.length;
        int i, j, temp;
        for (i = 1; i < n; i++) {
            temp = a[i];
            j = i - 1;
            while (j >= 0 && temp < a[j]) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = temp;
        }
    }


    /**
     * 快速排序：找数组中间值做参考点，从两头相向查找
     * 目的是将 参考点左边的所有小于参考点值的元素 与 参考点右边大于参考点值的元素 进行交换
     * 利用递归
     *
     * @param a 待排序数组
     * @param n 左边起始点下标
     * @param m 右边起始点下标
     */
    public static void quickSort(int[] a, int n, int m) {
        int i = n, j = m, temp;
        int k = a[(i + j) / 2];
        do {
            while (a[i] > k && i < m) i++;
            while (a[j] < k && j > n) j--;
            if (i < j) {
                temp = a[j];
                a[j] = a[i];
                a[i] = temp;
                i++;
                j--;
            }
        } while (i <= j);
        if (j < m) quickSort(a, j, m);
        if (i > n) quickSort(a, n, i);
    }

    /**
     * @param a 待排序数组
     */
    public static void shellSort(int[] a) {
        int n = a.length;
        int i, j, k, temp;
        k = n / 2;
        while (k >= 1) {
            for (i = k; i < n; i++) {
                temp = a[i];
                j = i - k;
                while (j >= 0 && temp < a[j]) {  // 从小到大排序
                    a[j + k] = a[j];
                    j -= k;
                }
                a[j + k] = temp;
            }
            k = k / 2;
        }
    }


    public static void arrSort(int[] a) {
        java.util.Arrays.sort(a);
    }
}

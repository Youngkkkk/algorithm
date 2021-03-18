import java.util.*;

/**
 * 排序组合问题
 *
 * @author ykm
 * @date 2021/1/21 2:59 下午
 */
public class RandomSort {

    /**
     * 求出一个字符串所有排序方式，如”ａｂｃ“　－＞　"ａｂｃ＂、”ａｃｂ“、”ｂａｃ“、”ｂｃａ“、”ｃａｂ“、”ｃｂａ“
     */
    static class Sort1 {
        public static void main(String[] args) {
            String str = "abc";
            char[] chars = str.toCharArray();
            Set<String> result = new HashSet<>();
            sort(chars, 0, result);
            result.forEach(System.out::println);
        }

        /**
         * 思路：
         * 1.首先考虑递归。例如输入字符串abcd，提取第一个字符a，交换bcd的位置；提取第一个字符b，交换acd的位置…以此类推。整个核心代码就是一手交换，难点在于确定每一步的“第一个元素”。
         * 2.接着考虑如何去重与排序，由于Collcetion集合中提供了Set接口：存储无序的（不等于随机性，根据数据的hash值确定），不可重复的数据（用equals()方法 ），
         * 并且Set中有TreeSet类可以实现排序，此种排序方式为默认排序，即，将集合中的元素按升序排序，符合题目要求。
         * 3.考虑如何将“第一个元素”确定。使用一个循环，将第一个元素于其他所有元素进行交换。
         * 4.考虑递归的终止条件。当递归到第一个元素即为字符串末尾时，结束递归，将这个字符串添加到最终结果。
         *
         * @param chars
         * @param index
         * @param result
         */
        public static void sort(char[] chars, int index, Set<String> result) {
            if (chars.length <= 0) {
                return;
            }
            if (index == chars.length - 1) {
                result.add(String.valueOf(chars));
            }
            for (int i = index; i < chars.length; i++) {
                // 将第i个数与剩余字符串第一个数交换
                char temp = chars[i];
                chars[i] = chars[index];
                chars[index] = temp;

                sort(chars, index + 1, result);

                // 将数据还原，以便下次循环
                temp = chars[i];
                chars[i] = chars[index];
                chars[index] = temp;
            }

        }
    }

    /**
     * 输入字符串，求它们的所有组合。例如输入abc，则有a、b、c、ab、ac、bc、abc这几种组合。（不考虑重复输入）
     */
    static class Sort2 {

        public static void main(String[] args) {
            String str = "abc";
            List<String> result = new ArrayList<>();
            sort(str, result);
            result.forEach(System.out::println);
        }


        public static void sort(String str, List<String> result) {
            for (int len = 1; len <= str.length(); len++) {
                perm(str, len, new ArrayList<>());
            }
        }

        /**
         * 题目解析：很容易想到递归的方式，但是这道题的递归有点特别，需要分别采用两种递归方式。
         * 假设在长度为n的字符串中选择长度为m的组合字符串，情况1，第一是选择长度为n的字符串中的第一个字符，那么要在其余的长度n-1的字符串中选择m-1个字符，
         * 比如输入abc，如果递归过程中m=2，选择第一个字符为a，那么之后要选择b与c，组成ab、ac。
         * 情况2，第二是不选择长度为n的字符串中的第一个字符，那么要在其余的长度n-1的字符串中选择m个字符，
         * 如输入abc，如果递归过程中m=2，选择bc。递归结束的条件就是，当m为0，即从字符串中不再选出字符的时候，这个时候已经找到了m个字符的组合，输出即可。
         *
         * @param s
         * @param len
         * @param temp
         */
        public static void perm(String s, int len, List<String> temp) {
            if (len == 0) {
                System.out.println(temp);
                return;
            }
            if (s.length() > 0) {
                // 当前字符串的第一个元素选中
                temp.add(String.valueOf(s.charAt(0)));
                perm(s.substring(1), len - 1, temp);
                // 取完一个后删除最近一个
                temp.remove(temp.size() - 1);
                // 当前字符串的第一个元素不选中
                perm(s.substring(1), len, temp);
            }

        }

    }

    /**
     * 在一个8X8的棋盘中，放置八个棋子，每个棋子不能处在同一行、同一列、同一对角线上。下图是一个八皇后的实例。
     * 思路和Sort1类似
     */
    static class Sort3 {
        public static void main(String[] args) {

            int n = 8;
            // 定义一个长度为n的数组，数组下标代表所处列，值代表所处行
            int[] column = new int[n];
            // 初始化状态为每个棋子都不在同一行和同一列，但处于同一对角线上，之后则采取行不变，列值两两交换，
            // 列举出所有情况，找出满足不在同一对角的情况，即满足，和Sort1题例高度类似
            for (int i = 0; i < column.length; i++) {
                column[i] = i;
            }
            combine(column, 0);
        }

        public static void combine(int[] column, int index) {
            if (index == column.length - 1) {
                // 判断是否在同一对角上
                // System.out.println(Arrays.toString(column));
                if (isAcross(column)) {
                    System.err.println(Arrays.toString(column));
                }
                return;
            }
            for (int i = index; i < column.length; i++) {
                // 将第i个数与当前数组第一个交换
                swap(column, i, index);
                combine(column, index + 1);
                swap(column, i, index);
            }
        }

        public static boolean isAcross(int[] column) {
            for (int i = 0; i < column.length; i++) {
                for (int j = i + 1; j < column.length; j++) {
                    if (Math.abs(i - j) == Math.abs(column[i] - column[j])) {
                        return false;
                    }
                }
            }
            return true;
        }

        public static void swap(int[] a, int i, int j) {
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

    }


}

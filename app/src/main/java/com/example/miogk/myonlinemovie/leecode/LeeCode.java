package com.example.miogk.myonlinemovie.leecode;

import java.util.HashSet;

public class LeeCode {
    // * Definition for a binary tree node.
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    class Solution {
        int depth1 = 0;
        int depth2 = 0;

        public int maxDepth(TreeNode root) {
            TreeNode left = root.left;
            TreeNode right = root.right;
            if (left != null) {
                depth1++;
                maxDepth(left);
            }
            if (right != null) {
                depth2++;
                maxDepth(right);
            }
            return Math.max(depth1, depth2);
        }
    }

    class solution2 {
        public int uniqueMorseRepresentations(String[] words) {
            if (words == null || words.length == 0) {
                return -1;
            }
            HashSet<String> hs = new HashSet<>();
            String[] ss = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
            StringBuilder sb = new StringBuilder();
            for (String s1 : words) {
                char[] cs = s1.toCharArray();
                for (char c : cs) {
                    sb.append(ss[c - 'a'] + 1);
                }
                hs.add(sb.toString());
            }
            return hs.size();
        }

        //输入：A = [1,2,3,4], queries = [[1,0],[-3,1],[-4,0],[2,3]]
        //输出：[8,6,2,4]
        //解释：
        //开始时，数组为 [1,2,3,4]。
        //将 1 加到 A[0] 上之后，数组为 [2,2,3,4]，偶数值之和为 2 + 2 + 4 = 8。
        //将 -3 加到 A[1] 上之后，数组为 [2,-1,3,4]，偶数值之和为 2 + 4 = 6。
        //将 -4 加到 A[0] 上之后，数组为 [-2,-1,3,4]，偶数值之和为 -2 + 4 = 2。
        //将 2 加到 A[3] 上之后，数组为 [-2,-1,3,6]，偶数值之和为 -2 + 6 = 4。

        //                内存消耗
        //	通过	1709 ms	59.5 MB
        public int[] sumEvenAfterQueries(int[] A, int[][] queries) {
            int[] result = new int[A.length];
            for (int i = 0; i < queries.length; i++) {
                int[] q = queries[i];
                int index = q[1];
                int add = q[0];
                int a = A[index];
                a = a + add;
                A[index] = a;
                int temp = 0;
                for (int aa : A) {
                    if (aa % 2 == 0) {
                        temp += aa;
                    }
                }
                result[i] = temp;
            }
            return result;
        }

        //给定一个二进制矩阵 A，我们想先水平翻转图像，然后反转图像并返回结果。
        //
        //水平翻转图片就是将图片的每一行都进行翻转，即逆序。例如，水平翻转 [1, 1, 0] 的结果是 [0, 1, 1]。
        //
        //反转图片的意思是图片中的 0 全部被 1 替换， 1 全部被 0 替换。例如，反转 [0, 1, 1] 的结果是 [1, 0, 0]。
        //
        //示例 1:
        //
        //输入: [[1,1,0],[1,0,1],[0,0,0]]
        //输出: [[1,0,0],[0,1,0],[1,1,1]]
        //解释: 首先翻转每一行: [[0,1,1],[1,0,1],[0,0,0]]；
        //     然后反转图片: [[1,0,0],[0,1,0],[1,1,1]]
        //示例 2:
        //
        //输入: [[1,1,0,0],[1,0,0,1],[0,1,1,1],[1,0,1,0]]
        //输出: [[1,1,0,0],[0,1,1,0],[0,0,0,1],[1,0,1,0]]
        //解释: 首先翻转每一行: [[0,0,1,1],[1,0,0,1],[1,1,1,0],[0,1,0,1]]；
        //     然后反转图片: [[1,1,0,0],[0,1,1,0],[0,0,0,1],[1,0,1,0]]
        public int[][] flipAndInvertImage(int[][] A) {
            int[][] result = new int[A.length][];
            for (int i = 0; i < A.length; i++) {
                int[] a = A[i];
                int[] b = new int[a.length];
                int len = a.length;
                for (int j = 0; j < len; j++) {
                    //水平翻转图片 --- 向右或者向左180°
                    b[j] = a[len - j - 1];
                    //反转图片 --- 0 > 1 || 1 > 0
                    if (b[j] == 0) {
                        b[j] = 1;
                    } else if (b[j] == 1) {
                        b[j] = 0;
                    }
                }
                result[i] = b;
            }
            return result;
        }
    }
}

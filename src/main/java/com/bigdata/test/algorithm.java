package com.bigdata.test;


import java.util.Arrays;

public class algorithm {

    public static void main(String[] args) {
        int[] str = {9,8,7,6,5,4,3,2,1,0,-1,-2,-3,};
        System.out.println("排序之前的数组：" + Arrays.toString(str));
//        maopao(str);
        xuanze(str);
        System.out.println("排序之后的数组：" + Arrays.toString(str));
    }

    /**
     * 1.冒泡排序
     *      （1）比较相邻的元素。如果第一个比第二个大，就交换它们两个；
     *      （2）对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
     *      （3）针对所有的元素重复以上的步骤，除了最后一个；
     *      （4）重复步骤1~3，直到排序完成。
     *
     *   时间复杂度：n^2
     *
     * */
    public static void maopao(int[] str){
        for (int i = 0;i < str.length; i++){
            for (int j = i+1;j< str.length ;j++){
                if(str[j] < str[i]){
                    int tmp = str[j];
                    str[j] = str[i];
                    str[i] = tmp;
                }
            }
        }
//        System.out.println("排序之后的数组：" + Arrays.toString(str));
    }

    /**
     * 2.选择排序
     *       原理：首先在未排序序列中找到最小（大）元素，存放在排序序列的起始位置，然后，在从剩余的未排序元素中
     *       继续寻找最小（大）元素，然后放到已排序序列的末尾，以此类推，知道所有元素均排序完毕。
     *
     *  时间复杂度：n(n-1)(n-2)...1 = n^2
     * */
    public static void xuanze(int[] str){
        for (int i = 0; i < str.length; i++){
            int minIndex = i;
            for (int j = i+1;j < str.length ;j++){
                if(str[j] < str[minIndex]){
                    minIndex = j;
                }
            }
            int tmp = str[i];
            str[i] = str[minIndex];
            str[minIndex] =tmp;
        }
    }

    /**
     * 3.插入排序
     *      原理：通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。
     *      步骤：
     *          （1）从第一个元素开始，该元素可以认为已经被排序；
     *          （2）取出下一个元素，在已经排序的元素序列中从后向前扫描；
     *          （3）如果该元素（已排序）大于新元素，将改元素移到下一位置；
     *          （4）重复步骤3，直到找到已排序的元素小于或者等于新元素的位置
     * */
}

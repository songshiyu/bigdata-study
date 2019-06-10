package com.bigdata.hadoop.java.btree;

/**
 * @author songshiyu
 * @date 2019/6/5 11:50
 */
public class TestBSCZTree {

    private static final int arr[] = {5,1,4,3,2,6};

    public static void main(String[] args) {

        BSCZTree<Integer> tree=new BSCZTree<Integer>();

        for (int i =0; i< arr.length; i++){
            tree.insert(arr[i]);
        }
        System.out.println(tree);
    }
}

package com.bigdata.hadoop.java.btree;

/**
 * @author songshiyu
 * @date 2019/6/5 11:15
 * <p>
 * 二叉查找树
 */
public class BSCZTree<T extends Comparable<T>> {

    private BSTNode<T> mroot;  //根节点

    public class BSTNode<T extends Comparable<T>> {
        T key;          //关键字(键值)
        BSTNode<T> left;    //左孩子
        BSTNode<T> right;    //左孩子
        BSTNode<T> parent;    //父节点

        public BSTNode(T key, BSTNode<T> left, BSTNode<T> right, BSTNode<T> parent) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public T getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "key:" + key;
        }
    }

    public BSCZTree() {
        mroot = null;
    }

    /**
     * 新建节点(key),并将其插入到二叉树中
     * <p>
     * 参数说明:
     * tree 二叉树的根节点
     * key 插入节点的键值
     */
    public void insert(T key) {
        BSTNode<T> z = new BSTNode<>(key, null, null, null);

        //如果新建节点失败，则返回
        if (z != null) {
            insert(this, z);
        }
    }

    /**
     * 将节点插入二叉树中
     * <p>
     * 参数说明
     * tree 二叉树
     * z 插入的节点
     */
    public void insert(BSCZTree<T> tree, BSTNode<T> z) {
        int cmp;
        BSTNode<T> y = null;
        BSTNode<T> x = tree.mroot;

        //查找z的插入位置---找出z的父节点是谁，也就是y
        while (x != null) {
            y = x;
            cmp = z.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        //若z没有父节点，那么z就是根节点
        if (y == null) {
            tree.mroot = z;
        } else {
            //确定z插入在父节点的左侧还是右侧
            cmp = z.key.compareTo(y.key);
            if (cmp < 0) {
                y.left = z;
            } else {
                y.right = z;
            }
        }
    }

    /**
     * 前序遍历二叉树
     */
    public void perOrder(BSTNode<T> tree){
        if (tree != null){
            System.out.println(tree.key + " ");
            perOrder(tree.left);
            perOrder(tree.right);
        }
    }

    public void perOrder(){
        perOrder(mroot);
    }


    /**
     * 中序遍历二叉树
     * */

    /**
     * 后序遍历二叉树
     * */

    /**
     *  (递归实现)查找"二叉树x"中键值为key的节点
     * */

    /**
     * (非递归实现)查找"二叉树x"中键值为key的节点
     * */

    /**
     *  查找最小结点：返回tree为根结点的二叉树的最小结点。
     * */

    /**
     * 查找最大结点：返回tree为根结点的二叉树的最大结点。
     * */

    /**
     * 找结点(x)的后继结点。即，查找"二叉树中数据值大于该结点"的"最小结点"。
     * */

    /**
     * 找结点(x)的前驱结点。即，查找"二叉树中数据值小于该结点"的"最大结点"。
     * */

    /**
     * 删除结点(z)，并返回被删除的结点
     * 参数说明：
     *   tree 二叉树的根结点
     *   z 删除的结点
     * */


    /**
     * 销毁二叉树
     * */


    /**
     * 打印"二叉查找树"
     *  key        -- 节点的键值
     *  direction  --  0，表示该节点是根节点;
     *                 -1，表示该节点是它的父结点的左孩子;
     *                 1，表示该节点是它的父结点的右孩子。
     *
     * */
}

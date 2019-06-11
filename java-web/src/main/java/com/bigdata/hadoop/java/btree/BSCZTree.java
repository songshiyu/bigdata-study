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
    private void insert(BSCZTree<T> tree, BSTNode<T> z) {
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
    private void perOrder(BSTNode<T> tree) {
        if (tree != null) {
            System.out.println(tree.key + " ");
            perOrder(tree.left);
            perOrder(tree.right);
        }
    }

    public void perOrder() {
        perOrder(mroot);
    }


    /**
     * 中序遍历二叉树
     */
    private void inOrder(BSTNode<T> tree) {
        if (tree != null) {
            inOrder(tree.left);
            System.out.println(tree.key + " ");
            inOrder(tree.right);
        }
    }

    public void inOrder() {
        inOrder(mroot);
    }

    /**
     * 后序遍历二叉树
     */
    private void postOrder(BSTNode<T> tree) {
        if (tree != null) {
            postOrder(tree.left);
            postOrder(tree.right);
            System.out.println(tree.key + " ");
        }
    }

    public void postOrder() {
        postOrder(mroot);
    }

    /**
     * (递归实现)查找"二叉树x"中键值为key的节点
     */
    public BSTNode<T> search(T key) {
        return search(mroot, key);
    }

    private BSTNode<T> search(BSTNode<T> x, T key) {
        if (x == null) return x;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return search(x.left, key);
        else if (cmp > 0) return search(x.right, key);
        else return x;
    }


    /**
     * (非递归实现)查找"二叉树x"中键值为key的节点
     */
    public BSTNode<T> iterativeSearch(T key) {
        return iterativeSearch(mroot, key);
    }

    private BSTNode<T> iterativeSearch(BSTNode<T> x, T key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                return x;
            }
        }
        return x;
    }

    /**
     * 查找最小结点：返回tree为根结点的二叉树的最小结点。
     */

    public T minimum() {
        BSTNode<T> p = minimum(mroot);
        if (p != null) return p.key;

        return null;
    }

    private BSTNode<T> minimum(BSTNode<T> tree) {
        if (tree == null) return null;

        while (tree.left != null) {
            tree = tree.left;
        }
        return tree;
    }

    /**
     * 查找最大结点：返回tree为根结点的二叉树的最大结点。
     */
    public T maxmum() {
        BSTNode<T> maxmum = maxmum(mroot);
        if (maxmum == null) return null;
        return maxmum.key;
    }

    private BSTNode<T> maxmum(BSTNode<T> mroot) {
        if (mroot == null) return null;
        while (mroot.right != null) {
            mroot = mroot.right;
        }
        return mroot.right;
    }


    /**
     * 找结点(x)的后继结点。即，查找"二叉树中数据值大于该结点"的"最小结点"。
     */
    public BSTNode<T> getSuccessor(BSTNode<T> key) {
        //如果key存在右孩子，则"key的后继结点"为 "以其右孩子为根的子树的最小结点"
        if (key.right != null) return minimum(key.right);
        //如果该节点没有右孩子，那么有两种情况
        //①如果该节点是一个左节点，则后继节点为其父节点
        //②如果该节点是一个右节点，则查找key最低的父节点，并且该父节点要有左孩子，这个最低的父节点就是该节点的后继节点
        // TODO 我对此处有疑问，既然该节点是右节点，那么该节点一定比其父节点大，那么为什么还能是后继节点呢？
        BSTNode<T> parent = key.parent;
        while ((parent != null) && (key == parent.right)) {
            key = parent;
            parent = parent.parent;
        }
        return parent;
    }

    /**
     * 找结点(x)的前驱结点。即，查找"二叉树中数据值小于该结点"的"最大结点"。
     */
    public BSTNode<T> getPredecessor(BSTNode<T> key) {
        //如果该节点存在左节点，则前驱节点为"以其左孩子为根的子树的最大节点"。
        if (key.left != null) return maxmum(key.left);

        //如果该节点不存在左节点，则前驱节点有两种情况
        //①key是一个右孩子，则"key的前驱节点为他的父节点"
        //②key是一个左孩子，则查找"key的最低的父结点，并且该父结点要具有右孩子"，找到的这个"最低的父结点"就是"key的前驱结点"。
        // TODO 我对此处有疑问，既然该节点是左节点，那么该节点一定比其父节点小，那么为什么其最低的父节点还能是后继节点呢？
        BSTNode<T> parent = key.parent;
        while ((parent != null) && (key == parent.left)) {
            key = parent;
            parent = parent.parent;
        }
        return parent;
    }

    /**
     * 删除结点(z)，并返回被删除的结点
     * 参数说明：
     * tree 二叉树的根结点
     * z 删除的结点
     * <p>
     * TODO 此处也没有明白，需要在看一遍
     */
    public void remove(T key) {
        BSTNode<T> z, node;
        if ((z = search(mroot, key)) != null)
            if ((node = remove(this, z)) != null)
                node = null;
    }

    private BSTNode<T> remove(BSCZTree<T> tbsczTree, BSTNode<T> z) {
        BSTNode<T> x = null;
        BSTNode<T> y = null;
        if ((z.left == null) || (z.right == null))
            y = z;
        else
            y = getSuccessor(z);
        if (y.left != null)
            x = y.left;
        else
            x = y.right;
        if (x != null)
            x.parent = y.parent;
        if (y.parent == null)
            tbsczTree.mroot = x;
        else if (y == y.parent.left)
            y.parent.left = x;
        else
            y.parent.right = x;
        if (y != z)
            z.key = y.key;
        return y;
    }


    /**
     * 销毁二叉树
     */
    private void destroy(BSTNode<T> tree) {
        if (tree == null)
            return;
        if (tree.left != null)
            destroy(tree.left);
        if (tree.right != null)
            destroy(tree.right);
        tree = null;
    }

    public void clear() {
        destroy(mroot);
        mroot = null;
    }

    /**
     * 打印"二叉查找树"
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     * -1，表示该节点是它的父结点的左孩子;
     * 1，表示该节点是它的父结点的右孩子。
     */
    private void print(BSTNode<T> tree, T key, int direction) {
        if (tree != null) {
            if (direction == 0)    // tree是根节点
                System.out.printf("%2d is root\n", tree.key);
            else                // tree是分支节点
                System.out.printf("%2d is %2d's %6s child\n", tree.key, key, direction == 1 ? "right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);

        }
    }

    public void print() {
        if (mroot != null)
            print(mroot, mroot.key, 0);
    }
}

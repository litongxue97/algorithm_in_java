package dataStructure.tree;

/**
 * @author : liqinzuo
 * @date : 2023/11/4
 * Description :
 */
public class MorrisTree {

    //普通遍历时间复杂度O(n),空间复杂度O(logn),树高度
    // Morris优化空间为O(1),利用空闲指针实现
    // (1) cur没有左树,cur = cur.right
    // (2) cur有左树,找左子树的最右节点mostRight
           // 1. 若mostRight.right = null; mostRight.right = cur; cur = cur.left
           // 2. 若mostRight.right = cur; 则让mostRight.right = null; cur = cur.right;
    public static class Node {
        public int value;
        Node left;
        Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static void morris(Node head) {
        if (head == null) {
            return ;
        }
        Node cur = head;
        Node mostRight;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null ) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            cur = cur.right;
        }
    }

    public static void morrisIn(Node head) {
        if (head == null) {
            return ;
        }
        Node cur = head;
        Node mostRight;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null ) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            System.out.print(cur.value + " ");
            cur = cur.right;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(4);
        head.left = new Node(3);
        head.left.left = new Node(1);
        head.left.right = new Node(5);
        head.right = new Node(2);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        morrisIn(head);
        //1 3 5 4 6 2 7
    }

}

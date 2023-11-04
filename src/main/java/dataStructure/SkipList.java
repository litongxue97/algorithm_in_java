package dataStructure;

import java.util.*;

/**
 * @author : liqinzuo
 * @date : 2023/11/3
 * Description :
 */
public class SkipList {

    public static class SkipListNode<K extends Comparable<K>, V> {
        public K key;
        public V val;
        public List<SkipListNode<K, V>> nextNodes;

        public SkipListNode(K k, V v) {
            this.key = k;
            this.val = v;
            this.nextNodes = new ArrayList<>();
        }

        public boolean isKeyLess(K otherKey) {
            return otherKey != null && (key == null || key.compareTo(otherKey) < 0);
        }

        public boolean isKeyEqual(K otherKey) {
            return (key == null && otherKey == null)
                            || (key != null &&  otherKey != null && key.compareTo(otherKey) == 0);
        }
    }

    public static class SkipListMap<K extends Comparable<K>, V> {
        private static final double PROBABILITY = 0.5;
        private SkipListNode<K, V> head;
        private int size;
        private int maxLevel;

        public SkipListMap() {
            head = new SkipListNode<>(null, null);
            head.nextNodes.add(null);
            size = 0;
            maxLevel = 0;
        }

        //在跳表中查找离key最近的比key小的数，一层一层往下找
        // --|
        //   |__  台阶状
        //最终找到< key的最右的节点返回
        public SkipListNode<K,V> mostRightLessNodeInTree(K key) {
            if (key == null) {
                return null;
            }
            int level = maxLevel;
            SkipListNode<K,V> cur = head;
            while (level >= 0) {
                cur = mostRightLessNodeInLevel(key, cur, level--);
            }
            return cur;
        }

        //cur节点的第level层，开始往右边找，最终在level层上找到< key的最右的节点返回
        private SkipListNode<K,V> mostRightLessNodeInLevel(K key, SkipListNode<K,V> cur, int level) {
            SkipListNode<K,V> next = cur.nextNodes.get(level);
            while (next != null && next.isKeyLess(key)) {
                // 要找20，当前在5
                // 5 -> 10 -> 30
                cur = next;
                next = cur.nextNodes.get(level);
            }
            return cur;
        }

        public void put(K key, V value) {
            if (key == null) {
                return ;
            }
            SkipListNode<K, V> less = mostRightLessNodeInTree(key);
            //看第0层<key的最右边的节点的下一个key,如果已经存在当前key，那么更新val,否则加入；
            SkipListNode<K, V> find = less.nextNodes.get(0);
            if(find != null && find.isKeyEqual(key)) {
                find.val = value;
            } else {
                size ++;
                int newNodeLevel = 0;
                //摇骰子 <0.5继续摇骰子 >= 0.5退出
                while (Math.random() < PROBABILITY) {
                    newNodeLevel ++;
                }
                //当前头节点的层数是5，但是新加的摇出来的20层,此时头节点需要膨胀
                while(newNodeLevel > maxLevel) {
                    head.nextNodes.add(null);
                    maxLevel ++;
                }
                SkipListNode<K, V> newNode = new SkipListNode<>(key, value);
                for (int i = 0; i <= newNodeLevel; i++ ) {
                    newNode.nextNodes.add(null);
                }
                int level = maxLevel;
                SkipListNode<K, V> pre = head;
                while (level >= 0) {
                    pre = mostRightLessNodeInLevel(key, pre, level);
                    //如果摇出来的层数（6）小于当前的最大层数（10），那么从6才开始链接
                    if (level <= newNodeLevel) {
                        //newNode的下一个指向pre的下一个
                        newNode.nextNodes.set(level, pre.nextNodes.get(level));
                        //pre的下一个指向newNode
                        pre.nextNodes.set(level, newNode);
                    }
                    level --;
                }
            }
        }

        public V get(K key) {
            if (key == null) {
                return null;
            }
            SkipListNode<K,V> less = mostRightLessNodeInTree(key);
            SkipListNode<K,V> next = less.nextNodes.get(0);
            return next != null && next.isKeyEqual(key) ? next.val : null;
        }

        public void remove(K key) {
            if (containsKey(key)) {
                size --;
                int level = maxLevel;
                SkipListNode<K,V> pre = head;
                while (level >= 0) {
                    pre = mostRightLessNodeInLevel(key, pre, level);
                    SkipListNode<K, V> next = pre.nextNodes.get(level);
                    if (next != null && next.isKeyEqual(key)) {
                        pre.nextNodes.set(level, next.nextNodes.get(level));
                    }
                    //删除head中空余的位置，可选
                    if (level != 0 && pre == head && pre.nextNodes.get(level) == null) {
                        head.nextNodes.remove(level);
                        maxLevel --;
                    }
                    level --;
                }
            }
        }

        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            SkipListNode<K,V> less = mostRightLessNodeInTree(key);
            SkipListNode<K,V> next = less.nextNodes.get(0);
            return next != null && next.isKeyEqual(key);
        }

        public int size() {
            return size;
        }
    }

    // for test
    public static void printAll(SkipListMap<String, String> obj) {
        for (int i = obj.maxLevel; i >= 0; i--) {
            System.out.print("Level " + i + " : ");
            SkipListNode<String, String> cur = obj.head;
            while (cur.nextNodes.get(i) != null) {
                SkipListNode<String, String> next = cur.nextNodes.get(i);
                System.out.print("(" + next.key + " , " + next.val + ") ");
                cur = next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipListMap<String, String> test = new SkipListMap<>();
        printAll(test);
        System.out.println("======================");
        test.put("A", "10");
        printAll(test);
        System.out.println("======================");
        test.remove("A");
        printAll(test);
        System.out.println("======================");
        test.put("E", "E");
        test.put("B", "B");
        test.put("A", "A");
        test.put("F", "F");
        test.put("C", "C");
        test.put("D", "D");
        printAll(test);
    }
}

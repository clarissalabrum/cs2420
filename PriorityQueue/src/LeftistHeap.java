import java.util.ArrayList;

public class LeftistHeap<E extends Comparable<? super E>> {
    private Tree<E> tree = new Tree<>(null);

    public LeftistHeap(ArrayList<E> vals){
        for (E val : vals) {
            insert(val);
        }
    }

    /**
     * primer method to merge two trees.
     */
    private Tree<E> merge(Tree<E> tree1, Tree<E> tree2){
        if(tree1 == null)
            return tree2;
        if(tree2 == null)
            return tree1;
        if(tree1.element == null)
            return tree2;
        if(tree2.element == null)
            return tree1;
        if(tree1.element.compareTo( tree2.element ) < 0)
            return mergeRec(tree1, tree2);
        else
            return mergeRec(tree2, tree1);
    }

    /**
     * recursive method that recursively merges tree1 and tree2
     * tree1 has the smaller value.
     */
    private Tree<E> mergeRec(Tree<E> tree1, Tree<E> tree2){
        if (tree1.left == null)
            tree1.left = tree2;
        else {
            tree1.right = merge(tree1.right, tree2);
            if (tree1.left.dist < tree1.right.dist)
                swapChildren(tree1);
            tree1.dist = tree1.right.dist + 1;
        }
        return tree1;
    }

    /**
     * Swaps a trees children
     */
    private void swapChildren(Tree<E> node) {
        Tree<E> temp = node.left;
        node.left = node.right;
        node.right = temp;
    }

    /**
     * Insert a new value into a tree
     * @param val the item to insert.
     */
    public void insert(E val){
        tree = merge(new Tree<E>(val), tree);
    }

    /**
     * delete min and return its value
     * @return the smallest item
     */
    public E deleteMin(){
        E minItem = tree.element;
        tree = merge( tree.left, tree.right );
        return minItem;
    }

    /**
     * Tree class creates a tree object with a left and right child
     */
    public static class Tree <E extends Comparable<? super E>>{
        E element;
        Tree<E> left;
        Tree<E> right;
        int dist;

        Tree(E val){
            element = val;
            left = null;
            right = null;
            dist = 0;
        }
    }
}


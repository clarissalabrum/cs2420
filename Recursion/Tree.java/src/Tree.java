// ******************ERRORS********************************
// Throws UnderflowException as appropriate

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    final String ENDLINE = "\n";
    private BinaryNode<E> root;  // Root of tree
    private BinaryNode<E> curr;  // Last node accessed in tree
    private String treeName;     // Name of tree
    private String str = "";     // Used in toString

    /**
     * Create an empty tree
     *
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create non ordered tree from list in preorder
     * @param arr    List of elements
     * @param label  Name of tree
     * @param height Maximum height of tree
     */
    public Tree(ArrayList<E> arr, String label, int height) {
        this.treeName = label;
        root = buildTree(arr, height, null);
    }

    /**
     * Create BST
     * @param arr   List of elements to be added
     * @param label Name of tree
     */
    public Tree(ArrayList<E> arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.size(); i++) {
            bstInsert(arr.get(i));
        }
    }

    /**
     * Create BST from Array
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.length; i++) {
            bstInsert(arr[i]);
        }
    }

    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents as a tree with one node per line
     */
    public String toString() {
        str = "";
        if (root == null)
            return (treeName + " Empty tree\n");
        else {
            //System.out.println(treeName);
            toString(0, root);
            return (treeName + str);
        }
    }

    /**
     * This Method Prints out the tree from largest to smallest and indents according to its position in the tree.
     * Each line contains the element and its parent.
     * Order complexity O(n).
     * @param length keeps track of how deep in the tree the node is
     * @param node the root node of each sub tree
     */
    public void toString(int length, BinaryNode<E> node){
        if (node == null) return;

        toString(length + 1, node.right);

        // save contents to a string
        String spaces = "";
        for (int i = 0; i < length; i++) spaces += " ";
        if(node.parent != null) str += "\n" + spaces + node.element + "[" + node.parent.element + "]";
        else str += "\n" + node.element + "[no parent]";

        toString(length + 1, node.left);
    }

    /**
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * reverse left and right children recursively
     */
    public void flip() {
        str = "";
        flip(root);
    }

    /**
     * This method switch the left and right child on every node
     * Order complexity O(n)
     * @param node root of each sub tree
     */
    public void flip(BinaryNode node){
        if (node == null) return;

        flip(node.left);

        BinaryNode temp = node.left;
        node.left = node.right;
        node.right = temp;

        flip(node.left);
    }

    /**
     * Find successor of "curr" node in tree
     * @return String representation of the successor
     */
    public String successor() {
        if (curr == null) curr = root;
        curr = successor(curr);
        if (curr == null) return "null";
        else return curr.toString();
    }

    /**
     * This method find the next greater node in the tree
     * The complexity is O(n)
     * @param node current node in which the successor is being hunted for
     * @return the successor node
     */
    public BinaryNode<E> successor(BinaryNode<E> node){
        if (node == curr){
            if (node.right != null){
                BinaryNode<E> traverse = node.right;
                while (traverse.left != null) traverse = traverse.left;
                return traverse;
            }
        }
        int compareResults = node.parent.element.compareTo(node.element);
        if (compareResults >= 0) return node.parent;
        return successor(node.parent);
    }

    /**
     * Counts number of nodes in specifed level
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        str = "";
        return nodesInLevel(root, level, 0);
    }

    /**
     * This method will dig into the tree and when the specified level is reached it will count how
     * many nodes are there.
     * Complexity O(n)
     * @param node root of sub tree
     * @param level level looking to reach
     * @param height Level currently at
     * @return the number of nodes in each level
     */
    public int nodesInLevel(BinaryNode<E> node, int level, int height){
        if (node == null) return 0;
        if (height == level) return 1;
        return nodesInLevel(node.left, level, height + 1) + nodesInLevel(node.right, level, height + 1);
    }

    /**
     * Print all paths from root to leaves
     */
    public void printAllPaths() {
        printAllPaths(root);
    }

    /**
     * This method traveses to each leaf node and then comes back to the root printing the path from
     * the root to the leaf node.
     * Complexity O(n)
     * @param node root of each sub tree
     */
    public void printAllPaths(BinaryNode<E> node){
        if (node == null) return;
        if (node.left == null && node.right == null){
            ArrayList<BinaryNode<E>> path = new ArrayList<BinaryNode<E>>();
            BinaryNode<E> pathNode = node;
            path.add(pathNode);
            while (pathNode.parent != null) {
                path.add(pathNode.parent);
                pathNode = pathNode.parent;
            }
            for (int i = path.size()-1; i>= 0; i--){
                System.out.print(path.get(i).element + " ");
            }
            System.out.println();
            return;
        }
        printAllPaths(node.left);
        printAllPaths(node.right);
    }

    /**
     * Print contents of levels in zig zag order starting at maxLevel
     * @param maxLevel how deep in the tree to go
     */
    public void byLevelZigZag(int maxLevel) {
        for (int i = maxLevel; i >= 0; i--) {
            ArrayList<BinaryNode<E>> level = byLevelZigZag(root, i, 0, new ArrayList<>());
            if ((i % 2) == 0){
                for (int j = level.size() - 1; j >= 0; j--) {
                    System.out.print(level.get(j).element + " ");
                }
            }
            if ((i % 2) == 1){
                for (int j = 0; j < level.size(); j++) {
                    System.out.print(level.get(j).element + " ");
                }
            }
        }
        System.out.println();
    }

    /**
     * This method creates an array list of all the nodes in one level of a tree
     * Complexity O(n)
     * @param node root of each sub tree
     * @param level desired level to hit
     * @param height current height of node
     * @param list list of nodes at specified level
     * @return array list of nodes at the specified level
     */
    public ArrayList<BinaryNode<E>> byLevelZigZag(BinaryNode<E> node, int level, int height, ArrayList<BinaryNode<E>> list){
        if (node == null) return list;
        if (height == level) {
            list.add(node);
            return list;
        }
        byLevelZigZag(node.left, level, height + 1, list);
        byLevelZigZag(node.right, level, height + 1, list);
        return list;
    }

    /**
     * Counts all non-null binary search trees embedded in tree
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null) return 0;
        ArrayList<BinaryNode<E>> list = countBST(root, new ArrayList<>());
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count++;
            //Print out root od sub BST
            //System.out.print(list.get(i).element + " ");
        }
        return count;
    }

    /**
     * This method checks every node to see if it is the root of a sub BST
     * Complexity O(n)
     * @param node root of each sub tree
     * @param list list of the roots of the BST
     * @return returns the list of roots that are a sub BST
     */
    public ArrayList<BinaryNode<E>> countBST(BinaryNode<E> node, ArrayList<BinaryNode<E>> list){
        if (node == null) return list;

        countBST(node.left, list);
        countBST(node.right, list);

        int compareLeft;
        int compareRight;

        if (node.left != null) compareLeft = node.left.element.compareTo(node.element);
        else compareLeft = 0;
        if (node.right != null) compareRight = node.right.element.compareTo(node.element);
        else compareRight = 0;

        //if leaf node
        if (compareLeft ==0 && compareRight ==0) {
            list.add(node);
            return list;
        }

        //check for non leaf nodes
        if (compareLeft <= 0 ) {
            if (compareRight >= 0) {
                if (list.contains(node.left) && list.contains(node.right)) {
                    list.add(node);
                    return list;
                }
            }
        }
        return list;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * @param x the item to insert.
     */
    public void bstInsert(E x) {
        root = bstInsert(x, root, null);
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return bstContains(item, root);
    }

    /**
     * Remove all paths from tree that sum to less than given value
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer sum) {
        ArrayList<BinaryNode<E>> list = pruneK(sum, new ArrayList<>(), root);
        ArrayList<BinaryNode<E>> treeList = listOfTree(root, new ArrayList<>());
        for (int i = 0; i < treeList.size(); i++) {
            if (!list.contains(treeList.get(i))){
                BinaryNode<E> parent = treeList.get(i).parent;
                if (parent.left == treeList.get(i)) parent.left = null;
                if (parent.right == treeList.get(i)) parent.right = null;
            }
        }

    }

    /**
     * This method will take every path and count the values, if they are greater than the sum each path node
     * is added to the returned array
     * Complexity O(n^2)
     * @param sum value the path needs to surpass
     * @param list arraylist containing all surpassing nodes
     * @param node root of each sub tree
     * @return array list of all the nodes that need to be kept
     */
    public ArrayList<BinaryNode<E>> pruneK(int sum, ArrayList<BinaryNode<E>> list, BinaryNode<E> node){
            if (node == null) return list;

            //create every path
            if (node.left == null && node.right == null){
                ArrayList<BinaryNode<E>> path = new ArrayList<BinaryNode<E>>();
                BinaryNode<E> pathNode = node;
                path.add(pathNode);
                while (pathNode.parent != null) {
                    path.add(pathNode.parent);
                    pathNode = pathNode.parent;
                }
                //count the path
                Integer count = 0;
                for (int i = 0; i < path.size(); i++) {
                    count += (Integer) path.get(i).element;
                }
                //check is path is greater than the needed sum
                if (count >= sum){
                    for (BinaryNode<E> eBinaryNode : path) {
                        list.add(eBinaryNode);
                    }
                }
                return list;
            }
            pruneK(sum, list, node.left);
            pruneK(sum, list, node.right);
            return list;
    }

    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        ArrayList<E> inArr = new ArrayList<>();
        ArrayList<E> preArr = new ArrayList<>();
        inArr.addAll(Arrays.asList(inOrder));
        preArr.addAll(Arrays.asList(preOrder));

        root = buildTreeTraversals(inArr, preArr, new BinaryNode<E>(null));
    }

    /**
     * This method takes the preorder and inorder and makes a tree
     * Complexity O(n)
     * @param inOrder arraylist of inorder elements
     * @param preOrder arraylsit of preorder elements
     * @param node root of sub tree
     * @return root node
     */
    public BinaryNode<E> buildTreeTraversals(ArrayList<E> inOrder, ArrayList<E> preOrder, BinaryNode<E> node){
        if (preOrder.isEmpty()) return null;

        node = new BinaryNode<E>(preOrder.get(0), null, null, node);

        //reached the end
        if(preOrder.size() == 1) return node;

        //create left node
        if (preOrder.get(0) != inOrder.get(0)) {
            preOrder.remove(0);
            node.left = buildTreeTraversals(inOrder, preOrder, node);
        }
        inOrder.remove(0);
        //right node or go back?
        if(node.parent.element == inOrder.get(0)) {
            return node;
        }
        if(!preOrder.contains(inOrder.get(0))) return node;

        //right node
        preOrder.remove(0);
        node.right = buildTreeTraversals(inOrder, preOrder, node);
        return node;
    }

    /**
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(E a, E b) {
        BinaryNode<E> ancestor = null;

        ArrayList<BinaryNode<E>> pathA = lca(root, a, new ArrayList<>());
        ArrayList<BinaryNode<E>> pathB = lca(root, b, new ArrayList<>());

        if(!pathA.get(pathA.size()-1).element.equals(a)) return pathB.get(pathB.size()-1).toString();
        if(!pathB.get(pathB.size()-1).element.equals(b)) return pathA.get(pathA.size()-1).toString();

        for (int i = pathA.size()-1; i >= 0; i--) {
            for (int j = pathB.size()-1; j >= 0; j--) {
                if (pathA.get(i).element.equals(pathB.get(j).element)) return pathA.get(i).toString();
            }
        }
        return "no lca";
    }

    /**
     * Method finds the path for a given value in the tree
     * Complexity O(n)
     * @param node root of each sub tree
     * @param value given value
     * @param list arraylist containing path
     * @return path
     */
    public ArrayList<BinaryNode<E>> lca(BinaryNode<E> node, E value, ArrayList<BinaryNode<E>> list){
        if (node == null) {
            return list;
        }
        if (value.compareTo(node.element) == 0) {
            list.add(node);
            return list;
        }
        if (value.compareTo(node.element) < 0) {
            list.add(node);
            lca(node.left, value, list);
        }
        else  {
            list.add(node);
            lca(node.right, value, list);
        }
        return list;
    }

    /**
     * Balance the tree
     */
    public void balanceTree() {
        str = "";
        ArrayList<BinaryNode<E>> list = listOfTree(root, new ArrayList<>());
        ArrayList<E> elementList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            elementList.add(list.get(i).element);
        }
        root = balanceTree(elementList, 0, elementList.size()-1, new BinaryNode<E>(null));
    }

    /**
     * This method take the array of numbers and turns it into a balances bst
     * complexity O(n)
     * @param list elements for nodes
     * @param lower lower bound
     * @param upper upper bound
     * @param node used to keep track of the parent
     * @return returns the root node
     */
    public BinaryNode<E> balanceTree(ArrayList<E> list, int lower, int upper, BinaryNode<E> node){
        if (lower > upper) return null;

        //get middle
        int middle = (upper + lower)/2;
        if(node.element == null) node.parent = null;
        node = new BinaryNode<E>(list.get(middle), null, null, node);

        node.left = balanceTree(list, lower, middle - 1, node);

        node.right = balanceTree(list, middle +1, upper,node);

        return node;
    }

    /**
     * In a BST, keep only nodes between range
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        str = "";
        keepRange(a, b, root);
     }

    /**
     * This method removes any node that is not in the range
     * complexity O(nlogn)
     * @param a lower bound
     * @param b upper bound
     * @param node root of subtree
     */
     public void keepRange(E a, E b, BinaryNode<E> node){
        if(node == null) return;
        while(node.element.compareTo(a) < 0){
            remove(node.element, node);
            if(node.element == null) return;
        }
        while(node.element.compareTo(b) > 0){
            remove(node.element, node);
            if(node.element == null) {
                node.parent.right = null;
                return;
            }
        }
        keepRange(a,b,node.left);
        keepRange(a,b,node.right);
     }


    //PRIVATE

    /**
     * This method removes a node from a tree
     * complexity O(1)
     * @param value value were looking for
     */
    private void remove(E value, BinaryNode<E> node) {
        // Case for no left child
        if (node.left == null) {
            // Special case for root node
            if (node.parent == null) {
                root = node.right;
                root.parent = null;
            }
            else { // General case for no left child
                if (node.right == null) node.element = null;
                else {
                    node.element = node.right.element;
                    if (node.right != null) {
                        node.right = node.right.right;
                        if (node.right != null) {
                            node.right.parent = node.parent;
                        }
                    }
                    if (value.compareTo(node.parent.element) < 0) {
                        node.parent.left = node.right;
                    } else {
                        node.parent.right = node.right;
                    }
                }
            }
        }
        else { // Case for there IS a left child
            BinaryNode<E> parentOfRightMost = node;
            BinaryNode<E> rightMost = node.left;
            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right;
            }
            node.element = rightMost.element;
            if (parentOfRightMost.right == rightMost) {
                parentOfRightMost.right = rightMost.left;
                if(rightMost.left != null) {
                    parentOfRightMost.right.parent = parentOfRightMost;
                }
            }
            else {
                parentOfRightMost.left = rightMost.left;
                if(rightMost.left != null) {
                    parentOfRightMost.left.parent = parentOfRightMost;
                }
            }
        }
    }

    /**
     * This method looks through a tree and adds each element to an arraylist
     * Complexity O(n)
     * @param node root node of each sub tree
     */
    private ArrayList<BinaryNode<E>> listOfTree(BinaryNode<E> node, ArrayList<BinaryNode<E>> list) {
        if (node == null) return list;

        listOfTree(node.left, list);
        list.add(node);
        listOfTree(node.right, list);
        return list;
    }

     /**
     * Build a NON BST tree by preorder
     *
     * @param arr    nodes to be added
     * @param height maximum height of tree
     * @param parent parent of subtree to be created
     * @return new tree
     */
    private BinaryNode<E> buildTree(ArrayList<E> arr, int height, BinaryNode<E> parent) {
        if (arr.isEmpty()) return null;
        BinaryNode<E> curr = new BinaryNode<>(arr.remove(0), null, null, parent);
        if (height > 0) {
            curr.left = buildTree(arr, height - 1, curr);
            curr.right = buildTree(arr, height - 1, curr);
        }
        return curr;
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t, BinaryNode<E> parent) {
        if (t == null)
            return new BinaryNode<>(x, null, null, parent);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            t.left = bstInsert(x, t.left, t);
        } else {
            t.right = bstInsert(x, t.right, t);
        }

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     *          SIDE EFFECT: Sets local variable curr to be the node that is found
     * @return node containing the matched item.
     */
    private boolean bstContains(E x, BinaryNode<E> t) {
        curr = null;
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return bstContains(x, t.left);
        else if (compareResult > 0)
            return bstContains(x, t.right);
        else {
            curr = t;
            return true;    // Match
        }
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     * @param t the node that roots the subtree.
     */
    private String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }

    // Basic node stored in unbalanced binary trees
    private static class BinaryNode<AnyType> {
        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
        BinaryNode<AnyType> parent; //  Parent node

        // Constructors
        BinaryNode(AnyType theElement) {
            this(theElement, null, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt, BinaryNode<AnyType> pt) {
            element = theElement;
            left = lt;
            right = rt;
            parent = pt;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            if (parent == null) {
                sb.append("<>");
            } else {
                sb.append("<");
                sb.append(parent.element);
                sb.append(">");
            }

            return sb.toString();
        }

    }


    // Test program
    public static void main(String[] args) {
        long seed = 436543;
        Random generator = new Random(seed);  // Don't use a seed if you want the numbers to be different each time
        final String ENDLINE = "\n";

        int val = 60;
        final int SIZE = 8;

        Integer[] v1 = {25, 10, 60, 55, 58, 56, 14, 63, 8, 50, 6, 9};
        ArrayList<Integer> v2 = new ArrayList<Integer>();
        for (int i = 0; i < SIZE * 2; i++) {
            int t = generator.nextInt(200);
            v2.add(t);
        }
        v2.add(val);
        Integer[] v3 = {200, 15, 3, 65, 83, 70, 90};
        ArrayList<Integer> v4 = new ArrayList<Integer>(Arrays.asList(v3));
        Integer[] v = {21, 8, 5, 6, 7, 19, 10, 40, 43, 52, 12, 60};
        ArrayList<Integer> v5 = new ArrayList<Integer>(Arrays.asList(v));
        Integer[] inorder = {4, 2, 1, 7, 5, 8, 3, 6};
        Integer[] preorder = {1, 2, 4, 3, 5, 7, 8, 6};


        Tree<Integer> tree1 = new Tree<Integer>(v1, "Tree1:");
        Tree<Integer> tree2 = new Tree<Integer>(v2, "Tree2:");
        Tree<Integer> tree3 = new Tree<Integer>(v3, "Tree3:");
        Tree<Integer> treeA = new Tree<Integer>(v4, "TreeA:", 2);
        Tree<Integer> treeB = new Tree<Integer>(v5, "TreeB", 3);
        Tree<Integer> treeC = new Tree<Integer>("TreeC");

        System.out.println(tree1.toString());
        System.out.println(tree1.toString2());

        System.out.println(treeA.toString());

        treeA.flip();
        System.out.println("Now flipped\n" + treeA.toString());

        System.out.println(tree2.toString());
        tree2.contains(val);  //Sets the current node inside the tree6 class.
        int succCount = 5;  // how many successors do you want to see?
        System.out.println("In Tree2, starting at " + val + ENDLINE);
        for (int i = 0; i < succCount; i++) {
            System.out.println("The next successor is " + tree2.successor());
        }

        System.out.println(tree1.toString());
        for (int mylevel = 0; mylevel < SIZE; mylevel += 2) {
            System.out.println("Number nodes at level " + mylevel + " is " + tree1.nodesInLevel(mylevel));
        }

        System.out.println("All paths from tree1");
        tree1.printAllPaths();

        System.out.print("Tree1 byLevelZigZag: ");
        tree1.byLevelZigZag(5);
        System.out.print("Tree2 byLevelZigZag (3): ");
        tree2.byLevelZigZag(3);

        treeA.flip();
        System.out.println(treeA.toString());
        System.out.println("treeA Contains BST: " + treeA.countBST());

        System.out.println(treeB.toString());
        System.out.println("treeB Contains BST: " + treeB.countBST());

        treeB.pruneK(60);
        treeB.changeName("treeB after pruning 60");
        System.out.println(treeB.toString());
        treeA.pruneK(220);
        treeA.changeName("treeA after pruning 220");
        System.out.println(treeA.toString());

        treeC.buildTreeTraversals(inorder, preorder);
        treeC.changeName("Tree C built from inorder and preorder traversals");
        System.out.println(treeC.toString());

        System.out.println(tree1.toString());
        System.out.println("tree1 Least Common Ancestor of (56,61) " + tree1.lca(56, 61) + ENDLINE);

        System.out.println("tree1 Least Common Ancestor of (6,25) " + tree1.lca(6, 25) + ENDLINE);

        System.out.println(tree3.toString());
        tree3.balanceTree();
        tree3.changeName("tree3 after balancing");
        System.out.println(tree3.toString());

        System.out.println(tree1.toString());
        tree1.keepRange(10, 50);
        tree1.changeName("tree1 after keeping only nodes between 10 and 50");
        System.out.println(tree1.toString());
        tree3.keepRange(3, 85);
        tree3.changeName("tree3 after keeping only nodes between 3  and 85");
        System.out.println(tree3.toString());


    }

}

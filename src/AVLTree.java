/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {
    AVLNode root;


    /**
     * public boolean empty()
     *
     * Returns true if and only if the tree is empty.
     *
     */
    public boolean empty() {
        return this.root == null;
    }

    /**
     * public String search(int k)
     *
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
        AVLNode node = this.root;
        while (node != null){
            if (node.key == k) return node.info;
            else if (k < node.key){
                node = node.left;
            }
            else{
                node = node.right;
            }
        }
        return node.info;
    }

    /**
     * public int insert(int k, String i)
     *
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        AVLNode externalNodeRight = new AVLNode("External Node", -1, -1);
        AVLNode externalNodeLeft = new AVLNode("External Node", -1, -1);
        AVLNode node = new AVLNode(k, i, externalNodeRight, externalNodeLeft, null, 0);
        int counter = 0;
        if (empty()){
            this.root = node;
            return counter;
        }
        AVLNode parentNode = treePosition(k);
        if(parentNode.key == node.key) return -1;
        node.parent = parentNode;
        boolean condition = false;
        if(parentNode.key < node.key){
            parentNode.right = node;
            condition = true; // the node is a right child
        }
        else{
            parentNode.left = node;

        }
        int numCase;
        while ((numCase = whichCase(node)) != 4){
            if(numCase == 1){
                node.parent.rank += 1;
                counter += 1;
                node = node.parent;
            }
            else if (numCase == 2){
                counter +=1;
                if(condition){
                    rotationL(node);
                }
                else{
                    rotationR(node);
                }
                break;
            }
            else{
                counter += 2;
                if(condition){
                    rotationDL(node);
                }
                else{
                    rotationDR(node);
                }
                break;
            }

        }

        return counter;	// to be replaced by student code
    }

    public int whichCase(AVLNode node){
        if (node == null) return 4;
        int rightDiff = node.rank - node.right.rank;
        int leftDiff = node.rank - node.left.rank;
        int parentDiff;
        // we are at the root
        if(node.parent == null) {
            if((rightDiff == 0 && leftDiff == 1) || (rightDiff == 1 && leftDiff == 0)) { //change line!!!!!!!!
            node.rank += 1;
                return 4;
            } else{
                return 4;
            }
        }
        else {
            parentDiff = node.parent.rank - node.rank;
            int parentOtherDiff;
            boolean condition = false;
            if (node.key > node.parent.key) {
                parentOtherDiff = node.parent.rank - node.parent.left.rank;
                condition = true; // node is right child.
            } else {
                parentOtherDiff = node.parent.rank - node.parent.right.rank;
            }
            if (parentDiff == 0 && parentOtherDiff == 1) return 1;
            if (parentDiff == 0 && parentOtherDiff == 2) {
                if ((condition && rightDiff == 1) || (!condition && leftDiff == 1)) return 2;
                if ((condition && rightDiff == 2) || (!condition && leftDiff == 2)) return 3;
            }
        }
        return 4;
    }
    // node is the node that it and his parent are rotated
    public void rotationR(AVLNode node){
        AVLNode tmp = node.right;
        AVLNode tmp2 = node.parent;
        node.right = tmp2;
        node.parent.left = tmp;
        node.parent = tmp2.parent;
        if(tmp2.parent == null){
            this.root = node;
        }
        else{
            if(tmp2.parent.key > tmp2.key){
                tmp2.parent.left = node;
            }
            else {
                tmp2.parent.right = node;
            }
        }
        tmp2.parent = node;
        tmp.parent = tmp2;
        tmp2.rank -= 1;


    }



    public void rotationL(AVLNode node){
        AVLNode tmp = node.left;
        AVLNode tmp2 = node.parent;
        node.left = tmp2;
        node.parent.right = tmp;
        node.parent = tmp2.parent;
        if(tmp2.parent == null){
            this.root = node;
        }
        else{
            if(tmp2.parent.key > tmp2.key){
                tmp2.parent.left = node;
            }
            else {
                tmp2.parent.right = node;
            }
        }
        tmp2.parent = node;
        tmp.parent = tmp2;
        tmp2.rank -= 1;

    }
    // inserting to the function problem.Left
    public void rotationDR(AVLNode node){
        AVLNode tmp = node.right;
        AVLNode tmp2 = node.parent;
        node.right = tmp.left;
        tmp.left.parent = node;
        tmp.left = node;
        node.parent.left = tmp;
        node.parent = tmp;
        tmp.parent = tmp2;
        node.rank -= 1;
        node.right.rank += 1;
        rotationR(tmp);

    }
    public void rotationDL(AVLNode node){
        AVLNode tmp = node.left;
        AVLNode tmp2 = node.parent;
        node.left = tmp.right;
        tmp.right.parent = node;
        tmp.right = node;
        node.parent.right = tmp;
        node.parent = tmp;
        tmp.parent = tmp2;
        node.rank -= 1;
        node.left.rank += 1;
        rotationL(tmp);

    }
    public AVLNode treePosition(int k){
        AVLNode node = this.root;
        AVLNode node2 = node;
        while(node.key != -1){
            node2 = node;
            if(k == node.key){
                return node;
            }
            else if( k < node.key){
                node = node.left;
            }
            else{
                node = node.right;
            }
        }
        return node2;
    }
    /**
     * public int delete(int k)
     *
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k)
    {
        return 421;	// to be replaced by student code
    }

    /**
     * public String min()
     *
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min()
    {
        return "minDefaultString"; // to be replaced by student code
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max()
    {
        return "maxDefaultString"; // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     *
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray()
    {
        return new int[33]; // to be replaced by student code
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray()
    {
        return new String[55]; // to be replaced by student code
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     */
    public int size()
    {
        return 422; // to be replaced by student code
    }

    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot()
    {
        return null;
    }

    /**
     * public AVLTree[] split(int x)
     *
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     *
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x)
    {
        return null;
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     *
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t)
    {
        return -1;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); // Returns node's key (for virtual node return -1).
        public String getValue(); // Returns node's value [info], for virtual node returns null.
        public void setLeft(IAVLNode node); // Sets left child.
        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
        public void setRight(IAVLNode node); // Sets right child.
        public IAVLNode getRight(); // Returns right child, if there is no right child return null.
        public void setParent(IAVLNode node); // Sets parent.
        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
        public void setHeight(int height); // Sets the height of the node.
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
    }

    /**
     * public class AVLNode
     *
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     *
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public class AVLNode implements IAVLNode{
        String info;
        int key;
        AVLNode right;
        AVLNode left;
        AVLNode parent;
        int rank = 0;

        public AVLNode(int key, String info, AVLNode right, AVLNode left, AVLNode parent, int rank) {
            this.info = info;
            this.key = key;
            this.right = right;
            this.left = left;
            this.parent = parent;
            this.rank = rank;
        }

        public AVLNode(String info, int key, int rank) {
            this.info = info;
            this.key = key;
            this.rank = -1;
        }

        public AVLNode(int key, String info) {
            this.info = info;
            this.key = key;
        }

        public int getKey()
        {
            return 423; // to be replaced by student code
        }
        public String getValue()
        {
            return "getValueDefault"; // to be replaced by student code
        }
        public void setLeft(IAVLNode node)
        {
            return; // to be replaced by student code
        }
        public IAVLNode getLeft()
        {
            return null; // to be replaced by student code
        }
        public void setRight(IAVLNode node)
        {
            return; // to be replaced by student code
        }
        public IAVLNode getRight()
        {
            return null; // to be replaced by student code
        }
        public void setParent(IAVLNode node)
        {
            return; // to be replaced by student code
        }
        public IAVLNode getParent()
        {
            return null; // to be replaced by student code
        }
        public boolean isRealNode()
        {
            return true; // to be replaced by student code
        }
        public void setHeight(int height)
        {
            return; // to be replaced by student code
        }
        public int getHeight()
        {
            return 424; // to be replaced by student code
        }
    }

}
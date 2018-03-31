import java.util.ArrayList;
import java.util.List;

public class RedBlackTree {

    private RedBlackNode sentinel = new RedBlackNode();
    private RedBlackNode root = sentinel;

    public RedBlackTree() {
        root.setLeft(sentinel);
        root.setRight(sentinel);
        root.setParent(sentinel);
    }

    /**
     * Binary search to locate a node for a given jobID. Iteratively check in left or right subtrees based on jobId
     */
    public RedBlackNode search(int key) {

        if (root == sentinel) return null;

        RedBlackNode currentNode = root;

        while (!isSentinel(currentNode)) {

            if (key < currentNode.getJob().getJobID()) {
                currentNode = currentNode.getLeft();
            } else if (key > currentNode.getJob().getJobID()) {
                currentNode = currentNode.getRight();
            } else if (key == currentNode.getJob().getJobID()) {
                return currentNode;
            }
        }

        return null;
    }


    /**
     * Inserts the node in RedBlack Tree similar to insertion in a Binary Search Tree.
     * Fix any violations that could occur after insertion and balance the tree.
     *
     */
    public void insertJob(Job job) {

        RedBlackNode node = new RedBlackNode(job);
        RedBlackNode x = root;
        RedBlackNode y = sentinel;

        //search down the tree to locate parent of the node to be inserted
        while(!isSentinel(x)) {
            y = x;

            if (job.getJobID() < x.getJob().getJobID()) {
                x.nodesToLeft++;
                x = x.getLeft();
            } else {
                x.nodesToRight++;
                x = x.getRight();
            }
        }

        node.setParent(y);

        if (isSentinel(y)) {
            root = node;
        } else if (job.getJobID() < y.getJob().getJobID()){
            y.setLeft(node);
        } else {
            y.setRight(node);
        }


        /**
         * The color of a newly inserted node in a non-empty tree is always red.
         * This preserves the blacklength property of RedBlackTree
         */
        node.setLeft(sentinel);
        node.setRight(sentinel);
        node.setColor(Color.RED);


        balanceTreeAfterInsert(node);


        return;
    }

    /**
     * Fixes any violations that occur after the insertion of a node
     */
    private void balanceTreeAfterInsert(RedBlackNode node) {

        while (node.getParent().getColor() == Color.RED) {
            RedBlackNode uncle = sentinel;


            /**
             * Check if the parent is a left child of grandParent
             */
            if (node.getParent() == node.getParent().getParent().getLeft()) {
                uncle = node.getParent().getParent().getRight();

                /**
                 * If uncle exists and is a red node, we can fix balance the tree by recoloring.(no rotations needed)
                 * The violation is either resolved or moved up by two levels
                 */
                if (uncle != sentinel && uncle.getColor() == Color.RED) {
                   node = balanceWhenUncleIsRed(uncle, node);

                }
                /**
                 * uncle is black, node is a right child
                 * LR imbalance
                 * LR => RR + LL
                 * rotation => rotateLeft followed by rotateRight
                 */
                else if (node == node.getParent().getRight()) {
                    node = node.getParent();
                    rotateLeft(node);
                }

                /**
                 * uncle is black, node is a left child
                 * LL imbalance
                 * rotation => rotateRight
                 */
                else {
                    node.getParent().setColor(Color.BLACK);
                    node.getParent().getParent().setColor(Color.RED);
                    rotateRight(node.getParent().getParent());
                }

            }

            /**
             * Check if the parent is a right child of grandParent
             */
           else if (node.getParent() == node.getParent().getParent().getRight()) {
                uncle = node.getParent().getParent().getLeft();

                /**
                 * If uncle exists and is a red node, we can fix balance the tree by recoloring.(no rotations needed)
                 * The violation is either resolved or moved up by two levels
                 */
                if (uncle != sentinel && uncle.getColor() == Color.RED) {
                    node = balanceWhenUncleIsRed(uncle, node);

                }

                /**
                 * uncle is black, node is a left child
                 * RL imbalance
                 * RL =>  LL + RR
                 * rotation => rotateRight followed by rotateLeft
                 */
                else if (node == node.getParent().getLeft()) {
                    node = node.getParent();
                    rotateRight(node);

                }
                /**
                 * uncle is black, node is a right child
                 * RR imbalance
                 * rotation => rotateLeft
                 */

                else {
                    node.getParent().setColor(Color.BLACK);
                    node.getParent().getParent().setColor(Color.RED);
                    rotateLeft(node.getParent().getParent());
                }
            }
        }
        /**
         * root is always black in a RedBlacktree
         */
        root.setColor(Color.BLACK);
    }

    /**
     *          z
     *         / \T4                        y
     *        y            =>            /    \
     *       / \                        x      z
     *      x   T3                    / \     / \
     *     / \                      T1  T2   T3  T4
     *    T1  T2
     */
    private void rotateRight(RedBlackNode node) {

        setUpRotateRight(node);

        RedBlackNode x;
        x = node.getLeft();
        node.setLeft(x.getRight());

        if (!isSentinel(x.getRight())) {
            x.getRight().setParent(node);
        }
        x.setParent(node.getParent());

        if (isSentinel(node.getParent())) {
            root = x;
        }
        else if (node.getParent().getRight() == node) {
            node.getParent().setRight(x);
        }
        else {
            node.getParent().setLeft(x);
        }
        x.setRight(node);
        node.setParent(x);
    }

    /**
     * Helper function to setup rotateRight
     */
    private void setUpRotateRight(RedBlackNode node) {

        if (isSentinel(node.getRight()) && isSentinel(node.getLeft().getRight())){
            node.nodesToRight = 0;
            node.nodesToLeft = 0;
            node.getLeft().nodesToRight = 1;
        }

        else if (isSentinel(node.getRight()) && !isSentinel(node.getLeft().getRight())){
            node.nodesToRight = 0;
            node.nodesToLeft = 1 + node.getLeft().getRight().nodesToRight +
                    node.getLeft().getRight().nodesToLeft;
            node.getLeft().nodesToRight = 2 + node.getLeft().getRight().nodesToRight +
                    node.getLeft().getRight().nodesToLeft;
        }

        else if (!isSentinel(node.getRight()) && isSentinel(node.getLeft().getRight())){
            node.nodesToLeft = 0;
            node.getLeft().nodesToRight = 2 + node.getRight().nodesToRight +node.getRight().nodesToLeft;

        }

        else{
            node.nodesToLeft = 1 + node.getLeft().getRight().nodesToRight +
                    node.getLeft().getRight().nodesToLeft;
            node.getLeft().nodesToRight = 3 + node.getRight().nodesToRight +
                    node.getRight().nodesToLeft +
                    node.getLeft().getRight().nodesToRight + node.getLeft().getRight().nodesToLeft;
        }

    }

    /**
     *       z                                   y
     *     /   \                              /    \
     *    T4    y           =>              z         x
     *        /   \                       /  \      /  \
     *      T3     x                    T4   T3   T1   T2
     *           /  \
     *        T1     T2
     */

    private void rotateLeft(RedBlackNode node) {

        setUpRotateLeft(node);

        RedBlackNode y ;
        y = node.getRight();
        node.setRight(y.getLeft());

        if (!isSentinel(y.getLeft())) {
            y.getLeft().setParent(node);
        }
        y.setParent(node.getParent());

        if (isSentinel(node.getParent())) {
            root = y;
        }

        else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(y);
        }

        else {
            node.getParent().setRight(y);
        }

        y.setLeft(node);
        node.setParent(y);

    }

    /**
     * Helper function to setup rotateLeft
     */
    private void setUpRotateLeft(RedBlackNode node) {

        if (isSentinel(node.getLeft()) && isSentinel(node.getRight().getLeft())){
            node.nodesToLeft = 0;
            node.nodesToRight = 0;
            node.getRight().nodesToLeft = 1;
        }


        else if (isSentinel(node.getLeft()) && !isSentinel(node.getRight().getLeft())){
            node.nodesToLeft = 0;
            node.nodesToRight = 1 + node.getRight().getLeft().nodesToLeft +
                    node.getRight().getLeft().nodesToRight;
            node.getRight().nodesToLeft = 2 + node.getRight().getLeft().nodesToLeft +
                    node.getRight().getLeft().nodesToRight;
        }

        else if (!isSentinel(node.getLeft()) && isSentinel(node.getRight().getLeft())){
            node.nodesToRight = 0;
            node.getRight().nodesToLeft = 2 + node.getLeft().nodesToLeft + node.getLeft().nodesToRight;

        }

        else{
            node.nodesToRight = 1 + node.getRight().getLeft().nodesToLeft +
                    node.getRight().getLeft().nodesToRight;
            node.getRight().nodesToLeft = 3 + node.getLeft().nodesToLeft + node.getLeft().nodesToRight +
                    node.getRight().getLeft().nodesToLeft + node.getRight().getLeft().nodesToRight;
        }

    }

    /**
     * Balances tree if any violations occur after insertion when uncle of the inserted node is red
     */
    private RedBlackNode balanceWhenUncleIsRed(RedBlackNode uncleNode, RedBlackNode violationNode) {
        violationNode.getParent().setColor(Color.BLACK);
        uncleNode.setColor(Color.BLACK);
        violationNode.getParent().getParent().setColor(Color.RED);
        violationNode = uncleNode.getParent().getParent();
        return violationNode;

    }

    /**
     * Deletes job from the redblack tree
     */
    public void removeJob(int jobId) {

        RedBlackNode node = search(jobId);

        RedBlackNode x = sentinel;
        RedBlackNode y = sentinel;

        if (isSentinel(node.getLeft()) || isSentinel(node.getRight())) {
            y = node;
        }
        else {
            y = getSuccessor(node);
        }

        if (!isSentinel(y.getLeft())) {
            x = y.getLeft();
        }
        else {
            x = y.getRight();
        }

        x.setParent(y.getParent());

        if (isSentinel(y.getParent())) {
            root = x;
        }
        else if (!isSentinel(y.getParent().getLeft()) && (y.getParent().getLeft() == y)){
            y.getParent().setLeft(x);

        }
        else if ((!isSentinel(y.getParent().getRight())) && y.getParent().getRight() == y) {
            y.getParent().setRight(x);
        }

        if (y != node) node.getJob().jobID = y.getJob().jobID;

        updateNodeData(x,y);

        if (y.getColor() == Color.BLACK) {
            balanceAfterRemove(x);
        }
    }

    /**
     * Balances tree if any violations occur after deletion of a node
     */
    private void balanceAfterRemove(RedBlackNode node) {

        RedBlackNode sibling;

        while (node != root && node.getColor() == Color.BLACK) {

            /**
             * Checks if node is a left child of the parent
             */
            if (node == node.getParent().getLeft()) {

                /**
                 * Read parent's right child into sibling
                 */
                sibling = node.getParent().getRight();

                /**
                 * case 1 : sibling is red
                 */
                if (sibling.getColor() == Color.RED) {
                    sibling.setColor(Color.BLACK);
                    node.getParent().setColor(Color.RED);
                    rotateLeft(node.getParent());
                    sibling = node.getParent().getRight();
                }

                /**
                 * case 2: sibling has two black children
                 */
                if (sibling.getLeft().getColor() == Color.BLACK && sibling.getRight().getColor() == Color.BLACK) {
                    sibling.setColor((Color.RED));
                    node = node.getParent();
                }

                else {

                    /**
                     * case 3: sibling has one black child on the right
                     */
                    if(sibling.getRight().getColor() == Color.BLACK) {
                        sibling.getLeft().setColor(Color.BLACK);
                        sibling.setColor(Color.RED);
                        rotateRight(sibling);
                        sibling = node.getParent().getRight();
                    }

                    /**
                     * case 4: sibling is black and has a red child on the right
                     */
                    sibling.setColor(node.getParent().getColor());
                    node.getParent().setColor(Color.BLACK);
                    sibling.getRight().setColor(Color.BLACK);
                    rotateLeft(node.getParent());
                    node = root;
                }
            }

            /**
             * The node is a right child of it's parent
             */
            else {
                /**
                 * Read left child of node's parent into sibling
                 */
                sibling = node.getParent().getLeft();

                /**
                 *  case 1 : sibling is red
                 */
                if (sibling.getColor() == Color.RED) {
                    sibling.setColor(Color.BLACK);
                    node.getParent().setColor(Color.RED);
                    rotateRight(node.getParent());
                    sibling = node.getParent().getLeft();
                }

                /**
                 *  case 2: sibling has two black children
                 */
                if (sibling.getRight().getColor() == Color.BLACK && sibling.getLeft().getColor() == Color.BLACK) {
                    sibling.setColor(Color.RED);
                    node = node.getParent();
                }

                else {

                    /**
                     *  case 3: sibling has one black child on the left
                     */
                    if (sibling.getLeft().getColor() == Color.BLACK) {
                        sibling.getRight().setColor(Color.BLACK);
                        sibling.setColor(Color.RED);
                        rotateLeft(sibling);
                        sibling = node.getParent().getLeft();
                    }

                    /**
                     *  case 4: sibling is black and has a red child on the left
                     */
                    sibling.setColor(node.getParent().getColor());
                    node.getParent().setColor(Color.BLACK);
                    sibling.getLeft().setColor(Color.BLACK);
                    rotateRight(node.getParent());
                    node = root;
                }
            }
        }

        node.setColor((Color.BLACK));
    }


    private void updateNodeData(RedBlackNode x, RedBlackNode node) {

        RedBlackNode current = sentinel;
        RedBlackNode track = sentinel;

        if (isSentinel(x)){
            current = node.getParent();
            track = node;
        } else {
            current = x.getParent();
            track =x;
        }

        while (!isSentinel(current)) {
            if (node.getJob().getJobID() != current.getJob().getJobID()) {
                if (node.getJob().getJobID() > current.getJob().getJobID()) {
                    current.nodesToRight--;
                }

                if (node.getJob().getJobID() < current.getJob().getJobID()) {
                    current.nodesToLeft--;
                }

            } else {
                if (isSentinel(current.getLeft())) {
                    current.nodesToLeft--;

                } else if (isSentinel(current.getRight())) {
                    current.nodesToRight--;

                } else if (track == current.getRight()) {
                    current.nodesToRight--;

                } else if (track == current.getLeft()) {
                    current.nodesToLeft--;
                }
            }

            track = current;
            current = current.getParent();
        }

    }

    /**
     * Returns the node with the lowest jobID that is greater than the given jobID of the node.
     */
    public RedBlackNode getSuccessor(RedBlackNode node) {

        /**
         * If node has a left child, traverse along left nodes of the right child
         * to find the minimum in the sub-tree
         */
        if (!isSentinel(node.getRight())) {
            return getSmallestInTree(node.getRight());
        }

        RedBlackNode temp = node.getParent();

        while (!isSentinel(temp) && node == temp.getRight()) {
            node = temp;
            temp = temp.getParent();
        }
        return temp;
    }

    /**
     * Returns the node with the greatest jobID that is less than the given jobID of the node.
     */
    private RedBlackNode getPredecessor(RedBlackNode node) {

        if (!isSentinel(node.getLeft())) {
            return getLargestInTree(node.getLeft());
        }

        RedBlackNode temp = node.getParent();

        while (!isSentinel(temp) && node == temp.getLeft()) {
            node = temp;
            temp = temp.getParent();
        }
        return temp;
    }

    /**
     * Helper function to recursively search for the smallest jobId that is greater than given jobId
     */
    public RedBlackNode getNextJobUtil(RedBlackNode subroot, RedBlackNode parent, int jobid) {
        if (isSentinel(subroot)) {
            if (parent.getJob().jobID > jobid) return parent;
            return getSuccessor(parent);
        }
        if(subroot.getJob().jobID == jobid) return getSuccessor(subroot);
        if (subroot.getJob().jobID > jobid) return getNextJobUtil(subroot.getLeft(), subroot, jobid);
        if (subroot.getJob().jobID < jobid) return getNextJobUtil(subroot.getRight(), subroot, jobid);
        return null;
    }

    /**
     * Helper function to recursively search for the greatest jobId that is less than given jobId
     */
    public RedBlackNode getPreviousJobUtil(RedBlackNode subroot, RedBlackNode parent, int jobid) {
        if (isSentinel(subroot)) {
            if (parent.getJob().jobID < jobid) return parent;
            return getPredecessor(parent);
        }
        if(subroot.getJob().jobID == jobid) return getPredecessor(subroot);
        if (subroot.getJob().jobID > jobid) return getPreviousJobUtil(subroot.getLeft(), subroot, jobid);
        if (subroot.getJob().jobID < jobid) return getPreviousJobUtil(subroot.getRight(), subroot, jobid);
        return null;
    }

    public RedBlackNode getNextJob(int jobId) {
        return (root != null) ? getNextJobUtil(root, null, jobId) : null;
    }

    public RedBlackNode getPreviousJob(int jobId) {
        return (root != null) ? getPreviousJobUtil(root, null, jobId) : null;
    }


    /**
     * Returns the node with the largest jobID from the sub-tree rooted at node
     */
    private RedBlackNode getLargestInTree(RedBlackNode node) {
        if (node == null) return null;
        while (!isSentinel(node.getRight())) {
            node = node.getRight();
        }
        return node;
    }

    /**
     * Returns the node with the smallest jobID from the sub-tree rooted at node
     */
    private RedBlackNode getSmallestInTree(RedBlackNode node) {
        if(node == null) return null;
        while (!isSentinel(node.getLeft())) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Helper function to check if a node is sentinel or not
     */
    private boolean isSentinel(RedBlackNode node) {
        return node == sentinel;
    }

    /**
     * Returns the size of the RedBlack Tree
     */
    public int size() {
        return root.nodesToLeft + root.nodesToRight +1;
    }

    /**
     * Returns an inclusive list of jobs with jobId's between jobId1 and jobId2
     */
    public List<RedBlackNode> getJobsInRange(int jobId1, int jobId2) {
        List<RedBlackNode> res = new ArrayList<>();
        getJobs(this.root, jobId1, jobId2, res);
        return res;
    }

    /**
     * Helper function to recursively search through the tree and return jobs in given range.
     */
    private void getJobs(RedBlackNode r, int start, int end, List<RedBlackNode> res) {
        if (isSentinel(r)) return;
        int jobId = r.getJob().getJobID();
        if (end < jobId) getJobs(r.left, start, end, res);
        else if (start > jobId) getJobs(r.right, start, end, res);
        else {
            getJobs(r.left, start, jobId, res);
            res.add(r);
            getJobs(r.right, jobId, end, res);
        }
    }


}




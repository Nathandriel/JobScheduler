public class RedBlackNode {

    private Color color;
    private Job job;

    RedBlackNode left, right, parent;
    int nodesToLeft = 0;
    int nodesToRight = 0;

    public RedBlackNode() {
        this.color = Color.BLACK;
        this.nodesToLeft = 0;
        this.nodesToRight = 0;
        this.parent = null;
        this.left = null;
        this.right = null;
    }

    public RedBlackNode(Job job) {
       this();
       this.job = job;

    }


    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RedBlackNode getLeft() {
        return left;
    }

    public void setLeft(RedBlackNode left) {
        this.left = left;
    }

    public RedBlackNode getRight() {
        return right;
    }

    public void setRight(RedBlackNode right) {
        this.right = right;
    }

    public RedBlackNode getParent() {
        return parent;
    }

    public void setParent(RedBlackNode parent) {
        this.parent = parent;
    }

}

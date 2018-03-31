import java.util.ArrayList;

public class MinHeap {

    private static ArrayList<Job> jobs;
    private static final int HEAD = 1;

    public MinHeap() {
        jobs = new ArrayList<Job>();
        jobs.add(new Job(1,1,1));
    }

    /**
     * returns the size of the heap
     */
    public int getSize() {
        return jobs.size() - 1;
    }

    /**
     * returns true if heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    /**
     * inserts a job in the heap
     */
    public void insertJob(Job job) {
        jobs.add(job);
        int index = getSize();

        while ((index != 1) && (jobs.get(index).getExecutedTime() < jobs.get(getParent(index)).getExecutedTime())){
            swapElements(index, getParent(index));
            index = getParent(index);
        }
    }

    /**
     * removes a job from the heap
     */
    public Job removeJob() {
        Job leastExecutedTimeJob = null;

        if (jobs.size() > 1) {
            leastExecutedTimeJob = jobs.get(HEAD);
            jobs.set(HEAD, jobs.get(getSize()));
            jobs.remove(getSize());
            minHeapify(HEAD);
        }
        return leastExecutedTimeJob;

    }

    /**
     * returns leftchild of the node
     */
    private int getLeftChild(int i) {
        return 2*i;
    }

    /**
     * returns rightchild of the node
     */
    private int getRightChild(int i) {
        return 2*i + 1 ;
    }

    /**
     * returns parent of the node
     */
    private int getParent(int i) {
        return i/2;
    }

    /**
     * swaps the position of elements
     */
    private void swapElements(int a, int b) {
        Job temp = jobs.get(a);
        jobs.set(a, jobs.get(b));
        jobs.set(b, temp);
    }

    /**
     * returns true if a node is leaf, false otherwise
     */
    private boolean isLeaf(int i) {

        return ((i >= (jobs.size() / 2)) && (i <= jobs.size()));
    }

    /**
     * Performs heapify to satisy the heap properties
     */
    private void minHeapify(int i) {

        if (!isLeaf(i)) {
            if ((jobs.get(i).getExecutedTime() > jobs.get(getLeftChild(i)).getExecutedTime()) || (jobs.get(i).getExecutedTime() > jobs.get(getRightChild(i)).getExecutedTime())) {
                if (jobs.get(i).getExecutedTime() > jobs.get(getLeftChild(i)).getExecutedTime()) {
                    swapElements(i, getLeftChild(i));
                    minHeapify(getLeftChild(i));
                } else {
                    swapElements(i, getRightChild(i));
                    minHeapify(getRightChild(i));
                }
            }
        }
    }


}
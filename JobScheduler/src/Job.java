public class Job {
    private final int globalTime;

    protected int jobID;
    protected int executedTime;
    protected final int totalTime;


    public int getJobID() {
        return jobID;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    public void incrementExecutedTime(int executedTime) {
        this.executedTime += executedTime;
    }

    public int getTotalTime() {
        return totalTime;
    }


    public Job (int globalTime, int jobID, int totalTime) {
        this.globalTime = globalTime;
        this.jobID = jobID;
        this.totalTime = totalTime;

        this.executedTime = 0;
    }

    @Override
    public String toString() {
        return "(" + jobID +  ", " + executedTime + ", " + totalTime + ")";
        
    }
}

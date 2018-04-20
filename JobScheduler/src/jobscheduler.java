import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class jobscheduler {
    private int globalTime;
    private StringBuilder jobInfo;
    private MinHeap jobQueue;
    private RedBlackTree jobTree;

    private void executeJob(Job currJob) {

        int jobExecutedTime = Math.min(5, currJob.getTotalTime() - currJob.getExecutedTime());

        currJob.incrementExecutedTime(jobExecutedTime);
        globalTime += jobExecutedTime;

        if (currJob.getExecutedTime() == currJob.getTotalTime()) {
            jobTree.removeJob(currJob.getJobID());
        } else {
            jobQueue.insertJob(currJob);

        }
    }

    public static void main (String[] args) throws IOException, IllegalArgumentException {
        new jobscheduler().run(args);
    }
    public void run(String[] args) throws IOException, IllegalArgumentException {

        if (args.length == 0) throw new IllegalArgumentException ("Input file needed");

        jobQueue = new MinHeap();
        jobTree = new RedBlackTree();


        globalTime = 0;
        jobInfo = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));

        Pattern pattern = Pattern.compile("([0-9]+)(:(\\s{1}))(([a-zA-Z]+)(\\((([0-9]+)(,)?([0-9]+)?)\\)))");
        Matcher matcher;

        String s;


        while ((s = br.readLine())!= null) {

            //System.out.println(s);
            matcher = pattern.matcher(s);
            matcher.find();

            int jobArrivalTime = Integer.parseInt(matcher.group(1));

            while (globalTime < jobArrivalTime) {
                if (jobQueue.isEmpty()) {
                        globalTime = jobArrivalTime;
                } else {
                    executeJob(jobQueue.removeJob());
                }
            }

            switch (matcher.group (5)) {
                case "Insert" : {
                    Job job = new Job (Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(10)));
                    jobQueue.insertJob(job);
                    jobTree.insertJob(job);
                    break;
                }

                case "NextJob" : {
                    RedBlackNode nj = jobTree.getNextJob(Integer.parseInt(matcher.group(8)));
                    if (nj != null && nj.getJob() != null)
                        jobInfo.append(nj.getJob().toString() + "\n");
                    else
                        jobInfo.append("(0,0,0)" + "\n");
                    break;
                }

                case "PreviousJob" : {
                   RedBlackNode pj = jobTree.getPreviousJob(Integer.parseInt(matcher.group(8)));
                   if (pj != null && pj.getJob() != null)
                       jobInfo.append(pj.getJob().toString() + "\n");
                   else
                       jobInfo.append("(0,0,0)" + "\n");
                    break;
                }

                case "PrintJob" : {
                    int p1 = Integer.parseInt(matcher.group(8));
                    int p2 = p1;
                    if (matcher.group(10) != null) {
                        p2 = Integer.parseInt(matcher.group(10));
                    }
                    List<RedBlackNode> res = jobTree.getJobsInRange(p1, p2);
                    for (int i = 0; i < res.size(); i++) {
                        if (i > 0) jobInfo.append(",");
                        RedBlackNode node = res.get(i);
                        jobInfo.append(node.getJob().toString());
                    }
		    if (res.isEmpty()) jobInfo.append("(0,0,0)");
                    jobInfo.append("\n");
                    break;
                }
            }

            try {
                Job job = jobQueue.removeJob();
                if (job != null) executeJob(job);
            } catch (NoSuchElementException e) {
                System.out.println("No jobs in queue");
            }
        }
        br.close();


        BufferedWriter bw =  new BufferedWriter (new FileWriter (new File ("output_file.txt")));

        bw.write (jobInfo.toString());
        bw.close();
        System.exit(0);
    }
}

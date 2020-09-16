import java.util.ArrayList;

public class Scheduler {
    int[] results = {0,0};

    /**
     * For each time the best task is taken and preformed and then updated and added back to the
     * list if it is not finished
     * @param header states priority and file used
     * @param tasks all the tasks in the file
     */
    public void makeSchedule(String header, ArrayList<Task> tasks){
        System.out.println(header);

        LeftistHeap<Task> heap = new LeftistHeap<>(tasks);
        int time = 1;
        int finishedTasks = 0;

        while(finishedTasks != tasks.size()){
            Task task = heap.deleteMin();
            task.update();
            if (task.duration == 0){
                print(time, task, true);
                finishedTasks++;
            }
            else {
                heap.insert(task);
                print(time, task, false);
            }
            time++;
        }
        System.out.println("Tasks late: " + results[0] + " Total time late: " + results[1] + "\n");
        results[0] = 0;
        results[1] = 0;
    }

    /**
     * Prints what happened at each time
     * @param time time of event
     * @param task the task of the hour
     * @param finished is the task finished
     */
    private void print(int time, Task task, boolean finished){
        int late = time - task.deadline;
        if (finished){
            if (late > 0) {
                results[0] += 1;
                results[1] += late;
                System.out.println("Time: " + time + " Task: " + task.ID + " ** Late " + late);
            }
            else System.out.println("Time: " + time + " Task: " + task.ID + " **");
        }
        else System.out.println("Time: " + time + " Task: " + task.ID);
    }
}

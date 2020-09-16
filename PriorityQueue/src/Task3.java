public class Task3 extends Task{
    public Task3(int ID, int start, int deadline, int duration) {
        super(ID,start,deadline,duration);
    }
    // Prioirity is the remaining duration, if equal then by start time, then by priority
    @Override
    public int compareTo(Task t2) {
        int compare = duration - t2.duration;
        if (compare == 0){
            int compare2 = start - t2.start;
            if (compare2 == 0){
                return deadline - t2.deadline;
            }
            else return compare2;
        }
         else return compare;
    }

}

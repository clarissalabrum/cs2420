import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestSched {

    /**
     * Read numbers from a file and places them into task objects
     * @param fileName name of file
     * @return ArrayList of tasks
     * @throws FileNotFoundException file could not exist
     */
    private static ArrayList<Task> readTasks(String fileName, int priority) throws FileNotFoundException {
        fileName = "data/" + fileName;
        String line = "";
        ArrayList<Task> tasks = new ArrayList<>();
        int task = 1;

        // pass the path to the file as a parameter
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            ArrayList<Integer> numbers = new ArrayList<>();
            if (!line.equals("")) {
                String[] subLine = line.split("\\s+");
                for (String num : subLine) {
                    if (!num.equals("")){
                        int number = Integer.parseInt(num);
                        numbers.add(number);
                    }
                }
                if (priority == 1){
                    tasks.add(new Task1(task++, numbers.get(0), numbers.get(1), numbers.get(2)));
                }
                if (priority == 2){
                    tasks.add(new Task2(task++, numbers.get(0), numbers.get(1), numbers.get(2)));
                }
                if (priority == 3){
                    tasks.add(new Task3(task++, numbers.get(0), numbers.get(1), numbers.get(2)));
                }
            }
        }
        return tasks;
    }

    public static void main(String args[]) throws FileNotFoundException {
        Scheduler s = new Scheduler();
        String [] files = {"taskset1.txt","taskset2.txt","taskset3.txt","taskset4.txt","taskset5.txt" };
        for (String f : files) {
            ArrayList<Task> t1 = readTasks(f, 1);    // elements are Task1
            ArrayList<Task> t2 = readTasks(f, 2);    // elements are Task2
            ArrayList<Task> t3 = readTasks(f, 3);    // elements are Task3

            s.makeSchedule("Deadline Priority " + f, t1);
            s.makeSchedule("Startime Priority " + f, t2);
            s.makeSchedule("Wild and Crazy priority " + f, t3);
       }
    }
}

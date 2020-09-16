import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;

public class LadderGame {
    static int MaxWordSize = 15;
    ArrayList<String>[] allList;  // Array of ArrayLists of words of each length.
    Random random;


    /*Splits up the dictionary file into list of words of the same size*/
    public LadderGame(String file) {
        random = new Random();
        allList = new ArrayList[MaxWordSize];
        for (int i = 0; i < MaxWordSize; i++)
            allList[i] = new ArrayList<String>();

        try {
            Scanner reader = new Scanner(new File(file));
            while (reader.hasNext()) {
                String word = reader.next();
                if (word.length() < MaxWordSize) allList[word.length()].add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Need a list word method to test that the dictionary file was split right*/
    public void listWords(int words, int size) {
        for (int i = 0; i < words; i++) {
            System.out.println((i + 1) + " " + allList[size].get(i));
        }
    }

    /*Method that sorts through words finding a word ladder*/
    public void play(String a, String b) {
        //checks to see if start and end are the same size
        if (a.length() != b.length()) {
            System.out.println("Words are not the same length");
            return;
        }

        //If words are larger than bounds it quits
        if (a.length() >= MaxWordSize) {
            System.out.println("Words not found in Dictionary");
            return;
        }

        //creates list of words to work from
        ArrayList list = new ArrayList();
        ArrayList<String> l = allList[a.length()];
        list = (ArrayList) l.clone();

        //Prints out the test words
        System.out.println("\nSeeking a solution from (" + a + " -> " + b + ") Size of List " + list.size());
        findLadder(a,b,list);
    }

    /*Creates random words of a specified length and runs the play(a,b) method*/
    public void play(int len) {
        if (len >= MaxWordSize) return;
        ArrayList<String> list = allList[len];
        String a = list.get(random.nextInt(list.size()));
        String b = list.get(random.nextInt(list.size()));
        play(a, b);

    }

    /*Takes the start and end words and what list of word they use to find a word letter from the start to the end*/
    void findLadder(String a, String b, ArrayList<String> list) {

        //Creating Linked list for Partial Solutions
        PartialSolution head = new PartialSolution<>(null);
        PartialSolution tail = new PartialSolution<>(null);
        PartialSolution sln = new PartialSolution<>(a);
        list.remove(a);
        tail.next = sln;
        head.next = sln;

        int enqueue = 1;
        boolean done = false;

        //Look for word ladders
        while (!done) {
            int count = 0;

            //test every word with current word in queue
            for (int i = 0; i < list.size(); i++) {

                //set test values
                String test = (String) head.next.value;
                String word = (String) list.get(i);

                //test individual letters and see if they match
                int match = 0;
                for (int j = 0; j < a.length(); j++) {
                    if (word.charAt(j) == test.charAt(j + test.length() - a.length())) {
                        match++;
                    }
                }

                //if all but one letter matches it is a possible solution
                if (match == (a.length() - 1)) {
                    count++;

                    //add to PartialSolution
                    String Solution = test + " " + word;
                    PartialSolution newsln = new PartialSolution<>(Solution); //made a sln with possible sol
                    PartialSolution temp = new PartialSolution<>(null); //made a sln with possible sol

                    //add new solution to linked list
                    sln.next = newsln;
                    sln = newsln;
                    temp = tail.next;
                    temp.next = newsln;
                    tail.next = newsln;

                    //count how many enqueue
                    enqueue++;

                    //Prints all partial solutions
//                    WordInfo results = new WordInfo(a, moves((String) tail.next.value), (String) tail.next.value);
//                    System.out.println(results.toString());

                    //see if test word equals end value
                    if (word.equals(b)) {
                        //count how many moves
                        System.out.println(a + " -> " + b + " " + moves((String) tail.next.value) + " Moves [" + tail.next.value + "] Total enqueues " + enqueue);
                        done = true;
                        break;
                    }

                    //remove word from list
                    list.remove(word);
                    i -= 1;
                }
            }
            //check conditions that qualify for no ladder existing
            PartialSolution temp1 = new PartialSolution<>(null);
            PartialSolution temp2 = new PartialSolution<>(null);
            temp1 = head.next;
            temp2 = tail.next;
            String Head = (String) temp1.value;
            String Tail = (String) temp2.value;
            if (Head.equals(Tail) || enqueue == 1 && count == 0) {
                System.out.println("No ladder was found");
                return;
            }

            //dequeue current test value
            head.next = (head.next).next;
        }
    }

    int moves(String phrase){
        int moves = 0;
        for (int j = 0; j < phrase.length(); j++) {
            if (phrase.charAt(j) == (' ')){
                moves++;
            }
        }
        return moves;
    }
}
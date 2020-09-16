import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Hex {
    private static int RIGHT=122;
    private static int LEFT = 123;
    private static int TOP=124;
    private static int BOTTOM=125;

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("moves.txt");
        play("moves.txt");

        System.out.println("moves2.txt");
        play("moves2.txt");
    }

    public static void play(String file) throws FileNotFoundException {
        int player;
        ArrayList<Integer> moves;
        DisjointSet set = new DisjointSet(127);
        int[] map = new int[122];

        moves = ReadFromFile(file);
        for (int i = 0; i < moves.size(); i++) {
            if (map[moves.get(i)] != 0){
                System.out.println("Move " + moves.get(i) + " has already been made.");
                moves.remove(i);
            }

            if(i % 2 == 0) player = 1;
            else player = 2;


            map[moves.get(i)] = player;

            int neighborLocation;
            int place = set.find(moves.get(i));

            //check the moves neighbors and see if they have been played by the same player
            for (int neighbor : getNeighbors(moves.get(i), player)) {
                if(neighbor > 121) set.union(place, neighbor);
                else if (neighbor > 0) {
                    neighborLocation = set.find(neighbor);
                    if (neighborLocation > 0){
                        if (map[neighbor] == player) set.union(neighbor, moves.get(i));
                    }
                }
            }

            //check to see if the set has gone from start to finish
            int union = set.find(moves.get(i));
            if (player == 1){
                if (set.find(LEFT) == set.find((RIGHT)) && union == set.find(LEFT)){
                    System.out.println("----------> Blue has won after " + (i+1) + " moves!");
                    break;
                }
            }
            int top = set.find(TOP);
            int bot = set.find(BOTTOM);
            if (player == 2){
                if (top == bot && union == top) {
                    System.out.println("----------> Red has won after " + (i+1) + " moves!");
                    break;
                }
            }
        }

        for (int i = 1; i < map.length; i++) {
            if (map[i] == 0) System.out.print("0 ");
            if (map[i] == 2) System.out.print(ANSI_RED + "R " + ANSI_RESET);
            if (map[i] == 1) System.out.print(ANSI_BLUE + "B " + ANSI_RESET);
            if (i%11 == 0){
                int spaces = i / 11;
                String gap = "";
                for (int j = 0; j < spaces; j++) {
                    gap += " ";
                }
                System.out.println();
                System.out.print(gap);
            }
        }
        System.out.println();
    }

    private static int[] getNeighbors(int item, int player){
        int one, two, three, four, five, six;
        int[] list = new int[6];
        int calc = item % 11;

        one = item - 11;
        if(one <= 0 && player == 2) list[0] = TOP;
        else if (one <= 0 && player == 1) list[0] = -1;
        else list[0] = one;

        two = item - 10;
        if(two <= 0 && player == 2) list[1] = TOP;
        else if (two <= 0 && player == 1) list[1] = -1;
        else list[1] = two;

        three = item - 1;
        if(calc == 1 && player == 1) list[2] = LEFT;
        else if (calc == 1 && player == 2) list[2] = -1;
        else list[2] = three;

        four = item + 1;
        if(calc == 0 && player == 1) list[3] = RIGHT;
        else if (calc == 0 && player == 2) list[3] = -1;
        else list[3] = four;

        five = item + 10;
        if(five > 121 && player == 2) list[4] = BOTTOM;
        else if (five > 121 && player == 1) list[4] = -1;
        else list[4] = five;

        six = item + 11;
        if(six > 121 && player == 2) list[5] = BOTTOM;
        else if (six > 121 && player == 1) list[5] = -1;
        else list[5] = six;

        return list;
    }

    private static ArrayList<Integer> ReadFromFile(String fileName) throws FileNotFoundException {
        fileName = "data/" + fileName;
        String line = "";
        ArrayList<Integer> moves = new ArrayList<>();

        // pass the path to the file as a parameter
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            moves.add(sc.nextInt());
        }
        return moves;
    }

}

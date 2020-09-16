public class TestLadder {
    public static void main(String[] args) {

        int RANDOMCT = 7;
        LadderGame g = new LadderGame("dictionary.txt");
        //g.listWords(10, 6);  //Lists the first ten words in the dictionary of length 6 as a test.
        g.play("oops", "tots");
        g.play("ride", "ands");
        g.play("happily", "angrily");
        g.play("slow", "fast");
        g.play("stone", "money");
        g.play("biff", "axal");
        for (int i = 3; i < RANDOMCT; i++)
            g.play(i);
    }
}

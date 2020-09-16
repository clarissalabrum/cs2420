public class WordInfo {
    public String word;
    public int moves;
    public String history;

    public WordInfo(String word, int moves, String history){
        this.word = word;
        this.moves = moves;
        this.history = history;
    }
    public String toString(){
        return "Word " + word + " Moves " + moves  + " History [" + history + "]";
    }
}

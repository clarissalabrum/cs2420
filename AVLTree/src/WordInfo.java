public class WordInfo implements Comparable<WordInfo>{
    public String word;
    public int moves;
    public String history;
    public int priority;

    public WordInfo(String word, int moves, String history, String b){
        this.word = word;
        this.moves = moves;
        this.history = history;
        this.priority = priority(b);
    }
    public String toString(){
        return "Word " + word + " Moves " + moves  + " History [" + history + "] Priority " + priority;
    }

    public int priority(String b){
        int length = historyLength();

        int match = 0;
        for (int j = 0; j < word.length(); j++) {
            if (b.charAt(j) == word.charAt(j)) {
                match++;
            }
        }
        int nonmatch = b.length()-match;
        return length + nonmatch;
    }

    @Override
    public int compareTo(WordInfo o) {
        return this.priority - o.priority;
    }

    private int historyLength(){
        if (history == null || history.isEmpty()) {
            return 0;
        }

        String[] words = history.split("\\s+");
        return words.length;
    }
}



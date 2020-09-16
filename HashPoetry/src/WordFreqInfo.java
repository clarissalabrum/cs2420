import java.util.*;

public class WordFreqInfo {
    public String word;
    public int occurCt;
    ArrayList<Freq> followList;

    public WordFreqInfo(String word, int count) {
        this.word = word;
        this.occurCt = count;
        this.followList = new ArrayList<Freq>();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Word :" + word+":");
        sb.append(" (" + occurCt + ") : ");
        for (Freq f : followList)
            sb.append(f.toString());

        return sb.toString();
    }

    public void updateFollows(String follow) {
        boolean updated = false;
        //System.out.println("updateFollows " + word + " " + follow);
        for (Freq f : followList) {
            if (follow.compareTo(f.follow) == 0) {
                f.followCt++;
                updated = true;
            }
        }
        if (!updated)
            followList.add(new Freq(follow, 1));
    }

    public boolean equals(WordFreqInfo f2) {
        return this.word.equals(f2.word);
    }

    public int contains(ArrayList<WordFreqInfo> list, WordFreqInfo word) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(word)) {
                return i;
            }
        }
        return -1;
    }

    public static class Freq {
        String follow;
        int followCt;

        public Freq(String follow, int ct) {
            this.follow = follow;
            this.followCt = ct;
        }

        public String toString() {
            return follow + " [" + followCt + "] ";
        }

        public boolean equals(Freq f2) {
            return this.follow.equals(f2.follow);
        }
    }


}


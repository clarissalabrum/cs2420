import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WritePoetry {

    /**
     * Creates a poem
     * @param file where words are saved
     * @param first first word in poem
     * @param words how many words to be in the poem
     * @param print should the hash table be printed
     * @return new poem
     * @throws FileNotFoundException file could not exist
     */
    public String WritePoem(String file, String first, int words, boolean print) throws FileNotFoundException {

        ArrayList<String> wordList = ReadFromFile(file);
        ArrayList<WordFreqInfo> poemList = MakePoemList(wordList);
        String poem = CreatePoem(poemList, words, first);

        // print hash
        if (print) {
            HashTable<WordFreqInfo> poemHash = new HashTable<>();
            for (WordFreqInfo wordFreqInfo : poemList) {
                poemHash.insert(wordFreqInfo);
            }
            System.out.println(poemHash.toString(poemHash.size()));
        }

        return poem;
    }

    /**
     * Reads in a file and returns an array list of all the words in the file
     * @param fileName file name
     * @return arraylist of words
     * @throws FileNotFoundException in case a wrong named file is used
     */
    private static ArrayList<String> ReadFromFile(String fileName) throws FileNotFoundException {
        fileName = "data/" + fileName;
        String line = "";
        ArrayList<String> words = new ArrayList<>();

        // pass the path to the file as a parameter
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (!line.equals("")) {
                String [] subLine = line.split(" ");
                for (String subWord : subLine) {
                    words.add(subWord.replaceAll("[\n,.!\"'-?()]", "").toLowerCase()
                            .replace("[","").replace("]",""));
                }
            }
        }

        return words;
    }

    /**
     * Takes a list of words and records how many times each word occurs and what words follow
     * each word
     * @param wordList list of words
     * @return A list of each word with its count and follow list
     */
    private static ArrayList<WordFreqInfo> MakePoemList(ArrayList<String> wordList) {

        ArrayList<WordFreqInfo> usedList = new ArrayList<>();
        int usedListCount = 0;

        for (int i = 0; i < wordList.size(); i++) {
            WordFreqInfo wordInfo = new WordFreqInfo(wordList.get(i), 1);
            int index = wordInfo.contains(usedList, wordInfo);
            if (index == -1) {
                usedList.add(wordInfo);
                if (i == wordList.size() - 1) {
                    usedList.get(usedListCount).updateFollows(wordList.get(0));
                } else usedList.get(usedListCount).updateFollows(wordList.get(i + 1));
                usedListCount++;
            } else {
                usedList.get(index).occurCt += 1;
                if (i == wordList.size() - 1) {
                    usedList.get(index).updateFollows(wordList.get(0));
                } else {
                    usedList.get(index).updateFollows(wordList.get(i + 1));
                }
            }
        }
        return usedList;
    }

    /**
     * Takes a poem list (frequency of words) and makes a new poem according to probablities of what
     * might come next
     * @param poemList frequency list of words
     * @param size number of words in poem
     * @param start the starting word
     * @return the new poem
     */
    private static String CreatePoem(ArrayList<WordFreqInfo> poemList, int size, String start) {
        int index = index(poemList, start);
        if (index == -1)
            return "error";
        StringBuilder poem = new StringBuilder(start);

        for (int i = 1; i < size; i++) {
            String nextWord = RandomWord(poemList.get(index).followList);
            poem.append(" ").append(nextWord);
            index = index(poemList, nextWord);

        }
        return poem.toString();
    }

    /**
     * knowing the frequency of each following word it returns a random next word
     * @param followList list of following words
     * @return next word
     */
    private static String RandomWord(ArrayList<WordFreqInfo.Freq> followList) {
        int count = 0;
        for (WordFreqInfo.Freq word: followList) {
            count += word.followCt;
        }
        //generate Random number
        double random = Math.random() * count;
        for (WordFreqInfo.Freq word: followList) {
            if (random < word.followCt) return word.follow;
            random -= word.followCt;
        }
        return followList.get(followList.size()-1).follow;
    }

    /**
     * finds the index of a word in the word info array
     * @param poemList frequency list of words
     * @param findWord specific word in poemList
     * @return index of the word
     */
    private static int index(ArrayList<WordFreqInfo> poemList, String findWord) {
        for (WordFreqInfo wordInfo: poemList) {
            if(wordInfo.word.equals(findWord)) {
                return poemList.indexOf(wordInfo);
            }
        }
        return -1;
    }
}

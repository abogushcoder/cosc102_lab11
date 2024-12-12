import java.util.*;
import java.io.*;

public class ShakespeareGPT {

  private HashMap<String, FreqMap> wordLibrary;

  private HashSet<String> starters;

  private static final String[] ENDPUNCT = { ".", "?", "!" };
  private static final String[] ENDQUOT = { "", "\"", "'" };

  public static Random rand = new Random();

  public ShakespeareGPT() {
    this.wordLibrary = new HashMap<String, FreqMap>();
    this.starters = new HashSet<String>();
  }

  public boolean trainOnFile(String fname) {
    try {
      Scanner scan = new Scanner(new File(fname));
      String first = "";
      if (scan.hasNext())
        first = scan.next();
      if (!scan.hasNext()) {
        System.err.printf("Error! File %s does not contain at least two words\n", fname);
        scan.close();
        return false;
      }
      processWords(scan, first);
      scan.close();
    } catch (FileNotFoundException fnfe) {
      System.err.printf("Could not read file %s\n", fname);
      return false;
    }
    return true;
  }

  private void processWords(Scanner scan, String first) {
    String prevWord = first;

    if (!this.starters.contains(first)) {
      this.starters.add(first);
    }

    if (!this.wordLibrary.containsKey(first)) {
      this.wordLibrary.put(first, new FreqMap());
    }

    while (scan.hasNext()) {
      String wordToProcess = scan.next();

      if (isSentenceEnder(prevWord)) {
        this.starters.add(wordToProcess);
        if (!this.wordLibrary.containsKey(wordToProcess)) {
          this.wordLibrary.put(wordToProcess, new FreqMap());
        }
      } else {
        if (!this.wordLibrary.containsKey(prevWord)) {
          this.wordLibrary.put(prevWord, new FreqMap());
        }
        this.wordLibrary.get(prevWord).add(wordToProcess);

        if (!this.wordLibrary.containsKey(wordToProcess)) {
          this.wordLibrary.put(wordToProcess, new FreqMap());
        }
      }

      prevWord = wordToProcess;
    }
  }

  private boolean isSentenceEnder(String word) {
    for (String p : ENDPUNCT) {
      for (String m : ENDQUOT) {
        if (word.endsWith(p + m)) {
          return true;
        }
      }
    }
    return false;
  }

  public String getRandomStarter() {
    if (this.starters == null || this.starters.size() == 0) {
      throw new IllegalStateException("No sentence starters available!");
    }
    int starterSize = this.starters.size();
    int randomNum = rand.nextInt(starterSize);
    int iterator = 0;
    for (String word : this.starters) {
      if (iterator == randomNum) {
        return word;
      }
      iterator++;
    }
    return null;
  }

  public void generate(int numOfSentences) {
    if (numOfSentences <= 0) {
      throw new IllegalArgumentException("Number of sentences must be > 0");
    }

    int sentencesGenerated = 0;
    String currentWord = getRandomStarter();
    System.out.print(currentWord);

    while (sentencesGenerated < numOfSentences) {
      if (isSentenceEnder(currentWord)) {
        sentencesGenerated++;
        if (sentencesGenerated == numOfSentences) {

          System.out.println();
          break;
        } else {
          currentWord = getRandomStarter();
          System.out.println();
          System.out.print(currentWord);
          continue;
        }
      }

      FreqMap freqMap = this.wordLibrary.get(currentWord);
      String nextWord = null;

      if (freqMap == null || freqMap.totalWordCount() == 0) {
        currentWord = getRandomStarter();
        System.out.println();
        System.out.print(currentWord);
        continue;
      } else {
        nextWord = freqMap.getRandWeightedWord();
      }
      System.out.print(" " + nextWord);
      currentWord = nextWord;
    }
  }

  public void generate(int numOfSentences, String seedWord) {
    if (numOfSentences <= 0)
      throw new IllegalArgumentException("Invalid number of lines specified: " + numOfSentences);
    if (!wordLibrary.containsKey(seedWord))
      throw new IllegalArgumentException("Seed word: \"" + seedWord + "\" doesn't exist in bot's word library!");

    int sentencesGenerated = 0;
    String currentWord = seedWord;
    System.out.print(currentWord);

    while (sentencesGenerated < numOfSentences) {
      if (isSentenceEnder(currentWord)) {
        sentencesGenerated++;
        if (sentencesGenerated == numOfSentences) {
          System.out.println();
          break;
        } else {
          currentWord = getRandomStarter();
          System.out.println();
          System.out.print(currentWord);
          continue;
        }
      }

      FreqMap freqMap = this.wordLibrary.get(currentWord);
      String nextWord = null;

      if (freqMap == null || freqMap.totalWordCount() == 0) {
        currentWord = getRandomStarter();
        System.out.println();
        System.out.print(currentWord);
        continue;
      } else {
        nextWord = freqMap.getRandWeightedWord();
      }

      System.out.print(" " + nextWord);
      currentWord = nextWord;
    }
  }

  public String toString() {
    if (wordLibrary.size() == 0)
      return "{}";
    StringBuilder sb = new StringBuilder("{");
    for (String key : wordLibrary.keySet())
      sb.append(key + "->" + wordLibrary.get(key) + "\n");
    return sb.substring(0, sb.length() - 1) + "}";
  }

  public HashSet<String> getLineStarters() {
    return this.starters;
  }

  public HashMap<String, FreqMap> getWordLibrary() {
    return this.wordLibrary;
  }
}

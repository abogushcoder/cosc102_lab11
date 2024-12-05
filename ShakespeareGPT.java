import java.util.*;
import java.io.*;

//A bot capable of procedurally generating Shakespeare text
//after being "trained" on any number of his work(s).
public class ShakespeareGPT {

  // Mapping all words the bot has seen in its training
  // to all the words that follow it
  private HashMap<String, FreqMap> wordLibrary;

  // Set of all words that start sentences.
  private HashSet<String> starters;

  // Constants used in detecting sentence enders.
  private static final String[] ENDPUNCT = { ".", "?", "!" };
  private static final String[] ENDQUOT = { "", "\"", "'" };

  // Use me for random number generation!
  public static Random rand = new Random();

  public ShakespeareGPT() {

    this.wordLibrary = new HashMap<String, FreqMap>();
    this.starters = new HashSet<String>();

  }

  // trains the bot on the text in the argument file name;
  // updates the bot's word library with the text in the argument file.
  //
  // Returns a boolean indicating if the file was able to be open and read (true)
  // or not (false... if file cannot be found or doesn't contain sufficient text).
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

  // Helper method to process the text in a file once its been deemed valid.
  // Updates the bot's word library as well as sets of sentence starters and
  // enders.
  // Arguments include a Scanner of the file to be processed (pointing at the
  // second word in the file) and the first word in the file.
  private void processWords(Scanner scan, String first) {
    String prevWord;
    prevWord = first;
    if (!starters.contains(first)) {
      this.starters.add(first);
      this.wordLibrary.put(first, new FreqMap());
    }
    while (scan.hasNext()) {
      wordToProcess = scan.next();
      if (prevWord.isSentenceEnder()) {
        this.starters.add(wordToProcess);

      } else {
        this.wordLibrary.get(prevWord).add(wordToProcess);
      }
      prevWord = wordToProcess;

    }

  }

  // Determines if a particular word is a sentence ender;
  // returns a boolean indicating if this word is a sentence ender or not.
  //
  // A word is a sentence ender if it ends with a ./?/!,
  // with or without a single or double quote following.
  //
  // The following would all be considered sentence enders:
  // day. day? day! day." day!'
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

  // Returns a random sentence starter from the bot's word library.
  // If the bot's sentence starter set is empty, throws an IllegalStateException
  // When picking, all sentence starters should be equally weighted
  public String getRandomStarter() {
    if (this.starters == null) {
      throw new IllegalStateException("Starters HashSet is empty");
    }
    int starterSize = this.starters.size();
    int randomNum = rand.nextInt((starterSize) + 1;
    int iterator = 1;
    for (String word: this.starters) {
      if (iterator == randomNum) {
        return word;
      }
      iterator++;
    }
  return null;
  }

  // Generates and PRINTS the argument number of sentences,
  // eginning with a random sentence starter
  public void generate(int numOfSentences) {

    // Implement me!!

  }

  // Generates and PRINTS the argument number of sentences,
  // using the second argument as the seed word.
  //
  // These sanity checks are done for you; failing them throws an exception:
  // - Number of sentences is invalid
  // - The sentence starter doesn't exist in the bot's word library
  public void generate(int numOfSentences, String seedWord) {
    if (numOfSentences <= 0)
      throw new IllegalArgumentException("Invalid number of lines specified: " + numOfSentences);
    if (!wordLibrary.containsKey(seedWord))
      throw new IllegalArgumentException("Seed word: \"" + seedWord + "\" doesn't exist in bot's word library!");

    // Implement me!!

  }

  // Returns a nice, pretty string of the bot's words and their frequency maps
  public String toString() {
    if (wordLibrary.size() == 0)
      return "{}";
    StringBuilder sb = new StringBuilder("{");
    for (String key : wordLibrary.keySet())
      sb.append(key + "->" + wordLibrary.get(key) + "\n");
    return sb.substring(0, sb.length() - 1) + "}";
  }

  // --Accessors--
  public HashSet<String> getStarters() {
    return this.starters;
  }

  public HashMap<String, FreqMap> getWordLibrary() {
    return this.wordLibrary;
  }
  // -------------

}

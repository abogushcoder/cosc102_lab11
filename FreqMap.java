import java.util.*;

//A mapping of words to the frequencies at which they appear,
//used by the bot to choose a word that follows another.
//Meant to be used as the value in a ShakespeareGPT's Word Library,
//each FreqMap is associated with some word in the training text. 
public class FreqMap {

  /* The words and their frequencies as keys and values, respectively */
  private HashMap<String, Integer> frequencies;

  // The total number of words added to this object (i.e. the sum of the
  // frequencies)
  // Ex: given a map of {"the": 4, "quick":1, "brown":2, "fox":3}, totalWords
  // would be 10
  private int totalWords;

  // Use me for your random number generation!
  public static Random rand = ShakespeareGPT.rand;

  public FreqMap() {
    this.totalWords = 0;
    frequencies = new HashMap<String, Integer>();
  }

  // Adds the argument word to the frequency map,
  // updating the frequencies and totalWords accordingly
  public void add(String word) {
    if (frequencies.containsKey(word))
      frequencies.put(word, frequencies.get(word) + 1);
    else
      frequencies.put(word, 1);
    totalWords++;
  }

  // Returns a random word from the FreqMap, weighted by the frequencies.
  // For example, given a FreqMap of {"the": 4, "quick":1, "brown":2, "fox":3},
  // this function would have a 40% chance to return "the", a 10% chance to return
  // "quick", and so on...
  //
  // If the FreqMap has no words added to it, this method returns null.
  public String getRandWeightedWord() {
    if (this.frequencies == null) {
      return null;
    }
    int valueSum = 0;
    for (int value : this.frequencies.values()) {
      valueSum += value;
    }
    int random = rand.nextInt(valueSum);
    int sum = 0;
    for (Map.Entry<String, Integer> entry : this.frequencies.entrySet()) {
      sum += entry.getValue();
      if (random < sum) {
        return entry.getKey();
      }
    }
    return null;

  }

  // A nice, pretty String of the frequency maps's words and frequencies,
  // similar to the instructions.
  public String toString() {
    if (frequencies.size() == 0)
      return "{}";
    StringBuilder sb = new StringBuilder("{");
    for (String key : frequencies.keySet())
      sb.append(key + ":" + frequencies.get(key) + ", ");
    return sb.substring(0, sb.length() - 2) + "}";
  }

  // --Accessors--
  public int totalWordCount() {
    return this.totalWords;
  }

}

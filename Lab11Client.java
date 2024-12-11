//Client class used to test your various ShakespeareGPT 
//and word bank functionalities
public class Lab11Client {

  public static void main(String[] args) {

    ShakespeareGPT bot = new ShakespeareGPT();
    bot.trainOnFile("AsYouLikeIt.txt");
    bot.trainOnFile("RomeoAndJuliet.txt");
    bot.trainOnFile("TwelfthNight.txt");

    System.out.println(bot.getLineStarters());
    bot.generate(10, "notInFile");
    // bot.generate(20);
  }

}

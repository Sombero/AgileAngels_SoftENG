package edu.wpi.agileAngels;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import edu.wpi.agileAngels.Controllers.AppController;
import java.io.IOException;
import javafx.application.Platform;

public class MenuSpeech implements Runnable {
  private static Configuration configuration = new Configuration();
  private static LiveSpeechRecognizer recognizer;
  private SpeechResult result;
  private static Thread t = null;
  private String resStr = "None";
  private boolean stop = false;

  private AppController appController = AppController.getInstance();

  public MenuSpeech() throws IOException {}

  public static void main(String[] args) throws Exception {

    // create an object of Runnable target
    MenuSpeech thread = new MenuSpeech();
    configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
    configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
    configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
    recognizer = new LiveSpeechRecognizer(configuration);

    // pass the runnable reference to Thread

    t = new Thread(thread, "CommandListener");

    // start the thread
    startConfiguration();
    t.start();

    // get the name of the thread
    System.out.println(t.getName());

    // configuration.setSampleRate(8000);

  }

  public static void startConfiguration() {
    recognizer.startRecognition(true);
  }

  public String listen() throws IOException {
    result = recognizer.getResult();
    String out = "Hypothesis: " + result.getHypothesis();
    System.out.format("Hypothesis: %s\n", result.getHypothesis());
    out = findCommand(result);
    if (out.compareTo("lab") == 0) {

      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/lab-view.fxml");
          });
    }
    if (out.compareTo("equipment") == 0) {

      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/equipment-view.fxml");
          });
    }
    if (out.compareTo("sanitation") == 0) {

      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/sanitation-view.fxml");
          });
    }
    if (out.compareTo("gift") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/gifts-view.fxml");
          });
    }
    if (out.compareTo("maintenance") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/maintenance-view.fxml");
          });
    }
    if (out.compareTo("laundry") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/laundryRequest-view.fxml");
          });
    }
    if (out.compareTo("morgue") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/morgue-view.fxml");
          });
    }
    if (out.compareTo("patient") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/patientTransport-view.fxml");
          });
    }
    if (out.compareTo("medical") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/medAid-view.fxml");
          });
    }
    if (out.compareTo("meal") == 0) {
      shuttingDownThread();
      Platform.runLater(
          () -> {
            appController.loadPage("/edu/wpi/agileAngels/views/mealRequest-view.fxml");
          });
    }

    return out;
    // Pause recognition process. It can be resumed then with startRecognition(false).
  }

  private void shuttingDownThread() {
    stop = true;
    System.out.println("Command: Make Sanitation Request");
    closeRecognition();
    t.interrupt();
    System.out.println("Loading page");
  }

  public String findCommand(SpeechResult out) {
    String res = "None";
    for (WordResult r : out.getWords()) {
      System.out.println("Word: " + r.getWord());

      if (r.getWord().toString().compareTo("lab") == 0
          || r.getWord().toString().compareTo("lap") == 0
          || r.getWord().toString().compareTo("ab") == 0
          || r.getWord().toString().compareTo("lad") == 0
          || r.getWord().toString().compareTo("latter") == 0) {
        System.out.println("IT's LAB");
        res = "lab";
        return res;
      }
      if (r.getWord().toString().compareTo("med") == 0
          || r.getWord().toString().compareTo("medical") == 0
          || r.getWord().toString().compareTo("men") == 0
          || r.getWord().toString().compareTo("mad") == 0
          || r.getWord().toString().compareTo("medic") == 0) {
        System.out.println("It's Med");
        res = "medical";
        return res;
      }
      if (r.getWord().toString().compareTo("san") == 0
          || r.getWord().toString().compareTo("sanitation") == 0
          || r.getWord().toString().compareTo("sand") == 0
          || r.getWord().toString().compareTo("can") == 0
          || r.getWord().toString().compareTo("sun") == 0) {
        res = "sanitation";
        return res;
      }
      if (r.getWord().toString().compareTo("equipment") == 0
          || r.getWord().toString().compareTo("equip") == 0
          || r.getWord().toString().compareTo("") == 0
          || r.getWord().toString().compareTo("equal") == 0
          || r.getWord().toString().compareTo("equals") == 0) {
        res = "equipment";
        return res;
      }
      if (r.getWord().toString().compareTo("gift") == 0
          || r.getWord().toString().compareTo("giff") == 0
          || r.getWord().toString().compareTo("get") == 0
          || r.getWord().toString().compareTo("gifted") == 0
          || r.getWord().toString().compareTo("gig") == 0) {
        res = "gift";
        return res;
      }
      if (r.getWord().toString().compareTo("maintenance") == 0
          || r.getWord().toString().compareTo("main") == 0
          || r.getWord().toString().compareTo("maintain") == 0
          || r.getWord().toString().compareTo("maintains") == 0
          || r.getWord().toString().compareTo("may") == 0) {
        res = "maintenance";
        return res;
      }
      if (r.getWord().toString().compareTo("laundry") == 0
          || r.getWord().toString().compareTo("lawn") == 0
          || r.getWord().toString().compareTo("launders") == 0
          || r.getWord().toString().compareTo("laundries") == 0
          || r.getWord().toString().compareTo("lawns") == 0) {
        res = "laundry";
        return res;
      }
      if (r.getWord().toString().compareTo("morgue") == 0
          || r.getWord().toString().compareTo("dead") == 0
          || r.getWord().toString().compareTo("morgues") == 0
          || r.getWord().toString().compareTo("morgun") == 0
          || r.getWord().toString().compareTo("more") == 0) {
        res = "morgue";
        return res;
      }
      if (r.getWord().toString().compareTo("patient") == 0
          || r.getWord().toString().compareTo("patent") == 0
          || r.getWord().toString().compareTo("plate") == 0
          || r.getWord().toString().compareTo("transport") == 0
          || r.getWord().toString().compareTo("paint") == 0) {
        res = "patient";
        return res;
      }
      if (r.getWord().toString().compareTo("meal") == 0
          || r.getWord().toString().compareTo("me") == 0
          || r.getWord().toString().compareTo("menu") == 0
          || r.getWord().toString().compareTo("food") == 0
          || r.getWord().toString().compareTo("finn") == 0) {
        res = "meal";
        return res;
      }
    }

    return res;
  }

  public void closeRecognition() {
    recognizer.stopRecognition();
  }

  @Override
  public void run() {
    String res = null;
    try {
      res = listen();
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (res != null && (!stop)) {
      try {

        res = listen();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        t.wait(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

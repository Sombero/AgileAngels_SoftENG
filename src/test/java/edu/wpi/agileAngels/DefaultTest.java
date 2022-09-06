/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.agileAngels;

public class DefaultTest {
  /*
  LoginController dansLController = new LoginController();

  public DefaultTest() throws SQLException {}

  @Test
  public void test() {}

  @Test
  public void initialsMakerDanielTest() {
    String danInitials = "DO";
    assertTrue(danInitials.equals(dansLController.initialsMaker("Daniel Onyema")));
  }

  // Testing
  @Test
  public void initialsMakerJustinTest() {
    String justinInitials = "JW";
    assertTrue(
        justinInitials.equals(dansLController.initialsMaker("Justin Paul Santiago - Wonoski")));
  }

  @Test
  public void initialsMakerEmptyTest() {
    String notApplicable = "N/A";
    assertTrue(notApplicable.equals(dansLController.initialsMaker("")));
  }

  // Tests reqFilter with generic list
  @Test
  public void filterReqEmployeeHarmoniTest() throws SQLException {
    MethodTestClass dansMTC = new MethodTestClass(0);

    ObservableList<Request> dansList = FXCollections.observableArrayList();
    Employee harmoni = new Employee("Harmoni", "needsToPlayPokemon");
    Employee joe = new Employee("Joe", "Sheeeesh");
    Employee aaron = new Employee("Aaron", "He plays League?");
    Location unityHall =
        new Location(
            "balls", 1, 1, "your mom", "backrooms", "help", "guys im coding", "cook-hungy");

    Request hReq1 = new Request("hReq1", harmoni, unityHall, "ooh", "eee", "ooh", "aah", "ahh");
    Request hReq2 =
        new Request("hReq1", harmoni, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");
    Request notHReq =
        new Request("hReq1", joe, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");
    Request notHReq2 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");
    Request notHReq3 =
        new Request("hReq1", joe, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");
    Request notHReq4 =
        new Request("hReq1", joe, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");

    dansList.add(hReq1);
    dansList.add(hReq2);
    dansList.add(notHReq);
    dansList.add(notHReq2);
    dansList.add(notHReq3);
    dansList.add(notHReq4);

    ObservableList filterList = dansMTC.filterReqEmployeeNoMedData(harmoni.getName(), dansList);

    assertTrue(filterList.contains(hReq1) && filterList.contains(hReq2));
  }

  // Tests reqFilter with generic list, checks for what isn't there.
  @Test
  public void filterReqEmployeeHarmoniDOESNTCONTAINTest() throws SQLException {
    MethodTestClass dansMTC = new MethodTestClass(0);

    ObservableList<Request> dansList = FXCollections.observableArrayList();
    Employee harmoni = new Employee("Harmoni", "needsToPlayPokemon");
    Employee joe = new Employee("Joe", "Sheeeesh");
    Employee aaron = new Employee("Aaron", "He plays League?");
    Location unityHall =
        new Location(
            "balls", 1, 1, "your mom", "backrooms", "help", "guys im coding", "cook-hungy");

    Request hReq1 = new Request("hReq1", harmoni, unityHall, "ooh", "eee", "ooh", "aah", "ahh");
    Request hReq2 =
        new Request("hReq1", harmoni, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");
    Request notHReq =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "DRINKS", "HIS", "BIKE");
    Request notHReq2 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "CONSUMES", "HIS", "BIKE");
    Request notHReq3 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "DABS", "HIS", "BIKE");
    Request notHReq4 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "FLINGS", "HIS", "BIKE");

    dansList.add(hReq1);
    dansList.add(hReq2);
    dansList.add(notHReq);
    dansList.add(notHReq2);
    dansList.add(notHReq3);
    dansList.add(notHReq4);

    ObservableList filterList = dansMTC.filterReqEmployeeNoMedData(harmoni.getName(), dansList);

    assertTrue(
        !filterList.contains(notHReq)
            && !filterList.contains(notHReq2)
            && !filterList.contains(notHReq3)
            && !filterList.contains(notHReq4));
  }

  // Tests reqFilter with generic list, should return no requests.
  @Test
  public void filterReqEmployeeHarmoniEMPTYLISTTest() throws SQLException {
    MethodTestClass dansMTC = new MethodTestClass(0);

    ObservableList<Request> dansList = FXCollections.observableArrayList();
    Employee harmoni = new Employee("Harmoni", "needsToPlayPokemon");
    Employee joe = new Employee("Joe", "Sheeeesh");
    Employee aaron = new Employee("Aaron", "He plays League?");
    Location unityHall =
        new Location(
            "balls", 1, 1, "your mom", "backrooms", "help", "guys im coding", "cook-hungy");

    Request hReq1 = new Request("hReq1", harmoni, unityHall, "ooh", "eee", "ooh", "aah", "ahh");
    Request hReq2 =
        new Request("hReq1", harmoni, unityHall, "BALLS", "JAKOB", "TOUCHES", "HIS", "BIKE");
    Request notHReq =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "DRINKS", "HIS", "BIKE");
    Request notHReq2 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "CONSUMES", "HIS", "BIKE");
    Request notHReq3 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "DABS", "HIS", "BIKE");
    Request notHReq4 =
        new Request("hReq1", aaron, unityHall, "BALLS", "JAKOB", "FLINGS", "HIS", "BIKE");

    dansList.add(hReq1);
    dansList.add(hReq2);
    dansList.add(notHReq);
    dansList.add(notHReq2);
    dansList.add(notHReq3);
    dansList.add(notHReq4);

    ObservableList filterList = dansMTC.filterReqEmployeeNoMedData(joe.getName(), dansList);

    assertTrue(filterList.isEmpty());
  }*/
}

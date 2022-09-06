package edu.wpi.agileAngels;

import edu.wpi.agileAngels.Controllers.*;
import edu.wpi.agileAngels.Database.*;

public class DatabaseTests {
  /*
  private static Adb database;

  public static void main(String[] args) throws SQLException {
    /*database = new Adb();
      database.initialize();
      //  DBconnection.switchConnection();
      testMedicalEquipmentTable();
      testEmployeesTable();
      testLocationsTable();
      testServiceRequestsTable();
      // DBconnection.shutdown();
    }

    public static void testMedicalEquipmentTable() {
      Location loc1 = new Location("TOW101", 90.8, 70.8, "Floor 3", "Tower", "??", "Hallway", "HALL");
      Location loc2 =
          new Location("TOW102", 95.7, 70.8, "Floor 3", "Tower", "??", "Room 34", "ROOM34");
      Location loc3 =
          new Location("TOW103", 100.8, 70.8, "Floor 3", "Tower", "??", "Room 35", "ROOM35");
      MedicalEquip mE = new MedicalEquip("R1", "X-Ray Machine", true, loc1, "Complete");
      MedicalEquip mE2 = new MedicalEquip("R2", "X-Ray Machine", false, loc1, "In Progress");
      MedicalEquip mE3 = new MedicalEquip("R3", "Bed", true, loc3, "Not Started");
      MedicalEquip mE4 = new MedicalEquip("R4", "Bed", false, loc3, "Complete");
      MedicalEquip mE5 = new MedicalEquip("R5", "Recliner", false, loc2, "Complete");

      // Add
      Adb.addMedicalEquipment(mE);
      Adb.addMedicalEquipment(mE2);
      Adb.addMedicalEquipment(mE3);
      Adb.addMedicalEquipment(mE4);
      Adb.addMedicalEquipment(mE5);

    // Remove
    Adb.removeMedicalEquipment(mE2.getID());
    Adb.removeMedicalEquipment(mE2.getID());

    // Update
    mE3.setClean(false);
    // System.out.println(mE3.getID() + "'s clean is " + mE3.isClean());
    Adb.updateMedicalEquipment(mE3);
    mE4.setLocation(loc2);
    Adb.updateMedicalEquipment(mE4);
  }

  public static void testServiceRequestsTable() {
    ArrayList testAL = new ArrayList();

    Location loc = new Location("abc15", 130, 234, "1", "A", "d", "the Hallway", "the Hall");
    Employee dummy = new Employee("Matha", "atham");
    Request r1 = new Request("R1", dummy, loc, "MED", "Complete", "Descr.", "Available", "N/A");
    Request r2 = new Request("R2", dummy, loc, "GIFT", "In Progress", "Descr.", "N/A", "N/A");
    Request r3 =
        new Request("R3", dummy, loc, "MED", "Not Started", "Descr.", " Not Available", "N/A");
    Request r4 = new Request("R4", dummy, loc, "SAN", "Complete", "Descr.", "N/A", "N/A");

    // Add
    Adb.addRequest(r1);
    Adb.addRequest(r2);
    Adb.addRequest(r3);
    Adb.addRequest(r4);

    // Remove
    Adb.removeRequest(r1.getName());
    Adb.removeRequest(r1.getName());

    // Update
    Location loc2 = new Location("abc15", 130, 234, "1", "A", "d", "the Hallway", "the Hall");
    r4.setLocation(loc2);
    Adb.updateRequest(r4);
  }

  public static void testLocationsTable() {
    Location loc1 = new Location("TOW101", 90.8, 70.8, "Floor 3", "Tower", "??", "Hallway", "HALL");
    Location loc2 =
        new Location("TOW102", 95.7, 70.8, "Floor 3", "Tower", "??", "Room 34", "ROOM34");
    Location loc3 =
        new Location("TOW103", 100.8, 70.8, "Floor 3", "Tower", "??", "Room 35", "ROOM35");

    // Add
    Adb.addLocation(loc1);
    Adb.addLocation(loc2);
    Adb.addLocation(loc3);

    // Remove
    Adb.removeLocation(loc2.getNodeID());
    Adb.removeLocation("Room 36");

    // Update
    loc3.setYCoord(-1.0);
    Adb.updateLocation(loc3);
  }

  public static void testEmployeesTable() {
    ArrayList testAL = new ArrayList();
    Location loc = new Location("abc15", 130, 234, "1", "A", "d", "the Hallway", "the Hall");
    Employee dummy = new Employee("Matha", "atham");
    ArrayList<Request> reqs = new ArrayList<Request>();
    reqs.add(new Request("R1", dummy, loc, "MED", "Complete", "Descr.", "Available", "N/A"));
    Employee Emily = new Employee("Emily", "emily123");
    Employee Martha = new Employee("Martha", "jjjjjtype");
    reqs.remove(0);
    reqs.add(
        new Request("R3", Emily, loc, "MED", "Not Started", "Descr.", " Not Available", "N/A"));
    reqs.add(
        new Request("R6", Martha, loc, "MED", "Not Started", "Descr.", " Not Available", "N/A"));
    Employee Lou = new Employee("Lou", "kellyanne");

    // Add
    Adb.addEmployee(Emily);
    Adb.addEmployee(Martha);
    Adb.addEmployee(Lou);

    // Remove
    Adb.removeEmployee(Martha.getName());

    // Update

    Adb.updateEmployee(Emily);
  }*/
}

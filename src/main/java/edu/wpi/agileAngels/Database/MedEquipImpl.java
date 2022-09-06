package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import edu.wpi.agileAngels.Controllers.AppController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class MedEquipImpl implements MedEquipDAO {
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private static MedEquipImpl MedEquipDAO = null;
  private HashMap<String, MedicalEquip> MedEquipData;
  private AppController appController = AppController.getInstance();

  public MedEquipImpl(HashMap<String, MedicalEquip> data) throws SQLException {
    this.MedEquipData = data;
  }

  @Override
  public HashMap<String, MedicalEquip> getAllMedicalEquipment() {
    return MedEquipData;
  }

  public static MedEquipImpl getInstance() throws SQLException {
    if (MedEquipDAO == null) {
      HashMap<String, MedicalEquip> Data = new HashMap<>();
      MedEquipDAO = new MedEquipImpl(Data);
    }
    return MedEquipDAO;
  }

  @Override
  public void addEquipment(MedicalEquip medicalEquip) {
    MedEquipData.put(medicalEquip.getID(), medicalEquip);
    Adb.addMedicalEquipment(medicalEquip);
    // System.out.println("MedicalEquipment " + medicalEquip.getID() + " is added into the
    // database.");
  }

  @Override
  public void removeEquipment(MedicalEquip medicalEquip) {
    MedEquipData.remove(medicalEquip.getID());
    Adb.removeMedicalEquipment(medicalEquip.getID());
    // System.out.println(
    //   "MedicalEquipment " + medicalEquip.getID() + " is removed from the database.");
  }

  @Override
  public void updateEquipmentLocation(MedicalEquip medicalEquip, Location location) {
    medicalEquip.setLocation(location);
    Adb.updateMedicalEquipment(medicalEquip);
    // System.out.println("MedicalEquipment " + medicalEquip.getID() + " location is updated");
  }

  @Override
  public void updateStatus(MedicalEquip medicalEquip, String statusIn) {
    medicalEquip.setStatus(statusIn);
    Adb.updateMedicalEquipment(medicalEquip);
  }

  @Override
  public void updateMedicalCleanliness(MedicalEquip medicalEquip, Boolean clean) {
    String type = medicalEquip.getType();
    String floor = medicalEquip.getLocation().getFloor();
    if (clean) {
      appController.incrementDirty(type, floor, -1);
    } else {
      appController.incrementDirty(type, floor, 1);
    }
    medicalEquip.setClean(clean);
    Adb.updateMedicalEquipment(medicalEquip);
    // System.out.println("MedicalEquipment " + medicalEquip.getID() + " cleanliness is updated");
  }

  public void readCSV() {
    String line = "";
    String splitBy = ",";

    try {
      BufferedReader br = new BufferedReader(new FileReader("./MedEquip.csv"));
      boolean OnHeader = false;
      line.split(splitBy);

      while ((line = br.readLine()) != null) {
        if (OnHeader) {
          String[] values = line.split(splitBy);
          makeEquip(values);
        } else {
          OnHeader = true;
        }
      }
    } catch (IOException var7) {
      var7.printStackTrace();
    } catch (SQLException var8) {
      var8.printStackTrace();
    }
  }

  private void makeEquip(String[] values) throws SQLException {
    boolean clean = true;
    if (values[2].compareTo("false") == 0) {
      clean = false;
    }
    MedicalEquip medEquip =
        new MedicalEquip(values[0], values[1], clean, findLocation(values[3]), values[4]);
    MedEquipData.put(values[0], medEquip);
    Adb.addMedicalEquipment(medEquip);
  }

  private Location findLocation(String value) {
    Location location;
    HashMap<String, Location> locationData = locDAO.getAllLocations();
    location = locationData.get(value);
    // System.out.println("Location Value " + location.getLongName());
    return location;
  }

  public void resetAllEquips() {
    MedEquipData = new HashMap<>();
  }
}

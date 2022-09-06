package edu.wpi.agileAngels.Database;

import java.util.HashMap;

public interface MedEquipDAO {

  public HashMap<String, MedicalEquip> getAllMedicalEquipment();

  public void addEquipment(MedicalEquip medicalEquip);

  public void removeEquipment(MedicalEquip medicalEquip);

  public void updateMedicalCleanliness(MedicalEquip medicalEquip, Boolean clean);

  public void updateEquipmentLocation(MedicalEquip medicalEquip, Location location);

  public void updateStatus(MedicalEquip medicalEquip, String statusIn);
}

package edu.wpi.agileAngels.Database;

public class MedicalEquip {
  private final String ID;
  private String type;
  private boolean clean;
  private Location location;
  private String status;

  public MedicalEquip(
      String id, String typeIn, boolean cleanIn, Location locationIn, String statusIn) {
    this.ID = id;
    this.type = typeIn;
    this.clean = cleanIn;
    this.location = locationIn;
    this.status = statusIn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Location getLocation() {
    return this.location;
  }

  public String getID() {
    return ID;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isClean() {
    return clean;
  }

  public void setClean(boolean clean) {
    this.clean = clean;
  }
}

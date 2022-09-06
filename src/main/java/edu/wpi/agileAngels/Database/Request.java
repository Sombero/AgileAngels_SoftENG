//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.wpi.agileAngels.Database;

public class Request {
  private String name;
  private Employee employee;
  private Location location;
  private String type;
  private int requestType;
  private String status;
  private String description;
  private String attribute1;
  private String attribute2;
  private MedicalEquip medicalEquip;

  // constructor for medical requests
  public Request(
      String Name,
      Employee employee,
      Location location,
      String type,
      String status,
      String description,
      String attribute1,
      String attribute2,
      MedicalEquip medicalEquip) {
    this.name = Name;
    this.employee = employee;
    this.location = location;
    this.type = type;
    this.status = status;
    this.description = description;
    this.attribute1 = attribute1;
    this.attribute2 = attribute2;
    this.medicalEquip = medicalEquip;
  }

  // constructor for all other request types
  public Request(
      String Name,
      Employee employee,
      Location location,
      String type,
      String status,
      String description,
      String attribute1,
      String attribute2) {
    this.name = Name;
    this.employee = employee;
    this.location = location;
    this.type = type;
    this.status = status;
    this.description = description;
    this.attribute1 = attribute1;
    this.attribute2 = attribute2;
    this.medicalEquip = null;
  }

  public void setRequestType(int requestType) {
    this.requestType = requestType;
  }

  public String getStatus() {
    return this.status;
  }

  public int getRequestType() {
    return this.requestType;
  }

  public String getDescription() {
    return this.description;
  }

  public Employee getEmployee() {
    return this.employee;
  }

  public Location getLocation() {
    return this.location;
  }

  public String getType() {
    return this.type;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public String getAttribute1() {
    return this.attribute1;
  }

  public void setAttribute1(String attribute) {
    this.attribute1 = attribute;
  }

  public String getAttribute2() {
    return this.attribute2;
  }

  public void setAttribute2(String attribute) {
    this.attribute2 = attribute;
  }

  public MedicalEquip getMedicalEquip() {
    return medicalEquip;
  }

  public void setMedicalEquip(MedicalEquip medicalEquip) {
    this.medicalEquip = medicalEquip;
  }
}

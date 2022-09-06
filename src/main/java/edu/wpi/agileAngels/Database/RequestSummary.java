package edu.wpi.agileAngels.Database;

import java.util.HashMap;

public class RequestSummary {
  private String type;
  private String status;
  private String employee;
  private String floor;
  private HashMap<String, String> typeHash = new HashMap<>();

  public RequestSummary(Request request) {

    typeHash.put("Med", "Equipment");
    typeHash.put("Lab", "Lab");
    typeHash.put("San", "Sanitation");
    typeHash.put("Mea", "Meal");
    typeHash.put("Tra", "Transport");
    typeHash.put("Gif", "Gift");
    typeHash.put("Lau", "Laundry");
    typeHash.put("Mai", "Maintenance");
    typeHash.put("Mor", "Morgue");

    this.type = typeHash.get(request.getName().substring(0, 3));
    this.status = request.getStatus();
    this.employee = request.getEmployee().getName();
    this.floor = request.getLocation().getFloor();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }
}

package edu.wpi.agileAngels.Controllers;

public class MedicalStaff {
  String employee;
  String available;

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public String getAvailable() {
    return available;
  }

  public void setAvailable(String available) {
    this.available = available;
  }

  MedicalStaff(String employee, String available) {
    this.employee = employee;
    this.available = available;
  }
}

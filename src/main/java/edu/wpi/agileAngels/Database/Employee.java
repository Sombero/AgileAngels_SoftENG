package edu.wpi.agileAngels.Database;

import java.time.LocalTime;
import java.util.ArrayList;

public class Employee {

  private String name;
  private String password;
  private int permissionLevel;
  private String floorOnDuty;
  private String department;
  private LocalTime startTime;
  private LocalTime endTime;
  private Employee supervisor;
  private ArrayList<Employee> supervisees;

  public Employee(
      String name,
      String password,
      String floorOnDuty,
      int permissionLevel,
      String department,
      LocalTime startTime,
      LocalTime endTime,
      Employee supervisor,
      ArrayList<Employee> supervisees) {
    this.name = name;
    this.password = password;
    this.floorOnDuty = floorOnDuty;
    this.permissionLevel = permissionLevel;
    this.department = department;
    this.startTime = startTime;
    this.endTime = endTime;
    this.supervisor = supervisor;
    this.supervisees = supervisees;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPermissionLevel() {
    return permissionLevel;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String pass) {
    this.password = pass;
  }

  public String getFloorOnDuty() {
    return floorOnDuty;
  }

  public void setFloorOnDuty(String floorOnDuty) {
    this.floorOnDuty = floorOnDuty;
  }

  @Override
  public String toString() {
    return getName();
  }

  public String initialsMaker() {
    String initials;

    // Is this name empty? Initials ain't applicable...
    if (name.isEmpty()) {
      initials = "NA";
    }
    // Not empty? Not illegal? Run the actual method.
    else {
      // If the name has a space, 2+ names were given and need to be broken up.
      String firstInitial = ("" + name.charAt(0)).toUpperCase();
      if (name.contains(" ")) {
        int lastSpaceIndex = name.lastIndexOf(" ");
        String secondInitial = ("" + name.charAt(lastSpaceIndex + 1)).toUpperCase();

        initials = "" + firstInitial + secondInitial;
        System.out.println("test");
      }
      // Else, 1 name was given, throw the first character.
      else {
        initials = "" + firstInitial;
      }
    }
    return initials;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public Employee getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(Employee supervisor) {
    this.supervisor = supervisor;
  }

  public ArrayList<Employee> getSupervisees() {
    return supervisees;
  }

  public void addSupervisee(Employee supervisee) {
    this.supervisees.add(supervisee);
  }

  public void removeSupervisee(String name) {
    for (Employee employee : this.supervisees) {
      if (employee.getName().equals(name)) this.supervisees.remove(employee);
    }
  }

  public void setSupervisees(ArrayList<Employee> supervisees) {
    this.supervisees = supervisees;
  }

  public void setPermissionLevel(int permissionLevel) {
    this.permissionLevel = permissionLevel;
  }
}

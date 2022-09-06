package edu.wpi.agileAngels.Database;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public interface EmployeeDAO {
  HashMap getAllEmployees();

  Employee getEmployee(String userID);

  public void addEmployee(
      String name,
      String password,
      String duty,
      int permission,
      String department,
      LocalTime startTime,
      LocalTime endTime,
      Employee supervisor,
      ArrayList<Employee> supervisees);

  void removeEmployee(String userID);

  void updateEmployeeName(String userID, String newName);

  void updateEmployeePassword(String userID, String newPW);

  String getEmployeeFloorOnDuty(String name);

  void setEmployeeFloorOnDuty(String name, String floorOnDuty);

  String getEmployeeDepartment(String name);

  void setEmployeeDepartment(String name, String department);

  int getEmployeePermissionLevel(String name);

  void setEmployeePermissionLevel(String name, int permissionLevel);

  LocalTime getEmployeeStartTime(String name);

  void setEmployeeStartTime(String name, LocalTime startTime);

  LocalTime getEmployeeEndTime(String name);

  void setEmployeeEndTime(String name, LocalTime endTime);

  Employee getEmployeeSupervisor(String name);

  void setEmployeeSupervisor(String name, Employee supervisor);

  ArrayList<Employee> getEmployeeSupervisees(String name);

  void setEmployeeSupervisees(String name, ArrayList<Employee> supervisees);

  void addEmployeeSupervisee(String name, Employee supervisee);

  void removeEmployeeSupervisee(String name, String superviseeName);
}

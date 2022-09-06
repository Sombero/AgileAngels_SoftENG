package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Adb;
import edu.wpi.agileAngels.Database.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeManager implements EmployeeDAO {
  private static EmployeeManager employeeManagerDAO = null;
  private int count;

  private HashMap<String, Employee> employeeHashMap;

  public EmployeeManager(HashMap<String, Employee> employeeHashMap, int count) {
    this.employeeHashMap = employeeHashMap;
    this.count = count;
  }

  public static EmployeeManager getInstance() {
    if (employeeManagerDAO == null) {
      HashMap<String, Employee> Employeedata = new HashMap<>();
      employeeManagerDAO = new EmployeeManager(Employeedata, 0);
    }
    return employeeManagerDAO;
  }

  /**
   * Grabs all Employees in hash.
   *
   * @return
   */
  public HashMap getAllEmployees() {
    return this.employeeHashMap;
  }

  public void resetAllEmployees() {
    employeeHashMap = new HashMap<>();
  }

  /** Gets Employee from hash. */
  public Employee getEmployee(String name) {
    return employeeHashMap.get(name);
  }

  /** Adds an employee to the hashmap */
  @Override
  public void addEmployee(
      String name,
      String password,
      String duty,
      int permission,
      String department,
      LocalTime startTime,
      LocalTime endTime,
      Employee supervisor,
      ArrayList<Employee> supervisees) {
    Employee employee =
        new Employee(
            name,
            password,
            duty,
            permission,
            department,
            startTime,
            endTime,
            supervisor,
            supervisees);
    employeeHashMap.put(name, employee);
    Adb.addEmployee(employee);
  }

  /**
   * Checks if the name exists in the hashmap.
   *
   * @param name
   * @return
   */
  public boolean getName(String name) {
    return employeeHashMap.containsKey(name);
  }

  /**
   * is being used for to check the password.
   *
   * @param name
   * @return
   */
  public String getPassword(String name) {
    return employeeHashMap.get(name).getPassword();
  }

  /** Removes Employee from hash. */
  public void removeEmployee(String name) {
    employeeHashMap.remove(name);
  }

  /** Updates Employee's name with newName. */
  public void updateEmployeeName(String name, String newName) {
    employeeHashMap.get(name).setName(newName);
  }

  /** Updates Employee's password with newPW. */
  public void updateEmployeePassword(String name, String newPW) {
    employeeHashMap.get(name).setPassword(newPW);
  }

  /**
   * Gets an employee's duty (floor number or off duty).
   *
   * @param name Employee's name
   * @return Floor number or Off Duty
   */
  public String getEmployeeFloorOnDuty(String name) {
    return employeeHashMap.get(name).getFloorOnDuty();
  }

  /**
   * Sets an employee's duty.
   *
   * @param name Employee's name
   * @param floorOnDuty Floor number or Off Duty
   */
  public void setEmployeeFloorOnDuty(String name, String floorOnDuty) {
    employeeHashMap.get(name).setFloorOnDuty(floorOnDuty);
  }

  /**
   * Gets an employee's department.
   *
   * @param name Employee name
   * @return Department
   */
  @Override
  public String getEmployeeDepartment(String name) {
    return this.employeeHashMap.get(name).getDepartment();
  }

  /**
   * Sets an employee's department
   *
   * @param name Employee name
   * @param department Department
   */
  @Override
  public void setEmployeeDepartment(String name, String department) {
    this.employeeHashMap.get(name).setDepartment(department);
  }

  /**
   * Gets an employee's permission level
   *
   * @param name Employee name
   * @return Permission level
   */
  @Override
  public int getEmployeePermissionLevel(String name) {
    return this.employeeHashMap.get(name).getPermissionLevel();
  }

  /**
   * Sets an employee's permission level
   *
   * @param name Employee name
   * @param permissionLevel Employee permission level
   */
  @Override
  public void setEmployeePermissionLevel(String name, int permissionLevel) {
    this.employeeHashMap.get(name).setPermissionLevel(permissionLevel);
  }

  /**
   * Gets an employee's start time
   *
   * @param name Employee name
   * @return Start time
   */
  @Override
  public LocalTime getEmployeeStartTime(String name) {
    return this.employeeHashMap.get(name).getStartTime();
  }

  /**
   * Sets an employee's start time
   *
   * @param name Employee's name
   * @param startTime Start time
   */
  @Override
  public void setEmployeeStartTime(String name, LocalTime startTime) {
    this.employeeHashMap.get(name).setStartTime(startTime);
  }

  /**
   * Gets an employee's end time
   *
   * @param name Employee's name
   * @return End time
   */
  @Override
  public LocalTime getEmployeeEndTime(String name) {
    return this.employeeHashMap.get(name).getEndTime();
  }

  /**
   * Sets an employee's end time
   *
   * @param name Employee's name
   * @param endTime End time
   */
  @Override
  public void setEmployeeEndTime(String name, LocalTime endTime) {
    this.employeeHashMap.get(name).setEndTime(endTime);
  }

  /**
   * Gets an employee's supervisor
   *
   * @param name Employee's name
   * @return Supervisor
   */
  @Override
  public Employee getEmployeeSupervisor(String name) {
    return this.employeeHashMap.get(name).getSupervisor();
  }

  /**
   * Sets an employee's supervisor
   *
   * @param name Employee's name
   * @param supervisor Supervisor
   */
  @Override
  public void setEmployeeSupervisor(String name, Employee supervisor) {
    this.employeeHashMap.get(name).setSupervisor(supervisor);
  }

  /**
   * Gets all supervisees for an employee
   *
   * @param name Employee's name
   * @return All supervisees for an employee
   */
  @Override
  public ArrayList<Employee> getEmployeeSupervisees(String name) {
    return this.employeeHashMap.get(name).getSupervisees();
  }

  /**
   * Sets all supervisees for an employee
   *
   * @param name Employee's name
   * @param supervisees All supervisees for an employee
   */
  @Override
  public void setEmployeeSupervisees(String name, ArrayList<Employee> supervisees) {
    this.employeeHashMap.get(name).setSupervisees(supervisees);
  }

  /**
   * Adds a supervisee to an employee's list of supervisees
   *
   * @param name Employee's name
   * @param supervisee Supervisee
   */
  @Override
  public void addEmployeeSupervisee(String name, Employee supervisee) {
    this.employeeHashMap.get(name).addSupervisee(supervisee);
  }

  /**
   * Remove a supervisee from an employee's list of supervisees
   *
   * @param name Employee's name
   * @param superviseeName Supervisee name
   */
  @Override
  public void removeEmployeeSupervisee(String name, String superviseeName) {
    this.employeeHashMap.get(name).removeSupervisee(superviseeName);
  }

  // Assumptions: CSV file is written from admin level down
  public void readCSV() {
    String line = "";
    String splitBy = ",";

    try {
      BufferedReader br = new BufferedReader(new FileReader("./Employees.csv"));
      boolean OnHeader = false;
      line.split(splitBy);

      // Do not read the supervisees from the CSV anymore
      while ((line = br.readLine()) != null) {
        if (OnHeader) {
          String[] values = line.split(splitBy);
          ++this.count;
          LocalTime start = LocalTime.parse(values[5]);
          LocalTime end = LocalTime.parse(values[6]);
          Employee supervisor = null;
          if (!values[0].equals("Wong") && values.length > 7)
            supervisor = this.employeeHashMap.get(values[7]);
          Employee employee =
              new Employee(
                  values[0],
                  values[1],
                  values[2],
                  Integer.parseInt(values[3]),
                  values[4],
                  start,
                  end,
                  supervisor,
                  new ArrayList<Employee>());
          this.employeeHashMap.put(values[0], employee);
          if (supervisor != null) {
            this.employeeHashMap.get(supervisor.getName()).addSupervisee(employee);
            Adb.updateEmployee(this.employeeHashMap.get(supervisor.getName()));
          }
          Adb.addEmployee(employee);

        } else {
          OnHeader = true;
        }
      }
    } catch (IOException var7) {
      var7.printStackTrace();
    }
  }
}

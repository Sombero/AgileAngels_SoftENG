package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.Employee;
import java.util.LinkedList;

public class Condition {
  LinkedList<Employee> employees;
  LinkedList<String> treatments;

  public Condition(LinkedList<Employee> employees, LinkedList<String> treatments) {
    this.employees = employees;
    this.treatments = treatments;
  }
}

package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MethodTestClass {
  int timesDanLaughs;

  public MethodTestClass(int timesDanLaughs) {
    this.timesDanLaughs = timesDanLaughs;
  }

  public ObservableList<Request> filterReqEmployeeNoMedData(
      String employeeName, ObservableList<Request> listChecked) {
    ObservableList<Request> newList = FXCollections.observableArrayList();

    for (Request req : listChecked) {
      if (req.getEmployee().getName().equals(employeeName)) {
        newList.add(req);
      }
    }
    return newList;
  }
}

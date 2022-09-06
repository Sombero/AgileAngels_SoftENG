package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.Employee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MedAidController implements Initializable {
  @FXML AnchorPane anchor;
  public Pane treatment1, treatment2, treatment3;
  public TextField Condition;
  public Label treatmentLabel1, treatmentLabel2, treatmentLabel3;
  public TableView MedAidTable;
  public TableColumn availableColumn, employeeColumn;
  public ImageView icon1, icon2, icon3;
  public EmployeeManager empDAO = EmployeeManager.getInstance();

  AppController appController = AppController.getInstance();
  private static ObservableList<MedicalStaff> empList = FXCollections.observableArrayList();

  private void MedAidViewController() {}

  LinkedList<Employee> employees = new LinkedList<Employee>();
  LinkedList<String> treatments = new LinkedList<String>();
  HashMap<String, Condition> data = new HashMap<String, Condition>();
  // Condition condition = new Condition(employees, treatments);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scan(data);
    treatment1.setVisible(false);
    treatment2.setVisible(false);
    treatment3.setVisible(false);
    employeeColumn.setCellValueFactory(new PropertyValueFactory<MedicalStaff, String>("Employee"));
    availableColumn.setCellValueFactory(
        new PropertyValueFactory<MedicalStaff, String>("Available"));
    sortEmployees();
    setColor(appController.color);
  }

  public void submit(ActionEvent actionEvent) {

    String answer = Condition.getText();

    Condition con = data.get(answer);
    // System.out.println(con);

    //  System.out.println("answer " + con.treatments.size());
    int size = con.treatments.size();
    int count = 0;
    for (int i = 0; i < size; i++) {
      if (con.treatments.get(i).equals("N/A")) count++;
    }
    size = size - count;
    //  System.out.println("size after " + size);
    clearAlso();
    if (size == 1) {
      displayIcon(con.treatments.get(0), 1);
      treatment1.setVisible(true);
      treatmentLabel1.setText(con.treatments.get(0));
    } else if (size == 2) {
      displayIcon(con.treatments.get(0), 1);
      displayIcon(con.treatments.get(1), 2);
      treatment1.setVisible(true);
      treatmentLabel1.setText(con.treatments.get(0));
      treatment2.setVisible(true);
      treatmentLabel2.setText(con.treatments.get(1));
    } else {
      displayIcon(con.treatments.get(0), 1);
      displayIcon(con.treatments.get(1), 2);
      displayIcon(con.treatments.get(2), 3);

      treatment1.setVisible(true);
      treatmentLabel1.setText(con.treatments.get(0));
      treatment2.setVisible(true);
      treatmentLabel2.setText(con.treatments.get(1));
      treatment3.setVisible(true);
      treatmentLabel3.setText(con.treatments.get(2));
    }
    System.out.println("Before populate");
    populateTable(answer);
    System.out.println("After populate");
    Condition.setText(answer);
  }

  public void clear(ActionEvent actionEvent) {
    clearAlso();
  }

  public void clearAlso() {
    treatment1.setVisible(false);
    treatment2.setVisible(false);
    treatment3.setVisible(false);
    empList.clear();
    Condition.setText("");
  }

  private void populateTable(String condition) {

    System.out.println(condition);
    if (condition.equals("Cancer")) {
      for (int i = 0; i < Radiology.size(); i++) {
        MedicalStaff a =
            new MedicalStaff(Radiology.get(i).getName(), Radiology.get(i).getDepartment());
        System.out.println(i);
        empList.add(a);
      }
      for (int i = 0; i < Oncology.size(); i++) {
        MedicalStaff a =
            new MedicalStaff(Oncology.get(i).getName(), Oncology.get(i).getDepartment());
        System.out.println(i);
        empList.add(a);
      }
    } else if (condition.equals("Diabetes")) {
      for (int i = 0; i < Nursing.size(); i++) {
        MedicalStaff a = new MedicalStaff(Nursing.get(i).getName(), Nursing.get(i).getDepartment());

        empList.add(a);
      }
    } else if (condition.equals("Anxiety") || condition.equals("Depression")) {
      for (int i = 0; i < Neurology.size(); i++) {
        MedicalStaff a =
            new MedicalStaff(Neurology.get(i).getName(), Neurology.get(i).getDepartment());

        empList.add(a);
      }
      for (int i = 0; i < Nursing.size(); i++) {
        MedicalStaff a = new MedicalStaff(Nursing.get(i).getName(), Nursing.get(i).getDepartment());

        empList.add(a);
      }
    } else if (condition.equals("Influenza")
        || condition.equals("Pneumonia")
        || condition.equals("Covid-19")
        || condition.equals("Mononucleosis")) {
      for (int i = 0; i < Nursing.size(); i++) {
        MedicalStaff a = new MedicalStaff(Nursing.get(i).getName(), Nursing.get(i).getDepartment());

        empList.add(a);
      }
    } else if (condition.equals("Heart Failure") || condition.equals("Accidents"))
      for (int i = 0; i < Surgery.size(); i++) {
        MedicalStaff a = new MedicalStaff(Surgery.get(i).getName(), Surgery.get(i).getDepartment());

        empList.add(a);
      }
    MedAidTable.setItems(empList);
  }

  private void scan(HashMap<String, Condition> data) {
    String line = "";
    String splitBy = ",";

    try {
      BufferedReader br = new BufferedReader(new FileReader("MedAid.csv"));
      boolean OnHeader = false;
      line.split(splitBy);

      while ((line = br.readLine()) != null) {
        if (OnHeader) {
          String[] values = line.split(splitBy);
          // System.out.println("NUM ENTRIES " + values.length);
          data.put(values[0], new Condition(new LinkedList<>(), new LinkedList<>()));
          data.get(values[0].toString()).treatments.add(values[1]);
          data.get(values[0].toString()).treatments.add(values[2]);
          data.get(values[0].toString()).treatments.add(values[3]);
          // System.out.println(data.get(values[0]).treatments.get(1));
        } else {
          OnHeader = true;
        }
      }
      setEmployees(empDAO);
    } catch (IOException var7) {
      var7.printStackTrace();
    }
  }

  public void setEmployees(EmployeeManager empDAO) {
    sortEmployees();
    HashMap e = empDAO.getAllEmployees();
    for (int i = 0; i < data.size(); i++) {
      Condition c = data.get(i);
      Iterator it = empDAO.getAllEmployees().entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        // System.out.println(pair.getKey() + " = " + pair.getValue());
        String s = "" + pair.getValue();

        // System.out.println(empDAO.getEmployee(s));
        employees.add(empDAO.getEmployee(s));
        it.remove(); // avoids a ConcurrentModificationException
      }
    }
  }

  private void displayIcon(String treatment, int iconNum) {

    //  System.out.println("LOOK HERE " + treatment + " " + iconNum);

    // pills
    Image im1 =
        new Image(
            "https://cdn3.vectorstock.com/i/thumb-large/37/32/medicine-icon-in-on-white-background-vector-30683732.jpg");
    // bed
    Image im2 = new Image("http://simpleicon.com/wp-content/uploads/rest.png");
    // Hydration
    Image im3 = new Image("https://freeiconshop.com/wp-content/uploads/edd/water-bottle-solid.png");
    // Surgery
    Image im4 = new Image("https://cdn-icons-png.flaticon.com/512/3030/3030898.png");
    // Therapy
    Image im5 = new Image("https://icon-library.com/images/therapy-icon/therapy-icon-6.jpg");
    if (treatment.equals("Medication") || treatment.equals("Antibiotics")) {
      if (iconNum == 1) icon1.setImage(im1);
      else if (iconNum == 2) icon2.setImage(im1);
      else if (iconNum == 3) icon3.setImage(im1);
    } else if (treatment.equals("Rest")) {
      if (iconNum == 1) icon1.setImage(im2);
      else if (iconNum == 2) icon2.setImage(im2);
      else if (iconNum == 3) icon3.setImage(im2);
    } else if (treatment.equals("Drink Water") || treatment.equals("Hydrate")) {
      if (iconNum == 1) icon1.setImage(im3);
      else if (iconNum == 2) icon2.setImage(im3);
      else if (iconNum == 3) icon3.setImage(im3);
    } else if (treatment.equals("Surgery")) {
      if (iconNum == 1) icon1.setImage(im4);
      else if (iconNum == 2) icon2.setImage(im4);
      else if (iconNum == 3) icon3.setImage(im4);
    } else {
      if (iconNum == 1) icon1.setImage(im5);
      else if (iconNum == 2) icon2.setImage(im5);
      else if (iconNum == 3) icon3.setImage(im5);
    }
  }

  LinkedList<Employee> Management = new LinkedList<Employee>();
  LinkedList<Employee> Nursing = new LinkedList<Employee>();
  LinkedList<Employee> Oncology = new LinkedList<Employee>();
  LinkedList<Employee> Radiology = new LinkedList<Employee>();
  LinkedList<Employee> Neurology = new LinkedList<Employee>();
  LinkedList<Employee> Surgery = new LinkedList<Employee>();
  LinkedList<Employee> Sanitation = new LinkedList<Employee>();

  private void sortEmployees() {
    for (int i = 0; i < employees.size(); i++) {
      Employee e = employees.get(i);
      if (e.getDepartment().equals("man")) Management.add(e);
      if (e.getDepartment().equals("Nursing")) Nursing.add(e);
      if (e.getDepartment().equals("Oncology")) Oncology.add(e);
      if (e.getDepartment().equals("Radiology")) Radiology.add(e);
      if (e.getDepartment().equals("Neurology")) Neurology.add(e);
      if (e.getDepartment().equals("Surgery")) Surgery.add(e);
      if (e.getDepartment().equals("Sanitation")) Sanitation.add(e);
    }
  }

  public void setColor(String color) {
    if (color.equals("green")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestGreenTest.css");
    } else if (color.equals("red")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestRedTest.css");

    } else if (color.equals("blue")) {
      anchor.getStylesheets().removeAll();
      anchor.getStylesheets().add("/edu/wpi/agileAngels/views/stylesheets/styleRequest.css");

    } else if (color.equals("purple")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestPurpleTest.css");
    } else if (color.equals("yellow")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestYellowTest.css");
    }
  }
}

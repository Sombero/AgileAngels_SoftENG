package edu.wpi.agileAngels.Database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// export to the CSV file
// TODO generalize this and move to IMPL class
public class MedExportToCSV {
  // TODO: gemeralize export and import
  public void export(Connection connection) {

    String csvFilePath = "Medexport.csv";

    try {

      String sql = "SELECT * FROM MedicalEquipment";

      Statement statement = connection.createStatement();

      ResultSet result = statement.executeQuery(sql);

      BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));

      // write header line containing column names
      fileWriter.write("nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName");

      while (result.next()) {
        String name = result.getString("name");
        String available = result.getString("available");
        String type = result.getString("type");
        String location = result.getString("location");
        String employee = result.getString("employee");
        String status = result.getString("status");
        String description = result.getString("description");

        String line =
            String.format(
                "%s,%s,%s, %s, %s, %s, %s",
                name, available, type, location, employee, status, description);

        fileWriter.newLine();
        fileWriter.write(line);
      }

      statement.close();
      fileWriter.close();

    } catch (SQLException e) {
      System.out.println("Datababse error:");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("File IO error:");
      e.printStackTrace();
    }
  }
}

package edu.wpi.agileAngels.Database;

import java.util.HashMap;

public interface RequestDAO {

  public HashMap<String, Request> getAllRequests();

  public void updateEmployeeName(Request request, String newName);

  public void updateRequestType(Request request, int requestType);

  public void updateType(Request request, String newType);

  public void updateLocation(Request request, Location location);

  public void updateDescription(Request request, String description);

  public void updateStatus(Request request, String newStatus);

  public void deleteRequest(Request request);

  public void addRequest(Request request);
}

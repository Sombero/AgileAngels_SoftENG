package edu.wpi.agileAngels.Database;

public class Gift {
  private final String ID;
  private String sender;
  private String recipient;
  private boolean status;
  private String type;
  private String message;
  private String location;

  public Gift(
      String ID,
      String sender,
      String recipient,
      boolean status,
      String type,
      String message,
      String location) {
    this.ID = ID;
    this.sender = sender;
    this.recipient = recipient;
    this.status = status;
    this.type = type;
    this.message = message;
    this.location = location;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getID() {
    return ID;
  }
}

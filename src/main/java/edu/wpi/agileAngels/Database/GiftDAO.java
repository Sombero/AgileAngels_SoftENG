package edu.wpi.agileAngels.Database;

import java.util.HashMap;

public interface GiftDAO {

  public HashMap<String, Gift> getAllGiftRequests();

  public void addGift(Gift gift);

  public void removeGift(Gift gift);
}

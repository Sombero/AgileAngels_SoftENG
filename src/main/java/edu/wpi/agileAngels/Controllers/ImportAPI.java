package edu.wpi.agileAngels.Controllers;

import edu.wpi.teamW.*;

public class ImportAPI {

  public void LanguageInterp() throws ServiceException {
    edu.wpi.teamW.API.run(
        201,
        188,
        900,
        900,
        "/edu/wpi/agileAngels/views/stylesheets/style.css",
        "FDEPT00101",
        "FDEPT00201");
  }

  public void externalTrans() throws edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException {
    edu.wpi.cs3733.D22.teamZ.api.API api = new edu.wpi.cs3733.D22.teamZ.api.API();
    api.run(
        340,
        350,
        900,
        900,
        "edu/wpi/agileAngels/views/stylesheets/style.css",
        "CDEPT003L1",
        "HHALL01503");
  }
}

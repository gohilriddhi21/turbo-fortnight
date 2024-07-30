import javax.servlet.http.HttpServletResponse;

public class SkierService implements ResponseHandler{
  private final SkiersCache dataCache;
  private final DatabaseHandler databaseHandler;

  public SkierService() {
    this.databaseHandler = DatabaseHandler.getInstance();
    System.out.println(Constants.DATABASE_CON_EST);
    this.dataCache = SkiersCache.getInstance();
    System.out.println(Constants.CACHE_EST);
  }

  public void processRequest(HttpServletResponse res, int resortID, String seasonID, String dayID, int skierID) {
    try {
      int skierDayVerticalCache = dataCache.getSkierDayVertical(resortID, seasonID, dayID,skierID);
      if (skierDayVerticalCache > 0) {
        sendSuccessResponse(res, skierDayVerticalCache);
        System.out.println("Sent Data from Cache." + skierDayVerticalCache);
      } else
      {
        int skierDayVerticalDB = databaseHandler.getSkierDayVertical(resortID, seasonID, dayID, skierID);
        if (skierDayVerticalDB >= 0) {
          dataCache.cacheResult(resortID, seasonID, dayID, skierID,skierDayVerticalDB);
          sendSuccessResponse(res, skierDayVerticalDB);
        } else {
          sendErrorResponse(res, HttpServletResponse.SC_NOT_FOUND, Constants.MSG_DATA_NOT_FOUND);
        }
      }
    } catch (Exception e) {
      handleException(res, e);
    }
  }
}

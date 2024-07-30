import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletResponse;

public class ResortService implements ResponseHandler{
  private final SkiersCache dataCache;
  private final DatabaseHandler databaseHandler;
  public ResortService() {
    this.databaseHandler = DatabaseHandler.getInstance();
    System.out.println(Constants.DATABASE_CON_EST);
    this.dataCache = SkiersCache.getInstance();
    System.out.println(Constants.CACHE_EST);
  }

  public void processRequest(HttpServletResponse res, int resortID, String seasonID, String dayID) {
      try {
        int cachedNoOfSkiers = dataCache.getNoOfSkiers(resortID, seasonID, dayID);
        if (cachedNoOfSkiers > 0) {
          sendSuccessResponse(res, Constants.TIME_VALUE, cachedNoOfSkiers);
        } else {
          int noOfSkiers = databaseHandler.getUniqueSkiers(resortID, seasonID, dayID);
          if (noOfSkiers > 0) {
            dataCache.cacheResult(resortID, seasonID, dayID, noOfSkiers);
            sendSuccessResponse(res, Constants.TIME_VALUE, noOfSkiers);
          } else {
            sendErrorResponse(res, HttpServletResponse.SC_NOT_FOUND, Constants.MSG_DATA_NOT_FOUND);
          }
        }
      } catch (Exception e) {
          handleException(res, e);
      }
  }
}

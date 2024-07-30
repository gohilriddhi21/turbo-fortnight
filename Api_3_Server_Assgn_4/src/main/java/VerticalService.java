import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

public class VerticalService implements ResponseHandler {
  private final SkiersCache dataCache;
  private final DatabaseHandler databaseHandler;

  public VerticalService() {
    this.databaseHandler = DatabaseHandler.getInstance();
    System.out.println(Constants.DATABASE_CON_EST);
    this.dataCache = SkiersCache.getInstance();
    System.out.println(Constants.CACHE_EST);
  }

  public void processRequest(HttpServletResponse res, int skierID, int resortID, List<String> seasons) {
    try {
      String skierTotalVerticalCache = dataCache.getSkierTotalVertical(skierID, resortID, seasons);
      if (skierTotalVerticalCache != null) {
        sendSuccessResponse(res, skierTotalVerticalCache);
        System.out.println("Sent Data from Cache." + skierTotalVerticalCache);
      } else
      {
        List<Map<String, String>> allRecords = databaseHandler.getRecordsByRS(skierID, resortID, seasons);
        if(!allRecords.isEmpty()) {
          List<SkierVerticalResort> finalValues = calculateTotalVerticalBySeason(allRecords);
          if(!finalValues.isEmpty()) {
            String value = constructResortsJSON(finalValues);
            dataCache.cacheResult(skierID, resortID, seasons, value);
            System.out.println("Updated Cache.");
            sendSuccessResponse(res, value);
            System.out.println("Sent Data from DB.");
            return;
          }
        }
        sendErrorResponse(res, HttpServletResponse.SC_NOT_FOUND, Constants.MSG_DATA_NOT_FOUND);
      }
    } catch (Exception e){
      handleException(res, e);
    }
  }

  public static List<SkierVerticalResort> calculateTotalVerticalBySeason(List<Map<String, String>> records) {
    Map<String, Integer> totalVerticalBySeason = new HashMap<>();
    for (Map<String, String> record : records) {
      record.forEach((seasonID, liftID) -> totalVerticalBySeason.merge(seasonID, Integer.parseInt(liftID) * 10, Integer::sum));
    }
    List<SkierVerticalResort> resultList = new ArrayList<>();
    totalVerticalBySeason.forEach((seasonID, totalVertical) -> resultList.add(new SkierVerticalResort(Integer.parseInt(seasonID), totalVertical)));
    return resultList;
  }

}

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public interface ResponseHandler {

  default void sendErrorResponse(HttpServletResponse res, int statusCode, String message) throws IOException {
    res.setStatus(statusCode);
    res.setContentType("application/json");
    res.getWriter().write("{\"message\": \"" + message + "\"}");
    System.out.println("Error Response Sent.");
  }

  default void sendSuccessResponse(HttpServletResponse res, String value) throws IOException {
    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(value);
    System.out.println("Success Response Sent.");
  }

  default void handleException(HttpServletResponse res, Exception e) {
    e.printStackTrace();
    try {
      sendErrorResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  default String constructResortsJSON(List<SkierVerticalResort> resortsList) {
    Map<String, Object> jsonMap = new HashMap<>();
    List<Map<String, Object>> resortsJsonList = new ArrayList<>();

    for (SkierVerticalResort resort : resortsList) {
      Map<String, Object> resortMap = new HashMap<>();
      resortMap.put("seasonID", resort.getSeasonID());
      resortMap.put("totalVert", resort.getTotalVertical());
      resortsJsonList.add(resortMap);
    }

    jsonMap.put("resorts", resortsJsonList);
    return new Gson().toJson(jsonMap);
  }

}

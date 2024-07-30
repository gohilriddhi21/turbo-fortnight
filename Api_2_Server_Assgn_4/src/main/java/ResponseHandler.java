import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public interface ResponseHandler {

  default void sendErrorResponse(HttpServletResponse res, int statusCode, String message) throws IOException {
    res.setStatus(statusCode);
    res.setContentType("application/json");
    res.getWriter().write("{\"message\": \"" + message + "\"}");
    System.out.println("Error Response Sent.");
  }

  default void sendSuccessResponse(HttpServletResponse res, int totalVertical) throws IOException {
    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    JSONObject message = new JSONObject();
    message.put("TotalVertical", totalVertical);
    res.getWriter().write(message.toString());
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

}

import java.util.concurrent.Future;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebServlet("/skiers/*")
public class SkierDayVerticalServlet extends HttpServlet implements ResponseHandler {
  SkierService service;
  ExecutorService executor;

  @Override
  public void init(ServletConfig config) {
    try {
      super.init(config);
      service = new SkierService();
      this.executor = Executors.newFixedThreadPool(1000);
      System.out.println(Constants.SERVLET_EST);
    } catch (Exception e) {
      System.out.println(Constants.SERVLET_EST_ERROR);
      e.printStackTrace();
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res){
    try {
      String urlPath = req.getPathInfo();

      if (urlPath == null || urlPath.isEmpty() || !isUrlValid(urlPath)) {
        sendErrorResponse(res, HttpServletResponse.SC_BAD_REQUEST, Constants.MSG_INVALID_INPUTS);
        return;
      }

      String[] urlParts = urlPath.split(Constants.URL_DELIMITER);
      int resortID = Integer.parseInt(urlParts[1]);
      String seasonID = urlParts[3];
      String dayID = urlParts[5];
      int skierID = Integer.parseInt(urlParts[7]);

      Future<?> future = executor.submit(() -> {
        try {
            service.processRequest(res,resortID, seasonID, dayID, skierID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
      });
      future.get();
    } catch (Exception e) {
        handleException(res, e);
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    sendErrorResponse(res, HttpServletResponse.SC_METHOD_NOT_ALLOWED, Constants.MSG_METHOD_NOT_ALLOWED);
  }

  private boolean isUrlValid(String urlPath) {
    Pattern p = Pattern.compile(Constants.SKIERS_DAY_VERTICAL_URL_PATTERN);
    Matcher m = p.matcher(urlPath);
    return m.matches();
  }

  @Override
  public void destroy() {
    executor.shutdown();
  }
}


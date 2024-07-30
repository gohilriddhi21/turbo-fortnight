import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/skiers/*")
public class SkierTotalDayVertical extends HttpServlet implements ResponseHandler{
  VerticalService service;

  @Override
  public void init(ServletConfig config) {
    try {
      super.init(config);
      service = new VerticalService();
      System.out.println(Constants.SERVLET_EST);
    } catch (Exception e) {
      System.out.println(Constants.SERVLET_EST_ERROR);
      e.printStackTrace();
    }
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) {
    try {
      String urlPath = req.getPathInfo();

      if (urlPath == null || urlPath.isEmpty() || !isUrlValid(urlPath)) {
        sendErrorResponse(res, HttpServletResponse.SC_BAD_REQUEST, Constants.MSG_INVALID_INPUTS);
        return;
      }

      String[] urlParts = urlPath.split(Constants.URL_DELIMITER);
      String[] resortParam = {};
      String[] seasonParam = {};
      int resortID = 0,skierID=0;

      try {
        skierID = Integer.parseInt(urlParts[1]);
        resortParam = req.getParameterValues(Constants.PARAM_1);
        if (resortParam == null) {
          sendErrorResponse(res, HttpServletResponse.SC_BAD_REQUEST, Constants.MSG_INVALID_INPUTS);
          return;
        } else {
          resortID = Integer.parseInt(resortParam[0]);
        }
      } catch (Exception e) {
          handleException(res, e);
          return;
      }

      List<String> seasons = new ArrayList<>();
      seasonParam = req.getParameterValues(Constants.PARAM_2);
      if (seasonParam != null) {
        seasons = Arrays.asList(seasonParam);
      }

      service.processRequest(res, skierID, resortID, seasons);
    } catch (Exception e) {
        handleException(res, e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    sendErrorResponse(res, HttpServletResponse.SC_METHOD_NOT_ALLOWED, Constants.MSG_METHOD_NOT_ALLOWED);
  }

  private boolean isUrlValid(String urlPath) {
    Pattern p = Pattern.compile(Constants.SKIERS_TOTAL_VERTICAL_URL_PATTERN);
    Matcher m = p.matcher(urlPath);
    return m.matches();
  }

}


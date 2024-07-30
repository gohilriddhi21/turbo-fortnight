import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/resorts/*")
public class UniqueSkiersByRSD extends HttpServlet implements ResponseHandler {
  ResortService service;
  int corePoolSize = 128;
  int maxPoolSize = 128;
  BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
  ThreadPoolExecutor executor;


  @Override
  public void init(ServletConfig config) {
    try {
      super.init(config);
      service = new ResortService();
//      this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0L, TimeUnit.SECONDS, new SynchronousQueue<>(), new CallerRunsPolicy());
      this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, queue);
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
      int resortID = Integer.parseInt(urlParts[1]);
      String seasonID = urlParts[3];
      String dayID = urlParts[5];

      Future<?> future = executor.submit(() -> {
        try {
          service.processRequest(res, resortID, seasonID, dayID);
        } catch (Exception e) {
          throw new RuntimeException(e);
        } finally {
          try {
            res.getWriter().close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
      future.get();
    } catch (Exception e) {
      handleException(res, e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    sendErrorResponse(res, HttpServletResponse.SC_METHOD_NOT_ALLOWED, Constants.MSG_METHOD_NOT_ALLOWED);
  }
  private boolean isUrlValid(String urlPath) {
    Pattern p = Pattern.compile(Constants.UNIQUE_SKIERS_URL_PATTERN);
    Matcher m = p.matcher(urlPath);
    return m.matches();
  }

  @Override
  public void destroy() {
    executor.shutdown();
  }
}


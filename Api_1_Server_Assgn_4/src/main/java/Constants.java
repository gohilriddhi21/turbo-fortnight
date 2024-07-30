public class Constants {

  // URL Error Messages
  public static final String MSG_METHOD_NOT_ALLOWED = "POST Method not allowed. Use GET method instead.";
  public static final String MSG_DATA_NOT_FOUND = "Data not found";
  public static final String MSG_INVALID_INPUTS = "Invalid Inputs Provided";

  // Servlet Messages
  public static final String SERVLET_EST = "Servlet initialized.";
  public static final String SERVLET_EST_ERROR = "Could Not Initialize Servlet.";

  // Cache Messages
  public static final String CACHE_EST = "CACHE Connection established.";
  public static final String CACHE_CONNECTION_ERROR_MESSAGE = "Error connecting to Redis Cache";
  public static final String ERROR_INSERT_CACHE = "Could not insert data into Cache.";
  public static final String CACHE_FETCH_ERROR_MESSAGE = "Could not extract data from Redis";

  //DB Messages
  public static final String DATABASE_CON_EST = "Database Connection established.";
  public static final String DB_CONNECTION_ERROR_MESSAGE = "Error connecting to Database";
  public static final String DB_FETCH_ERROR_MESSAGE = "Could not extract data from DynamoDB";

  //
  public static final String UNIQUE_SKIERS_URL_PATTERN = "/\\d+/seasons/\\d+/day/\\d+/skiers";
  public static final String TIME_VALUE = "Mission Ridge";
  public static final String URL_DELIMITER = "/";

  // Cache Redis Credentials
  public static final String REDIS_HOST = "32.188.10.60";
  public static final Integer REDIS_PORT = 6379;

  // AWS DynamoDB Credentials
  public static final String TABLE_NAME = "SkierLiftRidesData_Final";
  public static final String INDEX_NAME = "resortID-dayID-index";
  public static final String AWS_ACCESS_KEY = "ASIA2UC3BCS5QNDDVACL";
  public static final String AWS_SECRET_KEY = "njffor6JCfDmJ7S1Wc/FRswu6LMSeEI8RPDHlFBc";
  public static final String AWS_SESSION_TOKEN = "IQoJb3JpZ2luX2VjEJ3//////////wEaCXVzLXdlc3QtMiJIMEYCIQDPc7e0U8K5eaI2rkcfuKK1mV/ZE/IPjq+c7qnhH0XsxgIhALnngSuOuVFfG0rMUJqCoAyEGVD8yzlgZe33C3uHsD0kKrsCCNb//////////wEQABoMNzMwMzM1MzUxOTk1IgzfByiCHeKNpi65/y0qjwJZF2PGORkVtc+1+297TNacDe2HjAUzpp/gHNfEg9qm1imVsIpBIq6A1Qj8dBxMFiSwzgeYQ6RwlZCM4hg0gMviq0U0K08u0RmgGe+0vrUDWFVVTO153ZH+WWLbFdHTYwSsJJYQM9eepa0vEsVUGdvELj+LBAhFu8/Obr2e1rhgtFb2jQbEVOs7fZkhnmp6dz2jVgOY4tYstZOIAqx4Sn7xyDNyfcnH9+z5KKylXHJTfAC3ZWNeRPjWYz+Pv81jr0wfJm+Dd6Jok8dOtUMfyGP+UGghym4Xq5K8sjNENMPlW11/5C6ZU4sN1euo5i5UlwH1wpoa7+rqMnHD+loAc+NqSafDORqsW6K9nDR1jpcfMLiQ/7AGOpwBxD9wxL+ibneDj+O0W8/jMjyadM+ZAoKeVk8YqPBW8r9Q06L8sZ3BBszlr+r0SrduGBwtrF2C6rMEqo5DIU97VDOYzIaUFOtUsRI5B/wZyl9iMWcpxULxsvs2/mDtaREFNdGRwCYOAal1gQW1sS09oz8LD1BsuCIYMq9az+nZS/iiQQj/OKzXuWvSN2HgTmAuGTsS1LKdaKilTaiF";
  public static final String AWS_REGION = "us-west-2";
}

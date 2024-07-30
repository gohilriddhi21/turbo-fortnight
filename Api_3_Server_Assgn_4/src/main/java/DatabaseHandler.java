import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class DatabaseHandler {
  private static DatabaseHandler instance;
  private static DynamoDbClient dynamoDbClient;

  private DatabaseHandler() {
    try {
      AwsSessionCredentials awsCreds = AwsSessionCredentials.create(Constants.AWS_ACCESS_KEY, Constants.AWS_SECRET_KEY,
          Constants.AWS_SESSION_TOKEN);

      dynamoDbClient = DynamoDbClient.builder()
          .region(Region.of(Constants.AWS_REGION))
          .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
          .build();

    } catch (Exception e) {
      throw new RuntimeException(Constants.DB_CONNECTION_ERROR_MESSAGE, e);
    }
  }

  public static synchronized DatabaseHandler getInstance() {
    if (instance == null) {
      instance = new DatabaseHandler();
    }
    return instance;
  }

  public List<Map<String, String>> getRecordsByRS(int skierID, int resortID, List<String> seasons) {
    List<Map<String, String>> resultMapList = new ArrayList<>();
    try {

      for (String season : seasons) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(season, "0");
        resultMapList.add(resultMap);
      }

      Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
      expressionAttributeValues.put(":skierId", AttributeValue.builder().n(String.valueOf(skierID)).build());
      expressionAttributeValues.put(":resortId", AttributeValue.builder().n(String.valueOf(resortID)).build());

      QueryRequest queryRequest = QueryRequest.builder()
          .tableName(Constants.TABLE_NAME)
          .indexName(Constants.INDEX_NAME)
          .keyConditionExpression("skierID = :skierId AND resortID = :resortId")
          .expressionAttributeValues(expressionAttributeValues)
          .build();

      QueryResponse response = dynamoDbClient.query(queryRequest);
      List<Map<String, AttributeValue>> items = response.items();
      Set<String> seasonSet = new HashSet<>(seasons);


      for (Map<String, AttributeValue> item : items) {
        String seasonID = item.get("seasonID").n();
        if (seasons.isEmpty() || seasonSet.contains(seasonID)) {
          String liftID = item.get("liftID").n();
          boolean seasonFound = false;
          for (Map<String, String> resultMap : resultMapList) {
            if (resultMap.containsKey(seasonID)) {
              seasonFound = true;
              int previousLiftIDInt = Integer.parseInt(resultMap.get(seasonID));
              int currentLiftIDInt = Integer.parseInt(liftID);
              resultMap.put(seasonID, String.valueOf(previousLiftIDInt + currentLiftIDInt));
              break;
            }
          }
          if (!seasonFound) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(seasonID, liftID);
            resultMapList.add(resultMap);
          }
        }
      }
    } catch (DynamoDbException e) {
      throw new RuntimeException(Constants.DB_FETCH_ERROR_MESSAGE + e);
    }
    return resultMapList;
  }

}

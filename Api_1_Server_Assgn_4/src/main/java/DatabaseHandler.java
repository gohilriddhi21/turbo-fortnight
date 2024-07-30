import java.util.Map;
import java.util.Set;
import java.util.HashSet;
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

  public int getUniqueSkiers(int resortID, String seasonID, String dayID) {
    int count;
    Set<String> uniqueSkierIDs = new HashSet<>();
    try {
      QueryRequest queryRequest = QueryRequest.builder()
          .tableName(Constants.TABLE_NAME)
          .indexName(Constants.INDEX_NAME)
          .keyConditionExpression("resortID = :resortId AND dayID = :dayId")
          .filterExpression("seasonID = :seasonId")
          .expressionAttributeValues(Map.of(":resortId", AttributeValue.builder().n(String.valueOf(resortID)).build(),
              ":seasonId",AttributeValue.builder().n(seasonID).build(),
              ":dayId", AttributeValue.builder().s(dayID).build()
          ))
          .build();

      QueryResponse response = dynamoDbClient.query(queryRequest);
      if(!response.items().isEmpty()) {
        for (Map<String, AttributeValue> item : response.items()) {
          String skierID = item.get("skierID").n();
          uniqueSkierIDs.add(skierID);
        }
        count = uniqueSkierIDs.size();
        System.out.println("NumOfSkiers: " + count);
      }
      else{
        count = 0;
      }
    } catch (DynamoDbException e) {
      throw new RuntimeException(Constants.DB_FETCH_ERROR_MESSAGE + e);
    }
    return count;
  }
}

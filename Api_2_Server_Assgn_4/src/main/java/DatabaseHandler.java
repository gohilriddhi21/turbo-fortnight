import java.util.HashMap;
import java.util.Map;
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

  public int getSkierDayVertical(int resortID, String seasonID, String dayID, int skierID) {
    int totalVertical=0;
    try {
      QueryRequest queryRequest = QueryRequest.builder()
          .tableName(Constants.TABLE_NAME)
          .indexName(Constants.INDEX_NAME)
          .keyConditionExpression("resortID = :resortId AND dayID = :dayId")
          .filterExpression("seasonID = :seasonId AND skierID = :skierId")
          .expressionAttributeValues(Map.of(":resortId", AttributeValue.builder().n(String.valueOf(resortID)).build(),
              ":seasonId",AttributeValue.builder().n(seasonID).build(),
              ":dayId", AttributeValue.builder().s(dayID).build(),
              ":skierId", AttributeValue.builder().n(String.valueOf(skierID)).build()
          ))
          .build();

      QueryResponse response = dynamoDbClient.query(queryRequest);
      if(!response.items().isEmpty())
      {
        for (Map<String, AttributeValue> item : response.items()) {
          int liftID = Integer.parseInt(item.get("liftID").n());
          totalVertical += liftID * 10;
        }
      }
    } catch (DynamoDbException e) {
        throw new RuntimeException(Constants.DB_FETCH_ERROR_MESSAGE + e);
    }
    return totalVertical;
  }
}

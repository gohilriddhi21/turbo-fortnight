import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.List;

public class SkiersCache implements ResponseHandler{
  private static SkiersCache instance;
  private final JedisPool jedisPool;

  private SkiersCache() {
    try {
      jedisPool = new JedisPool(Constants.REDIS_HOST, Constants.REDIS_PORT);
    }catch (Exception e){
      throw new RuntimeException(Constants.CACHE_CONNECTION_ERROR_MESSAGE);
    }
  }

  public static synchronized SkiersCache getInstance() {
    if (instance == null) {
      instance = new SkiersCache();
    }
    return instance;
  }

  private String getCacheKey(int skierId, int resortId, List<String> seasons) {
    String concatenatedSeasons = String.join("", seasons);
    return skierId + "" + resortId + concatenatedSeasons;
  }

  public synchronized String getSkierTotalVertical(int skierId, int resortId, List<String> seasons) {
    String cacheKey = getCacheKey(skierId, resortId, seasons);

    try (Jedis jedis = jedisPool.getResource()) {
      String cachedValue = jedis.get(cacheKey);
      if (cachedValue != null) {
        return cachedValue;
      }
    } catch (Exception e) {
      throw new RuntimeException(Constants.CACHE_FETCH_ERROR_MESSAGE);
    }
    return null;
  }

  public synchronized void cacheResult(int skierId, int resortId,List<String> seasons,String value) {
    try (Jedis jedis = jedisPool.getResource()) {
      String cacheKey = getCacheKey(skierId, resortId, seasons);
      jedis.set(cacheKey, value);
    }catch (Exception e){
      throw new RuntimeException(Constants.ERROR_INSERT_CACHE);
    }
  }
}

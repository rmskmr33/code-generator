import com.aerospike.client.*;
import com.aerospike.client.policy.*;
import com.aerospike.client.record.Record;

public class AerospikeCacheService {

    private static final String NAMESPACE = "test";
    private static final String SET = "cache";
    private static final String BIN_NAME = "value";

    private final AerospikeClient client;

    public AerospikeCacheService(String host, int port) {
        this.client = new AerospikeClient(host, port);
    }

    /**
     * Save key-value data with expiration time (TTL)
     */
    public void saveToCache(String key, String value, int expirationSeconds) {

        WritePolicy writePolicy = new WritePolicy();
        writePolicy.expiration = expirationSeconds; // TTL in seconds

        Key aerospikeKey = new Key(NAMESPACE, SET, key);
        Bin bin = new Bin(BIN_NAME, value);

        client.put(writePolicy, aerospikeKey, bin);
    }

    /**
     * Retrieve data from cache
     * Returns null if record does not exist or is expired
     */
    public String retrieveFromCache(String key) {

        Key aerospikeKey = new Key(NAMESPACE, SET, key);
        Record record = client.get(null, aerospikeKey);

        if (record == null) {
            return null; // Not found or expired
        }

        return record.getString(BIN_NAME);
    }

    /**
     * Close Aerospike connection
     */
    public void close() {
        client.close();
    }

    // Example usage
    public static void main(String[] args) throws InterruptedException {

        AerospikeCacheService cache =
                new AerospikeCacheService("localhost", 3000);

        cache.saveToCache("user123", "John Doe", 5); // expires in 5 seconds

        System.out.println("Value: " + cache.retrieveFromCache("user123"));

        Thread.sleep(6000); // wait for expiry

        System.out.println("After expiry: " + cache.retrieveFromCache("user123"));

        cache.close();
    }
}




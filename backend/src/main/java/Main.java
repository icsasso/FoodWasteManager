import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import io.github.cdimascio.dotenv.Dotenv;


public class Main{

    public static void main(String[] args){
        System.out.println("Hello World");
        getItemsInDB();
    }

    public static MongoClient getConnection(){
        Dotenv dotenv = Dotenv.load();
        String connectionString = dotenv.get("MONGO_URL");
        ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .serverApi(serverApi)
            .build();
        // Create a new client and connect to the server
        try {
            MongoClient mongoClient = MongoClients.create(settings);
            try {
            // Send a ping to confirm a successful connection
            MongoDatabase database = mongoClient.getDatabase("admin");
            database.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            // Return the client for future use
            return mongoClient;
            } catch (MongoException e) {
            mongoClient.close();
            e.printStackTrace();
            return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void getItemsInDB(){
        MongoClient mongoClient = getConnection();

        MongoDatabase database = mongoClient.getDatabase("ingredients");
        MongoCollection<Document> collection = database.getCollection("items");

        FindIterable<Document> documents = collection.find();

            for (Document doc : documents) {
                System.out.println(doc);
            }
        
    }
}
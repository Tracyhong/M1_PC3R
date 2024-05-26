////package org.pc3r.webtoontracker_server.persistence;
////import com.mongodb.ConnectionString;
////import com.mongodb.MongoClientSettings;
////import com.mongodb.MongoException;
////import com.mongodb.ServerApi;
////import com.mongodb.ServerApiVersion;
////import com.mongodb.client.MongoClient;
////import com.mongodb.client.MongoClients;
////import com.mongodb.client.MongoCollection;
////import com.mongodb.client.MongoDatabase;
////import com.mongodb.client.model.Projections;
////import com.mongodb.client.result.InsertOneResult;
////import com.mongodb.client.result.DeleteResult;
////import com.mongodb.client.model.Filters;
////import com.mongodb.client.FindIterable;
////import org.bson.Document;
////import org.bson.conversions.Bson;
////import org.bson.types.ObjectId;
////
////import java.lang.reflect.Array;
////import java.util.Arrays;
////import java.util.List;
////
////import static com.mongodb.client.model.Filters.eq;
////
////public class Database {
////
////
////    public MongoClient mongoClient;
////    private static final String connectionURL = "mongodb+srv://webtoonTracker:projetPC3R@webtoontracker.bqxh8yr.mongodb.net/?retryWrites=true&w=majority&appName=webtoonTracker";
////    private List<User> users;
////
////    private static Database database;   //singleton : only one instance of the database
////
////    static {
////        database = new Database();
////    }
////
////    //private constructor to not allow the creation of a database object outside of the class
////    private Database() {
////         initMongoDBClient();
////        initUsers();
////            //recup les users
////            //recup les webtoons
////            // ...
////
////    }
////
////    public static Database getInstance() {
////        return database;
////    }
////    private void initMongoDBClient(){
////        ServerApi serverApi = ServerApi.builder()
////                .version(ServerApiVersion.V1)
////                .build();
////        MongoClientSettings settings = MongoClientSettings.builder()
////                .applyConnectionString(new ConnectionString(connectionURL))
////                .serverApi(serverApi)
////                .build();
////        try (MongoClient mongoClient = MongoClients.create(settings)) {
////            this.mongoClient = mongoClient;
////        }
////    }
////    private void initUsers() {
////
////        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
////        MongoCollection<Document> collection = database.getCollection("Users");
////        FindIterable<Document> documents = collection.find();
////        for (Document doc : documents) {
////            users.add(new User(doc.getString("_id"),doc.getString("name"), doc.getString("email"), doc.getString("password")));
////        }
////
////    }
////
////    //fonction pour ajouter un user
////    public void addUser(String name, String email, String password) {
////        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
////        MongoCollection<Document> collection = database.getCollection("Users");
////        try {
////            // Inserts a sample document describing a movie into the collection
////            InsertOneResult result = collection.insertOne(new Document()
////                    .append("_id", new ObjectId())
////                    .append("name", name)
////                    .append("email", email)
////                    .append("password", password));    //a changer pour le hash du password ? ou le hash est fait avant l'appel de la fonction ? ou on met comme ca
////            // Prints the ID of the inserted document
////            System.out.println("Success! Inserted document id: " + result.getInsertedId());
////
////            // Prints a message if any exceptions occur during the operation
////        } catch (MongoException me) {
////            System.err.println("Unable to insert due to an error: " + me);
////        }
////    }
////
////    //fonction pour ajouter un webtoon
////    public void addWebtoon(String title, String author, String description) {
////        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
////        MongoCollection<Document> collection = database.getCollection("Webtoons");
////        try {
////            // Inserts a sample document describing a movie into the collection
////            InsertOneResult result = collection.insertOne(new Document()
////                    .append("_id", new ObjectId())
////                    .append("title", title)
////                    .append("author", author)
////                    .append("description", description));
////            //+ autre infos sur le webtoon
////            // Prints the ID of the inserted document
////            System.out.println("Success! Inserted document id: " + result.getInsertedId());
////
////            // Prints a message if any exceptions occur during the operation
////        } catch (MongoException me) {
////            System.err.println("Unable to insert due to an error: " + me);
////        }
////    }
////
////    //fonction pour recuperer les webtoons d'un user
////    //return json ? array ? pour envoyer au front ? peut etre json ou array pour stocker dans user ?
////    public List<Webtoon> getWebtoons(String email) {
////        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
////        MongoCollection<Document> collection = database.getCollection("Users");
////        Document doc = collection.find(eq("email", email)) //a modif avec l'id de l'user
////
////                .first();
////        //recup les webtoons de l'user
////        //...
////        return null;
////    }
////
////    //fonction pour checker si un user existe
////    public boolean userExists(String name, String password) {
////        System.out.println("userExists ?");
////        boolean exists = false;
//////        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//////        MongoCollection<Document> collection = database.getCollection("Users");
//////        //TODO : hash du password
//////
//////        Bson filter = Filters.and(Filters.eq("name", name),Filters.eq("password",password));
//////
//////        Document doc = collection.find(eq(filter))
//////                .first();
//////        if(doc != null){
//////            //TODO : stocker les infos de l'user dans la bd
//////            users.add(new User(doc.getString("_id"),name, doc.getString("email"), password));
//////            exists = true;
//////        }
////        for (User user : users) {
////            if (user.exists(name, password)) {
////                exists = true;
////                break;
////            }
////        }
////        System.out.println("exists : " + exists);
////        return exists;
////    }
////
////
////    public static void main(String[] args) {
////        String connectionString = "mongodb+srv://webtoonTracker:projetPC3R@webtoontracker.bqxh8yr.mongodb.net/?retryWrites=true&w=majority&appName=webtoonTracker";
////        ServerApi serverApi = ServerApi.builder()
////                .version(ServerApiVersion.V1)
////                .build();
////        MongoClientSettings settings = MongoClientSettings.builder()
////                .applyConnectionString(new ConnectionString(connectionString))
////                .serverApi(serverApi)
////                .build();
////        // Create a new client and connect to the server
//////        try (MongoClient mongoClient = MongoClients.create(settings)) {
//////            try {
//////                // Send a ping to confirm a successful connection
//////                MongoDatabase database = mongoClient.getDatabase("admin");
//////                database.runCommand(new Document("ping", 1));
//////                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
//////            } catch (MongoException e) {
//////                e.printStackTrace();
//////            }
////
//////            MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//////            MongoCollection<Document> collection = database.getCollection("Users");
//////            try {
//////                // Inserts a sample document describing a movie into the collection
//////                InsertOneResult result = collection.insertOne(new Document()
//////                        .append("_id", new ObjectId())
//////                        .append("email", "Tracy@mail.fr")
//////                        .append("name", "Tracy")
//////                        .append("password", "aaa"));
//////                // Prints the ID of the inserted document
//////                System.out.println("Success! Inserted document id: " + result.getInsertedId());
//////
//////                // Prints a message if any exceptions occur during the operation
//////            } catch (MongoException me) {
//////                System.err.println("Unable to insert due to an error: " + me);
//////            }
//////        }
////
////        // find
////        try (MongoClient mongoClient = MongoClients.create(settings)) {
////            MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
////            MongoCollection<Document> collection = database.getCollection("Users");
////            // Creates instructions to project two document fields
////            Bson projectionFields = Projections.fields(
////                    Projections.include("name"),
////                   Projections.excludeId());
////            Bson filter = Filters.and(Filters.eq("name", "Tracy"), Filters.eq("email","Tracy@mail.fr"),Filters.eq("password","aaa"));
//////            // Retrieves the first matching document, applying a projection and a descending sort to the results
////            Document doc = collection.find(filter)
//////                    .projection(projectionFields)
////                  .first();
////
////            // ou
////            /*
////            Bson filter = Filters.eq("department", "Engineering");
////            Bson projection = Projections.fields(Projections.include("name", "age"));
////            FindIterable<Document> documents = collection.find(filter)
////                    .projection(projection);
////            Document doc = documents.first();
////
////             */
////            // Prints a message if there are no result documents, or prints the result document as JSON
////            if (doc == null) {
////                System.out.println("No results found.");
////            } else {
////                System.out.println("Found : " + doc.toJson());
////            }
////        }
////
////        // delete
//////        try (MongoClient mongoClient = MongoClients.create(settings)) {
//////            MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//////            MongoCollection<Document> collection = database.getCollection("Users");
//////            Bson query = eq("name", "Tracy");
//////            try {
//////                // Deletes the first document that has a "name" value of "Laura"
//////                DeleteResult result = collection.deleteMany(query);
//////                System.out.println("Deleted document count: " + result.getDeletedCount());
//////                // Prints a message if any exceptions occur during the operation
//////            } catch (MongoException me) {
//////                System.err.println("Unable to delete due to an error: " + me);
//////            }
//////
//////        }
////    }
////    // Optional: Clean up resources when the application shuts down
////    public void close() {
////        if (mongoClient != null) {
////            mongoClient.close();
////        }
////    }
////}
//package org.pc3r.webtoontracker_server.persistence;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.MongoException;
//import com.mongodb.ServerApi;
//import com.mongodb.ServerApiVersion;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.result.InsertOneResult;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.mongodb.client.model.Filters.eq;
//
//public class Database {
//
//    private MongoClient mongoClient;
//    private static final String connectionURL = "mongodb+srv://webtoonTracker:projetPC3R@webtoontracker.bqxh8yr.mongodb.net/?retryWrites=true&w=majority&appName=webtoonTracker";
//    private List<User> users;
//
//    private static final Database instance = new Database();   //singleton instance
//
//    // Private constructor to prevent external instantiation
//    private Database() {
//        initMongoDBClient();
//        initUsers();
//    }
//
//    public static Database getInstance() {
//        return instance;
//    }
//
//    private void initMongoDBClient() {
//        System.out.println("initMongoDBClient--");
//        ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(connectionURL))
//                .serverApi(serverApi)
////                .applyToSslSettings(builder ->
////                        builder.enabled(true))
//                .build();
//        this.mongoClient = MongoClients.create(settings);
//    }
//
//    private void initUsers() {
//        System.out.println("initUsers--");
//        this.users = new ArrayList<>();
//        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//        MongoCollection<Document> collection = database.getCollection("Users");
//        FindIterable<Document> documents = collection.find();
//        for (Document doc : documents) {
//            users.add(new User(doc.getString("_id"), doc.getString("name"), doc.getString("email"), doc.getString("password")));
//        }
//    }
//
//    public void addUser(String name, String email, String password) {
//        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//        MongoCollection<Document> collection = database.getCollection("Users");
//        try {
//            InsertOneResult result = collection.insertOne(new Document()
//                    .append("_id", new ObjectId())
//                    .append("name", name)
//                    .append("email", email)
//                    .append("password", password));
//            System.out.println("Success! Inserted document id: " + result.getInsertedId());
//        } catch (MongoException me) {
//            System.err.println("Unable to insert due to an error: " + me);
//        }
//    }
//
//    public void addWebtoon(String title, String author, String description) {
//        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//        MongoCollection<Document> collection = database.getCollection("Webtoons");
//        try {
//            InsertOneResult result = collection.insertOne(new Document()
//                    .append("_id", new ObjectId())
//                    .append("title", title)
//                    .append("author", author)
//                    .append("description", description));
//            System.out.println("Success! Inserted document id: " + result.getInsertedId());
//        } catch (MongoException me) {
//            System.err.println("Unable to insert due to an error: " + me);
//        }
//    }
//
//    public List<Webtoon> getWebtoons(String email) {
//        MongoDatabase database = mongoClient.getDatabase("webtoonTracker");
//        MongoCollection<Document> collection = database.getCollection("Users");
//        Document doc = collection.find(eq("email", email)).first();
//        // Retrieve the webtoons for the user
//        //...
//        return null;
//    }
//
//    public boolean userExists(String name, String password) {
//        System.out.println("DB : userExists?");
//        boolean exists = false;
//        for (User user : users) {
//            if (user.exists(name, password)) {
//                exists = true;
//                break;
//            }
//        }
//        System.out.println("exists: " + exists);
//        return exists;
//    }
//
//    public void close() {
//        if (mongoClient != null) {
//            mongoClient.close();
//        }
//    }
//}
package org.pc3r.webtoontracker_server.persistence;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private static final String connectionURL = "mongodb+srv://webtoonTracker:projetPC3R@webtoontracker.bqxh8yr.mongodb.net/?retryWrites=true&w=majority&appName=webtoonTracker";
    private List<User> users;
    private static final Database instance = new Database();

    private Database() {
        initMongoDBClient();
        if (mongoClient != null) {
            System.out.println("MongoDB client initialized successfully.");
            initUsers();
        }
        else
            System.err.println("Error initializing MongoDB client.");

    }

    public static Database getInstance() {
        return instance;
    }

    private void initMongoDBClient() {
        try {
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionURL))
                    .serverApi(serverApi)
                    .build();
            this.mongoClient = MongoClients.create(settings);
            this.database = mongoClient.getDatabase("webtoonTracker");
            System.out.println("MongoDB client initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB client: " + e.getMessage());
            throw new RuntimeException("MongoDB client initialization error", e);
        }
    }
    private void initUsers() {
        System.out.println("initUsers--");
        this.users = new ArrayList<User>();
        MongoDatabase database = this.mongoClient.getDatabase("webtoonTracker");
        MongoCollection<Document> collection = database.getCollection("Users");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
            ObjectId objectId = doc.getObjectId("_id");
            if(objectId != null && doc.getString("name") != null && doc.getString("email") != null && doc.getString("password") != null && doc.get("favorites") != null){
                ArrayList<String> favorites = (ArrayList<String>) doc.get("favorites");
                if(favorites == null)
                    favorites = new ArrayList<String>();
                User user = new User(objectId.toHexString(), doc.getString("name"), doc.getString("email"), doc.getString("password"), favorites);
                users.add(user);
                System.out.println("User added: " + user);
            }
        }
    }
    public User userExists(String name, String password) {
        System.out.println("DB : users");
        for(User user : users){
            System.out.println("User: " + user.getName() + " " + user.getPassword());
        }
        User userExists = null;
        for (User user : users) {
            if (user.exists(name, password)) {
                userExists = user;
                break;
            }
        }
        System.out.println("DB : userExists?: " + userExists);
        return userExists;
    }
    public User addUser(String name, String email, String password) {
        MongoCollection<Document> collection = database.getCollection("Users");
        System.out.println("DB : addUser " + name + " " + email + " " + password);
        User user = null;
        try {
            // Inserts a sample document describing a movie into the collection
            ObjectId id = new ObjectId();
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", id)
                    .append("email", email)
                    .append("name", name)
                    .append("password", password)
                    .append("favorites", new ArrayList<String>()));

            // Prints the ID of the inserted document
            System.out.println("Success! Inserted document id: " + result.getInsertedId());

            user = new User(id.toHexString(), name, email, password, new ArrayList<String>());
            this.users.add(user);
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return user;
    }

    public void addWebtoon(String userId, String webtoonId) {
        MongoCollection<Document> collection = database.getCollection("Users");
        try {
            // Add a new webtoon ID to the favorites array
            collection.updateOne(
                    Filters.eq("_id", new ObjectId(userId)),
                    Updates.addToSet("favorites", webtoonId)
            );
//            System.out.println("DB : addWebtoon " + userId + " " + webtoonId);
            for (User user : users) {
                if (user.getId().equals(userId)) {
//                    System.out.println("User found: " + user.getName() + " " + user.getId() + " " + user.getWebtoons());
                    user.addWebtoon(webtoonId);
                    break;
                }
            }
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }
    public void removeWebtoon(String userId, String webtoonId) {
        MongoCollection<Document> collection = database.getCollection("Users");
        try {

            // Remove a webtoon ID from the favorites array
            collection.updateOne(
                    Filters.eq("_id", new ObjectId(userId)),
                    Updates.pull("favorites", webtoonId)
            );

            for (User user : users) {
                if (user.getId().equals(userId)) {
                    user.removeWebtoon(webtoonId);
                    break;
                }
            }
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }

    public List<String> getWebtoons(String userId) {
        MongoCollection<Document> collection = database.getCollection("Users");
        Document doc = collection.find(Filters.eq("_id", new ObjectId(userId))).first();
        if (doc != null) {
            return (List<String>) doc.get("favorites");
        }
        return null;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB client closed.");
        }
    }
//
//    public static void main(String[] args) {
//        Database db = Database.getInstance();
//        String user = "664fc58fc467011a2ac0ca13";
//        String webtoon = "webtoon1";
//        String webtoon3 = "webtoon3";
//        db.addWebtoon(user, webtoon3);
//
//        String webtoon2 = "webtoon2";
//        db.addWebtoon(user, webtoon2);
//
//        db.removeWebtoon(user, webtoon);
//    }
}

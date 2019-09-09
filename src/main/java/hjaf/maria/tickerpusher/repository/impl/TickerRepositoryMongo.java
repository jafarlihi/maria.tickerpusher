package hjaf.maria.tickerpusher.repository.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hjaf.maria.tickerpusher.model.TickerWrapper;
import hjaf.maria.tickerpusher.repository.TickerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

public class TickerRepositoryMongo implements TickerRepository {

    private static final Logger logger = LogManager.getLogger(TickerRepositoryMongo.class);

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection mongoCollection;
    String database;
    String collection;

    public TickerRepositoryMongo(String host, int port, String database, String collection) {
        this.database = database;
        this.collection = collection;
        mongoClient = new MongoClient(host, port);
        mongoDatabase = mongoClient.getDatabase(database);
        try {
            mongoDatabase.createCollection(collection);
        } catch (MongoCommandException ex) {
            logger.info("Collection already exists? Exception: " + ex.getMessage());
        }
        mongoCollection = mongoDatabase.getCollection(collection);
    }

    public void push(TickerWrapper tickerWrapper) {
        Document document = new Document("timestamp", tickerWrapper.getTimestamp())
                .append("open", tickerWrapper.getTicker().getOpen())
                .append("ask", tickerWrapper.getTicker().getAsk())
                .append("askSize", tickerWrapper.getTicker().getAskSize())
                .append("bid", tickerWrapper.getTicker().getBid())
                .append("bidSize", tickerWrapper.getTicker().getBidSize())
                .append("high", tickerWrapper.getTicker().getHigh())
                .append("low", tickerWrapper.getTicker().getLast())
                .append("last", tickerWrapper.getTicker().getLast())
                .append("quoteVolume", tickerWrapper.getTicker().getQuoteVolume())
                .append("volume", tickerWrapper.getTicker().getVolume())
                .append("vwap", tickerWrapper.getTicker().getVwap());
        mongoCollection.insertOne(document);
    }
}

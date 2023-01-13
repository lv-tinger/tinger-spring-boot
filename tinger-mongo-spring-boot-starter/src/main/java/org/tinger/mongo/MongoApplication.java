package org.tinger.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.tinger.common.utils.RandomUtils;

public class MongoApplication {
    public static void main(String[] args) {

        PojoCodecProvider.Builder builder = PojoCodecProvider.builder().register("org.tinger.mongo");

        ClassModel<Account> accountClassModel = ClassModel.builder(Account.class)
                .idPropertyName("id")
                .build();

        CodecProvider pojoCodecProvider = builder.build();
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(pojoCodecProvider));

        String uri = "mongodb://admin:123456@localhost:27017/?maxPoolSize=20&w=majority";
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase("tinger");
            MongoCollection<Account> collection = database.getCollection("account", Account.class).withCodecRegistry(pojoCodecRegistry);
            Account account = Account.builder().id(RandomUtils.nextId()).username("risesun").password("123456").build();
            MongoCollection<Document> documentMongoCollection = database.getCollection("account");
            FindIterable<Document> documents = documentMongoCollection.find();
            for (Document document: documents){

            }
            collection.insertOne(account);
            System.out.println("hello world");
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}

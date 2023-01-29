package org.tinger.data.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.tinger.data.core.meta.TingerNamespace;
import org.tinger.data.core.repo.AbstractRepository;

public abstract class TingerMongoRepository<T, K> extends AbstractRepository<T, K> {
    protected <T> T create(TingerNamespace namespace, T document) {
        return document;
    }

    protected <T> T update(TingerNamespace namespace, T document) {

        return document;
    }

    private MongoCollection<Document> collection(TingerNamespace namespace) {
        return null;
    }
}
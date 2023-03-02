package org.tinger.common.serialize;

import org.tinger.common.utils.ServiceLoaderUtils;

public interface ByteSerializer {
    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] bytes, Class<T> type);

    static ByteSerializer getInstance() {
        return HOLDER.BINARY_SERIALIZER;
    }

    class HOLDER {
        private static final ByteSerializer BINARY_SERIALIZER;

        static {
            BINARY_SERIALIZER = ServiceLoaderUtils.load(ByteSerializer.class);
        }
    }
}
package org.tinger.data.jdbc.resolver.template;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.data.core.meta.TingerMetadata;

/**
 * Created by tinger on 2023-01-30
 */
public class DocumentJdbcResolverTemplateHolder {
    private static final TingerMapBuffer<Class<?>, TingerDocumentJdbcResolverTemplate<?>> BUFFER = new TingerMapBuffer<>();


    @SuppressWarnings("unchecked")
    public static <T> TingerDocumentJdbcResolverTemplate<T> build(TingerMetadata<T> metadata) {
        TingerDocumentJdbcResolverTemplate<?> resolver = BUFFER.get(metadata.getType(), () -> new DefaultDocumentJdbcResolverTemplate<T>().generate(metadata));
        return (TingerDocumentJdbcResolverTemplate<T>) resolver;
    }

    public static void register(Class<?> type, TingerDocumentJdbcResolverTemplate<?> resolver) {
        BUFFER.set(type, resolver);
    }
}

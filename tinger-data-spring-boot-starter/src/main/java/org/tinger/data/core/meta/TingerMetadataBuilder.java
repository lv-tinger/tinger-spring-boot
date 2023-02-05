package org.tinger.data.core.meta;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.ConstructorUtils;
import org.tinger.common.utils.FieldUtils;
import org.tinger.data.core.anno.TingerDataColumn;
import org.tinger.data.core.anno.TingerDataColumnIgnore;
import org.tinger.data.core.anno.TingerDataPrimaryKey;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TingerMetadataBuilder {

    private static final TingerMetadataBuilder TINGER_METADATA_BUILDER = new TingerMetadataBuilder();

    public static TingerMetadataBuilder getInstance() {
        return TINGER_METADATA_BUILDER;
    }

    private final TingerMapBuffer<Class<?>, TingerMetadata<?>> buffer = new TingerMapBuffer<>();

    @SuppressWarnings("unchecked")
    public <T> TingerMetadata<T> build(Class<T> type) {
        return (TingerMetadata<T>) buffer.get(type, () -> this.metadata(type));
    }

    private <T> TingerMetadata<T> metadata(Class<T> type) {
        Field[] fields = FieldUtils.getMemberField(type);
        List<TingerProperty> properties = Arrays.stream(fields).map(this::property).filter(Objects::nonNull).collect(Collectors.toList());
        TingerProperty primaryKey = properties.stream().filter(x -> x.getField().getAnnotation(TingerDataPrimaryKey.class) != null).findFirst().orElse(null);
        properties.remove(primaryKey);
        Constructor<T> constructor = ConstructorUtils.getDeclaredConstructor(type);
        return TingerMetadata.<T>builder().type(type).constructor(constructor).primaryKey(primaryKey).properties(properties).build();
    }

    private TingerProperty property(Field field) {
        Annotation ignore = FieldUtils.getAnnotation(field, TingerDataColumnIgnore.class);
        if (ignore != null) {
            return null;
        }
        field.setAccessible(true);

        String column = field.getName();
        TingerDataColumn tingerDataColumn = FieldUtils.getAnnotation(field, TingerDataColumn.class);
        if (tingerDataColumn != null) {
            column = tingerDataColumn.value();
        }

        return TingerProperty.builder().name(field.getName()).column(column).type(field.getType()).field(field).build();

        //JdbcHandler<?> handler = JdbcHandlerHolder.getInstance().get(field.getType());

        //return TingerProperty.builder().name(field.getName()).column(column).type(field.getType()).field(field).handler(handler).build();
    }
}
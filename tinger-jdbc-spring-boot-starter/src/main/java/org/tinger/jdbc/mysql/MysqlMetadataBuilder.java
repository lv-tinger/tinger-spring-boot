package org.tinger.jdbc.mysql;

import org.tinger.common.utils.ConstructorUtils;
import org.tinger.common.utils.FieldUtils;
import org.tinger.jdbc.anno.*;
import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.handler.JdbcHandlerHolder;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.metadata.JdbcMetadataBuilder;
import org.tinger.jdbc.metadata.JdbcProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlMetadataBuilder implements JdbcMetadataBuilder {
    @Override
    public JdbcDriver getDriver() {
        return JdbcDriver.MYSQL;
    }

    @Override
    public <T, K> JdbcMetadata<T, K> build(Class<T> metadataType, Class<?> repositoryType) {
        JdbcMetadata<T, K> metadata = metadata(metadataType);
        repository(metadata, repositoryType);
        return metadata;
    }

    private <T, K> void repository(JdbcMetadata<T, K> metadata, Class<T> repositoryType) {

        JdbcDatabaseName databaseName = repositoryType.getAnnotation(JdbcDatabaseName.class);
        metadata.setDatabase(databaseName.value());

        JdbcDataTableName tableName = repositoryType.getAnnotation(JdbcDataTableName.class);
        metadata.setDatatable(tableName.value());
    }

    private <T, K> JdbcMetadata<T, K> metadata(Class<T> type) {
        Field[] fields = FieldUtils.getMemberField(type);
        List<JdbcProperty> jdbcProperties = Arrays.stream(fields).map(this::property).collect(Collectors.toList());
        JdbcProperty primaryKey = jdbcProperties.stream().filter(x -> x.getAttr(JdbcProperty.PRIMARY_KEY)).findFirst().orElse(null);
        JdbcMetadata<T, K> metadata = new JdbcMetadata<>();
        metadata.setType(type);
        metadata.setConstructor(ConstructorUtils.getDeclaredConstructor(type));
        metadata.setProperties(jdbcProperties);
        metadata.setPrimaryKey(primaryKey);
        return metadata;
    }


    private JdbcProperty property(Field field) {
        Annotation ignore = FieldUtils.getAnnotation(field, JdbcIgnore.class);
        if (ignore != null) {
            return null;
        }

        field.setAccessible(true);

        JdbcProperty property = new JdbcProperty();
        property.setType(field.getType());
        property.setField(field);
        property.setName(field.getName());
        JdbcHandler<?> jdbcHandler = JdbcHandlerHolder.getInstance().get(field.getType());
        property.setHandler(jdbcHandler);

        column(property, field);

        attr(property, field, JdbcPrimaryKey.class);
        attr(property, field, JdbcCreateTime.class);
        attr(property, field, JdbcUpdateTime.class);
        attr(property, field, JdbcVersion.class);
        attr(property, field, JdbcStatus.class);

        return property;
    }

    private void column(JdbcProperty property, Field field) {
        String name = field.getName();

        JdbcDataColumn jdbcDataColumn = FieldUtils.getAnnotation(field, JdbcDataColumn.class);

        if (jdbcDataColumn != null) {
            name = jdbcDataColumn.name();
        }

        property.setColumn(name);
    }

    private void attr(JdbcProperty property, Field field, Class<? extends Annotation> anno) {
        Annotation annotation = FieldUtils.getAnnotation(field, anno);
        if (annotation == null) {
            return;
        }

        if (JdbcPrimaryKey.class.equals(anno)) {
            property.setAttr(JdbcProperty.PRIMARY_KEY);
        } else if (JdbcCreateTime.class.equals(anno)) {
            property.setAttr(JdbcProperty.CREATE_TIME);
        } else if (JdbcUpdateTime.class.equals(anno)) {
            property.setAttr(JdbcProperty.UPDATE_TIME);
        } else if (JdbcVersion.class.equals(anno)) {
            property.setAttr(JdbcProperty.VERSION);
        } else if (JdbcStatus.class.equals(anno)) {
            property.setAttr(JdbcProperty.STATUS);
        }
    }
}

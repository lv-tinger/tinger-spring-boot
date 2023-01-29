package org.tinger.data.core.repo;

import lombok.Getter;
import org.tinger.common.utils.ClassUtils;
import org.tinger.data.core.anno.TingerRepository;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerMetadataBuilder;
import org.tinger.data.core.value.TingerDriver;

import java.util.List;

@Getter
public abstract class AbstractRepository<T, K> {

    protected final TingerMetadata<T> metadata;
    protected final TingerDriver driver;

    @SuppressWarnings("unchecked")
    protected AbstractRepository() {
        List<Class<?>> classes = ClassUtils.getGenericSuperclass(this);
        this.metadata = TingerMetadataBuilder.getInstance().build((Class<T>) classes.get(0));
        Class<?> repositoryType = this.getClass();
        TingerRepository repositoryAnnotation = repositoryType.getDeclaredAnnotation(TingerRepository.class);
        this.driver = repositoryAnnotation.driver();
    }
}
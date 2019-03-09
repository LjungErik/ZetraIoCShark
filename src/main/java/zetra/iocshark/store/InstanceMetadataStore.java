package zetra.iocshark.store;

import zetra.iocshark.annotations.Instance;
import zetra.iocshark.exceptions.*;
import zetra.iocshark.store.instance.InstanceMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InstanceMetadataStore {

    private final InstanceMetadataRegistry registry = new InstanceMetadataRegistry();

    public InstanceMetadataStore() {
    }

    public <T> void registerClass(Class<T> interfaceClass, Class<? extends T> instanceClass) throws IocStoreException {

        InstanceMetadata metadata = InstanceMetadata.create(interfaceClass, instanceClass);
        registry.register(metadata);
    }

    public void registerConfiguration(Class<?> configurationClass) throws IocStoreException {
        registerConfiguration(configurationClass, null);
    }

    public void registerConfiguration(Object config) throws IocStoreException {
        registerConfiguration(config.getClass(), config);
    }

    public Object getInstance(Class<?> dependencyClass) throws IocStoreException {
        return registry.getInstance(dependencyClass);
    }

    public void build() throws IocStoreException {
        registry.build();
    }

    private void registerConfiguration(Class<?> configurationClass, Object config) throws IocStoreException {
        Method[] methods = configurationClass.getDeclaredMethods();

        for (Method method: methods) {
            Annotation instanceAnnotation = method.getDeclaredAnnotation(Instance.class);

            if(instanceAnnotation != null) {
                InstanceMetadata metadata = InstanceMetadata.create(method, config);
                registry.register(metadata);
            }
        }
    }
}

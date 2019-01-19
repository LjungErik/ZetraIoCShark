package zetra.iocshark.store;

import zetra.iocshark.annotations.Instance;
import zetra.iocshark.exceptions.*;
import zetra.iocshark.store.instance.InstanceMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class InstanceMetadataStore {

    private final InstanceMetadataRegistry registry = new InstanceMetadataRegistry();

    public InstanceMetadataStore() {
    }

    public <T> void registerClass(Class<T> interfaceClass, Class<? extends T> instanceClass) throws IocStoreException {

        InstanceMetadata metadata = InstanceMetadata.create(interfaceClass, instanceClass);
        registry.register(metadata);
    }

    public void registerConfiguration(Class<?> configurationClass) throws IocStoreException {

        Method[] methods = configurationClass.getDeclaredMethods();

        for (Method method: methods) {
            Annotation instanceAnnotation = method.getDeclaredAnnotation(Instance.class);

            if(instanceAnnotation != null) {
                InstanceMetadata metadata = InstanceMetadata.create(method);
                registry.register(metadata);
            }
        }
    }

    public void registerConfiguration(Object config) throws IocStoreException {
        Method[] methods = config.getClass().getDeclaredMethods();

        for (Method method: methods) {
            Annotation instanceAnnotation = method.getDeclaredAnnotation(Instance.class);

            if(instanceAnnotation != null) {
                InstanceMetadata metadata = InstanceMetadata.create(method, config);
                registry.register(metadata);
            }
        }
    }

    public Object getInstance(Class<?> dependencyClass) throws IocStoreException {
        return createInstance(dependencyClass);
    }

    public void build() throws IocStoreException {
        validateDependencies();
        initializeSingletons();
    }

    private void validateDependencies() throws IocStoreException {

        Map<Class, Boolean> visiting = new HashMap<>();
        Map<Class, Boolean> visited = new HashMap<>();

        for (Class instance: registry.getRegisteredClasses()) {
            visit(instance, visiting, visited);
        }
    }

    private void initializeSingletons() throws IocStoreException {

        for (InstanceMetadata metadata: registry.getRegisteredMetadata()) {
            if(metadata.isSingleton()) {
                createInstance(metadata.getInterfaceClass());
            }
        }

    }

    private void visit(Class instanceClass, Map<Class, Boolean> visiting, Map<Class, Boolean> visited) throws IocStoreException {

        if(visiting.getOrDefault(instanceClass, false)) {
            throw new InstanceCycleException(instanceClass);
        }

        if(visited.getOrDefault(instanceClass, false)) {
            return;
        }

        InstanceMetadata metadata = registry.getMetadata(instanceClass);
        if(metadata == null) {
            throw new InstanceNotRegisteredException(instanceClass);
        }

        List<Class> dependencies = metadata.getCreationDependencies();

        visiting.put(instanceClass, true);

        for (Class dependency: dependencies) {
            visit(dependency, visiting, visited);
        }

        visiting.put(instanceClass, false);
        visited.put(instanceClass, true);
    }

    private Object createInstance(Class<?> instanceClass) throws IocStoreException {
        InstanceMetadata metadata = registry.getMetadata(instanceClass);

        if(metadata == null) {
            throw new InstanceNotRegisteredException(instanceClass);
        }

        Object instance = metadata.getSavedInstance();

        if(instance != null) {
            return instance;
        }

        List<Class> dependencies = metadata.getCreationDependencies();
        List<Object> objects = new ArrayList<>();

        for (Class dep: dependencies) {
            objects.add(createInstance(dep));
        }

        return metadata.createInstance(objects.toArray());
    }
}

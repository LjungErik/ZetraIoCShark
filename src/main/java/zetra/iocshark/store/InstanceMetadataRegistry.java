package zetra.iocshark.store;

import zetra.iocshark.exceptions.*;
import zetra.iocshark.store.instance.InstanceMetadata;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceMetadataRegistry {

    private boolean isRegistryBuilt = false;
    private ConcurrentHashMap<Class, InstanceMetadata> registry = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class, Object> singletons = new ConcurrentHashMap<>();

    public synchronized void register(InstanceMetadata metadata) throws IocStoreException {
        if(isRegistryBuilt) {
            throw new ContainerAlreadyBuiltException(metadata.getInterfaceClass());
        }


        if(registry.containsKey(metadata.getInterfaceClass())) {
            throw new InstanceAlreadyRegisteredException(metadata.getInterfaceClass());
        }

        registry.put(metadata.getInterfaceClass(), metadata);
    }

    public Object getInstance(Class instanceClass) throws IocStoreException {
        if(!isRegistryBuilt) {
            throw new ContainerNotBuiltInstanceRetrieveException(instanceClass);
        }

        if(singletons.containsKey(instanceClass)) {
            return singletons.get(instanceClass);
        }

        return createInstance(instanceClass);
    }

    public synchronized void build() throws IocStoreException {
        validateDependencies();
        initializeSingletons();

        isRegistryBuilt = true;
    }

    private void validateDependencies() throws IocStoreException {

        Map<Class, Boolean> visiting = new HashMap<>();
        Map<Class, Boolean> visited = new HashMap<>();

        for (Class instance: registry.keySet()) {
            visit(instance, visiting, visited);
        }
    }

    private void visit(Class instanceClass, Map<Class, Boolean> visiting, Map<Class, Boolean> visited) throws IocStoreException {

        if(visiting.getOrDefault(instanceClass, false)) {
            throw new InstanceCycleException(instanceClass);
        }

        if(visited.getOrDefault(instanceClass, false)) {
            return;
        }

        InstanceMetadata metadata = registry.getOrDefault(instanceClass, null);
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

    private void initializeSingletons() throws IocStoreException {
        for (InstanceMetadata metadata: registry.values()) {
            if(metadata.isSingleton()) {
                Object instance = createInstance(metadata.getInterfaceClass());
                registerSingleton(metadata.getInterfaceClass(), instance);
            }
        }
    }

    private void registerSingleton(Class instanceClass, Object instance) {
        singletons.put(instanceClass, instance);
    }

    private Object createInstance(Class<?> instanceClass) throws IocStoreException {
        InstanceMetadata metadata = registry.getOrDefault(instanceClass, null);

        if(metadata == null) {
            throw new InstanceNotRegisteredException(instanceClass);
        }

        Object instance = singletons.get(metadata.getInterfaceClass());

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

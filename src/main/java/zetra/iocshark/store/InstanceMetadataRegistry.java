package zetra.iocshark.store;

import zetra.iocshark.exceptions.InstanceAlreadyRegisteredException;
import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.store.instance.InstanceMetadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceMetadataRegistry {

    private ConcurrentHashMap<Class, InstanceMetadata> registry = new ConcurrentHashMap<>();

    void register(InstanceMetadata metadata) throws IocStoreException {
        if(registry.containsKey(metadata.getInterfaceClass())) {
            throw new InstanceAlreadyRegisteredException(metadata.getInterfaceClass());
        }

        registry.put(metadata.getInterfaceClass(), metadata);
    }

    Collection<Class> getRegisteredClasses() {
        return registry.keySet();
    }

    Collection<InstanceMetadata> getRegisteredMetadata() {
        return registry.values();
    }

    InstanceMetadata getMetadata(Class instanceClass) {
        return registry.getOrDefault(instanceClass, null);
    }

}

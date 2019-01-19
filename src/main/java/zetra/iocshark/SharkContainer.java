package zetra.iocshark;

import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.store.InstanceMetadataStore;

public class SharkContainer {

    private InstanceMetadataStore store;

    public SharkContainer() {
        store = new InstanceMetadataStore();
    }

    public <T> void registerInterface(Class<T> interfaceClass, Class<? extends T> instanceClass) throws IocStoreException {
        store.registerClass(interfaceClass, instanceClass);
    }

    public void registerClass(Class<?> instanceClass) throws IocStoreException {
        //Check if configuration class or actual instance
        store.registerClass(null, instanceClass);
    }

    public void registerConfig(Class<?> configClass) throws IocStoreException {
        store.registerConfiguration(configClass);
    }

    public void registerConfig(Object config) throws IocStoreException {
        store.registerConfiguration(config);
    }

    public <T> T getInstance(Class<T> instanceClass) throws IocStoreException {
        return (T)store.getInstance(instanceClass);
    }

    public void build() throws IocStoreException {
        store.build();
    }
}

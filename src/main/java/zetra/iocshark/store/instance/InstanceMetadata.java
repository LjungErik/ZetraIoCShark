package zetra.iocshark.store.instance;

import zetra.iocshark.annotations.enums.ScopeType;
import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.store.creation.InstanceCreationMetadata;
import zetra.iocshark.store.scope.ScopeMetadata;

import java.lang.reflect.Method;
import java.util.List;

public class InstanceMetadata {

    private Class<?> interfaceClass;
    private InstanceCreationMetadata creationMetadata;
    private ScopeMetadata scopeMetadata;
    private Object savedInstance;

    private InstanceMetadata(Class<?> interfaceClass, InstanceCreationMetadata creationMetadata, ScopeMetadata scopeMetadata) {
        this.interfaceClass = interfaceClass;
        this.creationMetadata = creationMetadata;
        this.scopeMetadata = scopeMetadata;
        this.savedInstance = null;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public List<Class> getCreationDependencies() {
        return creationMetadata.getDependencies();
    }

    public Object getSavedInstance() {
        return savedInstance;
    }

    public Object createInstance(Object... args) throws IocStoreException {
        Object instance = creationMetadata.createInstance(args);

        if(scopeMetadata.saveInstance()) {
            savedInstance = instance;
        }

        return instance;
    }

    public boolean isSingleton() {
        return scopeMetadata.getType() == ScopeType.Singleton;
    }

    public static <T> InstanceMetadata create(Class<T> interfaceClass, Class<? extends T> instanceClass) throws IocStoreException {
        InstanceCreationMetadata creationMetadata = InstanceCreationMetadata.create(instanceClass);
        ScopeMetadata scopeMetadata = ScopeMetadata.create(instanceClass);

        Class<?> baseClass = interfaceClass;
        if(baseClass == null) {
            baseClass = instanceClass;
        }

        return new InstanceMetadata(baseClass, creationMetadata, scopeMetadata);
    }

    public static InstanceMetadata create(Method method) {
        return create(method, null);
    }

    public static InstanceMetadata create(Method method, Object config) {
        Class<?> instanceClass = method.getReturnType();
        InstanceCreationMetadata creationMetadata = InstanceCreationMetadata.create(method, config);
        ScopeMetadata scopeMetadata = ScopeMetadata.create(method);

        return new InstanceMetadata(instanceClass, creationMetadata, scopeMetadata);
    }


}

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

    private InstanceMetadata(Class<?> interfaceClass, InstanceCreationMetadata creationMetadata, ScopeMetadata scopeMetadata) {
        this.interfaceClass = interfaceClass;
        this.creationMetadata = creationMetadata;
        this.scopeMetadata = scopeMetadata;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public List<Class> getCreationDependencies() {
        return creationMetadata.getDependencies();
    }

    public Object createInstance(Object... args) throws IocStoreException {
        Object instance = creationMetadata.createInstance(args);

        return instance;
    }

    public boolean isSingleton() {
        return scopeMetadata.isSingleton();
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

    public static InstanceMetadata create(Method method, Object config) {
        Class<?> instanceClass = method.getReturnType();
        InstanceCreationMetadata creationMetadata = InstanceCreationMetadata.create(method, config);
        ScopeMetadata scopeMetadata = ScopeMetadata.create(method);

        return new InstanceMetadata(instanceClass, creationMetadata, scopeMetadata);
    }


}

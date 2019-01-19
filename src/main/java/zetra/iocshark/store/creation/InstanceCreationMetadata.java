package zetra.iocshark.store.creation;

import zetra.iocshark.exceptions.InstanceCreationException;
import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.exceptions.MissingConstructorException;
import zetra.iocshark.exceptions.MoreThanOneConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InstanceCreationMetadata {

    private CreationMethod creationMethod;
    private List<Class> creationArgs;

    private InstanceCreationMetadata(CreationMethod method, List<Class> args) {
        this.creationArgs = args;
        this.creationMethod = method;
    }

    public List<Class> getDependencies() {
        return creationArgs;
    }

    public Object createInstance(Object... objects) throws IocStoreException {
        CreationArgs creationArgs = new CreationArgs(objects);
        try {
            return creationMethod.execute(creationArgs);
        }
        catch(Exception ex) {
            throw new InstanceCreationException(ex);
        }
    }

    public static InstanceCreationMetadata create(Class<?> instanceClass) throws IocStoreException {

        Constructor[] constructors = instanceClass.getConstructors();

        if(constructors.length < 1) {
            throw new MissingConstructorException(instanceClass);
        }

        if(constructors.length > 1) {
            throw new MoreThanOneConstructorException(instanceClass);
        }

        // ToDo add better solution than just using first constructor
        Constructor constructor = constructors[0];
        List<Class> constructClasses = Arrays.asList(constructor.getParameterTypes());

        CreationMethod creationMethod;


        if (constructClasses.isEmpty()) {
            creationMethod = creationArgs -> constructor.newInstance();
        } else {
            creationMethod = creationArgs -> constructor.newInstance(creationArgs.getArgs());
        }

        return new InstanceCreationMetadata(creationMethod, constructClasses);
    }

    public static InstanceCreationMetadata create(Method method, Object instance) {
        List<Class> parameterTypes = Arrays.asList(method.getParameterTypes());
        CreationMethod creationMethod;

        if (parameterTypes.isEmpty()) {
            creationMethod = creationArgs -> method.invoke(instance);
        } else {
            creationMethod = creationArgs -> method.invoke(instance, creationArgs.getArgs());
        }

        return new InstanceCreationMetadata(creationMethod, parameterTypes);
    }
}

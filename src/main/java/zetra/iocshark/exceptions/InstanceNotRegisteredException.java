package zetra.iocshark.exceptions;

public class InstanceNotRegisteredException extends IocStoreException {

    public InstanceNotRegisteredException(Class instanceClass) {
        super("No registered instance for: " + instanceClass.getName());
    }
}

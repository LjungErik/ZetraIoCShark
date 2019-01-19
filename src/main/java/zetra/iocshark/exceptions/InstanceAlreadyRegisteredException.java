package zetra.iocshark.exceptions;

public class InstanceAlreadyRegisteredException extends IocStoreException {

    public InstanceAlreadyRegisteredException(Class instanceClass) {
        super("Instance already registered for class: " + instanceClass.getName());
    }
}

package zetra.iocshark.exceptions;

public class ContainerAlreadyBuiltException extends IocStoreException {

    public ContainerAlreadyBuiltException(Class registeredClass) {
        super("Container is already built cannot register: " + registeredClass.getName());
    }
}

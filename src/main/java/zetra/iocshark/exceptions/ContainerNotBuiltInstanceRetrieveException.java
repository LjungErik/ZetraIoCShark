package zetra.iocshark.exceptions;

public class ContainerNotBuiltInstanceRetrieveException extends IocStoreException {

    public ContainerNotBuiltInstanceRetrieveException(Class instanceClass) {
        super("Container has not yet been built, unable to retrieve instance: " + instanceClass.getName());
    }
}

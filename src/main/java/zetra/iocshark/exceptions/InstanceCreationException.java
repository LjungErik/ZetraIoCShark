package zetra.iocshark.exceptions;

public class InstanceCreationException extends IocStoreException {

    private Exception internalException;

    public InstanceCreationException(Exception internalException) {
        super("Instance could not be created due to internal exception: " + internalException.getMessage());
        this.internalException = internalException;
    }
}

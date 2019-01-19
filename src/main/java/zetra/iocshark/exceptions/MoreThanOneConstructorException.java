package zetra.iocshark.exceptions;

public class MoreThanOneConstructorException extends IocStoreException {

    public MoreThanOneConstructorException(Class instanceClass) {
        super("More than once constructor found for the given instance: " + instanceClass.getName());
    }
}

package zetra.iocshark.exceptions;

public class MissingConstructorException extends IocStoreException {

    public MissingConstructorException(Class instanceClass) {
        super("Missing a constructor for the given instance: " + instanceClass.getName());
    }
}

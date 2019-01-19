package zetra.iocshark.exceptions;

public class InstanceCycleException extends IocStoreException {

    public InstanceCycleException(Class instanceClass) {
        super("Instance cycle detected for class: " + instanceClass.getName());
    }
}

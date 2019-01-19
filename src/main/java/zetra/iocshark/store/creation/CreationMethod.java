package zetra.iocshark.store.creation;

public interface CreationMethod {

    Object execute(CreationArgs creationArgs) throws Exception;
}

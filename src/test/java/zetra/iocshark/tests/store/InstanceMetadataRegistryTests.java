package zetra.iocshark.tests.store;

import org.junit.Before;
import org.junit.Test;
import zetra.iocshark.exceptions.ContainerAlreadyBuiltException;
import zetra.iocshark.exceptions.ContainerNotBuiltInstanceRetrieveException;
import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.store.InstanceMetadataRegistry;
import zetra.iocshark.store.instance.InstanceMetadata;
import zetra.iocshark.tests.stubentities.car.SparkPlug;

public class InstanceMetadataRegistryTests {

    private InstanceMetadataRegistry registry;

    @Before
    public void setup() {
        registry = new InstanceMetadataRegistry();
    }


    @Test
    public void register_NothingHasBeenBuilt_FinishWithoutException() throws IocStoreException {
        InstanceMetadata metadata = InstanceMetadata.create(SparkPlug.class, SparkPlug.class);

        registry.register(metadata);

        registry.build();
    }

    @Test(expected = ContainerAlreadyBuiltException.class)
    public void register_RegistryHasBeenBuilt_ThrowException() throws IocStoreException {
        InstanceMetadata metadata = InstanceMetadata.create(SparkPlug.class, SparkPlug.class);

        registry.build();
        registry.register(metadata);
    }

    @Test
    public void getInstance_RegistryHasBeenBuilt_FinishWithoutException() throws IocStoreException {
        InstanceMetadata metadata = InstanceMetadata.create(SparkPlug.class, SparkPlug.class);

        registry.register(metadata);
        registry.build();

        registry.getInstance(SparkPlug.class);
    }

    @Test(expected = ContainerNotBuiltInstanceRetrieveException.class)
    public void getInstance_NothingHasBeenBuilt_ThrowException() throws IocStoreException {
        InstanceMetadata metadata = InstanceMetadata.create(SparkPlug.class, SparkPlug.class);

        registry.register(metadata);

        registry.getInstance(SparkPlug.class);
    }
}

package zetra.iocshark.tests.store;

import org.junit.Before;
import org.junit.Test;
import zetra.iocshark.exceptions.InstanceCycleException;
import zetra.iocshark.exceptions.InstanceNotRegisteredException;
import zetra.iocshark.store.InstanceMetadataStore;
import zetra.iocshark.tests.stubentities.car.Car;
import zetra.iocshark.tests.stubentities.car.Motor;
import zetra.iocshark.tests.stubentities.car.SparkPlug;
import zetra.iocshark.tests.stubentities.car.Tire;
import zetra.iocshark.tests.stubentities.configuration.BasicConfiguration;
import zetra.iocshark.tests.stubentities.configuration.CycleConfiguration;
import zetra.iocshark.tests.stubentities.configuration.MiniConfiguration;
import zetra.iocshark.tests.stubentities.random.RandomA;
import zetra.iocshark.tests.stubentities.random.RandomB;
import zetra.iocshark.tests.stubentities.random.RandomC;
import zetra.iocshark.tests.stubentities.random.RandomD;

public class InstanceMetadataStoreTests {

    private InstanceMetadataStore store;

    @Before
    public void setup() {
        store = new InstanceMetadataStore();
    }


    @Test
    public void validateDependencies_NoDependencyCycle_FinishWithoutException() throws Exception {
        store.registerClass(null, Car.class);
        store.registerClass(null, Motor.class);
        store.registerClass(null, SparkPlug.class);
        store.registerClass(null, Tire.class);

        store.build();
    }

    @Test(expected = InstanceCycleException.class)
    public void validateDependencies_DependencyCycle_ThrowException() throws Exception {
        store.registerClass(null, RandomD.class);
        store.registerClass(null, RandomC.class);
        store.registerClass(null, RandomB.class);
        store.registerClass(null, RandomA.class);

        store.build();
    }

    @Test(expected = InstanceNotRegisteredException.class)
    public void validateDependencies_MissingDependency_ThrowException() throws Exception {
        store.registerClass(null, Motor.class);
        store.registerClass(null, Tire.class);
        store.registerClass(null, Car.class);

        store.build();
    }

    @Test
    public void validateDependencies_ConfigClass_FinishWithoutException() throws Exception {
        store.registerConfiguration(BasicConfiguration.class);

        store.build();
    }

    @Test(expected = InstanceNotRegisteredException.class)
    public void validateDependencies_ConfigClassMissingDep_ThrowException() throws Exception {
        store.registerConfiguration(MiniConfiguration.class);

        store.build();
    }

    @Test(expected = InstanceCycleException.class)
    public void validateDependencies_ConfigClassWithCycle_ThrowException() throws Exception {
        store.registerConfiguration(CycleConfiguration.class);

        store.build();
    }

    @Test
    public void validateDependencies_MixedConfigAndClass_FinishWithoutException() throws Exception {
        store.registerConfiguration(MiniConfiguration.class);
        store.registerClass(null, SparkPlug.class);
        store.registerClass(null, Motor.class);

        store.build();
    }


    @Test
    public void validateDependencies_ConfigInstance_FinishWithoutException() throws Exception {
        BasicConfiguration config = new BasicConfiguration();
        store.registerConfiguration(config);

        store.build();
    }

    @Test(expected = InstanceNotRegisteredException.class)
    public void validateDependencies_ConfigInstanceMissingDep_ThrowException() throws Exception {
        MiniConfiguration config = new MiniConfiguration();
        store.registerConfiguration(config);

        store.build();
    }

    @Test(expected = InstanceCycleException.class)
    public void validateDependencies_ConfigInstanceWithCycle_ThrowException() throws Exception {
        CycleConfiguration config = new CycleConfiguration();
        store.registerConfiguration(config);

        store.build();
    }

    @Test
    public void validateDependencies_MixedInstanceAndClass_FinishWithoutException() throws Exception {
        MiniConfiguration config = new MiniConfiguration();
        store.registerConfiguration(config);
        store.registerClass(null, SparkPlug.class);
        store.registerClass(null, Motor.class);

        store.build();
    }
}

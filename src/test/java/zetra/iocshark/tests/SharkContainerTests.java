package zetra.iocshark.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import zetra.iocshark.SharkContainer;
import zetra.iocshark.exceptions.InstanceAlreadyRegisteredException;
import zetra.iocshark.exceptions.InstanceCycleException;
import zetra.iocshark.exceptions.InstanceNotRegisteredException;
import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.tests.stubentities.car.*;
import zetra.iocshark.tests.stubentities.configuration.BasicConfiguration;
import zetra.iocshark.tests.stubentities.configuration.CycleConfiguration;
import zetra.iocshark.tests.stubentities.configuration.MiniConfiguration;

public class SharkContainerTests {

    private SharkContainer container;

    @Before
    public void setup() {
        container = new SharkContainer();
    }

    @Test
    public void testRegisterClass_WithDependencies_FinishWithSuccess() throws IocStoreException {
        container.registerClass(Car.class);
        container.registerClass(Motor.class);
        container.registerClass(Tire.class);
        container.registerClass(SparkPlug.class);

        container.build();

        Car car = container.getInstance(Car.class);
    }

    @Test
    public void testRegisterConfigClass_WithDependencies_FinishWithSuccess() throws IocStoreException {
        container.registerConfig(BasicConfiguration.class);
        container.build();

        Car car = container.getInstance(Car.class);
    }

    @Test
    public void testRegisterConfigInstance_WithDependencies_FinishWithSuccess() throws IocStoreException {
        container.registerConfig(new BasicConfiguration());
        container.build();

        Car car = container.getInstance(Car.class);
    }

    @Test
    public void testRegisterInterfaceClass_ForBaseClass_FinishWithSuccess() throws IocStoreException {
        container.registerClass(Motor.class);
        container.registerClass(Tire.class);
        container.registerClass(SparkPlug.class);
        container.registerInterface(Car.class, Audi.class);
        container.build();

        Car car = container.getInstance(Car.class);
        Assert.assertTrue(car instanceof Audi);
    }

    @Test
    public void testRegisterInterfaceClass_ForInterface_FinishWithSuccess() throws IocStoreException {
        container.registerClass(Motor.class);
        container.registerClass(Tire.class);
        container.registerClass(SparkPlug.class);
        container.registerInterface(IVehicle.class, Audi.class);
        container.build();

        IVehicle vehicle = container.getInstance(IVehicle.class);
        Assert.assertTrue(vehicle instanceof Audi);
    }

    @Test(expected = InstanceAlreadyRegisteredException.class)
    public void testRegisterClass_DuplicateRegistration_ThrowException() throws IocStoreException {
        container.registerClass(Car.class);
        container.registerClass(Car.class);

        container.build();
    }

    @Test(expected = InstanceNotRegisteredException.class)
    public void testRegisterClass_MissingDependency_ThrowException() throws IocStoreException {
        container.registerClass(Car.class);
        container.registerClass(Motor.class);

        container.build();
    }

    @Test(expected = InstanceCycleException.class)
    public void testRegisterConfigClass_CycleDependency_ThrowException() throws IocStoreException {
        container.registerConfig(CycleConfiguration.class);

        container.build();
    }

    @Test(expected = InstanceNotRegisteredException.class)
    public void testRegisterConfigInstance_MissingDependency_ThrowException() throws IocStoreException {
        container.registerConfig(new MiniConfiguration());

        container.build();
    }
}

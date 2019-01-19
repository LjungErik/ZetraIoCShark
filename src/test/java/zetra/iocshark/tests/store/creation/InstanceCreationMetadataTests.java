package zetra.iocshark.tests.store.creation;

import org.junit.Test;
import zetra.iocshark.exceptions.InstanceCreationException;
import zetra.iocshark.exceptions.IocStoreException;
import zetra.iocshark.exceptions.MissingConstructorException;
import zetra.iocshark.exceptions.MoreThanOneConstructorException;
import zetra.iocshark.store.creation.InstanceCreationMetadata;
import zetra.iocshark.tests.stubentities.car.Car;
import zetra.iocshark.tests.stubentities.car.Motor;
import zetra.iocshark.tests.stubentities.car.SparkPlug;
import zetra.iocshark.tests.stubentities.car.Tire;
import zetra.iocshark.tests.stubentities.configuration.BasicConfiguration;
import zetra.iocshark.tests.stubentities.random.RandomMissingConstructor;
import zetra.iocshark.tests.stubentities.random.RandomMultiConstructor;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InstanceCreationMetadataTests {

    @Test
    public void testCreationMetadata_NoDeps() throws IocStoreException {
        InstanceCreationMetadata metadata = InstanceCreationMetadata.create(Tire.class);

        List<Class> deps = metadata.getDependencies();

        assertEquals(0, deps.size());
    }

    @Test
    public void testCreationMetadata_WithSingleDep() throws IocStoreException {
        InstanceCreationMetadata metadata = InstanceCreationMetadata.create(Motor.class);

        List<Class> deps = metadata.getDependencies();

        assertEquals(1, deps.size());
        assertEquals(SparkPlug.class, deps.get(0));
    }

    @Test
    public void testCreationMetadata_WithMultiDeps() throws IocStoreException {
        InstanceCreationMetadata metadata = InstanceCreationMetadata.create(Car.class);

        List<Class> deps = metadata.getDependencies();

        assertEquals(2, deps.size());
        assertEquals(Motor.class, deps.get(0));
        assertEquals(Tire.class, deps.get(1));
    }

    @Test
    public void testCreationMetadata_ConfigAndNullInstance() throws NoSuchMethodException {
        Method method = BasicConfiguration.class.getDeclaredMethod("car", Motor.class, Tire.class);

        InstanceCreationMetadata metadata = InstanceCreationMetadata.create(method, null);

        List<Class> deps = metadata.getDependencies();

        assertEquals(2, deps.size());
        assertEquals(Motor.class, deps.get(0));
        assertEquals(Tire.class, deps.get(1));
    }

    @Test
    public void testCreationMetadata_ConfigAndInstance() throws Exception {
        Method method = BasicConfiguration.class.getDeclaredMethod("car", Motor.class, Tire.class);
        BasicConfiguration config = new BasicConfiguration();

        InstanceCreationMetadata metadata = InstanceCreationMetadata.create(method, config);

        List<Class> deps = metadata.getDependencies();

        assertEquals(2, deps.size());
        assertEquals(Motor.class, deps.get(0));
        assertEquals(Tire.class, deps.get(1));

        Object instance = metadata.createInstance(new Motor(new SparkPlug()), new Tire());

        assertEquals(Car.class, instance.getClass());
    }

    @Test(expected = InstanceCreationException.class)
    public void testCreationMetadata_CreateWithIncorrectParams() throws Exception {
        Method method = BasicConfiguration.class.getDeclaredMethod("car", Motor.class, Tire.class);
        BasicConfiguration config = new BasicConfiguration();

        InstanceCreationMetadata metadata = InstanceCreationMetadata.create(method, config);

        Object instance = metadata.createInstance(new SparkPlug());
    }

    @Test(expected = MoreThanOneConstructorException.class)
    public void testCreationMetadata_MultiConstructorInstance() throws IocStoreException {
        InstanceCreationMetadata.create(RandomMultiConstructor.class);
    }

    @Test(expected = MissingConstructorException.class)
    public void testCreationMetadata_MissingConstructorInstance() throws IocStoreException {
        InstanceCreationMetadata.create(RandomMissingConstructor.class);
    }
}

package zetra.iocshark.tests.store.scope;

import org.junit.Test;
import zetra.iocshark.annotations.enums.ScopeType;
import zetra.iocshark.store.scope.ScopeMetadata;
import zetra.iocshark.tests.stubentities.car.Car;
import zetra.iocshark.tests.stubentities.car.Motor;
import zetra.iocshark.tests.stubentities.car.SparkPlug;
import zetra.iocshark.tests.stubentities.car.Tire;
import zetra.iocshark.tests.stubentities.configuration.BasicConfiguration;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class ScopeMetadataTests {

    @Test
    public void testScopeMetadata_ClassWithSingleton_Singleton() {
        ScopeMetadata metadata = ScopeMetadata.create(Car.class);

        assertEquals(ScopeType.Singleton, metadata.getType());
    }

    @Test
    public void testScopeMetadata_ClassWithTransient_Transient() {
        ScopeMetadata metadata = ScopeMetadata.create(Motor.class);

        assertEquals(ScopeType.Transient, metadata.getType());
    }

    @Test
    public void testScopeMetadata_ClassWithoutScope_Singleton() {
        ScopeMetadata metadata = ScopeMetadata.create(SparkPlug.class);

        assertEquals(ScopeType.Singleton, metadata.getType());
    }

    @Test
    public void test_ScopeMetaData_MethodWithSingleton_Singleton() throws NoSuchMethodException{
        Method method = BasicConfiguration.class.getMethod("car", Motor.class, Tire.class);
        ScopeMetadata metadata = ScopeMetadata.create(method);

        assertEquals(ScopeType.Singleton, metadata.getType());
    }

    @Test
    public void test_ScopeMetaData_MethodWithTransient_Transient() throws NoSuchMethodException{
        Method method = BasicConfiguration.class.getMethod("motor", SparkPlug.class);
        ScopeMetadata metadata = ScopeMetadata.create(method);

        assertEquals(ScopeType.Transient, metadata.getType());
    }

    @Test
    public void test_ScopeMetaData_MethodWithoutScope_Singleton() throws NoSuchMethodException{
        Method method = BasicConfiguration.class.getMethod("sparkPlug");
        ScopeMetadata metadata = ScopeMetadata.create(method);

        assertEquals(ScopeType.Singleton, metadata.getType());
    }

}

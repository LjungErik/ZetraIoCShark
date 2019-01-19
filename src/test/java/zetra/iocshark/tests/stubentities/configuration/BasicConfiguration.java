package zetra.iocshark.tests.stubentities.configuration;

import zetra.iocshark.annotations.Configuration;
import zetra.iocshark.annotations.Instance;
import zetra.iocshark.annotations.Scope;
import zetra.iocshark.annotations.enums.ScopeType;
import zetra.iocshark.tests.stubentities.car.Car;
import zetra.iocshark.tests.stubentities.car.Motor;
import zetra.iocshark.tests.stubentities.car.SparkPlug;
import zetra.iocshark.tests.stubentities.car.Tire;

@Configuration
public class BasicConfiguration {

    @Instance
    @Scope(value = ScopeType.Singleton)
    public static Car car(Motor motor, Tire tire) {
        return new Car(motor, tire);
    }

    @Instance
    @Scope(value = ScopeType.Transient)
    public static Motor motor(SparkPlug sparkPlug) {
        return new Motor(sparkPlug);
    }

    @Instance
    @Scope(value = ScopeType.Transient)
    public static Tire tire() {
        return new Tire();
    }

    @Instance
    public static SparkPlug sparkPlug() {
        return new SparkPlug();
    }

}

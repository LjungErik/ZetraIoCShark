package zetra.iocshark.tests.stubentities.car;

import zetra.iocshark.annotations.Scope;
import zetra.iocshark.annotations.enums.ScopeType;

@Scope(value = ScopeType.Singleton)
public class Car {

    private Motor motor;
    private Tire tire;

    public Car(Motor motor, Tire tire) {
        this.motor = motor;
        this.tire = tire;
    }
}

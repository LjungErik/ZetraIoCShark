package zetra.iocshark.tests.stubentities.car;

public class Audi extends Car implements IVehicle {

    public Audi(Motor motor, Tire tire) {
        super(motor, tire);
    }

    @Override
    public void turnOn() {
        System.out.println("Audi is now turned on");
    }

    @Override
    public void turnOff() {
        System.out.println("Audi is now turned off");
    }
}

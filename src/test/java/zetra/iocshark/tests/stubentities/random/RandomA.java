package zetra.iocshark.tests.stubentities.random;

public class RandomA {

    private RandomB randomB;
    private RandomC randomC;

    public RandomA(RandomB randomB, RandomC randomC) {
        this.randomB = randomB;
        this.randomC = randomC;
    }
}

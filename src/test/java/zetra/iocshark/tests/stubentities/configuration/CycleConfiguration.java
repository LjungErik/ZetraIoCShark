package zetra.iocshark.tests.stubentities.configuration;

import zetra.iocshark.annotations.Configuration;
import zetra.iocshark.annotations.Instance;
import zetra.iocshark.annotations.Scope;
import zetra.iocshark.annotations.enums.ScopeType;
import zetra.iocshark.tests.stubentities.random.RandomA;
import zetra.iocshark.tests.stubentities.random.RandomB;
import zetra.iocshark.tests.stubentities.random.RandomC;
import zetra.iocshark.tests.stubentities.random.RandomD;

@Configuration
public class CycleConfiguration {

    @Instance
    @Scope(value = ScopeType.Singleton)
    public static RandomA randomA(RandomB randomB, RandomC randomC) {
        return new RandomA(randomB, randomC);
    }

    @Instance
    @Scope(value = ScopeType.Transient)
    public static RandomB randomB(RandomC randomC) {
        return new RandomB(randomC);
    }

    @Instance
    public static RandomC randomC(RandomD randomD) {
        return new RandomC(randomD);
    }

    @Instance
    public static RandomD randomD(RandomA randomA) {
        return new RandomD(randomA);
    }
}

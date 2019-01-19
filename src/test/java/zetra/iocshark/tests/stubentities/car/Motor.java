package zetra.iocshark.tests.stubentities.car;

import zetra.iocshark.annotations.Scope;
import zetra.iocshark.annotations.enums.ScopeType;

@Scope(value = ScopeType.Transient)
public class Motor {

    private SparkPlug sparkPlug;

    public Motor(SparkPlug sparkPlug) {
        this.sparkPlug = sparkPlug;
    }
}

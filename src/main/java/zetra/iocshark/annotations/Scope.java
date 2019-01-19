package zetra.iocshark.annotations;

import zetra.iocshark.annotations.enums.ScopeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Scope {
    ScopeType value() default ScopeType.Singleton;

}
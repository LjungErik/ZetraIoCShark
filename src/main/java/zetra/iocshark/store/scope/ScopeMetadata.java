package zetra.iocshark.store.scope;

import zetra.iocshark.annotations.Scope;
import zetra.iocshark.annotations.enums.ScopeType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ScopeMetadata {

    private ScopeType type;

    private ScopeMetadata(ScopeType type) {
        this.type = type;
    }

    public ScopeType getType() {
        return type;
    }

    public boolean isSingleton() {
        return type == ScopeType.Singleton;
    }

    public static ScopeMetadata create(Class instanceClass) {

        Annotation annotation = instanceClass.getAnnotation(Scope.class);

        return create(annotation);
    }

    public static ScopeMetadata create(Method method) {

        Annotation annotation = method.getDeclaredAnnotation(Scope.class);

        return create(annotation);
    }

    private static ScopeMetadata create(Annotation annotation) {

        ScopeType type = ScopeType.Singleton;

        if(annotation != null) {
            type = ((Scope) annotation).value();
        }

        return new ScopeMetadata(type);
    }
}

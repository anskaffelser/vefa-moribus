package no.difi.vefa.moribus.api;

import no.difi.vefa.moribus.annotation.Step;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
@FunctionalInterface
public interface Generator {

    void perform() throws IOException, MoribusException;

    default String getStep() {
        return getClass().isAnnotationPresent(Step.class) ?
                getClass().getAnnotation(Step.class).value() : null;
    }

    default int getOrder() {
        return getClass().isAnnotationPresent(Order.class) ?
                getClass().getAnnotation(Order.class).value() : 0;
    }
}

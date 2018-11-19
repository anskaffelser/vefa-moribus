package no.difi.vefa.moribus.api;

import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
@FunctionalInterface
public interface Generator {

    void perform() throws IOException, MoribusException;

    default int getOrder() {
        if (!getClass().isAnnotationPresent(Order.class))
            return 0;

        return getClass().getAnnotation(Order.class).value();
    }
}

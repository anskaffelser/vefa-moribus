package no.difi.vefa.moribus.generator;

import com.google.inject.Singleton;
import no.difi.vefa.moribus.annotation.Step;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
@Singleton
@Order(2000)
@Step("homepage")
public class HomepageGenerator extends AbstractXsltGenerator {

    @Override
    public void perform() throws IOException, MoribusException {
        generate("/xslt/site-web.xslt", "generated/site-web.xml");
    }
}

package no.difi.vefa.moribus.module;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import no.difi.vefa.moribus.api.Generator;
import no.difi.vefa.moribus.generator.*;
import org.kohsuke.MetaInfServices;

/**
 * @author erlend
 */
@MetaInfServices(Module.class)
public class GeneratorModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Generator> multibinder = Multibinder.newSetBinder(binder(), Generator.class);
        multibinder.addBinding().to(CleanGenerator.class);
        multibinder.addBinding().to(ApiV2Generator.class);
        multibinder.addBinding().to(HomepageGenerator.class);
        multibinder.addBinding().to(CombineGenerator.class);
        multibinder.addBinding().to(BootstrapGenerator.class);
    }
}

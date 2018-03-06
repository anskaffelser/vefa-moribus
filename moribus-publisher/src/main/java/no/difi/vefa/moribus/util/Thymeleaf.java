package no.difi.vefa.moribus.util;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
public class Thymeleaf {

    @Inject
    private PathGenerator pathGenerator;

    private final TemplateEngine engine;

    public Thymeleaf() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");

        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.addDialect(new LayoutDialect());
    }

    public void publish(String template, Path path, Context context) throws IOException {
        context.setVariable("pathgen", pathGenerator);

        if (!Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());

        try (Writer writer = Files.newBufferedWriter(path)) {
            engine.process(template, context, writer);
        }
    }
}

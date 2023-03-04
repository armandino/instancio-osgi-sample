package org.sample.instancio.component;

import org.instancio.Instancio;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Component
public class SampleComponent {
    private static final Logger LOG = LoggerFactory.getLogger(SampleComponent.class);

    @Activate
    void activate(final BundleContext context) {
        final Person person = Instancio.create(Person.class);
        LOG.info("Created: {}", person);
        shutdown(context);
    }

    private static void shutdown(final BundleContext context) {
        CompletableFuture.runAsync(() -> {
            try {
                context.getBundle(0).stop();
            } catch (BundleException ex) {
                LOG.error("Error stopping bundle", ex);
            }
        });
    }

    private static class Person {
        String name;
        int age;

        private Person() {
            throw new AssertionError("Should instantiate bypassing constructor");
        }

        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d}", name, age);
        }
    }
}

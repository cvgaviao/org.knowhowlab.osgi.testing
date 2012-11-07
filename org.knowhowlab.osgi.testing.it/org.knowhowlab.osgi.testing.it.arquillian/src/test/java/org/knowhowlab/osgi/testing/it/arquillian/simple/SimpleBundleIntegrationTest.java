/*
 * Copyright (c) 2010-2012 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.knowhowlab.osgi.testing.it.arquillian.simple;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.osgi.spi.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.it.arquillian.simple.bundle.SimpleActivator;
import org.knowhowlab.osgi.testing.it.arquillian.simple.bundle.SimpleService;
import org.osgi.framework.*;

import javax.inject.Inject;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class SimpleBundleIntegrationTest {

    @Inject
    public Bundle bundle;

    @Deployment
    public static JavaArchive createdeployment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "example-bundle");
        archive.addClasses(SimpleActivator.class, SimpleService.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                builder.addBundleActivator(SimpleActivator.class);
                builder.addImportPackages(BundleActivator.class);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Test
    public void testBundleInjection() throws Exception {

        // Assert that the injected bundle
        assertNotNull("Bundle injected", bundle);
        assertEquals("example-bundle", bundle.getSymbolicName());
        assertEquals(Version.emptyVersion, bundle.getVersion());

        // Assert that the bundle is in state RESOLVED
        // Note when the test bundle contains the test case it
        // must be resolved already when this test method is called
        assertEquals(Bundle.RESOLVED, bundle.getState());

        // Start the bundle
        bundle.start();
        assertEquals(Bundle.ACTIVE, bundle.getState());

        // Get the service reference
        BundleContext context = bundle.getBundleContext();
        ServiceReference sref = context.getServiceReference(SimpleService.class.getName());
        assertNotNull("ServiceReference not null", sref);

        // Get the service for the reference
        SimpleService service = (SimpleService) context.getService(sref);
        assertNotNull("Service not null", service);

        // Invoke the service
        int sum = service.sum(1, 2, 3);
        assertEquals(6, sum);

        // Stop the bundle
        bundle.stop();
        assertEquals(Bundle.RESOLVED, bundle.getState());
    }
}

/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.concurrent.spec.Platform.dd;

import static ee.jakarta.tck.concurrent.common.TestGroups.JAKARTAEE_WEB;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import ee.jakarta.tck.concurrent.framework.TestClient;
import ee.jakarta.tck.concurrent.spi.context.IntContextProvider;
import ee.jakarta.tck.concurrent.spi.context.StringContextProvider;
import jakarta.enterprise.concurrent.spi.ThreadContextProvider;

/**
 * Covers context-service, managed-executor, managed-scheduled-executor,
 * and managed-thread-factory defined in a deployment descriptor.
 */
@Test(groups = JAKARTAEE_WEB)
public class DeploymentDescriptorWebTests extends TestClient{
    
    @ArquillianResource(DeploymentDescriptorServlet.class)
    URL baseURL;
    
    @Deployment(name="DeploymentDescriptorTests", testable=false)
    public static WebArchive createDeployment() {
        
        WebArchive war = ShrinkWrap.create(WebArchive.class, "DeploymentDescriptorTests_web.war")
                .addPackages(false,
                		DeploymentDescriptorWebTests.class.getPackage(),
                        getFrameworkPackage(),
                        getContextPackage(),
                        getContextProvidersPackage())
                .addAsServiceProvider(ThreadContextProvider.class.getName(),
                        IntContextProvider.class.getName(),
                        StringContextProvider.class.getName())
                .addAsWebInfResource(DeploymentDescriptorWebTests.class.getPackage(), "web.xml", "web.xml");


        return war;
    }
    
    @Override
    protected String getServletPath() {
        return "DeploymentDescriptorServlet";
    }
    
    @Test
    public void testDeploymentDescriptorDefinesContextService() {
        runTest(baseURL);
    }

    @Test
    public void testDeploymentDescriptorDefinesManagedExecutor() {
        runTest(baseURL);
    }

    @Test
    public void testDeploymentDescriptorDefinesManagedScheduledExecutor() {
        runTest(baseURL);
    }

    // Accepted TCK challenge: https://github.com/jakartaee/concurrency/issues/226
    @Test(enabled = false)
    public void testDeploymentDescriptorDefinesManagedThreadFactory() {
        runTest(baseURL);
    }
}

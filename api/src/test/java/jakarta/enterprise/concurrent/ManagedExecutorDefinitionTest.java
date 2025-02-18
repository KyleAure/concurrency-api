/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package jakarta.enterprise.concurrent;

import static jakarta.enterprise.concurrent.ContextServiceDefinition.APPLICATION;
import static jakarta.enterprise.concurrent.ContextServiceDefinition.SECURITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

@ManagedExecutorDefinition( // from ManagedExecutorDefinition JavaDoc
        name = "java:module/concurrent/MyExecutor",
        context = "java:module/concurrent/MyExecutorContext",
        hungTaskThreshold = 120000,
        maxAsync = 5)
@ContextServiceDefinition( // from ManagedExecutorDefinition JavaDoc, used by above
        name = "java:module/concurrent/MyExecutorContext",
        propagated = { SECURITY, APPLICATION })
@ManagedExecutorDefinition(
        name = "java:app/concurrent/ManagedExecutorDefinitionDefaults")
class ManagedExecutorDefinitionTest {

    // from ManagedExecutorDefinition JavaDoc
    @Resource(lookup = "java:module/concurrent/MyExecutor",
              name = "java:module/concurrent/env/MyExecutorRef")
    ManagedExecutorService myExecutor;

    /**
     * Validate the default values for ManagedExecutorDefinition.
     */
    @Test
    void testManagedExecutorDefinitionDefaultValues() throws Exception {
        ManagedExecutorDefinition def = null;
        for (ManagedExecutorDefinition anno : ManagedExecutorDefinitionTest.class.getAnnotationsByType(ManagedExecutorDefinition.class))
            if ("java:app/concurrent/ManagedExecutorDefinitionDefaults".equals(anno.name()))
                def = anno;
        assertNotNull(def);
        assertEquals(-1, def.hungTaskThreshold());
        assertEquals(-1, def.maxAsync());
        assertEquals("java:comp/DefaultContextService", def.context());
    }

    /**
     * Validate the example that is used in ManagedExecutorDefinition JavaDoc.
     */
    @Test
    void testManagedExecutorDefinitionJavaDocExample() throws Exception {
        ManagedExecutorDefinition def = null;
        for (ManagedExecutorDefinition anno : ManagedExecutorDefinitionTest.class.getAnnotationsByType(ManagedExecutorDefinition.class))
            if ("java:module/concurrent/MyExecutor".equals(anno.name()))
                def = anno;
        assertNotNull(def);
        assertEquals(120000, def.hungTaskThreshold());
        assertEquals(5, def.maxAsync());
        assertEquals("java:module/concurrent/MyExecutorContext", def.context());
    }
}

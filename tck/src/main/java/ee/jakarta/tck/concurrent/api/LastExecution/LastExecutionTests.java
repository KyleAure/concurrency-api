/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.concurrent.api.LastExecution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.concurrent.common.fixed.counter.CounterCallableTask;
import ee.jakarta.tck.concurrent.common.fixed.counter.CounterRunnableTask;
import ee.jakarta.tck.concurrent.common.fixed.counter.StaticCounter;
import ee.jakarta.tck.concurrent.framework.TestConstants;
import ee.jakarta.tck.concurrent.framework.junit.anno.Common;
import ee.jakarta.tck.concurrent.framework.junit.anno.Common.PACKAGE;
import ee.jakarta.tck.concurrent.framework.junit.anno.TestName;
import ee.jakarta.tck.concurrent.framework.junit.anno.Web;
import ee.jakarta.tck.concurrent.framework.junit.extensions.Wait;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutors;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.concurrent.ManagedTask;

@Web
@Common({ PACKAGE.FIXED_COUNTER })
public class LastExecutionTests {

    public static final String IDENTITY_NAME_TEST_ID = "lastExecutionGetIdentityNameTest";

    @Deployment(name = "LastExecutionTests")
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addPackages(true, LastExecutionTests.class.getPackage());
    }

    @BeforeEach
    public void reset() {
        StaticCounter.reset();
    }

    @TestName
    public String testname;

    @Resource(lookup = TestConstants.DefaultManagedScheduledExecutorService)
    public ManagedScheduledExecutorService scheduledExecutor;

    /*
     * @testName: lastExecutionGetIdentityNameTest
     *
     * @assertion_ids: CONCURRENCY:JAVADOC:15
     *
     * @test_Strategy: The name or ID of the identifiable object, as specified in
     * the ManagedTask#IDENTITY_NAME execution property of the task if it also
     * implements the ManagedTask interface.
     */
    @Test
    public void lastExecutionGetIdentityNameTest() {

        Map<String, String> executionProperties = new HashMap<String, String>();
        executionProperties.put(ManagedTask.IDENTITY_NAME, IDENTITY_NAME_TEST_ID);

        ScheduledFuture<?> sf = scheduledExecutor.schedule(
                ManagedExecutors.managedTask(new CounterRunnableTask(), executionProperties, null),
                new LogicDrivenTrigger(TestConstants.PollInterval.toMillis(), testname));
        Wait.waitTillFutureIsDone(sf);

        assertEquals(LogicDrivenTrigger.RIGHT_COUNT, // expected
                StaticCounter.getCount(), // actual
                "Got wrong identity name. See server log for more details.");
    }

    /*
     * @testName: lastExecutionGetResultTest
     *
     * @assertion_ids: CONCURRENCY:JAVADOC:16
     *
     * @test_Strategy: Result of the last execution.
     */
    @Test
    public void lastExecutionGetResultRunnableTest() {
        // test with runnable, LastExecution should return null
        ScheduledFuture<?> sf = scheduledExecutor.schedule(
                ManagedExecutors.managedTask(new CounterRunnableTask(), null, null),
                new LogicDrivenTrigger(TestConstants.PollInterval.toMillis(), testname));
        Wait.waitTillFutureIsDone(sf);

        assertEquals(LogicDrivenTrigger.RIGHT_COUNT, // expected
                StaticCounter.getCount(), // actual
                "Got wrong last execution result. See server log for more details.");
    }

    /*
     * @testName: lastExecutionGetResultTest
     *
     * @assertion_ids: CONCURRENCY:JAVADOC:16
     *
     * @test_Strategy: Result of the last execution.
     */
    @Test
    public void lastExecutionGetResultCallableTest() {
        // test with callable, LastExecution should return 1
        ScheduledFuture<?> sf = scheduledExecutor.schedule(
                ManagedExecutors.managedTask(new CounterCallableTask(), null, null),
                new LogicDrivenTrigger(TestConstants.PollInterval.toMillis(), testname));
        Wait.waitTillFutureIsDone(sf);

        assertEquals(LogicDrivenTrigger.RIGHT_COUNT, // expected
                StaticCounter.getCount(), // actual
                "Got wrong last execution result. See server log for more details.");
    }

    /*
     * @testName: lastExecutionGetRunningTimeTest
     *
     * @assertion_ids: CONCURRENCY:JAVADOC:17; CONCURRENCY:JAVADOC:18;
     * CONCURRENCY:JAVADOC:19
     *
     * @test_Strategy: The last time in which the task was completed.
     */
    @Test
    public void lastExecutionGetRunningTimeTest() {
        ScheduledFuture<?> sf = scheduledExecutor.schedule(
                ManagedExecutors.managedTask(new CounterRunnableTask(TestConstants.PollInterval), null, null),
                new LogicDrivenTrigger(TestConstants.PollInterval.toMillis(), testname));
        Wait.waitTillFutureIsDone(sf);
        assertEquals(LogicDrivenTrigger.RIGHT_COUNT, // expected
                StaticCounter.getCount(), // actual
                "Got wrong last execution result.");
    }

}

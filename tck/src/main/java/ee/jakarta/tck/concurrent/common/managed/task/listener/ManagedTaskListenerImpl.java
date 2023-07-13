/*
 * Copyright (c) 2013, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.concurrent.common.managed.task.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import ee.jakarta.tck.concurrent.framework.TestLogger;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.concurrent.ManagedTaskListener;

public class ManagedTaskListenerImpl implements ManagedTaskListener {

    private static final TestLogger log = TestLogger.get(ManagedTaskListenerImpl.class);

    private final List<ListenerEvent> events = Collections.synchronizedList(new ArrayList<ListenerEvent>());

    @Override
    public void taskAborted(final Future<?> future, final ManagedExecutorService mes, final Object arg2, final Throwable arg3) {
        events.add(ListenerEvent.ABORTED);
        log.info("task aborted");
    }

    @Override
    public void taskDone(final Future<?> future, final ManagedExecutorService mes, final Object arg2, final Throwable arg3) {
        events.add(ListenerEvent.DONE);
        log.info("task done");
    }

    @Override
    public void taskStarting(final Future<?> future, final ManagedExecutorService mes, final Object arg2) {
        events.add(ListenerEvent.STARTING);
        log.info("task starting");
    }

    @Override
    public void taskSubmitted(final Future<?> future, final ManagedExecutorService mes, final Object arg2) {
        events.add(ListenerEvent.SUBMITTED);
        log.info("task submitted");
    }

    public boolean eventCalled(final ListenerEvent event) {
        return events.contains(event);
    }

    public void clearEvents() {
        events.clear();
    }

    public void update(final ListenerEvent event) {
        events.add(event);
    }

    public List<ListenerEvent> events() {
        return events;
    }
}

/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import jdk.jshell.JShell;
import jdk.jshell.execution.JdiDefaultExecutionControl;
import jdk.jshell.execution.RemoteExecutionControl;

/**
 * Hang for three minutes (long enough to cause a timeout).
 */
class HangingRemoteAgent extends RemoteExecutionControl {

    private static final long DELAY = 4000L;
    private static final int TIMEOUT = 2000;
    private static final boolean INFRA_VERIFY = false;

    public static void main(String[] args) throws Exception {
        if (INFRA_VERIFY) {
            RemoteExecutionControl.main(args);
        } else {
            long end = System.currentTimeMillis() + DELAY;
            long remaining;
            while ((remaining = end - System.currentTimeMillis()) > 0L) {
                try {
                    Thread.sleep(remaining);
                } catch (InterruptedException ex) {
                    // loop again
                }
            }
        }
    }

    static JShell state(boolean isLaunch, String host) {
        return JShell.builder().executionEngine(
                JdiDefaultExecutionControl.create(
                        HangingRemoteAgent.class.getName(),
                        isLaunch,
                        host,
                        TIMEOUT)).build();
    }

}

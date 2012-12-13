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

package org.knowhowlab.osgi.testing.utils;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * OSGi Services utilities class
 *
 * @author dmytro.pishchukhin
 * @version 1.1
 * @see org.osgi.framework.BundleContext
 * @see org.osgi.util.tracker.ServiceTracker
 * @see org.osgi.util.tracker.ServiceTrackerCustomizer
 * @see org.osgi.framework.ServiceEvent
 * @see org.osgi.framework.ServiceListener
 * @see org.osgi.framework.Filter
 */
public class ServiceUtils {
    /**
     * Utility class. Only static methods are available.
     */
    private ServiceUtils() {
    }

    /**
     * Get ServiceReference by filter
     *
     * @param bc     BundleContext
     * @param filter filter
     * @return ServiceReference instance or <code>null</code>
     * @throws NullPointerException If <code>bc</code> or <code>filter</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, Filter filter) {
        ServiceTracker tracker = new ServiceTracker(bc, filter, null);
        tracker.open();
        try {
            return tracker.getServiceReference();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by filter with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param filter          filter
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, Filter filter, long timeoutInMillis) {
        return getServiceReference(bc, filter, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get ServiceReference by filter with timeout.
     *
     * @param bc       BundleContext
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit time unit for the time interval
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, Filter filter, long timeout, TimeUnit timeUnit) {
        CountDownLatch latch = new CountDownLatch(1);

        long timeoutInMillis = timeUnit.toMillis(timeout);
        ServiceTracker tracker = new ServiceTracker(bc, filter, new ServiceTrackerCustomizerWithLock(bc, latch));
        tracker.open();
        try {
            return waitForServiceReference(tracker, timeoutInMillis, latch);
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by class name
     *
     * @param bc        BundleContext
     * @param className className
     * @return ServiceReference instance or <code>null</code>
     * @throws NullPointerException If <code>bc</code> or <code>className</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, String className) {
        ServiceTracker tracker = new ServiceTracker(bc, className, null);
        tracker.open();
        try {
            return tracker.getServiceReference();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by class name with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param className       className
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>className</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, String className, long timeoutInMillis) {
        return getServiceReference(bc, className, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get ServiceReference by class name with timeout.
     *
     * @param bc        BundleContext
     * @param className className
     * @param timeout   time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit  time unit for the time interval
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>className</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, String className, long timeout, TimeUnit timeUnit) {
        CountDownLatch latch = new CountDownLatch(1);

        long timeoutInMillis = timeUnit.toMillis(timeout);
        ServiceTracker tracker = new ServiceTracker(bc, className, new ServiceTrackerCustomizerWithLock(bc, latch));
        tracker.open();
        try {
            return waitForServiceReference(tracker, timeoutInMillis, latch);
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by class
     *
     * @param bc    BundleContext
     * @param clazz Class
     * @return ServiceReference instance or <code>null</code>
     * @throws NullPointerException If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, Class clazz) {
        ServiceTracker tracker = new ServiceTracker(bc, clazz.getName(), null);
        tracker.open();
        try {
            return tracker.getServiceReference();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by class with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param clazz           Class
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, Class clazz, long timeoutInMillis) {
        return getServiceReference(bc, clazz, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get ServiceReference by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param timeout  time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit time unit for the time interval
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @since 1.0
     */
    public static ServiceReference getServiceReference(BundleContext bc, Class clazz, long timeout, TimeUnit timeUnit) {
        return getServiceReference(bc, clazz.getName(), timeout, timeUnit);
    }

    /**
     * Get service instance by filter
     *
     * @param bc     BundleContext
     * @param filter filter
     * @return service instance or <code>null</code>
     * @throws NullPointerException If <code>bc</code> or <code>filter</code> are <code>null</code>
     * @since 1.0
     */
    public static Object getService(BundleContext bc, Filter filter) {
        ServiceTracker tracker = new ServiceTracker(bc, filter, null);
        tracker.open();
        try {
            return tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by filter with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param filter          filter
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     * @since 1.0
     */
    public static Object getService(BundleContext bc, Filter filter, long timeoutInMillis) {
        return getService(bc, filter, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by filter with timeout.
     *
     * @param bc       BundleContext
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @since 1.0
     */
    public static Object getService(BundleContext bc, Filter filter, long timeout, TimeUnit timeUnit) {
        ServiceTracker tracker = new ServiceTracker(bc, filter, null);
        tracker.open();
        try {
            return tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class name
     *
     * @param bc        BundleContext
     * @param className className
     * @return service instance or <code>null</code>
     * @throws NullPointerException If <code>bc</code> or <code>className</code> are <code>null</code>
     * @since 1.0
     */
    public static Object getService(BundleContext bc, String className) {
        ServiceTracker tracker = new ServiceTracker(bc, className, null);
        tracker.open();
        try {
            return tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class name with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param className       className
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>className</code> are <code>null</code>
     * @since 1.0
     */
    public static Object getService(BundleContext bc, String className, long timeoutInMillis) {
        return getService(bc, className, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by className with timeout.
     *
     * @param bc        BundleContext
     * @param className className
     * @param timeout   time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit  time unit for the time interval
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>className</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @since 1.0
     */
    public static Object getService(BundleContext bc, String className, long timeout, TimeUnit timeUnit) {
        ServiceTracker tracker = new ServiceTracker(bc, className, null);
        tracker.open();
        try {
            return tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class
     *
     * @param bc    BundleContext
     * @param clazz Class
     * @return service instance instance or <code>null</code>
     * @throws NullPointerException If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz) {
        ServiceTracker tracker = new ServiceTracker(bc, clazz.getName(), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param clazz           Class
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, long timeoutInMillis) {
        return getService(bc, clazz, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param timeout  time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>clazz</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, long timeout, TimeUnit timeUnit) {
        ServiceTracker tracker = new ServiceTracker(bc, clazz.getName(), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class
     *
     * @param bc     BundleContext
     * @param clazz  Class
     * @param filter filter
     * @return service instance instance or <code>null</code>
     * @throws NullPointerException   If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException If <code>filter</code> contains an
     *                                invalid filter string that cannot be parsed
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, String filter) throws InvalidSyntaxException {
        return getService(bc, clazz, FrameworkUtil.createFilter(filter));
    }

    /**
     * Get service instance by class with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param clazz           Class
     * @param filter          filter
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, String filter, long timeoutInMillis) throws InvalidSyntaxException {
        return getService(bc, clazz, filter, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>clazz</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, String filter, long timeout, TimeUnit timeUnit) throws InvalidSyntaxException {
        return getService(bc, clazz, FrameworkUtil.createFilter(filter), timeout, timeUnit);
    }

    /**
     * Get service instance by class
     *
     * @param bc     BundleContext
     * @param clazz  Class
     * @param filter filter
     * @return service instance instance or <code>null</code>
     * @throws NullPointerException   If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException If <code>filter</code> contains an
     *                                invalid filter string that cannot be parsed
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, Filter filter) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FilterUtils.create(clazz, filter), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class with timeoutInMillis.
     *
     * @param bc              BundleContext
     * @param clazz           Class
     * @param filter          filter
     * @param timeoutInMillis time interval in milliseconds to wait. If zero, the method will wait indefinitely.
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeoutInMillis is negative
     * @throws NullPointerException     If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, Filter filter, long timeoutInMillis) throws InvalidSyntaxException {
        return getService(bc, clazz, filter, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinitely.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>clazz</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @since 1.0
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, Filter filter, long timeout, TimeUnit timeUnit) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FilterUtils.create(clazz, filter), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    public static ServiceEvent waitForServiceEvent(BundleContext bc, Filter filter, int eventTypeMask, long timeoutInMillis) {
        return waitForServiceEvent(bc, filter, eventTypeMask, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    public static ServiceEvent waitForServiceEvent(BundleContext bc, String className, int eventTypeMask, long timeoutInMillis) {
        return waitForServiceEvent(bc, className, eventTypeMask, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    public static ServiceEvent waitForServiceEvent(BundleContext bc, Class clazz, int eventTypeMask, long timeoutInMillis) {
        return waitForServiceEvent(bc, clazz, eventTypeMask, timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    public static ServiceEvent waitForServiceEvent(BundleContext bc, Filter filter, int eventTypeMask, long timeout, TimeUnit timeUnit) {
        return null; // todo
    }

    public static ServiceEvent waitForServiceEvent(BundleContext bc, String className, int eventTypeMask, long timeout, TimeUnit timeUnit) {
        return null; // todo
    }

    public static ServiceEvent waitForServiceEvent(BundleContext bc, Class clazz, int eventTypeMask, long timeout, TimeUnit timeUnit) {
        return null; // todo
    }

    /**
     * Wait for at least one ServiceReference to be tracked by ServiceTracker
     *
     * @param tracker         ServiceTracker
     * @param timeoutInMillis time interval in milliseconds to wait.
     *                        If zero, the method will wait indefinitely.
     * @param latch           external latch that is used to handle new service adding to ServiceTracker
     * @return ServiceReference instance or <code>null</code>
     * @throws IllegalArgumentException If the value of timeout is negative.
     * @throws InterruptedException     If another thread has interrupted the current thread.
     */
    private static ServiceReference waitForServiceReference(ServiceTracker tracker, long timeoutInMillis, CountDownLatch latch)
            throws InterruptedException {
        if (timeoutInMillis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        ServiceReference reference = tracker.getServiceReference();
        if (reference == null) {
            if (latch.await(timeoutInMillis, TimeUnit.MILLISECONDS)) {
                return tracker.getServiceReference();
            } else {
                return null;
            }
        } else {
            return reference;
        }
    }

    /**
     * ServiceTrackerCustomizer with lock support.
     *
     * @see java.util.concurrent.locks.ReentrantLock
     * @see org.osgi.util.tracker.ServiceTrackerCustomizer
     */
    private static class ServiceTrackerCustomizerWithLock implements ServiceTrackerCustomizer {
        private final BundleContext bc;
        private final CountDownLatch latch;

        public ServiceTrackerCustomizerWithLock(BundleContext bc, CountDownLatch latch) {
            this.bc = bc;
            this.latch = latch;
        }

        public Object addingService(ServiceReference serviceReference) {
            try {
                return bc.getService(serviceReference);
            } finally {
                latch.countDown();
            }
        }

        public void modifiedService(ServiceReference serviceReference, Object o) {
        }

        public void removedService(ServiceReference serviceReference, Object o) {
        }
    }
}

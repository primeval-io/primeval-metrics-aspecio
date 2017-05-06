package io.primeval.metrics.aspect.internal;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.promise.Promise;

import com.codahale.metrics.Timer.Context;

import io.primeval.aspecio.aspect.annotations.Aspect;
import io.primeval.metrics.Metrics;
import io.primeval.metrics.annotation.Measured;
import io.primeval.metrics.aspect.MetricsAspect;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.annotation.AnnotationInterceptor;
import io.primeval.reflex.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflex.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflex.proxy.handler.CharInterceptionHandler;
import io.primeval.reflex.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflex.proxy.handler.FloatInterceptionHandler;
import io.primeval.reflex.proxy.handler.IntInterceptionHandler;
import io.primeval.reflex.proxy.handler.InterceptionHandler;
import io.primeval.reflex.proxy.handler.LongInterceptionHandler;
import io.primeval.reflex.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflex.proxy.handler.VoidInterceptionHandler;

@Component
@Aspect(provides = MetricsAspect.class, extraProperties = MetricsAspect.MEASURED_PROPERTY)
public final class AnnotatedMetricInterceptorImpl implements AnnotationInterceptor<Measured> {

    private Metrics metrics;

    private final Map<CallContext, String> methodIds = new ConcurrentHashMap<>();

    @Override
    public <T, E extends Throwable> T onCall(Measured annotation, CallContext callContext,
            InterceptionHandler<T> handler)
            throws E {
        String methodid = methodIds.computeIfAbsent(callContext, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();

        boolean async = Promise.class.isAssignableFrom(callContext.method.getReturnType());

        try {
            T result = handler.invoke();
            if (async) {
                Context resolveTimer = metrics.timer(methodid + "::promise").time();
                Promise<?> pms = (Promise<?>) result;
                pms.onResolve(() -> resolveTimer.close());
            }
            return result;
        } finally {
            syncTimer.close();
        }
    }

    @Override
    public <E extends Throwable> boolean onCall(Measured annotation, CallContext context,
            BooleanInterceptionHandler handler) throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }

    @Override
    public <E extends Throwable> byte onCall(Measured annotation, CallContext context, ByteInterceptionHandler handler)
            throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> char onCall(Measured annotation, CallContext context, CharInterceptionHandler handler)
            throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> double onCall(Measured annotation, CallContext context,
            DoubleInterceptionHandler handler) throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> float onCall(Measured annotation, CallContext context,
            FloatInterceptionHandler handler) throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> int onCall(Measured annotation, CallContext context, IntInterceptionHandler handler)
            throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> long onCall(Measured annotation, CallContext context, LongInterceptionHandler handler)
            throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> short onCall(Measured annotation, CallContext context,
            ShortInterceptionHandler handler) throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            return handler.invoke();
        } finally {
            syncTimer.close();
        }
    }
    
    @Override
    public <E extends Throwable> void onCall(Measured annotation, CallContext context, VoidInterceptionHandler handler)
            throws E {
        String methodid = methodIds.computeIfAbsent(context, AnnotatedMetricInterceptorImpl::methodId);
        Context syncTimer = metrics.timer(methodid).time();
        try {
            handler.invoke();
        } finally {
            syncTimer.close();
        }
    }

    public static String methodId(CallContext cc) {
        Method method = cc.method;
        String classAndMethodName = "." + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "("
                + cc.parameters.stream().map(p -> p.getType().getSimpleName()).collect(Collectors.joining(","))
                + ")";
        String id = Stream.of(method.getDeclaringClass().getPackage().getName().split("\\."))
                .map(s -> s.subSequence(0, 1)).collect(
                        Collectors.joining(".", "", classAndMethodName));
        return id;
    }

    @Override
    public Class<Measured> intercept() {
        return Measured.class;
    }

    @Override
    public String toString() {
        return "MetricsInterceptor";
    }

    @Reference
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

}

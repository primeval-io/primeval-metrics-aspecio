# primeval-metrics-aspecio [![Build Status](https://travis-ci.org/primeval-io/primeval-metrics-aspect.svg?branch=master)](https://travis-ci.org/primeval-io/primeval-metrics-aspecio) [![Gitter primeval-io/Lobby](https://badges.gitter.im/primeval-io/Lobby.svg)](https://gitter.im/primeval-io/Lobby)


Uses [Aspecio](https://github.com/primeval-io/aspecio) to provide an aspect named `MetricsAspect`. It measures the execution time of methods annotated with the `@Measured` annotation.


# Maven coordinates


```xml

	<groupId>io.primeval</groupId>
	<artifactId>primeval-metrics-aspecio</artifactId>
	<version>1.0.0-SNAPSHOT</version>
```

Until a stable release, the snapshot version is available in the Sonatype OSS Snapshots repository.


# Dependencies

Primeval Reflex requires Java 8 and depends on [Dropwizard Metrics](https://github.com/dropwizard/metrics).

```xml
	<dependency>
		<groupId>io.primeval</groupId>
		<artifactId>primeval-metrics</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>
```

# Aspects provided

Aspect `MetricsAspect` is provided.

When active, this aspects uses a Metrics `timer` to time execution time of methods annotated with the `@Measured` annotation.

The timer is named from the method identifier: package names are short-form (initials only), exceptions and return types are ignored. For instance, a method identifier might look as `i.p.m.i.AnnotatedMetricInterceptorImpl.setMetrics(Metrics)`.

Counters under these names represent the time before a method returns (normally or exceptionally). 

If a method returns an OSGi `Promise` (as used in [Primeval Codex](https://github.com/primeval-io/primeval-codex)), then another timer is used and has the form `<methodId>::promise`. It represents the time until a `Promise` is resolved (successfully or not). A method returning a Promise should ideally be non-blocking and have negligible execution time, while the promise resolution time should display actual performance.

Also note that a service property named `primeval.metrics.measured` will be set to `Boolean.TRUE` to your service if it is properly woven with that aspect. 

# Adding MetricsAspect to your service

To add MetricsAspect to your service, set your OSGi service property `service.aspect.weave.optional` (or `service.aspect.weave` if you want mandatory weaving) to `io.primeval.metrics.aspect.MetricsAspect`. Don't forget your implementation must add `@Measured` to intercepted methods you wish to measure (Aspecio and Primeval Metrics must be active).

If you use Declarative Services, bnd and the Primeval's [`bnd-dsap-plugin`](https://github.com/primeval-io/bnd-dsap-plugin), you can measure a component this way:

```java
@Component
@Weave(optional = MetricsAspect.class)
public final class HelloGoodbyeImpl implements Hello, Goodbye {

    @Measured
    public String hello() {
        return "hello";
    }

    public String goodbye() {
        return "goodbye";
    }

}

``` 


# Getting help

Post a new GitHub issue or join on [Gitter](https://gitter.im/primeval-io/Lobby).
 

# Author

primeval-metrics-aspecio was developed by Simon Chemouil.

# Copyright

(c) 2016-2017, Simon Chemouil, Lambdacube

primeval-metrics-aspecio is part of the Primeval project.
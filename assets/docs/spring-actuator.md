# Spring Actuator

## 1. Spring Actuator

Definition of Actuator:
``
An actuator is a manufacturing term that refers to a mechanical device for moving or controlling something. 
Actuators can generate a large amount of motion from a small change.
``

Definition of Spring Actuator:
``
Spring Actuator helps to manage and monitor the application(production ready) by using HTTP endpoints or with JMX. 
Auditing, health, and metrics gathering can also be automatically applied to the application.
``

Note: All timestamps that are consumed by the endpoints, either as query parameters or in the request body, must be
formatted as an offset date and time as specified in **ISO 8601**.

## 2. Endpoints


- `auditevents` Exposes audit events information for the current application. Requires an AuditEventRepository bean.
- `beans` Displays a complete list of all the Spring beans in your application.
- `caches` Exposes available caches.
- `conditions` Shows the conditions that were evaluated on configuration and auto-configuration classes and the reasons why they did or did not match.
- `configprops` Displays a collated list of all @ConfigurationProperties.
- `env` Exposes properties from Spring’s ConfigurableEnvironment.
- `flyway` Shows any Flyway database migrations that have been applied. Requires one or more Flyway beans.
- `health` Shows application health information.
- `httptrace` Displays HTTP trace information (by default, the last 100 HTTP request-response exchanges). Requires an HttpTraceRepository bean.
- `info` Displays arbitrary application info.
- `integrationgraph` Shows the Spring Integration graph. Requires a dependency on spring-integration-core.
- `loggers` Shows and modifies the configuration of loggers in the application.
- `liquibase` Shows any Liquibase database migrations that have been applied. Requires one or more Liquibase beans.
- `metrics` Shows ‘metrics’ information for the current application.
- `mappings` Displays a collated list of all @RequestMapping paths.
- `scheduledtasks` Displays the scheduled tasks in your application.
- `sessions` Allows retrieval and deletion of user sessions from a Spring Session-backed session store. Requires a
  Servlet-based web application using Spring Session.
- `shutdown` Lets the application be gracefully shutdown. Disabled by default.
- `startup` Shows the startup steps data collected by the ApplicationStartup. Requires the SpringApplication to be configured with a BufferingApplicationStartup.
- `threaddump` Performs a thread dump.
- `heapdump` Returns an hprof heap dump file. Requires a HotSpot JVM.
- `jolokia` Exposes JMX beans over HTTP (when Jolokia is on the classpath, not available for WebFlux). Requires a
  dependency on jolokia-core.
- `logfile` Returns the contents of the logfile (if logging.file.name or logging.file.path properties have been set).
  Supports the use of the HTTP Range header to retrieve part of the log file’s content.
- `prometheus` Exposes metrics in a format that can be scraped by a Prometheus server. Requires a dependency on
  micrometer-registry-prometheus.

Base path for actuator endpoints is configurable by `management.endpoints.web.base-path` property (default is 
`/actuator`), server port also configured by `management.server.port=8081` property. (**server address**, **ssl 
certification** also configurable).

By default, all endpoints except for **shutdown** are enabled. To configure the enablement of an endpoint, use its 
`management.endpoint.<id>.enabled` property. The following example enables the **shutdown** endpoint: 
`management.endpoint.shutdown.enabled=true`

By default, most of the available endpoints are exposed as **JMX**, but only **health** endpoint are exposed from HTTP.
Endpoints are exposed by `management.endpoints.${jmx or http}.exposure.${include or exclude}` properties, regarding 
values it would be single or multiple ID(s) [endpoint's id] or asterisk (`management.endpoints.jmx.exposure.include=*`).

Note: If you expose it to the public, also consider enabling security [more details](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.security).

Endpoints automatically cache responses to read operations that do not take any parameters. 
To configure the amount of time for which an endpoint will cache a response, 
use its `cache.time-to-live` property. 
The following example sets the time-to-live of the beans endpoint’s cache to **10 seconds**:
`management.endpoint.beans.cache.time-to-live=10s`

A “discovery page” is added with links to all the endpoints.
To disable the “discovery page”, add the following property to your application properties:
`management.endpoints.web.discovery.enabled=false`

Cross-origin resource sharing (CORS) is a W3C specification that lets you specify in a flexible
way what kind of cross-domain requests are authorized. 
The following configuration permits GET and POST calls from the example.com domain
`management.endpoints.web.cors.allowed-origins=https://example.com`
`management.endpoints.web.cors.allowed-methods=GET,POST`

For implementing custom endpoints please [read document](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.implementing-custom) .

You can use health information to check the status of your running application. 
It is often used by monitoring software to alert someone when a production system goes down. 
The information exposed by the health endpoint depends on the `management.endpoint.health.show-details` and 
`management.endpoint.health.show-components` properties which can be configured with one of the following values:

| Name | Description |
| ---- | ----------- |
| never | Details are never shown. |
| when-authorized | Details are only shown to authorized users. Authorized roles can be configured using `management.endpoint.health.roles`. |
| always | Details are shown to all users. |

The default value is **never**.

You can also create health groups like `management.endpoint.health.group.custom.include=db`, **readiness** and 
**liveness** are health groups created for kubernetes to check states of the application.

Startup Lifecycle

| Startup phase | LivenessState | ReadinessState | HTTP server | Notes |
| ------------- | ------------- | -------------- | ----------- | ----- |
| Starting | BROKEN | REFUSING_TRAFFIC | Not started | Kubernetes checks the "liveness" Probe and restarts the application if it takes too long. |
| Started | CORRECT | REFUSING_TRAFFIC | Refuses requests | The application context is refreshed. The application performs startup tasks and does not receive traffic yet. |
| Ready | CORRECT | ACCEPTING_TRAFFIC | Accepts requests | Startup tasks are finished. The application is receiving traffic. |

Shutdown Lifecycle

| Shutdown phase | LivenessState | ReadinessState | HTTP server | Notes |
| ------------- | ------------- | -------------- | ----------- | ----- |
| Running | CORRECT | ACCEPTING_TRAFFIC | Accepts requests | Shutdown has been requested. |
| Graceful shutdown | CORRECT | REFUSING_TRAFFIC | New requests are rejected | If enabled, graceful shutdown processes in-flight requests. |
| Shutdown complete | N/A | N/A | Server is shut down | The application context is closed and the application is shut down. |

Note: When you configure readiness/liveness probe also consider `spring.lifecycle.timeout-per-shutdown-phase` make 
health checks less than it (default is 30 seconds).

### Audit Events `/auditevents`

Exposes audit events information for the current application. Requires an **AuditEventRepository** bean (by default 
**InMemoryAuditEventRepository** will be used).

## Metrics `/metrics`

Supported monitoring systems metrics

- AppOptics
- Atlas
- Datadog
- Dynatrace
- Elastic
- Ganglia
- Graphite
- Humio
- Influx
- JMX
- KairosDB
- New Relic
- Prometheus
- SignalFx
- Simple (in-memory)
- Stackdriver
- StatsD
- Wavefront

You can also create your own metrics, check micrometer documentation.

Credits:
- [Spring Boot Actuator Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Actuator API Docs](https://docs.spring.io/spring-boot/docs/2.5.6/actuator-api/htmlsingle/)
- [Micrometer Docs](https://micrometer.io/docs/concepts)

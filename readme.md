# Spring Actuator Demo

## 1. Requirements

- Unix/Linux OS for Makefile
- Docker and Docker Compose

## 2. Installation

- `make set-up` - for setting up a docker network
- `make up` - for starting spring-actuator-demo, prometheus, grafana apps
- `make down` - for shutdown spring-actuator-demo, prometheus, grafana apps

Ps: Each app also is runnable individually by `make spring-actuator-up` or `make spring-actuator-down`.

## 3. Accessibility & Configuration

- **Spring Actuator Demo** will be available at `http://localhost:8080`
- **Prometheus** will be available at `http://localhost:9090`
- **Grafana** will be available at `http://localhost:3000`

Note: Grafana will require authentication please use `admin` `admin` as username and password for 
signing in.

Choose micrometer Dashboard:
![Choose micrometer dashboard](./assets/images/choose_micrometer_dashboard.png)

Micrometer JVM Dashboard:
![Chose micrometer dashboard](./assets/images/micrometer_dashboard.png)

For more details please click [here](./assets/docs/spring-actuator.md).
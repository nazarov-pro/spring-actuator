set-up:
	@docker network create monitoring

clean:
	@docker network rm monitoring

spring-actuator-build:
	@./gradlew clean build

spring-actuator-prometheus-up: spring-actuator-build
	@PROMETHEUS_ENABLED=true docker-compose -f configs/spring-actuator/docker-compose.yaml up --build --force-recreate -d

spring-actuator-down:
	@docker-compose -f configs/spring-actuator/docker-compose.yaml down

spring-actuator-elk-up: spring-actuator-build
	@ELASTIC_ENABLED=true docker-compose -f configs/spring-actuator/docker-compose.yaml up --build --force-recreate -d


spring-actuator-up: spring-actuator-build
	@docker-compose -f configs/spring-actuator/docker-compose.yaml up --build --force-recreate -d

spring-actuator-restart: spring-actuator-down spring-actuator-up

prometheus-up:
	@docker-compose -f configs/prometheus/docker-compose.yaml up --build --force-recreate -d

prometheus-down:
	@docker-compose -f configs/prometheus/docker-compose.yaml down

grafana-up:
	@docker-compose -f configs/grafana/docker-compose.yaml up --build --force-recreate -d

grafana-down:
	@docker-compose -f configs/grafana/docker-compose.yaml down

elk-up:
	@docker-compose -f configs/elk/docker-compose.yaml up --build --force-recreate -d
	@sh ./configs/elk/kibana-set-up.sh

elk-down:
	@docker-compose -f configs/elk/docker-compose.yaml down

prometheus-grafana-actuator-up: spring-actuator-prometheus-up prometheus-up grafana-up

prometheus-grafana-actuator-down: grafana-down prometheus-down spring-actuator-down

elk-actuator-up: elk-up spring-actuator-elk-up

elk-actuator-down: spring-actuator-down elk-down


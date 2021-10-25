set-up:
	@docker network create monitoring

clean:
	@docker network rm monitoring

spring-actuator-build:
	@./gradlew clean build

spring-actuator-up: spring-actuator-build
	@docker-compose -f configs/spring-actuator/docker-compose.yaml up --build --force-recreate -d

spring-actuator-down:
	@docker-compose -f configs/spring-actuator/docker-compose.yaml down

spring-actuator-restart: spring-actuator-down spring-actuator-up

prometheus-up:
	@docker-compose -f configs/prometheus/docker-compose.yaml up --build --force-recreate -d

prometheus-down:
	@docker-compose -f configs/prometheus/docker-compose.yaml down

grafana-up:
	@docker-compose -f configs/grafana/docker-compose.yaml up --build --force-recreate -d

grafana-down:
	@docker-compose -f configs/grafana/docker-compose.yaml down

up: spring-actuator-up prometheus-up grafana-up

down: grafana-down prometheus-down spring-actuator-down

# celeste-users

Proyecto ejemplo Spring Boot 4 (Milestone) + WebFlux + R2DBC + MapStruct + Lombok
Package base: `com.celeste.users`
API base: `/api/v1/users`

## Comandos útiles

- Build (JVM jar): `./gradlew clean bootJar`
- Run: `./gradlew bootRun`
- Build native image (GraalVM): `./gradlew nativeCompile`
- Docker build (JVM): `docker build -f Dockerfile.jvm -t celeste-users:jvm .`
- Docker build (native): `docker build -f Dockerfile.native -t celeste-users:native .`


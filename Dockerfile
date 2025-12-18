FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

# 의존성 파일 복사
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --no-daemon || true

# 소스 코드 복사
COPY src src

# 빌드
RUN ./gradlew build -x test --no-daemon

# 실행
EXPOSE 8080

# 도커 프로필 활성화 추가
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "build/libs/*.jar"]
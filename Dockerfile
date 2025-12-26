FROM eclipse-temurin:21-jdk-alpine

# Build Argument로 모듈명 받기 (기본값: load-test-db)
ARG MODULE_NAME=load-test-db

WORKDIR /app
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

# 의존성 파일 복사 (루트 + common + 선택한 모듈)
COPY build.gradle settings.gradle ./
COPY common/build.gradle common/build.gradle
COPY ${MODULE_NAME}/build.gradle ${MODULE_NAME}/build.gradle

# 소스 코드 복사 (멀티 모듈)
COPY common common
COPY ${MODULE_NAME} ${MODULE_NAME}

# 빌드 (선택한 모듈 빌드)
RUN ./gradlew :${MODULE_NAME}:build -x test --no-daemon

# 실행
EXPOSE 8080

# ENV로 설정하여 런타임에 사용 가능하게 함
ENV MODULE_NAME=${MODULE_NAME}

# 도커 프로필 활성화 추가 (쉘 형식 사용하여 변수 확장)
ENTRYPOINT sh -c "java -Dspring.profiles.active=docker -jar ${MODULE_NAME}/build/libs/*.jar"
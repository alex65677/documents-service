## Описание

Full-stack приложение - справочник документов

## Структура

- `backend` - Backend на Java, Spring (Boot, Web, Data JPA, AOP), PostgreSQL, Kafka.
- `ui` - Frontend на React + Redux.

## Подготовка

Установите:

- [Node.js](https://nodejs.org) - Frontend
- [JDK 15](https://openjdk.java.net) - Java 15
- [Docker](https://docker.com)

## Запуск
Сборка frontend:
```
./gradlew ui:npm_run_build
```
Сборка jar:
```
./gradlew backend:bootJar
```
Запуск:
```
docker-compose up -d
```

## Адрес страницы
После запуска приложение доступно в браузере по адресу:
```
http://localhost:9000/#/
```

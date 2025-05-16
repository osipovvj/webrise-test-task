# WebRise Test Task

## Запуск приложения

### Через Docker Compose

1. Убедитесь, что установлены [Docker](https://www.docker.com/products/docker-desktop/) и [Docker Compose](https://docs.docker.com/compose/).
2. В корне проекта выполните команду:
   ```
   docker compose -f compose.yaml up --build
   ```
3. Приложение будет доступно по адресу: [http://localhost:8080](http://localhost:8080)

### Локальный запуск через Maven

1. Убедитесь, что установлены [Java 17+](https://adoptium.net/) и [Maven](https://maven.apache.org/).
2. Запустите PostgreSQL с параметрами:
   - БД: `webrisedb`
   - Пользователь: `webriseuser`
   - Пароль: `webrisesecret`
   - Порт: `5432`
3. Соберите и запустите приложение:
   ```
   mvn spring-boot:run
   ```
4. Приложение будет доступно по адресу: [http://localhost:8080](http://localhost:8080)

### Swagger UI

Документация API доступна по адресу: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

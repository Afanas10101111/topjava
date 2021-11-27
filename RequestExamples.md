# Примеры запросов для тестирования MealRestController

### 1. getAll
    curl --request GET \
    --url http://localhost:8080/topjava/rest/meals
### 2. get
    curl --request GET \
    --url http://localhost:8080/topjava/rest/meals/100008
### 3. getBetweenLocalDateTime
    curl --request GET \
    --url 'http://localhost:8080/topjava/rest/meals/filtered?startDate=2011-12-03&endDate=2021-11-03&startTime=00%3A00&endTime=23%3A59'
### 4. createWithLocation
    curl --request POST \
    --url http://localhost:8080/topjava/rest/meals \
    --header 'Content-Type: application/json' \
    --data '{
    "dateTime": "2021-11-30T20:00:00",
    "description": "Ужин",
    "calories": 510
    }'
### 5. update
    curl --request PUT \
    --url http://localhost:8080/topjava/rest/meals/100008 \
    --header 'Content-Type: application/json' \
    --data '{
    "id": 100008,
    "dateTime": "2020-01-31T20:00:00",
    "description": "Ужин",
    "calories": 51
    }'
### 6. delete
    curl --request DELETE \
    --url http://localhost:8080/topjava/rest/meals/100008 

package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.MEAL_TO_MATCHER;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.MealTestData.mealTos;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService service;

    @Test
    void getAll() throws Exception {
        performCheck(MockMvcRequestBuilders.get(REST_URL), status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void get() throws Exception {
        performCheck(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID), status().isOk())
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getBetweenLocalDateTime() throws Exception {
        performCheck(MockMvcRequestBuilders.get(
                REST_URL + "between?startDate=2011-12-03T00:00:00&startTime=2011-12-03T00:00:00" +
                        "&endDate=2021-12-03T10:15:30&endTime=2011-12-03T23:59:59"
        ), status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void getBetweenGoodOldLocalDateTime() throws Exception {
        performCheck(MockMvcRequestBuilders.get(
                REST_URL + "between?startDate=2011-12-03T00:00:00&startTime=2011-12-03T00:00:00" +
                        "&endDate=2011-12-03T10:15:30&endTime=2011-12-03T23:59:59"
        ), status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal aNew = getNew();
        ResultActions result = performCheck(
                MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(aNew)),
                status().isCreated()
        );
        Meal created = MEAL_MATCHER.readFromJson(result);
        int id = created.id();
        aNew.setId(id);
        MEAL_MATCHER.assertMatch(created, aNew);
        MEAL_MATCHER.assertMatch(service.get(id, USER_ID), aNew);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        performWithoutContentTypeCheck(
                MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
        );
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void delete() throws Exception {
        performWithoutContentTypeCheck(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }
}

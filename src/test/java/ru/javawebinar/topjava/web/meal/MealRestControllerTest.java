package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Collections;
import java.util.List;

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
        performAndCheckJsonContent(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void get() throws Exception {
        performAndCheckJsonContent(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getBetweenLocalDateTime() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("startDate", List.of("2011-12-03"));
        params.put("startTime", List.of("00:00"));
        params.put("endDate", List.of("2021-12-03"));
        params.put("endTime", List.of("23:59"));
        performAndCheckJsonContent(MockMvcRequestBuilders.get(REST_URL + "filtered").params(params))
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void getBetweenGoodOldTimes() throws Exception {
        performAndCheckJsonContent(MockMvcRequestBuilders.get(REST_URL + "filtered").param("endDate", "2011-12-03"))
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    void getBetweenWrongDateFormat() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filtered").param("endDate" , "2011-12-03T10:15:30"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithLocation() throws Exception {
        Meal aNew = getNew();
        ResultActions result = performAndCheckJsonContent(
                MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(aNew))
        )
                .andExpect(status().isCreated());
        Meal created = MEAL_MATCHER.readFromJson(result);
        int id = created.id();
        aNew.setId(id);
        MEAL_MATCHER.assertMatch(created, aNew);
        MEAL_MATCHER.assertMatch(service.get(id, USER_ID), aNew);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        performAndCheckIsNoContent(
                MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
        );
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void delete() throws Exception {
        performAndCheckIsNoContent(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }
}

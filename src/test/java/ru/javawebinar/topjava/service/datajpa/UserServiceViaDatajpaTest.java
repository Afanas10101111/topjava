package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.getNew;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceViaDatajpaTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        MEAL_MATCHER.assertMatch(service.getWithMeals(USER_ID).getMeals(), meals);
    }

    @Test
    public void getAbsentOneWithMeals() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getHungryOneWithoutMeals() {
        assertTrue(service.getWithMeals(service.create(getNew()).getId()).getMeals().isEmpty());
    }
}
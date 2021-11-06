package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.getNew;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceViaDatajpaTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() {
        assertEquals(7, service.getUserWithMeals(USER_ID).getMeals().size());
    }

    @Test
    public void getAbsentUserWithMeals() {
        assertThrows(NotFoundException.class, () -> service.getUserWithMeals(NOT_FOUND));
    }

    @Test
    public void getHungryUserWithoutMeals() {
        assertTrue(service.getUserWithMeals(service.create(getNew()).getId()).getMeals().isEmpty());
    }
}
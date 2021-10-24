package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.START_MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.referenceSortedUserMeals;
import static ru.javawebinar.topjava.MealTestData.userMeal02;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;
    @Autowired
    private UserService userService;

    @Test
    public void create() {
        Meal newMeal = service.create(getNew(), USER_ID);
        assertMatch(service.get(newMeal.getId(), USER_ID), newMeal);
    }

    @Test
    public void createDuplicateDateTimeCreate() {
        assertThrows(
                DataAccessException.class,
                () -> service.create(new Meal(service.get(START_MEAL_ID, USER_ID).getDateTime(), "Duplicate", 1), USER_ID)
        );
    }

    @Test
    public void get() {
        assertMatch(service.get(START_MEAL_ID, USER_ID), userMeal02);
    }

    @Test
    public void getSomeoneElseMeal() {
        assertThrows(NotFoundException.class, () -> service.get(START_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getNonExistentMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        int id = updated.getId();
        assertThat(updated).usingRecursiveComparison().isNotEqualTo(service.get(id, USER_ID));
        service.update(updated, USER_ID);
        assertMatch(service.get(id, USER_ID), updated);
    }

    @Test
    public void updateSomeoneElseMeal() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }

    @Test
    public void updateNonExistentMeal() {
        assertThrows(NotFoundException.class, () -> service.update(new Meal(NOT_FOUND, LocalDateTime.now(), "Some non-existent meal", 1), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(START_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(START_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteSomeoneElseMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(START_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void deleteNonExistentMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), referenceSortedUserMeals);
    }

    @Test
    public void getAllForNonExistentUser() {
        assertEquals(service.getAll(UserTestData.NOT_FOUND).size(), 0);
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startEndDate = LocalDate.of(2021, Month.JANUARY, 30);
        assertMatch(
                service.getBetweenInclusive(startEndDate, startEndDate, USER_ID),
                referenceSortedUserMeals.stream()
                        .filter(m -> Util.isBetweenHalfOpen(m.getDate(), startEndDate, startEndDate.plus(1, ChronoUnit.DAYS)))
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void getBetweenInclusiveWithNullBorders() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID), referenceSortedUserMeals);
    }

    @Test
    public void getBetweenInclusiveForNonExistentUser() {
        assertEquals(service.getBetweenInclusive(null, null, UserTestData.NOT_FOUND).size(), 0);
    }

    @Test
    public void cascadeMealDeletionOnOwnerDeletion() {
        assertEquals(service.getAll(USER_ID).size(), referenceSortedUserMeals.size());
        userService.delete(USER_ID);
        assertEquals(service.getAll(USER_ID).size(), 0);
    }
}
package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "DuserName", "email1@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "BuserName", "email2@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "CuserName", "email3@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "AuserName", "email4@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "AuserName", "email5@mail.ru", "password", Role.ADMIN));
            System.out.println(adminUserController.getAll());

            System.out.println("\n****\n");

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            SecurityUtil.setAuthUserId(1);
            System.out.println(mealRestController.getAll());
            System.out.println(mealRestController.getAllFilteredByDateTime(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX));

            System.out.println("\n****\n");

            Meal someFood = new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 20, 0), "SomeFood", 888);
            SecurityUtil.setAuthUserId(2);
            mealRestController.create(someFood);

            //SecurityUtil.setAuthUserId(1);
            //mealRestController.update(1, someFood, someFood.getId());
            //mealRestController.update(1, someFood, someFood.getId() + 1);

            SecurityUtil.setAuthUserId(2);
            mealRestController.update(someFood, someFood.getId());

            //mealRestController.get(2, someFood.getId() + 1);
            //SecurityUtil.setAuthUserId(1);
            //mealRestController.get(1, someFood.getId());

            SecurityUtil.setAuthUserId(2);
            System.out.println(mealRestController.get(someFood.getId()));

            //mealRestController.delete(2, someFood.getId() + 1);
            //SecurityUtil.setAuthUserId(1);
            //mealRestController.delete(1, someFood.getId());

            SecurityUtil.setAuthUserId(2);
            mealRestController.delete(someFood.getId());
            //System.out.println(mealRestController.get(someFood.getId()));
        }
    }
}

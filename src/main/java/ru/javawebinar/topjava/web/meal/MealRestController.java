package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<Meal> getAll(int userId) {
        log.info("user #{} getAll", userId);
        return service.getAll(userId);
    }

    public Meal get(int userId, int id) {
        log.info("user #{} get {}", userId, id);
        return service.get(userId, id);
    }

    public Meal create(int userId, Meal meal) {
        log.info("user #{} create {}", userId, meal);
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void delete(int userId, int id) {
        log.info("user #{} delete {}", userId, id);
        service.delete(userId, id);
    }

    public void update(int userId, Meal meal, int id) {
        log.info("user #{} update {} with id={}", userId, meal, id);
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }
}
package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        int authUserId = SecurityUtil.authUserId();
        log.info("user #{} getAll", authUserId);
        return MealsUtil.getTos(service.getAll(authUserId));
    }

    public List<MealTo> getAllFilteredByDateTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        int authUserId = SecurityUtil.authUserId();
        log.info("user #{} getAll", authUserId);
        return MealsUtil.getTos(service.getAllFilteredByDate(
                        authUserId,
                        startDate == null ? LocalDate.MIN : startDate,
                        endDate == null ? LocalDate.MAX : endDate
                ).stream()
                .filter(m -> DateTimeUtil.isBetweenHalfOpen(
                        m.getTime(),
                        startTime == null ? LocalTime.MIN : startTime,
                        endTime == null ? LocalTime.MAX : endTime
                ))
                .collect(Collectors.toList()));
    }

    public Meal get(int id) {
        int authUserId = SecurityUtil.authUserId();
        log.info("user #{} get {}", authUserId, id);
        return service.get(authUserId, id);
    }

    public Meal create(Meal meal) {
        int authUserId = SecurityUtil.authUserId();
        log.info("user #{} create {}", authUserId, meal);
        checkNew(meal);
        return service.create(authUserId, meal);
    }

    public void delete(int id) {
        int authUserId = SecurityUtil.authUserId();
        log.info("user #{} delete {}", authUserId, id);
        service.delete(authUserId, id);
    }

    public void update(Meal meal, int id) {
        int authUserId = SecurityUtil.authUserId();
        log.info("user #{} update {} with id={}", authUserId, meal, id);
        assureIdConsistent(meal, id);
        service.update(authUserId, meal);
    }
}
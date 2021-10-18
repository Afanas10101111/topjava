package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController restController;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        restController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                SecurityUtil.authUserId(),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            restController.create(meal);
        } else {
            restController.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                restController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        restController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                // TODO looks terrible, stinky boilerplate code detected!!!!
                String filterStartDateStr = Optional.ofNullable(request.getParameter("filterStartDate")).orElse("");
                String filterEndDateStr = Optional.ofNullable(request.getParameter("filterEndDate")).orElse("");
                String filterStartTimeStr = Optional.ofNullable(request.getParameter("filterStartTime")).orElse("");
                String filterEndTimeStr = Optional.ofNullable(request.getParameter("filterEndTime")).orElse("");
                request.setAttribute("filterStartDate", filterStartDateStr);
                request.setAttribute("filterEndDate", filterEndDateStr);
                request.setAttribute("filterStartTime", filterStartTimeStr);
                request.setAttribute("filterEndTime", filterEndTimeStr);
                log.info("getAll, filterStartDateStr={}, filterEndDateStr={}, filterStartTimeStr={}, filterEndTimeStr={}"
                        , filterStartDateStr, filterEndDateStr, filterStartTimeStr, filterEndTimeStr);
                LocalDate filterStartDate = filterStartDateStr.isEmpty() ? LocalDate.MIN : LocalDate.parse(filterStartDateStr);
                LocalDate filterEndDate = filterEndDateStr.isEmpty() ? LocalDate.MAX : LocalDate.parse(filterEndDateStr);
                LocalTime filterStartTime = filterStartTimeStr.isEmpty() ? LocalTime.MIN : LocalTime.parse(filterStartTimeStr);
                LocalTime filterEndTime = filterEndTimeStr.isEmpty() ? LocalTime.MAX : LocalTime.parse(filterEndTimeStr);
                request.setAttribute("meals",
                        MealsUtil.getTos(
                                        restController.getAllFilteredByDate(filterStartDate, filterEndDate),
                                        MealsUtil.DEFAULT_CALORIES_PER_DAY
                                ).stream()
                                .filter(m -> DateTimeUtil.isBetweenHalfOpen(m.getTime(), filterStartTime, filterEndTime))
                                .collect(Collectors.toList()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

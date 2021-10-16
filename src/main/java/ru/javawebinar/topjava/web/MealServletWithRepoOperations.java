package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.config.ProjectConstantsHolder.CALORIES_DAY_LIMIT;
import static ru.javawebinar.topjava.config.ProjectConstantsHolder.REPOSITORY;

public class MealServletWithRepoOperations extends HttpServlet {
    private static final Logger log = getLogger(MealServletWithRepoOperations.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final String FORWARD_TO_LIST = "extended_meals.jsp";
    private static final String FORWARD_TO_UPDATE = "update.jsp";
    private static final String REDIRECT_TO_LIST = "extended-meals";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forwardTo = FORWARD_TO_LIST;
        String action = request.getParameter("action");
        long mealId = Long.parseLong(Optional.ofNullable(request.getParameter("id")).orElse("-1"));
        log.debug("doGet -> action = {}; mealId = {}", action, mealId);

        if (mealId > -1 && "delete".equals(action)) {
            REPOSITORY.deleteMeal(mealId);
            response.sendRedirect(REDIRECT_TO_LIST);
            return;
        } else if (mealId > -1 && "update".equals(action)) {
            forwardTo = FORWARD_TO_UPDATE;
            Meal meal = REPOSITORY.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else {
            List<MealTo> mealsTo = MealsUtil.withoutFilter(REPOSITORY.getAllMeal(), CALORIES_DAY_LIMIT);
            log.debug("mealsTo size = {}", mealsTo.size());
            request.setAttribute("mealsTo", mealsTo);
        }
        request.getRequestDispatcher(forwardTo).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        long mealId = id.length() == 0 ? -1 : Long.parseLong(id);
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), FORMATTER);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        log.debug("doPost -> mealId = {}; dateTime = {}; description = {}; calories = {}", mealId, dateTime, description, calories);

        Meal meal = new Meal(dateTime, description, calories);
        meal.setId(mealId);
        if (mealId < 0) {
            REPOSITORY.addMeal(meal);
        } else {
            REPOSITORY.updateMeal(meal);
        }
        response.sendRedirect(REDIRECT_TO_LIST);
    }
}

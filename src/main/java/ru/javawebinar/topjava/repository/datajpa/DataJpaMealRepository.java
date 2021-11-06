package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudRepository;
    private final DataJpaUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, DataJpaUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        // this solution makes unnecessary user select for update scenario
        /*if (meal.isNew() || get(meal.id(), userId) != null) {
            meal.setUser(userRepository.get(userId));
            return crudRepository.save(meal);
        }
        return null;*/

        Meal mealFromDb;
        if (meal.isNew()) {
            meal.setUser(userRepository.get(userId));
        } else if ((mealFromDb = get(meal.id(), userId)) != null) {
            meal.setUser(mealFromDb.getUser());
        } else {
            return null;
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
        //return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findUserMeal(id, userId);
        //return crudRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllUserMeal(userId);
        //return crudRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllUserMealBetween(userId, startDateTime, endDateTime);
    }
}

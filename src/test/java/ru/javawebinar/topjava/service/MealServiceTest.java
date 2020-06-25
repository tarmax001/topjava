package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

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
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_1_ID, USER_ID);
        assertThat(meal).isEqualTo(MEAL_1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_1_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_1_ID, USER_ID);
        assertNull(repository.get(MEAL_1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_1_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LOCAL_DATE_FROM, LOCAL_DATE_TO, USER_ID);
        assertThat(meals).containsExactly(MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertThat(meals).containsExactly(MEAL_7, MEAL_6, MEAL_5, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void create() {
        Meal meal = NEW_MEAL;
        Meal created = service.create(meal, USER_ID);
        meal.setId(created.getId());
        assertThat(meal).isEqualTo(created);
        assertThat(service.get(created.getId(), USER_ID)).isEqualTo(created);
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdatedMeal();
        service.update(updatedMeal, USER_ID);
        assertThat(service.get(updatedMeal.getId(), USER_ID)).isEqualTo(updatedMeal);
    }

    @Test
    public void updateNotFound() {
        Meal updatedMeal = getUpdatedMeal();
        assertThrows(NotFoundException.class, () -> service.update(updatedMeal, ADMIN_ID));
    }
}
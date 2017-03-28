package track.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import track.container.config.Bean;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {
    private List<Bean> localBeans;
    private Map<String, Object> objByName = new HashMap<>();
    private Map<String, Object> objByClassName = new HashMap<>();

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        this.localBeans = beans;
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) {
        Object result;
        for (Bean bean : localBeans) {
            if (bean.getId().equals(id)) {
                return bean;
            }
        }
        try {
            Class c = Class.forName(id);
            Object obj = c.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();

        }



        return null;
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) {
        return null;
    }
}

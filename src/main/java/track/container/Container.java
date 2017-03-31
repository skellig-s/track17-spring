package track.container;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;
import track.container.config.Property;
import track.container.config.ValueType;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {
    private HashMap<String, Bean> beansMap = new HashMap<>();
    private Map<String, Object> objByName = new HashMap<>();
    private Map<String, Object> objByClassName = new HashMap<>();

    private Set<String> previousCalls;
    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) throws InvalidConfigurationException {
        if (beans == null) {
            throw new InvalidConfigurationException("No beans to read");
        }

        for (Bean bb : beans) {
            beansMap.put(bb.getId(), bb);
        }

        for (Bean bean : beansMap.values()) {
            try {

                    createObjByBean(bean, new HashSet<>());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private int createObjByBean(Bean bean, Set<String> previousCalls) throws Exception {
        String className = bean.getClassName();
        String idName = bean.getId();
        Map<String, Property> properties = bean.getProperties();
        Class cl = Class.forName(className);
        Object obj;
        if (previousCalls.contains(idName)) {
//            System.out.println("Wow, so repeatable... such loopy...\n");
            return -1;
        }
//        if (!(objByName.containsKey(idName))) {
            obj = cl.newInstance();
//        } else {
//            obj = objByName.get(idName);
//            System.out.println("met again: "+ obj);
//        }

        for (Map.Entry<String, Property> map : properties.entrySet()) {
            Property prop = map.getValue();
            int answer = 1;
            if (prop.getType() == (ValueType.VAL)) {
                propSetForVal(cl, prop, obj);
            } else {
                if (!objByName.containsKey(prop.getValue())) {
                    previousCalls.add(idName);
                    answer = createObjByBean(beansMap.get(prop.getValue()), previousCalls);
                    System.out.println("New OBJ for REF created!");
                }
                if (answer > 0) {
                    System.out.println("REF FOR started: "+ obj);
                    propSetForREF(cl, prop, obj);
                    System.out.println("REF FOR complete: "+ obj);
                }
            }
        }
        objByName.put(idName, obj);
        System.out.println("OBJ ADDED: "+ obj);
        objByClassName.put(className, obj);
        return 1;
    }

    private void propSetForREF(Class cl, Property prop, Object obj) throws Exception {
        Class[] arg = new Class[1];
        arg[0] = cl.getDeclaredField(prop.getName()).getType();
        Method method = cl.getDeclaredMethod(getSetterName(prop.getName()), arg);

        System.out.println("prop for set: " + objByName.get(prop.getValue()).toString());
        System.out.println("method name: "+ method.getName());
        method.invoke(obj, objByName.get(prop.getValue()));
        System.out.println(obj);

    }

    private void propSetForVal(Class cl, Property prop, Object obj) throws Exception {
        Class[] arg = new Class[1];
        arg[0] = cl.getDeclaredField(prop.getName()).getType();
        Method method = cl.getDeclaredMethod(getSetterName(prop.getName()), arg);
        method.invoke(obj, typedValue(prop.getValue(), arg[0]));
    }

    private Object typedValue(String value, Type claz ) {
        if ((claz == Integer.TYPE)) {
            return Integer.parseInt(value);
        }
        if (claz == Double.TYPE) {
            return Double.parseDouble(value);
        }
        if (claz == Boolean.TYPE) {
            return Boolean.parseBoolean(value);
        }
        if (claz == Long.TYPE) {
            return Long.parseLong(value);
        }
        if (claz == Short.TYPE) {
            return Short.parseShort(value);
        }
        if (claz == Byte.TYPE) {
            return Byte.parseByte(value);
        }
        return value;
    }

    private String getSetterName(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append("set");
        builder.append(name.substring(0,1).toUpperCase());
        builder.append(name.substring(1));
        return (builder.toString());
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) {
        if (objByName.containsKey(id)) {
            return objByName.get(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) {
        if (objByClassName.containsKey(className)) {
            return objByClassName.get(className);
        } else {
            throw new NoSuchElementException();
        }
    }
}

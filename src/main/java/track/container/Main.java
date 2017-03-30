package track.container;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception { //InvalidConfigurationException {

        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> list = reader.parseBeans(new File("src/main/resources/config.json"));

        for (Bean bean : list) {
            System.out.println(bean.toString());
        }
        Container container = new Container(list);
        System.out.println(container.getById("engineBean").toString());
        System.out.println(container.getById("carBean").toString());
        System.out.println(container.getByClass("track.container.beans.Gear").toString());


        HashMap<Integer, String> map1 = new HashMap<>();
        HashMap<Integer, String> map2 = new HashMap<>();

        String str = "Hello";




        /*

        ПРИМЕР ИСПОЛЬЗОВАНИЯ

         */

//        // При чтении нужно обработать исключение
//        ConfigReader reader = new JsonReader();
//        List<Bean> beans = reader.parseBeans("config.json");
//        Container container = new Container(beans);
//
//        Car car = (Car) container.getByClass("track.container.beans.Car");
//        car = (Car) container.getById("carBean");


    }
}

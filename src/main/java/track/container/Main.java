package track.container;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws InvalidConfigurationException {

        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> list = reader.parseBeans(new File("src/main/resources/config.json"));

//        Container container = new Container(list);
//        System.out.println(container.getById("carBe").toString());



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

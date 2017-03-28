package track.container;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import track.container.config.*;

/**
 * TODO: Реализовать
 */
public class JsonConfigReader implements ConfigReader {

    @Override
    public List<Bean> parseBeans(File configFile) throws InvalidConfigurationException {
        ObjectMapper mapper = new ObjectMapper();
        Root beansList = null;
        try {
            beansList = mapper.readValue(configFile, Root.class);
//            System.out.println("List of beans:");
//            for (Bean bean : beansList.getBeans()) {
//                System.out.println(bean);
//            }
            return beansList.getBeans();
        } catch (IOException ex) {
            System.out.println("Ouch, something went wrong:");
            ex.printStackTrace();
        }
        return null;
    }
}

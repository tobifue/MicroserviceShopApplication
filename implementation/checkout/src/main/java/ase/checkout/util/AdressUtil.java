package ase.checkout.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AdressUtil {

    public static String loadAdress(String serviceName) throws IOException {
        try (InputStream input = AdressUtil.class.getResourceAsStream("/adresses.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new IOException("Could not load adresses.properties!");
            }
            prop.load(input);
            return prop.getProperty(serviceName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        throw new IOException("Service " + serviceName + " does not exist");
    }

}

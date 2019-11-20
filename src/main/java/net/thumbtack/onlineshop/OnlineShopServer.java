package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.database.MyBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class OnlineShopServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineShopServer.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(args[0]);
            properties.load(fileInputStream);
            if (properties.getProperty("rest_http_port") != null &&
                    properties.getProperty("max_name_length") != null &&
                    properties.getProperty("min_password_length") != null) {
                SpringApplication.run(OnlineShopServer.class, args);
                MyBatisUtils.initSqlSessionFactory();
            } else {
                LOGGER.info("Can't read property");
            }
        } catch (IOException e) {
            LOGGER.info("Error file ", e);
        }
    }
}

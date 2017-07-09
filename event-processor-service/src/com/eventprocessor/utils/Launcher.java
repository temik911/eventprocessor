package com.eventprocessor.utils;

import com.eventprocessor.service.EventService;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.h2.tools.Server;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * @author Artem
 * @since 10.06.2017.
 */
public class Launcher {
    private static final String RMI_PORT = "rmi.port";

    public static void main(String[] args) throws IOException, SQLException {
        prepareStage();
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/beans.xml");
        PropertiesProvider propertiesProvider = (PropertiesProvider) context.getBean("propertiesProvider");
        EventService trendBarService = (EventService) context.getBean("eventService");
        int rmiPort = Integer.parseInt(propertiesProvider.getProperty(RMI_PORT));
        LocateRegistry.createRegistry(rmiPort);
        String serviceName = "rmi://localhost:" + rmiPort + "/" + EventService.class.getName();
        Naming.rebind(serviceName, trendBarService);
    }

    private static void prepareStage() throws FileNotFoundException, SQLException {
        configureLogging();
        createDb();
        Server.createWebServer("-tcpAllowOthers", "-tcpPort", "8082").start();
    }

    private static void configureLogging() {
        ConsoleAppender console = new ConsoleAppender();
        String PATTERN = "%d [%p] %c: %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.DEBUG);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);
    }

    public static void createDb() throws FileNotFoundException, SQLException {
        Scanner scanner = new Scanner(new File("resources/sql/init_h2.sql"));
        StringBuilder sqlBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            sqlBuilder.append(scanner.nextLine());
        }
        String sql = sqlBuilder.toString();
        try (Connection connection = DbConnection.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        }
    }
}

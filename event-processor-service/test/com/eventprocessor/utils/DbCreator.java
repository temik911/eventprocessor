package com.eventprocessor.utils;

import com.eventprocessor.utils.DbConnection;
import com.eventprocessor.utils.Launcher;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Artem
 * @since 12.06.2017.
 */
public class DbCreator {
    private static boolean IS_CREATED = false;

    public static void createDB() throws FileNotFoundException, SQLException {
        if (IS_CREATED) {
            try (Connection connection = DbConnection.getConnection()) {
                Statement statement = connection.createStatement();
                statement.execute("DROP TABLE events;");
                statement.close();
            }
        }
        Launcher.createDb();
        IS_CREATED = true;
    }
}

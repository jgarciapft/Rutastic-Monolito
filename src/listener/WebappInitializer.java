package listener;

import dao.factories.DAOAbstractFactory;
import dao.factories.DAOFactoryJDBC;
import dao.implementations.DAOImplJDBC;
import org.sqlite.SQLiteConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;

public class WebappInitializer implements ServletContextListener {

    public static final String SC_ATTR_CONNECTION_IDENTIFIER = "connection";
    private final Logger logger = Logger.getLogger(WebappInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Connection conn = null;
        SQLiteConfig sqLiteConfig = new SQLiteConfig(); // SQLite configurations
        String dbURL = "jdbc:sqlite:file:" + System.getProperty("user.home") + "/sqliteDB/" +
                sce.getServletContext().getInitParameter("dbName"); // Path to DB

        // Create DB connection, configure it and store it as an attribute of the servlet context

        try {
            Class.forName("org.sqlite.JDBC");
            sqLiteConfig.enforceForeignKeys(true); // Enable foreign key constraints

            // Create connection with options

            conn = DriverManager.getConnection(dbURL, sqLiteConfig.toProperties());

            // Check that the connection could be established correctly

            if (conn != null) {

                // Set options

                conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED); // Most relaxed isolation level
                conn.setAutoCommit(false); // Enforce explicit commit-rollback calls

                logger.info("[CONNECTED TO DB]");
                DatabaseMetaData dm = conn.getMetaData();
                logger.info("Driver name: " + dm.getDriverName());
                logger.info("Driver version: " + dm.getDriverVersion());
                logger.info("DB product name: " + dm.getDatabaseProductName());
                logger.info("Connection URL: " + dm.getURL());

                // Store the connection

                sce.getServletContext().setAttribute(SC_ATTR_CONNECTION_IDENTIFIER, conn);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Register DAOFactories and configure its dependencies

        Connection finalConn = conn; // Make the connection reference effectively final to be used in a lambda expression
        DAOAbstractFactory.get().registerDAOFactory(new DAOFactoryJDBC(), (dao, dependencies) -> {
            HashMap<String, Object> dependenciesMap = new HashMap<>();
            dependenciesMap.put(DAOImplJDBC.CONNECTION_IDENTIFIER, finalConn);
            dao.configureDependencies(dependenciesMap);
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("[DB CONNECTION SHUTDOWN START]");

        try {
            // Get the connection object stored as an attribute of the servlet context

            ServletContext sc = sce.getServletContext();
            Connection conn = (Connection) sc.getAttribute(SC_ATTR_CONNECTION_IDENTIFIER);

            // Close the connection and deregister associated drivers

            conn.close();
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                logger.info("DB deregistering drivers");
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                    logger.info(String.format("deregistering driver %s", driver));
                } catch (SQLException e) {
                    logger.severe(String.format("Exception thrown while deregistering driver %s", driver));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        logger.info("[DB CONNECTION CLOSED]");
    }
}

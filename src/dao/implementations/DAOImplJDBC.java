package dao.implementations;

import java.sql.Connection;
import java.util.Map;

/**
 * JDBC DAO Implementation to operate a database using the JDBC middleware
 *
 * @see DAOImplementation
 */
public interface DAOImplJDBC extends DAOImplementation {

    String CONNECTION_IDENTIFIER = "connection";

    /**
     * Configure dependencies for JDBC DAO classes. Essentially set the connection to the database
     *
     * @param dependencies Collection of the DAO dependencies identified by a string. It should contain the connection
     *                     to the database identified by the string 'connection'
     */
    @Override
    default void configureDependencies(Map<String, Object> dependencies) {
        Object connection = dependencies.getOrDefault(CONNECTION_IDENTIFIER, null);

        if (connection instanceof Connection) {
            setConnection((Connection) connection);
            setDependenciesConfigured(true);
        } else {
            setDependenciesConfigured(false);
        }
    }

    /**
     * Set the connection to the database
     *
     * @param connection Connection to the database
     */
    void setConnection(Connection connection);

}

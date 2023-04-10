package dao.implementations;

import java.util.Map;

/**
 * <h3>FLEXIBLE DAO WITH SUPPORT FOR MULTIPLE DATA SOURCES ARCHITECTURE</h3>
 * <p>
 * Base interface for specifying mandatory functionality for every DAO implementation. A DAO implementation can be
 * understood as the required methods and dependencies to operate a data source, which can be a database,
 * XML file, JSON object, etc...
 * <p></p>
 * To make a functional implementation of a DAO for a specific data source you should create a derived interface from
 * this one and add the necessary methods to initialize, register and operate the data source, as well as giving
 * a default implementation for already declared methods. Then each implementing class should implement the new
 * interface alongside a derived {@literal DAO<T>} interface.
 * <p></p>
 * It's not mandatory to define a DAO implementation this way, but it is desirable if you want to take advantage of
 * {@literal DAOFactory<T>} instantiation for your DAO implementing classes.
 *
 * @see dao.DAO
 * @see dao.factories.DAOFactory
 */
public interface DAOImplementation {

    /**
     * Use this method to set the required dependencies to fully register, initialize and operate a data source
     *
     * @param dependencies Collection of the DAO dependencies identified by a string
     */
    void configureDependencies(Map<String, Object> dependencies);

    /**
     * @return If DAO dependencies have been already set
     */
    boolean dependenciesConfigured();

    /**
     * Update the status of the dependencies required by this DAO
     *
     * @param status New dependencies configuration status
     */
    void setDependenciesConfigured(boolean status);

}

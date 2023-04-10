package dao.factories;

import dao.implementations.DAODependencyConfigurator;
import dao.implementations.DAOImplementation;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract DAO Factory with which you can register and retrieve your own DAO factories of a specific DAO implementation.
 * Implemented through a Singleton instance, which you can get calling {@link #get()}. Initially there isn't any
 * available factories, and there can only be one factory for a DAO implementation.
 *
 * @see DAOFactory
 * @see DAOImplementation
 */
public class DAOAbstractFactory {

    private final Map<Class<? extends DAOImplementation>, DAOFactory<? extends DAOImplementation>> DAOFactories;

    private DAOAbstractFactory() {
        DAOFactories = new HashMap<>();
    }

    /**
     * @return The Singleton instance of the abstract DAO factory
     */
    public static DAOAbstractFactory get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Register a new DAO factory for a specific DAO implementation. If a factory for {@literal <T>} implementation has
     * already been registered, calling this method will result in a no-op.
     *
     * @param daoFactory             The new DAO factory being registered
     * @param dependencyConfigurator Dependency configurator for DAO Implementation {@literal <T>}
     * @param <T>                    The DAO implementation of the DAO factory being registered
     */
    public <T extends DAOImplementation> void registerDAOFactory(
            DAOFactory<T> daoFactory, DAODependencyConfigurator<T> dependencyConfigurator) {

        /*
         *The factory is registered and its DAOs get their dependencies configured if there isn't already a DAO
         * for the DAO implementation <T>
         */

        if (!DAOFactories.containsKey(daoFactory.getDAOImplementation())) {
            daoFactory.setDAODependencyConfigurator(dependencyConfigurator);
            daoFactory.configureAllDAODependencies();

            DAOFactories.put(daoFactory.getDAOImplementation(), daoFactory);
        }
    }

    /**
     * Try querying a factory for a specific DAO implementation
     *
     * @param daoImplementation The class of the DAO implementation
     * @param <T>               DAO implementation
     * @return A factory for the requested DAO implementation or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T extends DAOImplementation> DAOFactory<T> impl(Class<T> daoImplementation) {
        DAOFactory<?> daoFactory = DAOFactories.getOrDefault(daoImplementation, null);

        // Return the requested DAOFactory casted to the specific interface requested, or null if not found

        if (daoFactory != null)
            return (DAOFactory<T>) daoFactory;
        else
            return null;
    }

    /**
     * Singleton holder for DAOAbstractFactory class
     */
    private static class SingletonHolder {
        private static final DAOAbstractFactory INSTANCE = new DAOAbstractFactory();
    }

}

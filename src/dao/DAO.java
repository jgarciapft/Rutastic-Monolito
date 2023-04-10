package dao;

import java.util.List;

/**
 * <h3>FLEXIBLE DAO WITH SUPPORT FOR MULTIPLE DATA SOURCES ARCHITECTURE</h3>
 * <p>
 * DAO stands for Data Access Object and are linked to a specific model class for which they provide data access.
 * This is the public interface of all DAOs and contains the common methods all DAOs should provide.
 * <p></p>
 * When creating a new DAO for a model class you can either extend this interface or directly implement it in a
 * non-abstract class, but interface to interface inheritance is the preferred way as it plays more nicely with the
 * architecture of the extensible DAO pattern design.
 *
 * @param <T> Model class
 */
public interface DAO<T> {

    /**
     * @return A list of all instances of the model class
     */
    List<T> getAll();

    /**
     * Retrieve an instance of the model class by its id
     *
     * @param id Variadic argument containing all the numeric values that identifies a concrete instance of
     *           model class T
     * @return The queried instance or null if it couldn't be found
     */
    T getById(long... id);

    /**
     * Shorthand for {@code add(instance, true)}
     *
     * @param instance Model instance
     * @return The id of the newly added model instance
     * @see #add(T, boolean)
     */
    long[] add(T instance);

    /**
     * Register / submit / add a new instance of the model class to persistent storage. If the identifier of the
     * new instance is provided by persistent storage then, if set, is ignored.
     *
     * @param instance Model instance
     * @param isAtomic A true value indicates that the operation should be performed atomically
     * @return The id of the newly added model instance or -1 if any error
     */
    long[] add(T instance, boolean isAtomic);

    /**
     * Shorthand for {@code save(instance, true)}
     *
     * @param instance Model instance
     * @return Whether the operation was successful or not
     * @see #save(T, boolean)
     */
    boolean save(T instance);

    /**
     * Update the stored values of the model instance identified by the id attributes stored in {@code instance} with
     * the values of non-identifier attributes also stored in {@code instance}
     *
     * @param instance Model instance that can identify the already stored instance and contains the updated values
     * @param isAtomic A true value indicates that the operation should be performed atomically
     * @return Whether the operation was successful or not
     */
    boolean save(T instance, boolean isAtomic);

    /**
     * Shorthand for {@code deleteById(true, id)}
     *
     * @param id Model identifier(s)
     * @return Whether the operation was successful or not
     * @see #deleteById(boolean, long...)
     */
    boolean deleteById(long... id);

    /**
     * Deletes an already existing model instance identified by {@code id}
     *
     * @param isAtomic A true value indicates that the operation should be performed atomically
     * @param id       Model identifier(s)
     * @return Whether the operation was successful or not
     */
    boolean deleteById(boolean isAtomic, long... id);

}

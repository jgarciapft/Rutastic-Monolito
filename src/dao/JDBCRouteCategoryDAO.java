package dao;

import dao.implementations.DAOImplJDBC;
import helper.model.ModelMapper;
import helper.model.ModelMapperFactory;
import model.RouteCategory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * JDBC Implementation for the {@code RouteCategory} DAO
 *
 * @see DAOImplJDBC
 * @see RouteCategoryDAO
 * @see RouteCategory
 */
public class JDBCRouteCategoryDAO implements RouteCategoryDAO, DAOImplJDBC {

    private static final Logger logger = Logger.getLogger(JDBCRouteCategoryDAO.class.getName());
    private boolean dependenciesConfigured;
    private Connection connection;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RouteCategory> getAll() {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL ROUTE CATEGORIES");

        RouteCategory currentCategory;
        List<RouteCategory> allCategories = new ArrayList<>();
        ModelMapper<RouteCategory> routeCategoryModelMapper = ModelMapperFactory.get().forModel(RouteCategory.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routecategories");

            while (rs.next()) {
                currentCategory = routeCategoryModelMapper.parseFromResultSet(rs);
                if (currentCategory != null) {
                    allCategories.add(currentCategory);
                    logger.info(String.format("[FETCHED ROUTE CATEGORY] id: %d | name: %s | description: %s",
                            currentCategory.getId(),
                            currentCategory.getName(),
                            currentCategory.getDescription()));
                } else {
                    logger.warning("Attempted to read a NULL route category");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allCategories;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public RouteCategory getById(long... id) {
        if (id.length != 1) throw new IllegalArgumentException("Wrong number of identifiers. Expected 1");

        if (!dependenciesConfigured()) return null;

        RouteCategory category = null;
        ModelMapper<RouteCategory> routeCategoryModelMapper = ModelMapperFactory.get().forModel(RouteCategory.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routecategories WHERE id = " + id[0]);

            if (rs.next()) {
                category = routeCategoryModelMapper.parseFromResultSet(rs);
                logger.info(String.format("[FETCHED ROUTE CATEGORY] id: %d | name: %s | description: %s",
                        category.getId(),
                        category.getName(),
                        category.getDescription()));
            } else {
                logger.warning("There's no category by the id (" + id[0] + ")");
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteCategory getByName(String name) {
        if (!dependenciesConfigured()) return null;

        RouteCategory category = null;
        ModelMapper<RouteCategory> routeCategoryModelMapper = ModelMapperFactory.get().forModel(RouteCategory.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM routecategories WHERE name = '%s'", name));

            if (rs.next()) {
                category = routeCategoryModelMapper.parseFromResultSet(rs);
                logger.info(String.format("[FETCHED ROUTE CATEGORY] id: %d | name: %s | description: %s",
                        category.getId(),
                        category.getName(),
                        category.getDescription()));
            } else {
                logger.warning("There's no category by the name (" + name + ")");
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(RouteCategory instance) {
        return add(instance, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(RouteCategory instance, boolean isAtomic) {
        long lastId, SQLERROR = -1L;
        long[] newId = new long[1];

        if (!dependenciesConfigured()) return new long[]{SQLERROR};

        Function<Connection, Long> queryLatestId = connection -> {
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT seq from sqlite_sequence WHERE name = 'routecategories'");

                if (rs.next()) {
                    long id = rs.getLong("seq");
                    st.close();
                    return id;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            return SQLERROR;
        };

        lastId = queryLatestId.apply(connection);

        if (lastId == SQLERROR) return newId;

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO routecategories(name, description) VALUES ('%s', '%s')",
                    instance.getName(),
                    instance.getDescription()));
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        newId[0] = queryLatestId.apply(connection);

        if (newId[0] == SQLERROR || newId[0] <= lastId) {
            if (isAtomic) {
                try {
                    connection.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            return new long[]{SQLERROR};
        }

        if (isAtomic) {
            try {
                connection.commit();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        logger.info(String.format("[NEW ROUTE CATEGORY CREATED] id: %d | name: %s | description: %s",
                newId[0],
                instance.getName(),
                instance.getDescription()));

        return newId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(RouteCategory instance) {
        return save(instance, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(RouteCategory instance, boolean isAtomic) {
        if (!dependenciesConfigured()) return false;

        boolean updateSuccessful = false;

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("UPDATE routecategories SET name = '%s', description = '%s' WHERE id = %d",
                    instance.getName(),
                    instance.getDescription(),
                    instance.getId()));

            if (isAtomic) connection.commit();
            updateSuccessful = true;
            st.close();

            logger.info(String.format("[ROUTE CATEGORY UPDATED] id: %d | name: %s | description: %s",
                    instance.getId(),
                    instance.getName(),
                    instance.getDescription()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (isAtomic) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return updateSuccessful;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public boolean deleteById(long... id) {
        return deleteById(true, id);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public boolean deleteById(boolean isAtomic, long... id) {
        if (id.length != 1) throw new IllegalArgumentException("Wrong number of identifiers. Expected 1");

        if (!dependenciesConfigured()) return false;

        boolean deletionSuccessful = false;

        try {
            Statement st = connection.createStatement();
            st.executeUpdate("DELETE FROM routecategories WHERE id = " + id[0]);

            if (isAtomic) connection.commit();
            deletionSuccessful = true;
            st.close();

            logger.info("[route category with the id (" + id[0] + ") has been deleted]");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (isAtomic) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return deletionSuccessful;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dependenciesConfigured() {
        return dependenciesConfigured;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDependenciesConfigured(boolean status) {
        dependenciesConfigured = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

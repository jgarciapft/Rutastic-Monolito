package dao;

import dao.implementations.DAOImplJDBC;
import helper.model.ModelMapper;
import helper.model.ModelMapperFactory;
import model.User;
import model.statistic.UserStatistic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * JDBC Implementation for the {@code User} DAO. The password of an user is never logged through this class
 *
 * @see DAOImplJDBC
 * @see UserDAO
 * @see User
 */
public class JDBCUserDAO implements UserDAO, DAOImplJDBC {

    private static final Logger logger = Logger.getLogger(JDBCUserDAO.class.getName());
    private boolean dependenciesConfigured;
    private Connection connection;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL USERS");

        User currentUser;
        List<User> allUsers = new ArrayList<>();
        ModelMapper<User> userModelMapper = ModelMapperFactory.get().forModel(User.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                currentUser = userModelMapper.parseFromResultSet(rs);
                if (currentUser != null) {
                    allUsers.add(currentUser);
                    logger.info(String.format("[FETCHED USER] id: %d | username: %s | email: %s | role: %s",
                            currentUser.getId(),
                            currentUser.getUsername(),
                            currentUser.getEmail(),
                            currentUser.getRole()));
                } else {
                    logger.warning("Attempted to read a NULL user");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allUsers;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public User getById(long... id) {
        if (id.length != 1) throw new IllegalArgumentException("Wrong number of identifiers. Expected 1");

        if (!dependenciesConfigured()) return null;

        User user = null;
        ModelMapper<User> userModelMapper = ModelMapperFactory.get().forModel(User.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE id = " + id[0]);

            if (rs.next()) {
                user = userModelMapper.parseFromResultSet(rs);
                logger.info(String.format("[FETCHED USER] id: %d | username: %s | email: %s | role: %s",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole()));
            } else {
                logger.warning("There's no user by the id (" + id[0] + ")");
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByUsername(String username) {
        if (!dependenciesConfigured()) return null;

        User user = null;
        ModelMapper<User> userModelMapper = ModelMapperFactory.get().forModel(User.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM users WHERE username = '%s'", username));

            if (rs.next()) {
                user = userModelMapper.parseFromResultSet(rs);
                logger.info(String.format("[FETCHED USER] id: %d | username: %s | email: %s | role: %s",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole()));
            } else {
                logger.warning("There's no user by the username (" + username + ")");
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(User instance) {
        return add(instance, true);
    }

    /**
     * {@inheritDoc}
     * <p></p>
     * This method can only add regular users, not admins or any other type of user / role
     */
    @Override
    public long[] add(User instance, boolean isAtomic) {
        long lastId, SQLERROR = -1L;
        long[] newId = new long[1];

        if (!dependenciesConfigured()) return new long[]{SQLERROR};

        Function<Connection, Long> queryLatestId = connection -> {
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT seq from sqlite_sequence WHERE name = 'users'");

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

        if (lastId == SQLERROR) return new long[]{SQLERROR};

        try {
            Statement st = connection.createStatement();
            // It can only register regular users, the default value for the role column
            st.executeUpdate(String.format("INSERT INTO users(username, email, password) VALUES ('%s', '%s', '%s')",
                    instance.getUsername(),
                    instance.getEmail(),
                    instance.getPassword()));
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

        logger.info(String.format("[NEW USER CREATED] id: %d | username: %s | email: %s | role: %s",
                newId[0],
                instance.getUsername(),
                instance.getEmail(),
                instance.getRole()));

        return newId;
    }

    /**
     * {@inheritDoc}
     * <p></p>
     * This method can't update the role of an user
     */
    @Override
    public boolean save(User instance) {
        return save(instance, true);
    }

    /**
     * {@inheritDoc}
     * <p></p>
     * This method can't update the role of an user
     */
    @Override
    public boolean save(User instance, boolean isAtomic) {
        if (!dependenciesConfigured()) return false;

        boolean updateSuccessful = false;

        try {
            Statement st = connection.createStatement();
            // Doesn't update the role of the user
            st.executeUpdate(String.format("UPDATE users SET username = '%s', email = '%s', password = '%s' WHERE id = %d",
                    instance.getUsername(),
                    instance.getEmail(),
                    instance.getPassword(),
                    instance.getId()));

            if (isAtomic) connection.commit();
            updateSuccessful = true;
            st.close();

            logger.info(String.format("[USER UPDATED] id: %d | username: %s | email: %s | role: %s",
                    instance.getId(),
                    instance.getUsername(),
                    instance.getEmail(),
                    instance.getRole()));
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
            st.executeUpdate("DELETE FROM users WHERE id = " + id[0]);

            if (isAtomic) connection.commit();
            deletionSuccessful = true;
            st.close();

            logger.info("[user with the id (" + id[0] + ") has been deleted]");
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

    @Override
    public List<UserStatistic> getTopUsersByTopMonthlyRoutes() {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING TOP USERS BY TOP MONTHLY ROUTES");

        UserStatistic currentUserStat;
        List<UserStatistic> topUsers = new ArrayList<>();

        // Query the view of top users by routes at the top monthly chart

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM top_users_by_top_monthly_routes");

            // Parse each row into a list of user stats which links an username to the number of top monthly routes

            while (rs.next()) {
                currentUserStat = new UserStatistic();

                currentUserStat.setUsername(rs.getString("username"));
                currentUserStat.setStat((int) rs.getFloat("top_routes"));

                // Check some stat could be parsed

                if (currentUserStat.getUsername() != null) {
                    topUsers.add(currentUserStat);
                    logger.info(String.format("[Top user stat] username: %s | top routes: %d",
                            currentUserStat.getUsername(),
                            (int) currentUserStat.getStat()));
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return topUsers;
    }

    @Override
    public List<UserStatistic> getTopUsersByAvgKudos() {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING TOP USERS BY TOP AVEGARE KUDOS");

        UserStatistic currentUserStat;
        List<UserStatistic> topUsers = new ArrayList<>();

        // Query the view of top users by average kudo ratings of their routes

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM top_users_by_top_avg_kudos");

            // Parse each row into a list of user stats which links an username to the average kudo rating

            while (rs.next()) {
                currentUserStat = new UserStatistic();

                currentUserStat.setUsername(rs.getString("username"));
                currentUserStat.setStat(rs.getFloat("avg_kudos"));

                // Check some stat could be parsed

                if (currentUserStat.getUsername() != null) {
                    topUsers.add(currentUserStat);
                    logger.info(String.format("[Top user stat] username: %s | avg. kudos: %f",
                            currentUserStat.getUsername(),
                            currentUserStat.getStat()));
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return topUsers;
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

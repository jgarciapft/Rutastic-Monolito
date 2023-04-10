package dao;

import model.Route;

import java.util.List;

/**
 * Extended public interface for all DAO implementations for model class {@code Route}. Specifies additional
 * methods all DAO implementations should support
 *
 * @see dao.implementations.DAOImplementation
 * @see Route
 */
public interface RouteDAO extends DAO<Route> {

    /**
     * @return A descending ordered list with the routes with more kudos this week. Routes with negative or 0 kudo
     * balance are not taking into account here. Consider the initial day of a week to be monday
     */
    List<Route> getTopRoutesOfTheWeek();

    /**
     * @return A descending ordered list with the routes with more kudos this month. Routes with negative or 0 kudo
     * balance are not taking into account here
     */
    List<Route> getTopRoutesOfTheMonth();

}

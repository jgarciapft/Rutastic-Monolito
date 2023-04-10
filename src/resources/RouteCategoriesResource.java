package resources;

import dao.RouteCategoryDAO;
import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import model.RouteCategory;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Path("/categoriasruta")
public class RouteCategoriesResource {

    private static final Logger logger = Logger.getLogger(RouteCategoriesResource.class.getName());

    @Context
    ServletContext sc;

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RouteCategory> getAllRouteCategoriesJSON() {

        RouteCategoryDAO routeCategoryDAO =
                DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(RouteCategory.class);

        List<RouteCategory> allCategories = routeCategoryDAO.getAll(); // Try retrieving all route categories

        // Check that the DAO could complete the operation

        if (allCategories == null)
            throw new WebApplicationException("Ocurrió un error al pedir todas las categorías de ruta",
                    Response.Status.SERVICE_UNAVAILABLE);

        // At least one category should be always retrieved

        if (allCategories.isEmpty())
            throw new WebApplicationException("No se ha encontrado ninguna categoría de ruta",
                    Response.Status.NOT_FOUND);

        logger.info("[REST] Serving all route categories");

        return allCategories;
    }

}

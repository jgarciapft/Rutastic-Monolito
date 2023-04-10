package resources;

import dao.KudoEntryDAO;
import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import model.KudoEntry;
import model.Route;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Path("/kudos")
public class KudoEntriesResource {

    private static final Logger logger = Logger.getLogger(UsersResource.class.getName());

    @Context
    ServletContext sc;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{userId : [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KudoEntry> getUserKudoEntriesJSON(
            @PathParam("userId") long userId, @Context HttpServletRequest req) {

        KudoEntryDAO kudoEntryDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(KudoEntry.class);
        HttpSession session = req.getSession();

        // Validate the requested user ID

        if (!User.validateID(userId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        // AUTHORISATION FILTER. The logged user can only retrieve his kudo entries

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser.getId() == userId) {
            logger.info("[REST] Serving kudo entries by user with ID (" + userId + ")");
            return kudoEntryDAO.getAllByUser(userId); // Return the collection of kudo entries for the requested user
        } else { // Insufficient privileges
            throw new WebApplicationException("Su usario no tiene permisos para recuperar las entradas kudo del usuario solicitado",
                    Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("/{userId : [0-9]+}/{routeId : [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public KudoEntry getUserKudoEntryForRouteJSON(
            @PathParam("userId") long userId, @PathParam("routeId") long routeId, @Context HttpServletRequest req) {

        KudoEntryDAO kudoEntryDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(KudoEntry.class);
        HttpSession session = req.getSession();

        // Validate the requested user and route IDs

        if (!User.validateID(userId) || !Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        // AUTHORISATION FILTER. The logged user can only retrieve his kudo entries

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser.getId() == userId) {
            logger.info("[REST] Serving kudo entry by user with ID (" + userId + ") to route with ID (" + routeId + ")");
            return kudoEntryDAO.getById(userId, routeId); // Return the kudo entry by the requested user to the requested route
        } else { // Insufficient privileges
            throw new WebApplicationException("Su usario no tiene permisos para recuperar las entradas kudo del usuario solicitado",
                    Response.Status.UNAUTHORIZED);
        }
    }
}

package model;

import helper.DateTimeUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Definition of Route model as a JavaBean
 */
public class Route implements Serializable {

    public static final String CATEGORY_SEPARATOR = ", ";
    private static final long SerialVersionUID = 1L;
    private long id;
    private long createdByUser;
    private String title;
    private String description;
    private int distance;
    private int duration;
    private int elevation;
    private String creationDate;
    private String categories;
    private String skillLevel;
    private int kudos;
    private boolean blocked;

    public Route() {
        // Default values
        kudos = 0;
        blocked = false;
    }

    /**
     * @param id Route ID to be validated
     * @return If the ID is valid. A valid ID is greater than 0
     */
    public static boolean validateID(long id) {
        return id > 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(long createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getKudos() {
        return kudos;
    }

    public void setKudos(int kudos) {
        this.kudos = kudos;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void changeDateFormat(SimpleDateFormat dateFormatter) {
        SimpleDateFormat currentDateFormatter = DateTimeUtils.getDateFormatter();
        String newDateFormat;

        // Try formatting the current creation date with the provided date formatter

        try {
            newDateFormat = getCreationDate() != null ?
                    dateFormatter.format(currentDateFormatter.parse(getCreationDate())) : null;

            // If it computed to a valid new date store it

            if (newDateFormat != null)
                setCreationDate(newDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate this route bean parsed from a route edition attempt. The user bean is valid if the attributes set from
     * form fields and the ID is valid (greater than 0).
     * Any validation message is added to the list {@code validationMessages}
     *
     * @param validationMessages Where to log any message for the user to see
     * @return If the route bean is valid for a route edition attempt
     */
    public boolean validateRouteEditionAttempt(List<String> validationMessages) {
        boolean validBean = true;

        // Validate route ID

        if (!validateID(getId())) {
            validationMessages.add("El ID de la ruta no es válida");
            validBean = false;
        }

        // Validate form fields

        boolean validFields = validateFormFields(validationMessages);

        return validBean && validFields;
    }

    /**
     * Validate this route bean parsed from a form. The user bean is valid if the attributes set from form fields
     * are valid.
     * Any validation message is added to the list {@code validationMessages}
     *
     * @param validationMessages Where to log any message for the user to see
     * @return If the route bean is valid
     */
    public boolean validateFormFields(List<String> validationMessages) {
        boolean validBean = true; // Suppose it initially is a valid bean

        // Validate all route attributes from form fields

        if (title == null || getTitle().trim().isEmpty()) {
            validationMessages.add("Introduzca un título para la ruta");
            validBean = false;
        }
        if (description == null || getDescription().trim().isEmpty()) {
            validationMessages.add("Introduzca una descripción para la ruta");
            validBean = false;
        }
        if (getDistance() <= 0) {
            validationMessages.add("La distancia de la ruta debe ser un número positivo de metros");
            validBean = false;
        }
        if (getDuration() <= 0) {
            validationMessages.add("La duración de la ruta debe ser un número positivo de minutos");
            validBean = false;
        }
        if (getElevation() <= 0) {
            validationMessages.add("La elevación de la ruta debe ser un número positivo de metros");
            validBean = false;
        }
        if (categories == null || getCategories().trim().isEmpty()) {
            validationMessages.add("No se ha especificado ninguna categoría para la ruta");
            validBean = false;
        }
        if (categories == null || !getCategories().trim().matches("((senderismo|carrera|ciclismo)(" + CATEGORY_SEPARATOR + ")?)+")) {
            validationMessages.add("Nombre(s) de categoría(s) desconocido(s)");
            validBean = false;
        }
        if (skillLevel == null || getSkillLevel().trim().isEmpty()) {
            validationMessages.add("No se ha especificado ninguna dificultad para la categoría");
            validBean = false;
        }
        if (skillLevel == null || !getSkillLevel().trim().matches("((facil|media|dificil),?)+")) {
            validationMessages.add("Grado de dificultad desconocido");
            validBean = false;
        }

        return validBean;
    }
}

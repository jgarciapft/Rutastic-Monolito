package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Definition of User model as a JavaBean
 */
@XmlRootElement
public class User implements Serializable {

    private static final long SerialVersionUID = 1L;

    private long id;
    private String username;
    private String email;
    private String password;
    private String role;

    public User() {
        // Default values
        role = "user";
    }

    /**
     * @param id User ID to be validated
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Validate this user bean parsed from a login attempt. The user bean is valid if the username and password
     * have been set. Due to security concerns there's no additional validation error messages sent back to the user.
     *
     * @return If the user bean is valid for a login attempt
     */
    public boolean validateLoginAttempt() {
        return (username != null && !getUsername().isEmpty()) && (password != null && !getPassword().isEmpty());
    }

    /**
     * Validate this user bean parsed from a registration submission attempt. The user bean is valid if the email,
     * password and username have been set. Any validation message is added to the list {@code validationMessages}
     *
     * @param validationMessages Where to log any message for the user to see
     * @return If the user bean is valid for a registration submission attempt
     */
    public boolean validateRegistrationAttempt(List<String> validationMessages) {
        boolean validBean = true; // Suppose it initially is a valid bean

        // Validate all user attribute involved and add the necessary error messages

        if (email == null || !validateEmail()) {
            validationMessages.add("El correo no es válido");
            validBean = false;
        }
        if (username == null || getUsername().trim().isEmpty()) {
            validationMessages.add("El nombre de usuario no puede estar vacío");
            validBean = false;
        }
        if (password == null || getPassword().trim().isEmpty()) {
            validationMessages.add("La contraseña no puede estar vacía");
            validBean = false;
        }

        return validBean;
    }

    /**
     * Validate this user bean parsed from a profile update attempt. The user bean is valid if the email has been set
     * (password change is performed elsewhere) and the user hasn't altered the username field
     * Any validation message is added to the list {@code validationMessages}
     *
     * @param validationMessages Where to log any message for the user to see
     * @return If the user bean is valid for a profile update attempt
     */
    public boolean validateProfileUpdateAttempt(List<String> validationMessages) {
        boolean validBean = true; // Suppose it initially is a valid bean

        // Validate all involved user attributes

        if (username == null || getUsername().trim().isEmpty()) {
            validationMessages.add("No podemos obtener el perfil de usuario que quiere editar");
            validBean = false;
        }
        if (email == null || !validateEmail()) {
            validationMessages.add("Proporcione un correo válido");
            validBean = false;
        }

        return validBean;
    }

    /**
     * @return If the email address is valid
     */
    private boolean validateEmail() {
        return email != null && getEmail().trim().matches("\\w+(.\\w+)*@\\w+(.\\w+)+");
    }
}

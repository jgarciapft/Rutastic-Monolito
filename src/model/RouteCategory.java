package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Definition of RouteCategory model as a JavaBean
 */
@XmlRootElement
public class RouteCategory implements Serializable {

    private static final long SerialVersionUID = 1L;

    private long id;
    private String name;
    private String description;

    public RouteCategory() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

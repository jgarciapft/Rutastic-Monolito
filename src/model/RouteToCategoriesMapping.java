package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Definition of RouteToCategoriesMapping model as a JavaBean
 */
@XmlRootElement
public class RouteToCategoriesMapping implements Serializable {

    private static final long SerialVersionUID = 1L;

    private long route;
    private long category;

    public RouteToCategoriesMapping() {
    }

    public long getRoute() {
        return route;
    }

    public void setRoute(long route) {
        this.route = route;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }
}

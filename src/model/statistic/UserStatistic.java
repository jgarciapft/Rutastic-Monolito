package model.statistic;

import java.io.Serializable;

/**
 * Java Bean that can hold one statistic about an user identified by the username
 */
public class UserStatistic implements Serializable {

    private static final long SerialVersionUID = 1L;

    private String username;
    private float stat;

    public UserStatistic() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getStat() {
        return stat;
    }

    public void setStat(float stat) {
        this.stat = stat;
    }
}

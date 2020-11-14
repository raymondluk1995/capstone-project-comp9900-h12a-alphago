package alphago.propertysale.shiro;

import java.io.Serializable;
import java.util.Objects;

/**
 * @program: propertysale
 * @description: POJO store user information for jwt
 **/
public class JwtInfo implements Serializable {
    private long uid;
    private String username;

    JwtInfo(){}

    public JwtInfo(long uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtInfo jwtInfo = (JwtInfo) o;
        return uid == jwtInfo.uid &&
                Objects.equals(username, jwtInfo.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, username);
    }
}
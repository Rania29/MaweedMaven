package entity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class UserAuth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Basic
    private String name;

    @Basic
    private String phoneNo;

    @Basic
    private String password;

    @Basic
    @Column(unique = true)
    private String email;

    @ManyToOne
    private Hospital hospital;

    @ManyToMany
    private List<GroupAuth> groupAuths;

    public UserAuth() {
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public List<GroupAuth> getGroupAuths() {
        if (groupAuths == null) {
            groupAuths = new ArrayList<>();
        }
        return this.groupAuths;
    }

    public void setGroupAuths(List<GroupAuth> groupAuths) {
        this.groupAuths = groupAuths;
    }

    public void addGroupAuth(GroupAuth groupAuth) {
        getGroupAuths().add(groupAuth);
        groupAuth.getUserAuths().add(this);
    }

    public void removeGroupAuth(GroupAuth groupAuth) {
        getGroupAuths().remove(groupAuth);
        groupAuth.getUserAuths().remove(this);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAuth other = (UserAuth) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserAuth{" + "name=" + name + '}';
    }

}

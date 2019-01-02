/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "userauth_groupauth_v")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserauthGroupauthV.findAll", query = "SELECT u FROM UserauthGroupauthV u")
    , @NamedQuery(name = "UserauthGroupauthV.findByEmail", query = "SELECT u FROM UserauthGroupauthV u WHERE u.email = :email")
    , @NamedQuery(name = "UserauthGroupauthV.findByPassword", query = "SELECT u FROM UserauthGroupauthV u WHERE u.password = :password")
    , @NamedQuery(name = "UserauthGroupauthV.findByGroupname", query = "SELECT u FROM UserauthGroupauthV u WHERE u.groupname = :groupname")})
public class UserauthGroupauthV implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    @Id
    private String email;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "groupname")
    private String groupname;

    public UserauthGroupauthV() {
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

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Samir
 */
@Entity
@Table(name = "nationalities")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nationalities.findAll", query = "SELECT n FROM Nationalities n")
    , @NamedQuery(name = "Nationalities.findByName", query = "SELECT n FROM Nationalities n WHERE n.name = :name")
    , @NamedQuery(name = "Nationalities.findByInarabic", query = "SELECT n FROM Nationalities n WHERE n.inarabic = :inarabic")})
public class Nationalities implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "name")
    private String name;
    @Size(max = 80)
    @Column(name = "inarabic")
    private String inarabic;
    @OneToMany(mappedBy = "nationalities")
    private List<Doctor> doctors;

    public Nationalities() {
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Nationalities(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInarabic() {
        return inarabic;
    }

    public void setInarabic(String inarabic) {
        this.inarabic = inarabic;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nationalities)) {
            return false;
        }
        Nationalities other = (Nationalities) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}

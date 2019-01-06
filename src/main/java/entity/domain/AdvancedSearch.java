/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.domain;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Samir
 */
@Entity
@Table(name = "advanced_search")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdvancedSearch.findAll", query = "SELECT a FROM AdvancedSearch a")
    , @NamedQuery(name = "AdvancedSearch.findById", query = "SELECT a FROM AdvancedSearch a WHERE a.id = :id")
    , @NamedQuery(name = "AdvancedSearch.findByCategName", query = "SELECT a FROM AdvancedSearch a WHERE a.categName = :categName")
    , @NamedQuery(name = "AdvancedSearch.findByCategAndArea", query = "SELECT a FROM AdvancedSearch a WHERE a.categName = :categName AND a.areaName = :areaName")
    , @NamedQuery(name = "AdvancedSearch.findByCategArAndAreaAr", query = "SELECT a FROM AdvancedSearch a WHERE a.categArabic = :categArabic AND a.areaArabic = :areaArabic")
    , @NamedQuery(name = "AdvancedSearch.findByCategArabic", query = "SELECT a FROM AdvancedSearch a WHERE a.categArabic = :categArabic")
    , @NamedQuery(name = "AdvancedSearch.findByHospitalName", query = "SELECT a FROM AdvancedSearch a WHERE a.hospitalName = :hospitalName")
    , @NamedQuery(name = "AdvancedSearch.findByHospitalArabic", query = "SELECT a FROM AdvancedSearch a WHERE a.hospitalArabic = :hospitalArabic")
    , @NamedQuery(name = "AdvancedSearch.findByAreaName", query = "SELECT a FROM AdvancedSearch a WHERE a.areaName = :areaName")
    , @NamedQuery(name = "AdvancedSearch.findByAreaArabic", query = "SELECT a FROM AdvancedSearch a WHERE a.areaArabic = :areaArabic")})
public class AdvancedSearch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    private BigInteger id;
    @Size(max = 255)
    @Column(name = "categ_name")
    private String categName;
    @Size(max = 255)
    @Column(name = "categ_arabic")
    private String categArabic;
    @Size(max = 255)
    @Column(name = "hospital_name")
    private String hospitalName;
    @Size(max = 255)
    @Column(name = "hospital_arabic")
    private String hospitalArabic;
    @Size(max = 255)
    @Column(name = "area_name")
    private String areaName;
    @Size(max = 255)
    @Column(name = "area_arabic")
    private String areaArabic;

    public AdvancedSearch() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCategName() {
        return categName;
    }

    public void setCategName(String categName) {
        this.categName = categName;
    }

    public String getCategArabic() {
        return categArabic;
    }

    public void setCategArabic(String categArabic) {
        this.categArabic = categArabic;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalArabic() {
        return hospitalArabic;
    }

    public void setHospitalArabic(String hospitalArabic) {
        this.hospitalArabic = hospitalArabic;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaArabic() {
        return areaArabic;
    }

    public void setAreaArabic(String areaArabic) {
        this.areaArabic = areaArabic;
    }
    
}

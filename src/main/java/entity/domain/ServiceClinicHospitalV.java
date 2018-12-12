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

@Entity
@Table(name = "service_clinic_hospital_v")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceClinicHospitalV.findAll", query = "SELECT s FROM ServiceClinicHospitalV s")
    , @NamedQuery(name = "ServiceClinicHospitalV.findById", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.id = :id")
    , @NamedQuery(name = "ServiceClinicHospitalV.findByPrice", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.price = :price")
    , @NamedQuery(name = "ServiceClinicHospitalV.findByDiscount", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.discount = :discount")
    , @NamedQuery(name = "ServiceClinicHospitalV.findByServicename", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.servicename = :servicename")
    , @NamedQuery(name = "ServiceClinicHospitalV.findByCategoryname", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.categoryname = :categoryname")
    , @NamedQuery(name = "ServiceClinicHospitalV.findServicesByClinicHospital", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.categoryname = :categoryname"
            + " AND s.hospitalname = :hospitalname")
    , @NamedQuery(name = "ServiceClinicHospitalV.findByHospitalname", query = "SELECT s FROM ServiceClinicHospitalV s WHERE s.hospitalname = :hospitalname")})
public class ServiceClinicHospitalV implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "price")
    private BigInteger price;
    @Column(name = "discount")
    private BigInteger discount;
    @Size(max = 255)
    @Column(name = "servicename")
    private String servicename;
    @Size(max = 255)
    @Column(name = "categoryname")
    private String categoryname;
    @Size(max = 255)
    @Column(name = "hospitalname")
    private String hospitalname;

    public ServiceClinicHospitalV() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public BigInteger getDiscount() {
        return discount;
    }

    public void setDiscount(BigInteger discount) {
        this.discount = discount;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }
    
}

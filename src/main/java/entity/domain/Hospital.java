package entity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hospital.findAll", query = "SELECT h FROM Hospital h")
    , @NamedQuery(name = "Hospital.findHospitalNames", query = "SELECT h.name FROM Hospital h order by h.name")
    , @NamedQuery(name = "Hospital.findByName", query = "SELECT h FROM Hospital h WHERE h.name = :name")
    , @NamedQuery(name = "Hospital.findHospitalsByMembership", query = "SELECT h FROM Hospital h WHERE h.membership = :membership")
    , @NamedQuery(name = "Hospital.findByInArabicName", query = "SELECT h FROM Hospital h WHERE h.inArabic = :inArabic")
    ,@NamedQuery(name = "Hospital.findHospitalNamesArabic", query = "SELECT h.inArabic FROM Hospital h order by h.inArabic")})
public class Hospital implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    @Basic
    private String membership;

    @Basic
    private String address;

    @Basic
    private String addressAr;

    @Basic
    private String inArabic;

    @Basic
    private String phoneNo;

    @Basic
    private String email;

    @Basic
    private String locationMap;

    @Basic
    private String workingDaysHours;

    @Basic
    private String workingDaysHoursArabic;

    @ManyToOne
    private Area area;

    @OneToMany(mappedBy = "hospital")
    private List<Offer> offers;

    @OneToMany(mappedBy = "hospital")
    private List<Clinic> clinics;

    @OneToMany(mappedBy = "hospital")
    private List<HospitalImage> hospitalImages;

//    @ManyToMany(mappedBy = "hospitals")
    @ManyToMany
    private List<Insurance> insurances;

    public Hospital() {
    }

    public String getAddress() {
        return address;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressAr() {
        return addressAr;
    }

    public void setAddressAr(String addressAr) {
        this.addressAr = addressAr;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getWorkingDaysHoursArabic() {
        return workingDaysHoursArabic;
    }

    public void setWorkingDaysHoursArabic(String workingDaysHoursArabic) {
        this.workingDaysHoursArabic = workingDaysHoursArabic;
    }

    public String getInArabic() {
        return inArabic;
    }

    public void setInArabic(String inArabic) {
        this.inArabic = inArabic;
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

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocationMap() {
        return this.locationMap;
    }

    public void setLocationMap(String locationMap) {
        this.locationMap = locationMap;
    }

    public String getWorkingDaysHours() {
        return this.workingDaysHours;
    }

    public void setWorkingDaysHours(String workingDaysHours) {
        this.workingDaysHours = workingDaysHours;
    }

    public Area getArea() {
        return this.area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Clinic> getClinics() {
        if (clinics == null) {
            clinics = new ArrayList<>();
        }
        return this.clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    public void addClinic(Clinic clinic) {
        getClinics().add(clinic);
        clinic.setHospital(this);
    }

    public void removeClinic(Clinic clinic) {
        getClinics().remove(clinic);
        clinic.setHospital(null);
    }

    public List<HospitalImage> getHospitalImages() {
        if (hospitalImages == null) {
            hospitalImages = new ArrayList<>();
        }
        return this.hospitalImages;
    }

    public void setHospitalImages(List<HospitalImage> hospitalImages) {
        this.hospitalImages = hospitalImages;
    }

    public void addHospitalImage(HospitalImage hospitalImage) {
        getHospitalImages().add(hospitalImage);
        hospitalImage.setHospital(this);
    }

    public void removeHospitalImage(HospitalImage hospitalImage) {
        getHospitalImages().remove(hospitalImage);
        hospitalImage.setHospital(null);
    }

    public List<Insurance> getInsurances() {
        if (insurances == null) {
            insurances = new ArrayList<>();
        }
        return this.insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }

    public void addInsurance(Insurance insurance) {
        getInsurances().add(insurance);
    }

    public void removeInsurance(Insurance insurance) {
        getInsurances().remove(insurance);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Hospital other = (Hospital) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}

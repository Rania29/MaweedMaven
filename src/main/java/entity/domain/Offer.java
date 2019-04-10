package entity.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Offer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String offerType;
    
    @Basic
    private String details;
    
    @Basic
    private String detailsInArabic;

    @Basic
    private Double discountPrice;
    
    @Basic
    private Double price;
    
    @Basic
    private Double percentage;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date start_date;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date end_date;

    @ManyToOne
    private Clinic clinic;

    @ManyToOne
    private Hospital hospital;

    @ManyToOne
    private ClinicService clinicService;

    public Offer() {
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getDetailsInArabic() {
        return detailsInArabic;
    }

    public void setDetailsInArabic(String detailsInArabic) {
        this.detailsInArabic = detailsInArabic;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    
    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDiscountPrice() {
        return this.discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Date getStart_date() {
        return this.start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Clinic getClinic() {
        return this.clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public ClinicService getClinicService() {
        return clinicService;
    }

    public void setClinicService(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.offerType);
        hash = 71 * hash + Objects.hashCode(this.details);
        hash = 71 * hash + Objects.hashCode(this.detailsInArabic);
        hash = 71 * hash + Objects.hashCode(this.discountPrice);
        hash = 71 * hash + Objects.hashCode(this.price);
        hash = 71 * hash + Objects.hashCode(this.percentage);
        hash = 71 * hash + Objects.hashCode(this.start_date);
        hash = 71 * hash + Objects.hashCode(this.end_date);
        hash = 71 * hash + Objects.hashCode(this.clinic);
        hash = 71 * hash + Objects.hashCode(this.hospital);
        hash = 71 * hash + Objects.hashCode(this.clinicService);
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
        final Offer other = (Offer) obj;
        if (!Objects.equals(this.offerType, other.offerType)) {
            return false;
        }
        if (!Objects.equals(this.details, other.details)) {
            return false;
        }
        if (!Objects.equals(this.detailsInArabic, other.detailsInArabic)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.discountPrice, other.discountPrice)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.percentage, other.percentage)) {
            return false;
        }
        if (!Objects.equals(this.start_date, other.start_date)) {
            return false;
        }
        if (!Objects.equals(this.end_date, other.end_date)) {
            return false;
        }
        if (!Objects.equals(this.clinic, other.clinic)) {
            return false;
        }
        if (!Objects.equals(this.hospital, other.hospital)) {
            return false;
        }
        if (!Objects.equals(this.clinicService, other.clinicService)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Offer{" + "id=" + id + ", offerType=" + offerType + ", details=" + details + ", detailsInArabic=" + detailsInArabic + ", discountPrice=" + discountPrice + ", price=" + price + ", percentage=" + percentage + ", start_date=" + start_date + ", end_date=" + end_date + ", clinic=" + clinic + ", hospital=" + hospital + ", clinicService=" + clinicService + '}';
    }
    
}

package entity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
    @NamedQuery(name = "Appointment.findAll", query = "SELECT a FROM Appointment a")
    , @NamedQuery(name = "Appointment.findById", query = "SELECT a FROM Appointment a WHERE a.id = :id")
    , @NamedQuery(name = "Appointment.findByDob", query = "SELECT a FROM Appointment a WHERE a.DOB = :DOB")
    , @NamedQuery(name = "Appointment.findByDescription", query = "SELECT a FROM Appointment a WHERE a.description = :description")
    , @NamedQuery(name = "Appointment.findByEmail", query = "SELECT a FROM Appointment a WHERE a.email = :email")
    , @NamedQuery(name = "Appointment.findByName", query = "SELECT a FROM Appointment a WHERE a.name = :name")
    , @NamedQuery(name = "Appointment.findByPhone", query = "SELECT a FROM Appointment a WHERE a.phone = :phone")})
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    @Basic
    private String phone;

    @Basic
    private String email;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date DOB;

    @Basic
    private String description;

    @ManyToOne
    private Hospital hospital;

    @ManyToOne
    private Gender gender;

    @ManyToOne
    private Shift morningOrEvening;

    @ManyToOne
    private Gender doctorGender;

    @ManyToMany
    private List<DaysOfWeek> daysOfWeeks;

    public Appointment() {
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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDOB() {
        return this.DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Shift getMorningOrEvening() {
        return this.morningOrEvening;
    }

    public void setMorningOrEvening(Shift morningOrEvening) {
        this.morningOrEvening = morningOrEvening;
    }

    public Gender getDoctorGender() {
        return this.doctorGender;
    }

    public void setDoctorGender(Gender doctorGender) {
        this.doctorGender = doctorGender;
    }

    public List<DaysOfWeek> getDaysOfWeeks() {
        if (daysOfWeeks == null) {
            daysOfWeeks = new ArrayList<>();
        }
        return this.daysOfWeeks;
    }

    public void setDaysOfWeeks(List<DaysOfWeek> daysOfWeeks) {
        this.daysOfWeeks = daysOfWeeks;
    }

    public void addDaysOfWeek(DaysOfWeek daysOfWeek) {
        getDaysOfWeeks().add(daysOfWeek);
    }

    public void removeDaysOfWeek(DaysOfWeek daysOfWeek) {
        getDaysOfWeeks().remove(daysOfWeek);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.phone);
        hash = 17 * hash + Objects.hashCode(this.email);
        hash = 17 * hash + Objects.hashCode(this.DOB);
        hash = 17 * hash + Objects.hashCode(this.description);
        hash = 17 * hash + Objects.hashCode(this.hospital);
        hash = 17 * hash + Objects.hashCode(this.gender);
        hash = 17 * hash + Objects.hashCode(this.morningOrEvening);
        hash = 17 * hash + Objects.hashCode(this.doctorGender);
        hash = 17 * hash + Objects.hashCode(this.daysOfWeeks);
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
        final Appointment other = (Appointment) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.DOB, other.DOB)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.hospital, other.hospital)) {
            return false;
        }
        if (!Objects.equals(this.gender, other.gender)) {
            return false;
        }
        if (!Objects.equals(this.morningOrEvening, other.morningOrEvening)) {
            return false;
        }
        if (!Objects.equals(this.doctorGender, other.doctorGender)) {
            return false;
        }
        if (!Objects.equals(this.daysOfWeeks, other.daysOfWeeks)) {
            return false;
        }
        return true;
    }

}

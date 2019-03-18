package entity.domain;

import entity.domain.util.JsfUtil;
import entity.domain.util.PaginationHelper;
import entity.domain.util.SendMail;
import facade.AdvancedSearchFacade;
import facade.AppointmentFacade;
import facade.AreaFacade;
import facade.CategoryFacade;
import facade.ClinicFacade;
import facade.ClinicServiceFacade;
import facade.DaysOfWeekFacade;
import facade.GuestFacade;
import facade.HospitalFacade;
import facade.ServiceClinicHospitalVFacade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

@Named("appointmentController")
@SessionScoped
public class AppointmentController implements Serializable {

    @EJB
    private ClinicFacade clinicFacade;
    @EJB
    private GuestFacade guestFacade;
    @EJB
    private AdvancedSearchFacade advancedSearchFacade;
    @Inject
    private Guest guest;
    @Inject
    private Clinic clinic;
    @Inject
    private AdvancedSearch advancedSearch;
    private List<AdvancedSearch> advancedSearchList;
    @Inject
    private Hospital hospital;
    @Inject
    private Area area;
    @Inject
    private Category category;
    @EJB
    private CategoryFacade categoryFacade;
    @EJB
    private DaysOfWeekFacade daysOfWeekFacade;
    @EJB
    private AreaFacade areaFacade;
    @EJB
    private HospitalFacade hospitalFacade;
    @EJB
    private ClinicServiceFacade clinicServiceFacade;
    @EJB
    private ServiceClinicHospitalVFacade serviceClinicHospitalVFacade;
    private List<String> days;
    private List<Hospital> hospitals;
    private List<Clinic> clinics;
    private List<ClinicService> clinicServices;
    private Appointment current;
    private DataModel items = null;
    @EJB
    private facade.AppointmentFacade ejbFacade;

    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String item = "";

    @PostConstruct
    public void init() {
        days = new ArrayList<>();
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public AdvancedSearch getAdvancedSearch() {
        return advancedSearch;
    }

    public List<ClinicService> getClinicServices() {
        return clinicServices;
    }

    public Appointment fetchDetails() {
        Map<String, String> id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return ejbFacade.find(Long.valueOf(id.get("id")));
    }

    public void setClinicServices(List<ClinicService> clinicServices) {
        this.clinicServices = clinicServices;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public List<AdvancedSearch> getAdvancedSearchList() {
        return advancedSearchList;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public void findByAreaAndCategory(String lang) {
        List<Object[]> ads;
        System.out.println("hospitals: " + hospitals);
        if (hospitals == null) {
            hospitals = new ArrayList<>();
        } else {
            hospitals.clear();
        }
        if (lang.equals("english")) {
            ads = advancedSearchFacade.findByAreaAndCategory(hospital.getArea(), clinic);
            for (Object[] ad : ads) {
                hospitals.add((Hospital) hospitalFacade.findHospitalByName(ad[0].toString()));
            }
        } else {
            ads = advancedSearchFacade.findByAreaAndCategoryAr(hospital.getArea(), clinic);
            for (Object[] ad : ads) {
                hospitals.add((Hospital) hospitalFacade.findHospitalsByInArabic(ad[0].toString()));
            }
        }
    }

//    public void findSearchResults(String lang) {
//        if (hospitals == null) {
//            hospitals = new ArrayList<>();
//        }
//        hospitals.clear();
//        if (lang.equals("english")) {
//            System.out.println("findSearchResults clinic.............. " + clinic);
//            if (clinic == null) {
//                clinics = clinicFacade.findAll();
//            } else {
//                clinics = clinicFacade.findClinicByCat(clinic.getCategory());
//            }
//            System.out.println("clinics........... " + clinics);
//            for (Clinic c : clinics) {
//                if (c.getHospital().getArea().getName().equals(hospital.getArea().getName())) {
//                    hospitals.add(c.getHospital());
//                }
//            }
//        } else {
//            clinics = clinicFacade.findClinicByCat(clinic.getCategory());
//            for (Clinic c : clinics) {
//                if (c.getHospital().getArea().getInArabic().equals(hospital.getArea().getInArabic())) {
//                    hospitals.add(c.getHospital());
//                }
//            }
//        }
//    }
    public String findHospitalsByName() {
        hospitals = hospitalFacade.findHospitalsByName(hospital.getName());
        return "search?faces-redirect=true";
    }

    public String findHospitalsByInArabicName() {
        hospitals = hospitalFacade.findHospitalsByInArabic(hospital.getInArabic());
        return "search_ar?faces-redirect=true";
    }

    public String toClinicHospital(Hospital hospital) {
        this.hospital = hospital;
        if (current == null) {
            prepareCreate();
        }
        current.setHospital(hospital);
        return "clinics?faces-redirect=true";
    }

    public String toClinicImage(HospitalImage hospitalImage) {
        this.hospital = hospitalImage.getHospital();
        if (current == null) {
            prepareCreate();
        }
        current.setHospital(hospital);
        return "clinics?faces-redirect=true";
    }

    public void clinicChange(ValueChangeEvent e) {
        if (clinicServices != null) {
            clinicServices.clear();
        } else {
            clinicServices = new ArrayList<>();
        }
        if (e.getNewValue() == null) {
            clinicServices.clear();
        } else {
            for (ServiceClinicHospitalV ser : serviceClinicHospitalVFacade.findServicesByClinicHospital(e.getNewValue().toString(), hospital.getName())) {
                clinicServices.add(clinicServiceFacade.find(ser.getId().longValue()));
            }
        }
    }

    public String toServices(Clinic clinic, String language) {
        this.clinic = clinic;
        if (current == null) {
            prepareCreate();
        }
        clinicServices = clinic.getClinicServices();
        if (language.equals("english")) {
            return "clinic-services?faces-redirect=true";
        } else {
            return "clinic-services_ar?faces-redirect=true";
        }
    }

    public String toClinicArabic(Hospital hospital) {
        this.hospital = hospital;
        if (current == null) {
            prepareCreate();
        }
        current.setHospital(hospital);
        return "clinics_ar?faces-redirect=true";
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public AppointmentController() {
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public Appointment getSelected() {
        if (current == null) {
            current = new Appointment();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AppointmentFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        current = (Appointment) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        days.clear();
        current = new Appointment();
        selectedItemIndex = -1;
        return "appointment?faces-redirect=true";
    }

    public String create() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        item = "";
        String dob = dateFormat.format(current.getDOB());
        for (String id : days) {
            DaysOfWeek daysOfWeek = daysOfWeekFacade.find(Long.parseLong(id));
            current.addDaysOfWeek(daysOfWeek);
        }
        try {
            current.getDaysOfWeeks().forEach(p -> item += p.getName() + ", ");
            getFacade().create(current);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("We will get back to you at the soonest " + current.getName()));
            String doctorGender = current.getDoctorGender() == null ? "Any" : current.getDoctorGender().getName();
            String shift = current.getMorningOrEvening() == null ? "Any" : current.getMorningOrEvening().getName();
            String msg = "<table border='1'>"
                    + "<tr>"
                    + "<th>Name</th><td>" + current.getName() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Email</th><td>" + current.getEmail() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Phone</th><td>" + current.getPhone() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Date of Birth (dd/mm/yyyy)</th><td>" + dob + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Gender</th><td>" + current.getGender() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Clinic</th><td>" + current.getClinic() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Service</th><td>" + current.getClinicservice() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Preferred Doctor</th><td>" + doctorGender + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Preferred timing</th><td>" + shift + "</td>"
                    + "<tr>"
                    + "<th>Preferred Days</th><td>" + item + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<th>Problem</th><td>" + current.getDescription() + "</td>"
                    + "</tr>"
                    + "</table>";
            SendMail.sendMail("maweed.noreply@gmail.com", "m@weed!29site", "Appointment Request - " + current.getEmail(), msg, current.getHospital().getEmail());
            SendMail.sendMail("maweed.noreply@gmail.com", "m@weed!29site", "Appointment Request - " + current.getEmail(), msg, "rania.rabie29@gmail.com");
            return prepareCreate();
        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String toAppointment(String appoint, String email) {
        if (!email.equals("")) {
            Guest g = ((Guest) guestFacade.findGuestByEmail(email));
            current.setEmail(g.getEmail());
            current.setName(g.getName());
            current.setInArabic(g.getInArabic());
            current.setPhone(g.getPhone());
            current.setDOB(g.getDOB());
            current.setGender(g.getGender());
        }
        if (clinic.getCategory() != null) {
            current.setClinic(clinic.getCategory().getName());
        }
        if (appoint.equals("english")) {
            return "appointment.xhtml?faces-redirect=true";
        }
        return "appointment_ar.xhtml?faces-redirect=true";
    }

    public String prepareEdit() {
        current = (Appointment) getItems().getRowData();
        days.clear();
        for (DaysOfWeek d : current.getDaysOfWeeks()) {
            days.add(d.getId().toString());
        }
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        current.getDaysOfWeeks().clear();
        for (String id : days) {
            DaysOfWeek daysOfWeek = daysOfWeekFacade.find(Long.parseLong(id));
            current.addDaysOfWeek(daysOfWeek);
        }
        try {
            getFacade().edit(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppointmentUpdated"));
            return "View?faces-redirect=true";
        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Appointment) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View?faces-redirect=true";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List?faces-redirect=true";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppointmentDeleted"));
        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Appointment getAppointment(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Appointment.class)
    public static class AppointmentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AppointmentController controller = (AppointmentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "appointmentController");
            return controller.getAppointment(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Appointment) {
                Appointment o = (Appointment) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Appointment.class.getName());
            }
        }

    }

}

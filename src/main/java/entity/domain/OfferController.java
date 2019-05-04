package entity.domain;

import entity.domain.util.JsfUtil;
import entity.domain.util.PaginationHelper;
import facade.OfferFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("offerController")
@SessionScoped
public class OfferController implements Serializable {

    private Offer current;
    private DataModel items = null;
    @EJB
    private facade.OfferFacade ejbFacade;
    @EJB
    private facade.HospitalFacade hospitalFacade;
    @EJB
    private facade.ClinicFacade clinicFacade;
    @EJB
    private facade.ClinicServiceFacade clinicServiceFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Clinic> clinics;
    private List<ClinicService> clinicServices;
    private Hospital hospital;
    private Clinic clinic;
    private ClinicService clinicService;
    private List<Offer> offers;
    
    @PostConstruct
    public void init() {
    }

    public OfferController() {
    }

    public Offer getSelected() {
        if (current == null) {
            current = new Offer();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Offer> getOffers() {
        offers = ejbFacade.findAll();
        return offers;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    public List<ClinicService> getClinicServices() {
        return clinicServices;
    }

    public void setClinicServices(List<ClinicService> clinicServices) {
        this.clinicServices = clinicServices;
    }

    private OfferFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(3) {

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
        return "List";
    }

    public String prepareView() {
        current = (Offer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        if (clinics != null) {
            clinics.clear();
        }
        if (clinicServices != null) {
            clinicServices.clear();
        }
        current = new Offer();
        selectedItemIndex = -1;
        return "Create";
    }

    public void clinicChange(ValueChangeEvent e) {
                
        if (clinicServices == null) {
            clinicServices = new ArrayList<>();
        } else {
            clinicServices.clear();
        }
        
        if (!ejbFacade.findServicesByHospitalAndClinic(hospital.getName(), e.getNewValue().toString().split(",")[0]).isEmpty()) {
            for (Object[] s : ejbFacade.findServicesByHospitalAndClinic(hospital.getName(), e.getNewValue().toString().split(",")[0])) {
                clinicServices.add(clinicServiceFacade.find(s[0]));
            }
        }
    }

    public void hospitalChange(ValueChangeEvent e) {

        if (clinics != null) {
            clinics.clear();
        }
        if (clinicServices != null) {
            clinicServices.clear();
        }
        hospital = (Hospital) hospitalFacade.findHospitalByName(e.getNewValue().toString());
        if (!hospital.getClinics().isEmpty()) {
            clinics = hospital.getClinics();
            current.setClinic(clinics.get(0));
            if (!current.getClinic().getClinicServices().isEmpty()) {
                clinicService = current.getClinic().getClinicServices().get(0);
                clinicServices = current.getClinic().getClinicServices();
            }
        }
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundles").getString("OfferCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundles").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Offer) getItems().getRowData();
        if (!current.getHospital().getClinics().isEmpty()) {
            clinics = current.getHospital().getClinics();
            current.setClinic(current.getClinic());
            if (!current.getClinic().getClinicServices().isEmpty()) {
                clinicService = current.getClinicService();
                clinicServices = current.getClinic().getClinicServices();
            }
        }
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundles").getString("OfferUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundles").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Offer) getItems().getRowData();
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
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundles").getString("OfferDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundles").getString("PersistenceErrorOccured"));
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

    public Offer getOffer(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Offer.class)
    public static class OfferControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OfferController controller = (OfferController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "offerController");
            return controller.getOffer(getKey(value));
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
            if (object instanceof Offer) {
                Offer o = (Offer) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Offer.class.getName());
            }
        }

    }

}

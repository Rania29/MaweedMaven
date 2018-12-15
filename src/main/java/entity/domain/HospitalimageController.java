package entity.domain;

import entity.domain.util.JsfUtil;
import entity.domain.util.PaginationHelper;
import facade.HospitalImageFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("hospitalimageController")
@SessionScoped
public class HospitalimageController implements Serializable {

    private HospitalImage current;
    private DataModel items = null;
    @EJB
    private facade.HospitalImageFacade ejbFacade;
    @EJB
    private facade.HospitalFacade hospitalFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public HospitalimageController() {
    }

    public HospitalImage getSelected() {
        if (current == null) {
            current = new HospitalImage();
            selectedItemIndex = -1;
        }
        return current;
    }

    private HospitalImageFacade getFacade() {
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
        return "List";
    }

    public String prepareView() {
        current = (HospitalImage) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new HospitalImage();
        selectedItemIndex = -1;
        return "Create";
    }

    public List<HospitalImage> getIndexImages() {
        List<Hospital> hospitals = hospitalFacade.findHospitalsByMembership("premium");
                        System.out.println("getIndexImages findHospitalsByMembership....................................... " + hospitals);
        List<HospitalImage> hospitalImages = new ArrayList<>();
        if (hospitals.size() > 0) {
            for (Hospital hosp : hospitals) {
                System.out.println("getIndexImages hosp.getHospitalImages....................................... " + hosp.getHospitalImages());
                if (!hosp.getHospitalImages().isEmpty()) {
                    for (HospitalImage hospitalImage : hosp.getHospitalImages()) {
                        if (hospitalImage.getImageType().equals("paid")) {
                            hospitalImages.add(hospitalImage);
                        }
                    }
                }
            }
        }
        return hospitalImages;
    }

    public List<HospitalImage> getImages(String hospital) {
        Hospital h = (Hospital) hospitalFacade.findHospitalByName(hospital);
        List<HospitalImage> hospitalImages = h.getHospitalImages();
        for (HospitalImage hImage : hospitalImages) {
            if (!hImage.getImageType().equals("slideshow")) {
                hospitalImages.remove(hImage);
            }
        }
        return hospitalImages;
    }

    public String findFirstImage(Hospital hospital) {
        String image;
        try {
            image = ((HospitalImage) (ejbFacade.findFirstImage(hospital))).getImage();
        } catch (NullPointerException e) {
            return "resources/interface/ambulance32.png";
        }
        return image;
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HospitalimageCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (HospitalImage) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HospitalimageUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (HospitalImage) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HospitalimageDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
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

    public HospitalImage getHospitalimage(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = HospitalImage.class)
    public static class HospitalimageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HospitalimageController controller = (HospitalimageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "hospitalimageController");
            return controller.getHospitalimage(getKey(value));
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
            if (object instanceof HospitalImage) {
                HospitalImage o = (HospitalImage) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + HospitalImage.class.getName());
            }
        }

    }

}

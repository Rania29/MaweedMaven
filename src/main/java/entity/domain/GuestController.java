package entity.domain;

import entity.domain.util.EncryptPassword;
import entity.domain.util.JsfUtil;
import entity.domain.util.PaginationHelper;
import entity.domain.util.SendMail;
import facade.GuestFacade;
import java.io.PrintWriter;

import java.io.Serializable;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@Named("guestController")
@SessionScoped
public class GuestController implements Serializable {

    private HttpSession httpSession;
    private Guest current;
    private DataModel items = null;
    @EJB
    private facade.GuestFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String currentPage;

    public GuestController() {
    }

    public Guest getSelected() {
        if (current == null) {
            current = new Guest();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GuestFacade getFacade() {
        return ejbFacade;
    }

    public HttpSession getHttpSession() {
        return httpSession;
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
        current = (Guest) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Guest();
        selectedItemIndex = -1;
        return "Create";
    }

    public String back() {
        return currentPage + "?faces-redirect=true";
    }

    public String authenticate() throws NoSuchAlgorithmException {
        for (Guest g : ejbFacade.findAll()) {
            if (g.getEmail().equals(current.getEmail()) && g.getPassword().equalsIgnoreCase(new EncryptPassword().encrypt("MD5", current.getPassword()))) {
                current = g;
                httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                httpSession.setAttribute("email", g.getEmail());
                return currentPage;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed to log in!"));
            }
        }
        return null;
    }

    public String saveCurrentPage(String fromPage, String toPage) {
        currentPage = fromPage;
        return toPage;
    }

    public void logout() {
        httpSession.invalidate();
        httpSession = null;
    }

    public boolean isValid() {
        return (httpSession == null);
    }

    public String create() {
        try {
            String body = "Nice to have you " + current.getName() + ",\n\nYour Password is: " + current.getPassword() + "\n\nThanks\nMaweed";
            current.setId(null);
            current.setPassword(new EncryptPassword().encrypt("MD5", current.getPassword()));
            getFacade().create(current);
            SendMail.sendMail("maweed.noreply@gmail.com", "m@weed!29site", "Maweed Password - " + current.getName(), body, current.getEmail());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GuestCreated"));
            return prepareCreate();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String s = sw.toString();
            for (String line : s.split("\n")) {
                if (line.contains("duplicate key value violates unique constraint \"userauth_email_uniq\"")) {
//                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("NonUniqueEmail"));
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, s, null));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already exists.. please use another one", "title"));
//                    FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("globalMessage");
                    return null;
                }
            }
        }
        return null;
    }

    public String prepareEdit() {
        current = (Guest) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public void update() {
        try {
            current.setPassword(new EncryptPassword().encrypt("MD5", current.getPassword()));
            getFacade().edit(current);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Profile is updated"));
//            return "View";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed to update profile"));
//            return null;
        }
    }

    public String destroy() {
        current = (Guest) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GuestDeleted"));
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

    public Guest getGuest(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Guest.class)
    public static class GuestControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GuestController controller = (GuestController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "guestController");
            return controller.getGuest(getKey(value));
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
            if (object instanceof Guest) {
                Guest o = (Guest) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Guest.class.getName());
            }
        }

    }

}

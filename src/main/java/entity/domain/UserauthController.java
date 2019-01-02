package entity.domain;

import entity.domain.util.EncryptPassword;
import entity.domain.util.JsfUtil;
import entity.domain.util.PaginationHelper;
import entity.domain.util.SendMail;
import facade.UserAuthFacade;
import facade.UserauthGroupauthVFacade;
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
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
<<<<<<< HEAD
=======
import org.eclipse.persistence.internal.xr.Util;
>>>>>>> 12a852bc2d0b5b5eae1bb8923890dc37105b12bb

@Named("userauthController")
@SessionScoped
public class UserauthController implements Serializable {

<<<<<<< HEAD
    @EJB
    private UserauthGroupauthVFacade userauthGroupauthVFacade;

=======
>>>>>>> 12a852bc2d0b5b5eae1bb8923890dc37105b12bb
    @Inject
    private UserauthGroupauthV userauthGroupauthV;
    private UserAuth current;
    private DataModel items = null;
    private HttpSession httpSession;
    @EJB
    private facade.UserAuthFacade ejbFacade;
    @EJB
    private UserauthGroupauthVFacade userauthGroupauthVFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String currentPage;
    private String message;
    private HttpSession httpSession;

    public UserauthController() {
    }

    public UserauthGroupauthV getUserauthGroupauthV() {
        return userauthGroupauthV;
    }

    public UserAuth getSelected() {
        if (current == null) {
            current = new UserAuth();
            selectedItemIndex = -1;
        }
        return current;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private UserAuthFacade getFacade() {
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
        current = (UserAuth) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new UserAuth();
        selectedItemIndex = -1;
        return "/registration?faces-redirect=true";
    }

    public String saveCurrentPage(String fromPage, String toPage) {
        currentPage = fromPage;
        return toPage;
<<<<<<< HEAD
    }

    public void logout() {
        httpSession.invalidate();
        httpSession = null;
    }

    public boolean isValid() {
        return (httpSession == null);
    }

    public HttpSession getHttpSession() {
        return httpSession;
=======
>>>>>>> 12a852bc2d0b5b5eae1bb8923890dc37105b12bb
    }

    public String authenticate() throws NoSuchAlgorithmException {
        for (UserauthGroupauthV u : userauthGroupauthVFacade.findAll()) {
            if (u.getEmail().equals(getUserauthGroupauthV().getEmail()) && u.getPassword().equals(new EncryptPassword().encrypt("MD5", getUserauthGroupauthV().getPassword()))) {
                httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                httpSession.setAttribute("email", u.getEmail());
                return currentPage;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed to log in!"));
            }
        }
        return null;
    }

    public void logout() {
        httpSession.invalidate();
        httpSession = null;
    }

    public boolean isValid() {
        return (httpSession == null);
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public String authenticate() throws NoSuchAlgorithmException {
        for (UserauthGroupauthV u : userauthGroupauthVFacade.findAll()) {
            if (u.getEmail().equals(getUserauthGroupauthV().getEmail()) && u.getPassword().equals(new EncryptPassword().encrypt("MD5", getUserauthGroupauthV().getPassword()))) {
                httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                httpSession.setAttribute("email", u.getEmail());
                return currentPage;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed to log in!"));
            }
        }
        return null;
    }

    public String create() throws NoSuchAlgorithmException {
        try {
            String body = "Nice to have you " + current.getName() + ",\n\nYour Password is: " + current.getPassword() + "\n\nThanks\nMaweed";
            current.setId(null);
            current.setPassword(new EncryptPassword().encrypt("MD5", current.getPassword()));
            getFacade().create(current);
            SendMail.sendMail("maweed.noreply@gmail.com", "m@weed!29site", "Maweed Password - " + current.getName(), body, current.getEmail());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserauthCreated"));
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
                    FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("globalMessage");
                    return null;
                }
            }
        }
        return null;
    }

    public String prepareEdit() {
        current = (UserAuth) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserauthUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (UserAuth) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserauthDeleted"));
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

    public UserAuth getUserauth(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = UserAuth.class)
    public static class UserauthControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserauthController controller = (UserauthController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userauthController");
            return controller.getUserauth(getKey(value));
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
            if (object instanceof UserAuth) {
                UserAuth o = (UserAuth) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserAuth.class.getName());
            }
        }

    }

}

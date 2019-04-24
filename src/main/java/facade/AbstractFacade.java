package facade;

import entity.domain.Area;
import entity.domain.Category;
import entity.domain.Clinic;
import entity.domain.Hospital;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<String> hospitalAutocomplete() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Hospital.findHospitalNames").getResultList();
    }

    public Object findGuestByEmail(String email) {
        return getEntityManager().createNamedQuery("Guest.findGuestByEmail").setParameter("email", email).getSingleResult();
    }

    public List<String> hospitalAutocompleteArabic() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager()
                .createNamedQuery("Hospital.findHospitalNamesArabic")
                .getResultList();
    }

    public List<Object[]> findServicesByHospitalAndClinic(String hospital, String clinic) {

        Query q;
        q = getEntityManager().createNativeQuery("SELECT cs.id, sl.name as clinic FROM clinic c "
                + "join category cat on cat.id=c.category_id "
                + "join hospital h on h.id=c.hospital_id "
                + "join clinicservice cs on cs.clinic_id=c.id "
                + "join servicelist sl on sl.id=cs.servicelist_id "
                + "WHERE h.name='" + hospital + "' "
                + "AND cat.name='" + clinic + "'");
        return q.getResultList();
    }

    public List<T> findHospitalsByName(String name) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Hospital.findByName")
                .setParameter("name", name)
                .getResultList();
    }

    public List<T> findHospitalsByInArabic(String name) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Hospital.findByInArabicName")
                .setParameter("inArabic", name)
                .getResultList();
    }

    public Object findHospitalByName(String name) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Hospital.findByName").setParameter("name", name).getSingleResult();
    }

    public Object findHospitalByNameAr(String name) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Hospital.findByInArabicName").setParameter("inArabic", name).getSingleResult();
    }

    public List<T> findHospitalsByMembership(String membership) {
        return getEntityManager()
                .createNamedQuery("Hospital.findHospitalsByMembership")
                .setParameter("membership", membership)
                .getResultList();
    }

    public List<T> findServicesByClinicHospital(String clinic, String hospital) {
        return getEntityManager()
                .createNamedQuery("ServiceClinicHospitalV.findServicesByClinicHospital")
                .setParameter("categoryname", clinic)
                .setParameter("hospitalname", hospital)
                .getResultList();
    }

    public List<T> findClinicByHospital(Hospital hospital) {
        return getEntityManager().createNamedQuery("Clinic.findClinicByHospital").setParameter("hospital", hospital).getResultList();
    }

//    public List<T> findServicesByClinic(Clinic clinic) {
//        return getEntityManager().createNamedQuery("ClinicService.findServicesByClinic").setParameter("clinic", clinic).getResultList();
//    }
    public Object findFirstImage(Hospital hospital) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        Object o;
        try {
            o = getEntityManager()
                    .createNamedQuery("HospitalImage.findByHospital")
                    .setParameter("hospital", hospital)
                    .getResultList().get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return o;
    }

    public Object findCatByName(String name) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Category.findByName")
                .setParameter("name", name)
                .getSingleResult();
    }

    public List<Object[]> findByAreaAndCategoryHospital(Hospital hospital, Clinic clinic) {

        Query q = null;
        
        if ((hospital.getName().equals("")) && (clinic.getCategory() == null) && (hospital.getArea() == null)) { //nothing selected
            return null;
        }

        if ((!hospital.getName().equals("")) && (clinic.getCategory() == null) && (hospital.getArea() == null)) { //hospital typed
            q = getEntityManager().createNativeQuery("select h.id, h.name from hospital h WHERE h.name = '" + hospital.getName() + "'");
            return q.getResultList();
        }
        
        if ((!hospital.getName().equals("")) && (clinic.getCategory() == null) && (hospital.getArea() != null)) { //hospital + area selected
            q = getEntityManager().createNativeQuery("select h.id,h.name,a.name from hospital h"
                    + " inner join area a on a.id=h.area_id WHERE h.name = '" + hospital.getName() + "' "
                    + " AND a.name = '" + hospital.getArea().getName() + "'");
            return q.getResultList();
        }
        
        if ((hospital.getName().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() != null)) { //clinic selected + area selected
            q = getEntityManager().createNativeQuery("select h.id, h.name as hospital_name, a.name as area, cat.name as clinic_name from clinic c"
                    + " INNER JOIN hospital h on h.id=c.hospital_id"
                    + " INNER JOIN category cat on cat.id=c.category_id"
                    + " INNER JOIN area a on a.id=h.area_id"
                    + " WHERE a.name = '" + hospital.getArea().getName() + "'"
                    + " AND cat.name = '" + clinic.getCategory().getName() + "'");
            return q.getResultList();
        }
        
        if ((!hospital.getName().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() != null)) { //hospital + clinic selected + area selected
            q = getEntityManager().createNativeQuery("select h.id, h.name as hospital_name, a.name as area, cat.name as clinic_name from clinic c"
                    + " INNER JOIN hospital h on h.id=c.hospital_id"
                    + " INNER JOIN category cat on cat.id=c.category_id"
                    + " INNER JOIN area a on a.id=h.area_id"
                    + " WHERE a.name = '" + hospital.getArea().getName() + "'"
                    + " AND cat.name = '" + clinic.getCategory().getName() + "'"
                    + " AND h.name = '" + hospital.getName() + "'");
            return q.getResultList();
        }
        
        if ((!hospital.getName().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() == null)) { //hospital + clinic selected
            q = getEntityManager().createNativeQuery("select h.id, h.name as hospital_name, cat.name as clinic_name from clinic c"
                    + " INNER JOIN hospital h on h.id=c.hospital_id"
                    + " INNER JOIN category cat on cat.id=c.category_id"
                    + " WHERE h.name = '" + hospital.getName() + "'"
                    + " AND cat.name = '" + clinic.getCategory().getName() + "'");
            return q.getResultList();
        }
        
        if ((hospital.getName().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() == null)) { //clinic selected
            q = getEntityManager().createNativeQuery("select h.id, h.name as hospital_name, cat.name as clinic_name from clinic c"
                    + " inner join hospital h on h.id=c.hospital_id"
                    + " inner join category cat on cat.id=c.category_id"
                    + " where cat.name = '" + clinic.getCategory().getName() + "'");
            return q.getResultList();
        }

        return q.getResultList();
    }
    
    public List<Object[]> findByAreaAndCategoryHospitalAr(Hospital hospital, Clinic clinic) {

        Query q = null;
        
        if ((hospital.getInArabic().equals("")) && (clinic.getCategory() == null) && (hospital.getArea() == null)) { //nothing selected
            return null;
        }

        if ((!hospital.getInArabic().equals("")) && (clinic.getCategory() == null) && (hospital.getArea() == null)) { //hospital typed
            q = getEntityManager().createNativeQuery("select h.id, h.inarabic from hospital h WHERE h.inarabic = '" + hospital.getInArabic() + "'");
            return q.getResultList();
        }
        
        if ((!hospital.getInArabic().equals("")) && (clinic.getCategory() == null) && (hospital.getArea() != null)) { //hospital + area selected
            q = getEntityManager().createNativeQuery("select h.id, h.inarabic, a.inarabic from hospital h"
                    + " inner join area a on a.id=h.area_id WHERE h.inarabic = '" + hospital.getInArabic() + "' "
                    + " AND a.inarabic = '" + hospital.getArea().getInArabic() + "'");
            return q.getResultList();
        }
        
        if ((hospital.getInArabic().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() != null)) { //clinic selected + area selected
            q = getEntityManager().createNativeQuery("select h.id, h.inarabic as hospital_name, a.inarabic as area, cat.inarabic as clinic_name from clinic c"
                    + " INNER JOIN hospital h on h.id=c.hospital_id"
                    + " INNER JOIN category cat on cat.id=c.category_id"
                    + " INNER JOIN area a on a.id=h.area_id"
                    + " WHERE a.inarabic = '" + hospital.getArea().getInArabic() + "'"
                    + " AND cat.inarabic = '" + clinic.getCategory().getInArabic() + "'");
            return q.getResultList();
        }
        
        if ((!hospital.getInArabic().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() != null)) { //hospital + clinic selected + area selected
            q = getEntityManager().createNativeQuery("select h.id, h.inarabic as hospital_name, a.inarabic as area, cat.inarabic as clinic_name from clinic c"
                    + " INNER JOIN hospital h on h.id=c.hospital_id"
                    + " INNER JOIN category cat on cat.id=c.category_id"
                    + " INNER JOIN area a on a.id=h.area_id"
                    + " WHERE a.inarabic = '" + hospital.getArea().getInArabic() + "'"
                    + " AND cat.inarabic = '" + clinic.getCategory().getInArabic() + "'"
                    + " AND h.inarabic = '" + hospital.getInArabic() + "'");
            return q.getResultList();
        }
        
        if ((!hospital.getInArabic().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() == null)) { //hospital + clinic selected
            q = getEntityManager().createNativeQuery("select h.id, h.inarabic as hospital_name, cat.inarabic as clinic_name from clinic c"
                    + " INNER JOIN hospital h on h.id=c.hospital_id"
                    + " INNER JOIN category cat on cat.id=c.category_id"
                    + " WHERE h.name = '" + hospital.getInArabic() + "'"
                    + " AND cat.inarabic = '" + clinic.getCategory().getInArabic() + "'");
            return q.getResultList();
        }
        
        if ((hospital.getInArabic().equals("")) && (clinic.getCategory() != null) && (hospital.getArea() == null)) { //clinic selected
            q = getEntityManager().createNativeQuery("select h.id, h.inarabic as hospital_name, cat.inarabic as clinic_name from clinic c"
                    + " inner join hospital h on h.id=c.hospital_id"
                    + " inner join category cat on cat.id=c.category_id"
                    + " where cat.inarabic = '" + clinic.getCategory().getInArabic() + "'");
            return q.getResultList();
        }

        return q.getResultList();
    }

    public List<Clinic> findClinicByCat(Category cat) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Clinic.findClinicByCat")
                .setParameter("category", cat)
                .getResultList();
    }

    public List<T> findAll() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAreas() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Area.findNameSorted").getResultList();
    }

    public Object findAreaByName(String area) {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Area.findByName").getSingleResult();
    }

    public List<T> findArabicAreas() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Area.findArabicNameSorted").getResultList();
    }

    public List<T> findCategoriesSorted() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return getEntityManager().createNamedQuery("Category.findAllSorted").getResultList();
    }

    public List<T> findResultsByCatAndArea(String categName, String areaName) {
        return getEntityManager().createNamedQuery("AdvancedSearch.findByCategAndArea")
                .setParameter("categName", categName)
                .setParameter("areaName", areaName)
                .getResultList();
    }

    public List<T> findResultsByCatAndAreaAr(String categName, String areaName) {
        return getEntityManager().createNamedQuery("AdvancedSearch.findByCategArAndAreaAr")
                .setParameter("categArabic", categName)
                .setParameter("areaArabic", areaName)
                .getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}

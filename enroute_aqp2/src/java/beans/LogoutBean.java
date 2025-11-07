package beans;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Simple bean to log out the current user by invalidating the session
 * and redirecting to the application index page.
 */
@ManagedBean(name = "logoutBean")
@RequestScoped
public class LogoutBean {

    public void logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        // Invalidate session
        ec.invalidateSession();
        try {
            // Redirect to context root index.xhtml
            ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        fc.responseComplete();
    }
}

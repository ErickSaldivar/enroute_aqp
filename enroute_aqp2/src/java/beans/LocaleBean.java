/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
@ManagedBean(name="localeBean")
@SessionScoped

public class LocaleBean implements Serializable {
    private Locale locale = Locale.forLanguageTag("es");
    public Locale getLocale() { return locale; }
    public String getLanguage() { return locale.getLanguage(); }
    public String change(String lang) {
        this.locale = Locale.forLanguageTag(lang);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(this.locale);
        return null;
    }
}
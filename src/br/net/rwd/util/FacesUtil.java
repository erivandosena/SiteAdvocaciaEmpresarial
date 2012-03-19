package br.net.rwd.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {


    public static void mensInfo(String message) {
        mensagem(message, FacesMessage.SEVERITY_INFO);
    }
    
    public static void mensAdvert(String message) {
        mensagem(message, FacesMessage.SEVERITY_WARN);
    }

    public static void mensErro(String message) {
        mensagem(message, FacesMessage.SEVERITY_ERROR);
    }
    
    public static void mensFatal(String message) {
        mensagem(message, FacesMessage.SEVERITY_FATAL);
    }

    public static void mensagem(String message, FacesMessage.Severity severity) {
    	
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
    }
    
    public static String get(String param) {
    	
    	return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);
    }
    
}

package br.net.rwd.util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewExpiredException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.primefaces.context.DefaultRequestContext;
import org.primefaces.context.RequestContext;
import org.primefaces.util.Constants;

public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper {
	
	private ExceptionHandler wrapped;
	
	public ViewExpiredExceptionExceptionHandler() {
		// TODO Auto-generated constructor stub
	}

    public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

	@Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        String redirectPage = null;
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                //ViewExpiredException vee = (ViewExpiredException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                //HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
                try {
                    // Push some useful stuff to the request scope for use in the page
                    //session.setAttribute("currentViewId", vee.getViewId());
                    redirectPage = "/index.jsp";//vee.getViewId();//"/expirou.jsf";
                } finally {
                    i.remove();
                }
                doRedirect(fc, redirectPage);
                // Importante, colocar a vari�vel de controle exibeDialog na sess�o para posteriormente ser capturada nos managedBean e ser exibido um dialog na respectiva p�gina xhtml.
				//session.setAttribute("exibeDialog", true);
            }
        }

        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
    }

    public void doRedirect(FacesContext fc, String redirectPage) throws FacesException{

        ExternalContext ec = fc.getExternalContext();

        try {
            // workaround for PrimeFaces
        	new DefaultRequestContext();
            if (ec.getRequestParameterMap().containsKey(Constants.PARTIAL_PROCESS_PARAM) && !ec.getRequestParameterMap().get(Constants.PARTIAL_PROCESS_PARAM).equals("@all")) {
            	fc.setViewRoot(new UIViewRoot());
            }

            //fix for renderer kit (Mojarra's and PrimeFaces's ajax redirect)
              if ((((DefaultRequestContext)RequestContext.getCurrentInstance()).isAjaxRequest() || fc.getPartialViewContext().isPartialRequest()) && fc.getResponseWriter() == null && fc.getRenderKit() == null) {
                ServletResponse response = (ServletResponse) ec.getResponse();
                ServletRequest request = (ServletRequest) ec.getRequest();
                response.setCharacterEncoding(request.getCharacterEncoding());

                RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                RenderKit renderKit = factory.getRenderKit(fc, fc.getApplication().getViewHandler().calculateRenderKitId(fc));
                ResponseWriter responseWriter = renderKit.createResponseWriter(response.getWriter(), null, request.getCharacterEncoding());
                fc.setResponseWriter(responseWriter);
            }

            ec.redirect(ec.getRequestContextPath() + (redirectPage != null ? redirectPage : ""));
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }
}

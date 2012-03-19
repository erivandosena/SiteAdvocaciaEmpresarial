package br.net.rwd.util;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.net.rwd.controlador.AdministradorControlador;

public class LoginPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent event) {
	}

	// executa antes de qualquer renderizar ao usuário
	public void beforePhase(PhaseEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		String viewId = context.getViewRoot().getViewId();

		// verifica as páginas que não possuem acesso externo
		if (viewId.equals("/adm/*.xhtml") 
				|| viewId.equals("/adm/inicial.xhtml")
				
				|| viewId.equals("/adm/formPagina.xhtml")
				|| viewId.equals("/adm/listarPaginas.xhtml")
				
				|| viewId.equals("/adm/formUsuario.xhtml")
				|| viewId.equals("/adm/listarUsuarios.xhtml")
				
				|| viewId.equals("/adm/formWebsite.xhtml")
				|| viewId.equals("/adm/listarWebsite.xhtml")
				
				|| viewId.equals("/adm/formPublicacao.xhtml")
				|| viewId.equals("/adm/listarPublicacoes.xhtml")
				
				|| viewId.equals("/adm/formLink.xhtml")
				|| viewId.equals("/adm/listarLinks.xhtml")
				
				) {

			// recupera os dados que estão em sessão
			Application app = context.getApplication();
			AdministradorControlador adminEmSessao = (AdministradorControlador) app
					.evaluateExpressionGet(context, "#{administradorControlador}",
							AdministradorControlador.class);

			// se não houver administrador logado
			if (adminEmSessao.getAdmin().getUsu_cod() == null) {

				// armazena a página ao qual o usuário está tentando entrar em sessão
				adminEmSessao.setOriginalViewId(viewId);
				// em seguida, cria a árvore de componentes para a página admin.jsf
				// que exigirá o login e senha
				ViewHandler viewHandler = app.getViewHandler();
				UIViewRoot viewRoot = viewHandler.createView(context,"/adm/admin.xhtml");
				context.setViewRoot(viewRoot);
			}
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}

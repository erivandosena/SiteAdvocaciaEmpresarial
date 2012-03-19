package br.net.rwd.controlador;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Administrador;
import br.net.rwd.util.CriptografaMD5;
import br.net.rwd.util.FacesUtil;

@Controller("administradorControlador")
@Scope("session")
public class AdministradorControlador {
	
	private String originalViewId;

	private Administrador admin;

	@Resource
	private DaoGenerico<Administrador, Integer> administradorDao;

	public AdministradorControlador() {
		admin = new Administrador();
	}
	
	// captura a sessão do contexto criado pelo JavaServer Faces
	FacesContext fc = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);

	public Administrador getAdmin() {
		return admin;
	}

	public void setAdmin(Administrador admin) {
		this.admin = admin;
	}

	// pega a viewid
	public String getOriginalViewId() {
		String temp = originalViewId;
		originalViewId = null;
		return temp;
	}

	public void setOriginalViewId(String originalViewId) {
		this.originalViewId = originalViewId;
	}
	
	// verifica a existência do administrador e o retorna
	@SuppressWarnings("unused")
	private Administrador verificaAdmin(String usuario) {
		String query = "SELECT a FROM Administrador a WHERE a.usu_email = :usuario";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usuario", usuario);
		return administradorDao.pesqParam(query, params);
	}

	// executa o login no sistema direcionando o usuário ao local indicado
	public String login() {
		Administrador a = verificaAdmin(admin.getUsu_email());
		// verifica se a senha resultante é igual a senha encontrada no banco de dados
		if (a != null && a.getUsu_senha().equals( CriptografaMD5.md5(this.admin.getUsu_senha()) )) {
			// deixa o objeto usuario agora com os valores encontrados no banco
			setAdmin(a);
			
			session.setMaxInactiveInterval(2 * 60 * 30); //3600 //1hora
			
			// cria uma sessão contendo o perfil do usuario
			session.setAttribute("usuPerfil", admin.getUsu_perfil()); 
			
			session.setAttribute("usuNome", admin.getUsu_nome());
			
			session.setAttribute("usuCod", admin.getUsu_cod());
			
			// caso não seja nulo redireciona onde o usuário tentou entrar via URL
			if (originalViewId != null) {
				FacesContext context = FacesContext.getCurrentInstance();
				ViewHandler viewHandler = context.getApplication().getViewHandler();
				UIViewRoot viewRoot = viewHandler.createView(context,originalViewId);
				context.setViewRoot(viewRoot);
				return null;
			}
			
			// redireciona para a área do usuário
			return "inicial";
			
		} else {
			FacesUtil.mensErro("Usuário ou senha não confere");
		}
		return null;
	}

	// sai da área administrativa
	public String logout() {
		session.removeAttribute("usuNome");
		session.removeAttribute("usuEmail");
		session.removeAttribute("usuPerfil");
		session.invalidate();
		return null;
	}
	
	public boolean isLogado() {
        return admin != null;
    }

}

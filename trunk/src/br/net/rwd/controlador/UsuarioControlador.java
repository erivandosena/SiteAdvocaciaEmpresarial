package br.net.rwd.controlador;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Cidade;
import br.net.rwd.entidades.Usuario;
import br.net.rwd.enumeracao.EnumPerfil;
import br.net.rwd.util.CriptografaMD5;
import br.net.rwd.util.FacesUtil;

@Controller("usuarioControlador")
@Scope("session")
public class UsuarioControlador {

	@Resource
	private DaoGenerico<Usuario, Integer> usuarioDao;

	@Resource
	private DaoGenerico<Cidade, Integer> cidadeDao;

	private Usuario usuario;

	private Usuario usuarioEdicao;

	@SuppressWarnings("unchecked")
	private DataModel model;
	
	// captura a sessão do contexto criado pelo JavaServer Faces
	FacesContext fc = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);

	public UsuarioControlador() {
		this.usuario = new Usuario();
	}

	public DaoGenerico<Usuario, Integer> getUsaurioDao() {
		return usuarioDao;
	}

	public void setUsaurioDao(DaoGenerico<Usuario, Integer> usaurioDao) {
		this.usuarioDao = usaurioDao;
	}

	public DaoGenerico<Cidade, Integer> getCidadeDao() {
		return cidadeDao;
	}

	public void setCidadeDao(DaoGenerico<Cidade, Integer> cidadeDao) {
		this.cidadeDao = cidadeDao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioEdicao() {
		return usuarioEdicao;
	}

	public void setUsuarioEdicao(Usuario usuarioEdicao) {
		this.usuarioEdicao = usuarioEdicao;
	}

	// verifica a existência do usuário e o retorna
	private Usuario verificaU(String email) {
		String query = "SELECT u FROM Usuario u WHERE u.usu_email = :email";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		return usuarioDao.pesqParam(query, params);
	}

	// valida a entrada de e-mails no cadastro e área de login
	public void validaEmail(FacesContext context, UIComponent componente,
			Object objeto) throws ValidatorException {
		String digitado = (String) objeto;
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(digitado);
		boolean matchFound = m.matches();

		if (!matchFound) {
			((UIInput) componente).setValid(false);
			FacesMessage message = new FacesMessage("Formato inválido.");
			context.addMessage(componente.getClientId(context), message);
		}
	}

	// vai para o cadastro de novo usuário
	public String novoUsuario() {
		if (!session.getAttribute("usuPerfil").equals("U")) {
			this.usuario = new Usuario();
			return "formUsuario";
		} else {
			FacesUtil.mensInfo("Sua conta não tem poderes para executar esta operação.");
			return "listarUsuarios";
		}
	}

	// caso o usuário seja salvo com sucesso salva um novo usuario ou que está em edição
	public String salvar() {
		// verifica se o cadastro não possui um usuário com o mesmo e-mail
		Usuario u = verificaU(usuario.getUsu_email());
		// se não há usuário logado
		if (usuario.getUsu_cod() == null) {
			if (u != null && u.getUsu_email().equals(usuario.getUsu_email())) {
				// lança um aviso global informando da existência do usuário
				FacesUtil.mensErro("Usuário existente");
				return null; // não redireciona a lugar algum
			} else { // caso o usuário não seja cadastrado
				usuario.setUsu_senha(CriptografaMD5.md5(this.usuario.getUsu_senha()));
				// salva no banco de dados
				usuario = usuarioDao.salvar(usuario);
				FacesUtil.mensInfo("Salvo com sucesso");
			}
		} else {
			if (!session.getAttribute("mailAtual").equals(usuario.getUsu_email())) { 
			if (u != null && u.getUsu_email().equals(usuario.getUsu_email())) {
				FacesUtil.mensErro("Usuário existente");
				return null;
			}else{
				atualiza();
			}
			} else {
				atualiza();
			}
		}
		return "listarUsuarios";
	}

	// mostra todos os usuarios em um DataTable
	@SuppressWarnings("unchecked")
	public DataModel getTodos() {
		if (session.getAttribute("usuPerfil").equals("A")) {
			model = new ListDataModel(usuarioDao.todos());
		} else if (session.getAttribute("usuPerfil").equals("U")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cod", (Long)session.getAttribute("usuCod"));
			String query = "SELECT u FROM Usuario u WHERE u.usu_cod = :cod";
			model = new ListDataModel(usuarioDao.listPesqParam(query, params));
		}
		return model;
	}

	// pega o usuario selecionado na tabela para editar ou excluir
	public Usuario getUsuarioParaEditarExcluir() {
		Usuario usuario = (Usuario) model.getRowData();
		return usuario;
	}

	// edita o usuario
	public String editar() {
		setUsuarioEdicao(getUsuarioParaEditarExcluir());
		session.setAttribute("mailAtual", usuario.getUsu_email());
		return "formUsuario";
	}

	// exclui o usuario selecionada no DataTable
	public String excluir() {
		if (!session.getAttribute("usuPerfil").equals("U")) {
			usuarioDao.excluir(usuario);
			FacesUtil.mensInfo("Excluído com sucesso");
			return "listarUsuarios";
		} else {
			FacesUtil.mensInfo("Sua conta não tem poderes para executar esta operação.");
			return "listarUsuarios";
		}
	}

	@SuppressWarnings("unchecked")
	public Map getPerfis() {
		Map<EnumPerfil, String> mapParam = new HashMap<EnumPerfil, String>();
		for (EnumPerfil type : EnumPerfil.values()) {
			mapParam.put(type, type.name());
		}
		return mapParam;
	}
	
	public boolean isUsuarioLogado() {
		if (!session.getAttribute("usuPerfil").equals("A")) {
			return false;
		}
		return true;
	}
	
	void atualiza() {
		usuario.setUsu_senha(CriptografaMD5.md5(this.usuario.getUsu_senha()));
		usuarioDao.atualizar(usuario);
		FacesUtil.mensInfo("Atualizado com sucesso");
	}
	
	public Map<String, String> getCidades() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, String> mParams = new LinkedHashMap<String, String>();
		mapParam.put("uf", usuario.getUsu_estado());
		String consulta = "SELECT distinct c FROM Cidade c WHERE UPPER(c.cid_uf) = :uf ORDER BY c.cid_nome";
		for (Iterator<Cidade> iter = cidadeDao.listPesqParam(consulta, mapParam).iterator(); iter.hasNext();) {
			Cidade c = (Cidade) iter.next();
			//Armazenando ao map os registros encontrados no banco
			mParams.put(c.getCid_nome(), c.getCid_nome());
		}
		return mParams;
	}
	
	public void retornaUf(ValueChangeEvent event) throws AbortProcessingException {
		if (null != event.getNewValue()) {
			usuario.setUsu_estado((String) event.getNewValue());
		}
	}
	
	public String getUsuarioSessao() {
		return (String)session.getAttribute("usuPerfil");
	}

}

	

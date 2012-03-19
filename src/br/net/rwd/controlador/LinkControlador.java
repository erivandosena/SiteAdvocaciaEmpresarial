package br.net.rwd.controlador;

import javax.annotation.Resource;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Link;
import br.net.rwd.util.FacesUtil;

@Controller("linkControlador")
@Scope("session")
public class LinkControlador {
	
	@Resource
	private DaoGenerico<Link, Integer> linkDao;

	private Link link;

	private DataModel<Link> model;
	
	/* substituido por: formLink?faces-redirect=true&amp;includeViewParams=true
	private Link linkEdicao;
	
	public Link getLinkEdicao() {
		return linkEdicao;
	}

	public void setLinkEdicao(Link linkEdicao) {
		this.linkEdicao = linkEdicao;
	}
	*/

	public LinkControlador() {
		this.link = new Link();
	}

	public DaoGenerico<Link, Integer> getLinkDao() {
		return linkDao;
	}

	public void setLinkDao(DaoGenerico<Link, Integer> linkDao) {
		this.linkDao = linkDao;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public String novoLink() {
		this.link = new Link();
		return "formLink";
	}

	@SuppressWarnings("unchecked")
	public DataModel getTodos() {
		model = new ListDataModel(linkDao.todos());
		return model;
	}

	/* substituido por: formLink?faces-redirect=true&amp;includeViewParams=true
	
	public Link getLinkParaEditarExcluir() {
		Link link = (Link) model.getRowData();
		return link;
	}

	public String editar() {
		setLinkEdicao(getLinkParaEditarExcluir());
		return "formLink";
	}
	*/

	public String salvar() {
		if (link.getLin_cod() == null) {
			linkDao.salvar(link);
			FacesUtil.mensInfo("Cadastrado com sucesso");
		} else {
			linkDao.atualizar(link);
			FacesUtil.mensInfo("Atualizado com sucesso");
		}
		return "listarLinks";
	}

	public String excluir() {
		linkDao.excluir(link);
		FacesUtil.mensInfo("Excluído com sucesso");
		return "listarLinks";
	}

	@SuppressWarnings("unchecked")
	public DataModel getLinks() {
		if (!getLinkDao().todos().isEmpty())
			return new ListDataModel(getLinkDao().todos());
		else
			return null;
	}

}

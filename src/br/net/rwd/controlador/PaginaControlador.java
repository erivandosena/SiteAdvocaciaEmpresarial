package br.net.rwd.controlador;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Pagina;
import br.net.rwd.enumeracao.EnumPosicaoMenu;
import br.net.rwd.util.FacesUtil;

@Controller("paginaControlador")
@Scope("session")
public class PaginaControlador {

	private Pagina pagina;
	private Pagina paginaEdicao;
	@Resource // Recurso injetado pelo Spring
	private DaoGenerico<Pagina, Integer> paginaDao;
	@SuppressWarnings("unchecked")
	private DataModel model;

	public PaginaControlador() {
		
	}
	
	public Pagina getPagina() {
		return pagina;
	}

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
	}
	
	public Pagina getPaginaEdicao() {
		return paginaEdicao;
	}
	
	public void setPaginaEdicao(Pagina paginaEdicao) {
		this.paginaEdicao = paginaEdicao;
	}

	public void setPaginaDao(DaoGenerico<Pagina, Integer> paginaDao) {
		this.paginaDao = paginaDao;
	}

	public DaoGenerico<Pagina, Integer> getPaginaDao() {
		return paginaDao;
	}

	// cadastra uma nova pagina
	public String novaPag() {
		this.pagina = new Pagina();
		return "formPagina";
	}

	// mostra todas as paginas em um DataTable
	@SuppressWarnings("unchecked")
	public DataModel getTodos() {
		model = new ListDataModel(paginaDao.todos());
		return model;
	}

	// pega a pagina selecionada na tabela para editar ou excluir
	public Pagina getPaginaParaEditarExcluir() {
		Pagina pagina = (Pagina) model.getRowData();
		return pagina;
	} 

	// edita a pagina
	public String editar() {
		setPaginaEdicao(getPaginaParaEditarExcluir());
		return "formPagina";
	}

	// salva uma nova pagina ou que está em edição
	public String salvar() {
		// verifica se não é uma pagina em edição
		if (pagina.getPag_cod() == null) {
			paginaDao.salvar(pagina);
			FacesUtil.mensInfo("Cadastrado com sucesso");
		} else {
			paginaDao.atualizar(pagina);
			FacesUtil.mensInfo("Atualizado com sucesso");
		}
		return "listarPaginas";
	}

	// exclui a pagina selecionada no DataTable
	public String excluir() {
		//Pagina pagina = getPaginaParaEditarExcluir();
		paginaDao.excluir(pagina);
		FacesUtil.mensInfo("Excluído com sucesso");
		return "listarPaginas";
	}
	
	@SuppressWarnings("unchecked")
	public Map getPosicoes() {
		Map<EnumPosicaoMenu, String> mapParam = new HashMap<EnumPosicaoMenu, String>();
		for (EnumPosicaoMenu type : EnumPosicaoMenu.values()) {
			mapParam.put(type, type.name());
		}
		return mapParam;
	}  

}

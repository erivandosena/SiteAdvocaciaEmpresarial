package br.net.rwd.controlador;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Link;
import br.net.rwd.entidades.Pagina;
import br.net.rwd.entidades.Publicacao;
import br.net.rwd.entidades.Website;
import br.net.rwd.util.Constantes;

/*
 * Controla o site e os links das paginas
 */

@Controller("siteControlador")
@Scope("request")
public class SiteControlador {
	
	@Resource
	private DaoGenerico<Pagina, Integer> paginaDao;
	@Resource
	private DaoGenerico<Website, Integer> websiteDao;
	@Resource
	private DaoGenerico<Publicacao, Integer> publicacaoDao;
	@Resource
	private DaoGenerico<Link, Integer> linkDao;
	
	private DataModel<?> model;

	private String pesquisa;
	private String numero;
	private String recurso;
	private String nome;

	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public DaoGenerico<Pagina, Integer> getPaginaDao() {
		return paginaDao;
	}

	public void setPaginaDao(DaoGenerico<Pagina, Integer> paginaDao) {
		this.paginaDao = paginaDao;
	}

	public DaoGenerico<Website, Integer> getWebsiteDao() {
		return websiteDao;
	}

	public void setWebsiteDao(DaoGenerico<Website, Integer> websiteDao) {
		this.websiteDao = websiteDao;
	}
	
	public DaoGenerico<Publicacao, Integer> getPublicacaoDao() {
		return publicacaoDao;
	}

	public void setPublicacaoDao(DaoGenerico<Publicacao, Integer> publicacaoDao) {
		this.publicacaoDao = publicacaoDao;
	}

	public DaoGenerico<Link, Integer> getLinkDao() {
		return linkDao;
	}

	public void setLinkDao(DaoGenerico<Link, Integer> linkDao) {
		this.linkDao = linkDao;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa.toLowerCase();
	}

	// Consulta Processual
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		if (recurso.equals(""))
			this.recurso = "0";
		else
		this.recurso = recurso;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}
	
	public DataModel<?> getLinks() {
		if (!getLinkDao().todos().isEmpty())
			return new ListDataModel<Link>(getLinkDao().todos());
		else
			return null;
	}

	// exibe os detalhes da pagina selecionada
	public Pagina getDetalhes() throws IOException {
		// captura o codigo por GET
		String cod = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("c");
		try {
			return paginaDao.pesquisarPorCod(Integer.parseInt(cod));
		} catch (Exception e) {
			return null;
		}

	}
	
	public Publicacao getPublicacao() {
		String cod = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("p");
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("codigo", Integer.parseInt(cod));
		} catch (Exception e) {
			params.put("codigo", 0);
		}
		String consulta = "SELECT p FROM Publicacao p WHERE p.pub_status = 'A' AND p.pub_cod = :codigo)";
		return publicacaoDao.pesqParam(consulta, params);
	}
	
	private List<?> rPublicacao() {
		String cod = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("p");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigo", Integer.parseInt(cod));
		String query = "SELECT p.pub_data,p.pub_dataalteracao,p.pub_legenda,p.pub_titulo,p.pub_sumario,p.pub_conteudo,p.pub_autor,p.pub_fonte,p.pub_img_nome,p.pub_img_descricao,p.pub_visualizacao FROM Publicacao p WHERE p.pub_status = 'A' AND p.pub_cod = :codigo)";
		return publicacaoDao.listPesqParam(query, params);
	}
	
	public List<Publicacao> getTodasPub() {
		List<Publicacao> retorno;
		String consulta = "SELECT p FROM Publicacao p WHERE p.pub_status = 'A' ORDER BY p.pub_data DESC";
		if (publicacaoDao.listPesq(consulta).isEmpty()) {
			retorno = null;
		} else {
			retorno = publicacaoDao.listPesq(consulta);
		}
		return retorno;
	}
	
	public String getContador() {
		String cod = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("p");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigo", Integer.parseInt(cod));
		String consulta = "SELECT p FROM Publicacao p WHERE p.pub_status = 'A' AND p.pub_cod = :codigo)";

		Publicacao pub = publicacaoDao.pesqParam(consulta, params);
		pub.setPub_visualizacao(pub.getPub_visualizacao() + 1);
		publicacaoDao.atualizar(pub);

		return pub.getPub_visualizacao().toString();
	}

	public DataModel<Pagina> getPesqPagina() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pesq", "%" + pesquisa + "%");
		String query = "SELECT p FROM Pagina p WHERE LOWER(p.pag_titulo) LIKE :pesq "
				+ "OR LOWER(p.pag_conteudo) LIKE :pesq";
		return new ListDataModel<Pagina>(paginaDao.listPesqParam(query, params));
	}
	
	public DataModel<Publicacao> getPesqPublicacao() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pesq", "%" + pesquisa + "%");
		String query = "SELECT p FROM Publicacao p WHERE LOWER(p.pub_titulo) LIKE :pesq AND "
				+ "p.pub_status = 'A' OR LOWER(p.pub_conteudo) LIKE :pesq AND p.pub_status = 'A'";
		return new ListDataModel<Publicacao>(publicacaoDao.listPesqParam(query,
				params));
	}
	
	@SuppressWarnings("unchecked")
	public Publicacao getUltimaPub() {
		try {
			String consulta = "SELECT p FROM Publicacao p WHERE p.pub_status = 'A' ORDER BY p.pub_cod DESC LIMIT 1)";
			model = new ListDataModel(getPublicacaoDao().listPesq(consulta));
			return (Publicacao) model.getRowData();
		} catch (Exception e) {
			return null;
		}
	}

	// mostra todas as paginas
	public List<Pagina> getTodasPag() {
		return paginaDao.todos();
	}

	// mostra somente as paginas de uma determinada posicao
	@SuppressWarnings("unchecked")
	public DataModel getPaginaTopo() throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String pos = (String) request.getParameter("pos");

		if (pos == null) {
			pos = "T";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pos", pos);
		String query = "SELECT p FROM Pagina p WHERE p.pag_posicao = :pos";

		return new ListDataModel(getPaginaDao().listPesqParam(query, params));
	}

	@SuppressWarnings("unchecked")
	public DataModel getPaginaDestaque() {
		String query = "SELECT p FROM Pagina p WHERE p.pag_posicao = 'D'";
		if (!getPaginaDao().listPesq(query).isEmpty())
			return new ListDataModel(getPaginaDao().listPesq(query));
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public DataModel getPaginaRodape() {
		String pos = "R";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pos", pos);
		String query = "SELECT p FROM Pagina p WHERE p.pag_posicao = :pos";
		return new ListDataModel(getPaginaDao().listPesqParam(query, params));
	}

	public Website getWebinfo() {
		Website resultado;
		String consulta = "SELECT w FROM Website w";
		if (websiteDao.listPesq(consulta).isEmpty()) {
			resultado = null;
		} else {
			resultado = websiteDao.listPesq(consulta).set(0, null);
		}
		return resultado;
	}

	public String getConsultaProcessual() {
		                //<script type='text/javascript'> function abreJanela(URL) { window.open(URL,'Resultado da Consulta','_self','scrollbars=YES') } abreJanela('"+URL_CONSUL_PROCES_NOM+nome+"'); </script>"; //popUp
		String retorno = "<script type='text/javascript'> document.location.assign('./index.jsp') </script>";
		if(!nome.equals("") && nome.length() < 10) 
			return retorno; 
		else 
			if (!nome.equals("")) { 
				return "<script type='text/javascript'> document.location.assign('"+Constantes.URL_CONSUL_PROCES_NOM+nome+"') </script>"; 
				} else 
					if (!numero.equals("")) { 
						return "<script type='text/javascript'> document.location.assign('"+Constantes.URL_CONSUL_PROCES_NUM+numero+Constantes.URL_CONSUL_PROCES_REC+recurso+"') </script>"; 
						} 
		return retorno; 
	}
	
	@SuppressWarnings("unchecked")
	public void executarRelatorio() throws ParseException {

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
		String pathInicial = ((ServletContext) fc.getExternalContext().getContext()).getRealPath("/");
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		// pega o caminho do arquivo .jasper da aplicação
		// InputStream reportStream =
		// fc.getExternalContext().getResourceAsStream("relatorio/publicacao.jasper");
		InputStream reportStream = getClass().getResourceAsStream("/br/net/rwd/relatorio/publicacao.jasper");

		// envia a resposta com o MIME Type PDF
		response.setContentType("application/pdf");

		// força a abertura de download
		// response.setHeader("Content-disposition",
		// "attachment;filename=arquivo.pdf");

		// definindo que a disposição do conteúdo será inline (dentro do próprio
		// browser) e o nome do arquivo será arquivo.pdf
		response.setHeader("Content-disposition", "inline;filename=arquivo.pdf");

		try {
			ServletOutputStream servletOutputStream = response
					.getOutputStream();

			// envia o título para o relatório, usando o parâmetro criado
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put("CAMINHO_IMAGEM", pathInicial + "/ckfinder/userfiles/images/");
			parametros.put("SITE", request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath().concat("/")));
			// parametros.put("SITE",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());

			List dados = new ArrayList();

			Map record = null;

			// varre a consulta e separa os objetos
			for (Iterator iterator = rPublicacao().iterator(); iterator.hasNext();) {

				Object[] obj = (Object[]) iterator.next();
				record = new HashMap();
				// coloca em um Map cada um dos campos criados manualmente
				// pelo relatório

				record.put("pub_data", sdf.parse(sdf.format((Date) (obj[0]))));
				try {
					record.put("pub_dataalteracao", sdf.parse(sdf.format((Date) (obj[1]))));
				} catch (Exception e) {
					record.put("pub_dataalteracao", null);
				}
				record.put("pub_legenda", obj[2].toString());
				record.put("pub_titulo", obj[3].toString());
				record.put("pub_sumario", obj[4].toString());
				record.put("pub_conteudo", obj[5].toString());
				record.put("pub_autor", obj[6].toString());
				record.put("pub_fonte", obj[7].toString());
				if (obj[8] == null)
					record.put("pub_img_nome", null);
				else
					record.put("pub_img_nome", obj[8].toString());
				if (obj[9] == null)
					record.put("pub_img_descricao", null);
				else
					record.put("pub_img_descricao", obj[9].toString());
				record.put("pub_visualizacao", Integer.parseInt(obj[10].toString()));

				// adiciona o List dados
				dados.add(record);
			}

			// cria uma fonte de dados para coleções
			JRBeanCollectionDataSource fonteDados = new JRBeanCollectionDataSource(dados);

			// envia para o navegador o PDF gerado
			JasperRunManager.runReportToPdfStream(reportStream,servletOutputStream, parametros, fonteDados);

			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// evita erro do JSF após completar a geração do relatório avisando
			// o FacesContext que a resposta está completa
			fc.responseComplete();

		}
	}

}

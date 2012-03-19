package br.net.rwd.controlador;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Cidade;
import br.net.rwd.entidades.Website;
import br.net.rwd.util.Constantes;
import br.net.rwd.util.CriaArquivo;
import br.net.rwd.util.FacesUtil;
import br.net.rwd.util.FormataBytes;
import br.net.rwd.util.Imagem;
import br.net.rwd.util.Redimensiona;

@Controller("websiteControlador")
@Scope("session")
public class WebsiteControlador {
	
	private Website website;
	private Website websiteEdicao;
	private boolean cadastrado;
	
	private String nomeArquivoSelecionado;
	private StreamedContent imagem;
	
	byte[] bytesArqN = null;
	String nomeArq = null;
	File fN = null;
	File fImgThumb = null;

	@Resource
	private DaoGenerico<Website, Integer> websiteDao;
	
	@Resource
	private DaoGenerico<Cidade, Integer> cidadeDao;

	@SuppressWarnings("unchecked")
	private DataModel model;

	public WebsiteControlador() {
		this.website = new Website();
		exibeSemfoto();
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public Website getWebsiteEdicao() {
		return websiteEdicao;
	}

	public String getNomeArquivoSelecionado() {
		return nomeArquivoSelecionado;
	}

	public void setNomeArquivoSelecionado(String nomeArquivoSelecionado) {
		this.nomeArquivoSelecionado = nomeArquivoSelecionado;
	}

	public void setWebsiteEdicao(Website websiteEdicao) {
		this.websiteEdicao = websiteEdicao;
	}
	
	public StreamedContent getImagem() {
		return imagem;
	}

	public void setImagem(StreamedContent imagem) {
		this.imagem = imagem;
	}

	public boolean isCadastrado() {
		if (getTodos().getRowCount() == 0) {
			cadastrado = false;
			return cadastrado;
		}
		else {
			cadastrado = true;
		return cadastrado;
		}
	}
	
	public void setCadastro(boolean cadastro) {
		this.cadastrado = cadastro;
	}

	public DaoGenerico<Website, Integer> getWebsiteDao() {
		return websiteDao;
	}

	public void setWebsiteDao(DaoGenerico<Website, Integer> websiteDao) {
		this.websiteDao = websiteDao;
	}

	public DaoGenerico<Cidade, Integer> getCidadeDao() {
		return cidadeDao;
	}

	public void setCidadeDao(DaoGenerico<Cidade, Integer> cidadeDao) {
		this.cidadeDao = cidadeDao;
	}

	public String novoWeb() {
		this.website = new Website();
		exibeSemfoto();
		return "formWebsite";
	}

	@SuppressWarnings("unchecked")
	public DataModel getTodos() {
		model = new ListDataModel(websiteDao.todos());
		return model;
	}

	public Website getWebsiteParaEditarExcluir() {
		Website website = (Website) model.getRowData();
		return website;
	}

	public String editar() {
		setWebsiteEdicao(getWebsiteParaEditarExcluir());
		if (website.getWeb_img_mime() != null) {
		imagem = new DefaultStreamedContent(new ByteArrayInputStream(website.getWeb_img_padrao()));
		setNomeArquivoSelecionado(null);
		} else {
			exibeSemfoto();
		}
		return "formWebsite";
	}

	public String salvar() {
		if (website.getWeb_cod() == null) {
			if (getTodos().getRowCount() >= 1) {
				cancela();
			}else{
			websiteDao.salvar(website);
			if (website.getWeb_img_mime() != null) {
				salvaEmDisco();
			}else{
				excluiArquivo();
			}
			
			FacesUtil.mensInfo("Cadastrado com sucesso");
			}
		} else {
			websiteDao.atualizar(website);
			if (website.getWeb_img_mime() != null) {
				salvaEmDisco();
			}else{
				excluiArquivo();
			}	
			FacesUtil.mensInfo("Atualizado com sucesso");
			return "listarWebsite";
		}
		return "listarWebsite";
	}

	public String excluir() {
		removeImagem();
		excluiArquivo();
		websiteDao.excluir(website);
		FacesUtil.mensInfo("Excluído com sucesso");
		return "listarWebsite";
	}
	
	public String cancela() {
		return "listarWebsite";
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			//Nome do arquivo
			nomeArq = event.getFile().getFileName();
			
			//MIMEType do arquivo
			String mimeArq = new MimetypesFileTypeMap().getContentType(new File(nomeArq));
			
			//Coleta os bytes e redimensiona a imagem p/ tamando desejado
			bytesArqN = Redimensiona.redimensionaImg(event.getFile().getContents(),80);

			//se ja existe exclui
			if (website.getWeb_img_mime() != null) {
			removeImagem();
			}
			
			//Prepara para salvar no banco
			website.setWeb_img_padrao(bytesArqN);
			website.setWeb_img_mime(mimeArq);
			website.setWeb_img_nome("imagem.png");

			//Exibe as imagens a salvar
			if (!website.getWeb_img_padrao().equals(null) || !event.equals(null)) {
			imagem = new DefaultStreamedContent(new ByteArrayInputStream(website.getWeb_img_padrao()));
			setNomeArquivoSelecionado("Arquivo visualizado: "+ nomeArq+" "+FormataBytes.formatInMegaBytes(event.getFile().getSize()));
			}else{
				this.imagem = Imagem.exibeImagem(null, -1, null);
				setNomeArquivoSelecionado(null);
			}
			nomeArq = "imagem.png";
			
		} catch (Exception ex) {
			Logger.getLogger(WebsiteControlador.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	void salvaEmDisco() {
		//Salvar no disco 1=Normal 2=Mini
		if(nomeArq != null){
		CriaArquivo.criaArq(bytesArqN, nomeArq, 1);
		}
	}
	
	void exibeSemfoto() {
		this.imagem = Imagem.exibeImagem(null, -1, null);
		setNomeArquivoSelecionado(null);
	}
	
	void excluiArquivo() {
		if(fN != null){
			fN.delete();
			fImgThumb.delete();
		}
	}
	
	public void removeImagem() {
		website.setWeb_img_padrao(null);
		website.setWeb_img_mime(null);
		fN = new File(Constantes.CAMINHO_IMG+"/"+website.getWeb_img_nome());
		fImgThumb = new File(Constantes.CAMINHO_IMG_THUMB+"/"+website.getWeb_img_nome());
		website.setWeb_img_nome(null);
		exibeSemfoto();
	}
	
	public Map<String, String> getCidades() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, String> mParams = new LinkedHashMap<String, String>();
		mapParam.put("uf", website.getWeb_estado());
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
			website.setWeb_estado((String) event.getNewValue());
		}
	}
	
}

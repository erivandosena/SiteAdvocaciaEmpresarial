package br.net.rwd.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "publicacoes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Publicacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pub_cod;
	@Temporal(TemporalType.DATE)
	private Date pub_data;
	@Temporal(TemporalType.DATE)
	private Date pub_dataalteracao;
	private String pub_legenda;
	private String pub_titulo;
	private String pub_sumario;
	private String pub_conteudo;
	private String pub_autor;
	private String pub_fonte;
	private String pub_img_nome;
	private String pub_img_descricao;
	private String pub_img_mime;
	private Integer pub_visualizacao;
	private String pub_status;
	
	public Integer getPub_cod() {
		return pub_cod;
	}
	public void setPub_cod(Integer pub_cod) {
		this.pub_cod = pub_cod;
	}
	public Date getPub_data() {
		return pub_data;
	}
	public void setPub_data(Date pub_data) {
		this.pub_data = pub_data;
	}
	public Date getPub_dataalteracao() {
		return pub_dataalteracao;
	}
	public void setPub_dataalteracao(Date pub_dataalteracao) {
		this.pub_dataalteracao = pub_dataalteracao;
	}
	public String getPub_legenda() {
		return pub_legenda;
	}
	public void setPub_legenda(String pub_legenda) {
		this.pub_legenda = pub_legenda;
	}
	public String getPub_titulo() {
		return pub_titulo;
	}
	public void setPub_titulo(String pub_titulo) {
		this.pub_titulo = pub_titulo;
	}
	public String getPub_sumario() {
		return pub_sumario;
	}
	public void setPub_sumario(String pub_sumario) {
		this.pub_sumario = pub_sumario;
	}
	public String getPub_conteudo() {
		return pub_conteudo;
	}
	public void setPub_conteudo(String pub_conteudo) {
		this.pub_conteudo = pub_conteudo;
	}
	public String getPub_autor() {
		return pub_autor;
	}
	public void setPub_autor(String pub_autor) {
		this.pub_autor = pub_autor;
	}
	public String getPub_fonte() {
		return pub_fonte;
	}
	public void setPub_fonte(String pub_fonte) {
		this.pub_fonte = pub_fonte;
	}
	public String getPub_img_nome() {
		return pub_img_nome;
	}
	public void setPub_img_nome(String pub_img_nome) {
		this.pub_img_nome = pub_img_nome;
	}
	public String getPub_img_descricao() {
		return pub_img_descricao;
	}
	public void setPub_img_descricao(String pub_img_descricao) {
		this.pub_img_descricao = pub_img_descricao;
	}
	public String getPub_img_mime() {
		return pub_img_mime;
	}
	public void setPub_img_mime(String pub_img_mime) {
		this.pub_img_mime = pub_img_mime;
	}
	public Integer getPub_visualizacao() {
		return pub_visualizacao;
	}
	public void setPub_visualizacao(Integer pub_visualizacao) {
		this.pub_visualizacao = pub_visualizacao;
	}
	public String getPub_status() {
		return pub_status;
	}
	public void setPub_status(String pub_status) {
		this.pub_status = pub_status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pub_cod == null) ? 0 : pub_cod.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Publicacao other = (Publicacao) obj;
		if (pub_cod == null) {
			if (other.pub_cod != null)
				return false;
		} else if (!pub_cod.equals(other.pub_cod))
			return false;
		return true;
	}

}

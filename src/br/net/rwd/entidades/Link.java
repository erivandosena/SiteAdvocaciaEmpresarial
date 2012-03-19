package br.net.rwd.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "links")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Link implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lin_cod;
	private String lin_nome;
	private String lin_url;

	public Link() {

	}

	public Integer getLin_cod() {
		return lin_cod;
	}

	public void setLin_cod(Integer lin_cod) {
		this.lin_cod = lin_cod;
	}

	public String getLin_nome() {
		return lin_nome;
	}

	public void setLin_nome(String lin_nome) {
		this.lin_nome = lin_nome;
	}

	public String getLin_url() {
		return lin_url;
	}

	public void setLin_url(String lin_url) {
		this.lin_url = lin_url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lin_cod == null) ? 0 : lin_cod.hashCode());
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
		final Link other = (Link) obj;
		if (lin_cod == null) {
			if (other.lin_cod != null)
				return false;
		} else if (!lin_cod.equals(other.lin_cod))
			return false;
		return true;
	}

}

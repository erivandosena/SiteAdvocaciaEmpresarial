package br.net.rwd.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "cidades")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cidade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	@Id
	private Long cid_cod;
	private String cid_nome;
	private String cid_uf;

	public String getCid_nome() {
		return cid_nome;
	}

	public void setCid_nome(String cid_nome) {
		this.cid_nome = cid_nome;
	}

	public String getCid_uf() {
		return cid_uf;
	}

	public void setCid_uf(String cid_uf) {
		this.cid_uf = cid_uf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid_cod == null) ? 0 : cid_cod.hashCode());
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
		final Cidade other = (Cidade) obj;
		if (cid_cod == null) {
			if (other.cid_cod != null)
				return false;
		} else if (!cid_cod.equals(other.cid_cod))
			return false;
		return true;
	}

}

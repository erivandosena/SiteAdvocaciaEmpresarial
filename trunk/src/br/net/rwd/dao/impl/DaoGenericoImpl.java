package br.net.rwd.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.rwd.dao.DaoGenerico;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public abstract class DaoGenericoImpl<T, ID extends Serializable>
		implements DaoGenerico<T, ID> {

	private EntityManager entityManager;
	
	private final Class<T> oClass;

	public Class<T> getObjectClass() {
		return this.oClass;
	}

	@SuppressWarnings("unchecked")
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

	protected EntityManager getEntityManager() {
		if (entityManager == null)
			throw new IllegalStateException("Erro");
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	public DaoGenericoImpl() {
		// determina através do argumento generico T a classe que será utilizada para o DAO
		this.oClass = (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void excluir(T object) {
		object = getEntityManager().merge(object);
		getEntityManager().remove(object);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public T salvar(T object) {
		getEntityManager().clear();
		getEntityManager().persist(object);
		return object;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public T atualizar(T object) {
		getEntityManager().merge(object);
		return object;
	}

	@Override
	public T pesquisarPorCod(ID cod) {
		return (T) getEntityManager().find(oClass, cod);
	}

	@SuppressWarnings("unchecked")
	public T pesqParam(String consulta, Map<String, Object> params) {
		Query q = getEntityManager().createQuery(consulta);
		// separa os parametros transmitidos
		for (String chave : params.keySet()) {
			q.setParameter(chave, params.get(chave));
		}
		try {
			return (T)q.getSingleResult();
		} catch (NoResultException nre) {// caso não haja resultados
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> todos() {
		String consultaS = "SELECT obj FROM " + oClass.getSimpleName() + " obj";
		Query consulta = getEntityManager().createQuery(consultaS);
		return consulta.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> listPesqParam(String consulta, Map<String, Object> params) {
		Query q = getEntityManager().createQuery(consulta);
		// separa os parametros transmitidos
		for (String chave : params.keySet()) {
			q.setParameter(chave, params.get(chave));
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> listPesqParam(String consulta, Map<String, Object> params,
			int maximo, int atual) {
		Query q = getEntityManager().createQuery(consulta).setMaxResults(maximo).setFirstResult(atual);
		for (String chave : params.keySet()) {q.setParameter(chave, params.get(chave));
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> listPesq(String consulta) {
		Query q = getEntityManager().createQuery(consulta);
		return q.getResultList();
	}

}

package br.net.rwd.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DaoGenerico<T, ID extends Serializable> {
	
	public Class<T> getObjectClass();

	public void excluir(T object);

	public T salvar(T object);

	public T atualizar(T object);

	public T pesquisarPorCod(ID cod);

	public T pesqParam(String consulta, Map<String, Object> params);

	public List<T> todos();

	public List<T> listPesqParam(String consulta, Map<String, Object> params);

	public List<T> listPesqParam(String consulta, Map<String, Object> params,
			int maximo, int atual);

	public List<T> listPesq(String consulta);
}

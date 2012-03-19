package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.CidadeDao;
import br.net.rwd.entidades.Cidade;

@Component("cidadeDao")
public class CidadeDaoImpl extends DaoGenericoImpl<Cidade, Integer> implements
		CidadeDao {
	
}
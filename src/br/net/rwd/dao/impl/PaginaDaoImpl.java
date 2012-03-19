package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.PaginaDao;
import br.net.rwd.entidades.Pagina;

@Component("paginaDao")
public class PaginaDaoImpl extends DaoGenericoImpl<Pagina, Integer> implements
		PaginaDao {
	
}

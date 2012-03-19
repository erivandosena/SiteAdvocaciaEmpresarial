package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.LinkDao;
import br.net.rwd.entidades.Link;

@Component("linkDao")
public class LinkDaoImpl extends DaoGenericoImpl<Link, Integer> implements
		LinkDao {
	
}
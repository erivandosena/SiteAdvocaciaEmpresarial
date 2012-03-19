package br.net.rwd.dao.impl;

import org.springframework.stereotype.Component;

import br.net.rwd.dao.WebsiteDao;
import br.net.rwd.entidades.Website;

@Component("websiteDao")
public class WebsiteDaoImpl extends DaoGenericoImpl<Website, Integer> implements
		WebsiteDao {
	
}

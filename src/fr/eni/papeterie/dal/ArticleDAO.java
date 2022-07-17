package fr.eni.papeterie.dal;

import fr.eni.papeterie.bo.Article;

import java.util.List;

public interface ArticleDAO
{
	int TYPE_PENCIL = 0;

	int TYPE_PAPER = 1;

	void insert(Article article) throws DALException;

	void update(Article article) throws DALException;

	Article selectById(int idArticle) throws DALException;

	List<Article> selectAll() throws DALException;

	List<Article> selectByType(int type) throws DALException;

	void delete(int idArticle) throws DALException;
}

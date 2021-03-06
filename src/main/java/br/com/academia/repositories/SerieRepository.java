package br.com.academia.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.academia.domain.Serie;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer>{

	@Query("Select sr from Serie sr INNER JOIN sr.solicitacao sol INNER JOIN sol.usuario WHERE sol.usuario.id = :usuario_logado")
	Page<Serie> findByUserLogged(Pageable pageRequest, @Param("usuario_logado") Integer id);

	@Query("Select sr from Serie sr INNER JOIN sr.solicitacao sol WHERE lower(sol.solicitante) LIKE lower(concat('%', :nome ,'%')) ORDER BY sr.dataCriacao DESC")
	List<Serie> findBySolicitante(@Param("nome") String nome);

	@Query("SELECT new map(CASE "
			+ "WHEN (s.tipoSerie = 1) THEN 'Hipertrofia' "
			+ "WHEN (s.tipoSerie = 2) THEN 'Definicao' "
			+ "WHEN (s.tipoSerie = 3) THEN 'Resistencia' "
			+ "WHEN (s.tipoSerie = 4) THEN 'Outros' END AS tipoSerie, "
			+ "COUNT(s.id) AS qtddSerie) "
			+ "FROM Serie s "
			+ "INNER JOIN s.solicitacao sol "
			+ "INNER JOIN sol.usuario "
			+ "WHERE sol.usuario.id = :usuario_logado "
			+ "GROUP BY s.tipoSerie")
	List<Serie> myDashboard(@Param("usuario_logado") Integer id);
	
	@Query("SELECT new map(CASE "
			+ "WHEN (s.tipoSerie = 1) THEN 'Hipertrofia' "
			+ "WHEN (s.tipoSerie = 2) THEN 'Definicao' "
			+ "WHEN (s.tipoSerie = 3) THEN 'Resistencia' "
			+ "WHEN (s.tipoSerie = 4) THEN 'Outros' END AS tipoSerie, "
			+ "COUNT(s.id) AS qtddSerie) "
			+ "FROM Serie s "
			+ "GROUP BY s.tipoSerie")
	List<Serie> dashboard();

}

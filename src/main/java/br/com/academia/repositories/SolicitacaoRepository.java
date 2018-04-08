package br.com.academia.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.academia.domain.Solicitacao;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer>{

	@Query("SELECT sol from Solicitacao sol WHERE sol.usuario.id = :usuario_logado ORDER BY sol.dataSolicitacao DESC")
	public List<Solicitacao> findByUser(@Param("usuario_logado") Integer usuario_logado);

	@Query("SELECT sol from Solicitacao sol WHERE sol.statusSerie = :status")
	public Page<Solicitacao> findPageSolicitacaoPendente(Pageable pageRequest,@Param("status") Integer status);

	public List<Solicitacao> findBySolicitanteContainingIgnoreCaseOrderByDataSolicitacaoDesc(String nome);
	
	@Query("SELECT sol from Solicitacao sol WHERE sol.statusSerie = 1 AND sol.usuario.id = :usuario_logado")
	public Solicitacao existsSolicitacaoPendente(@Param("usuario_logado")Integer id);

}

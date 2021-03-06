package br.com.academia.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.academia.domain.Solicitacao;
import br.com.academia.domain.dto.RejeitarDTO;
import br.com.academia.domain.dto.SolicitacaoDTO;
import br.com.academia.services.SolicitacaoService;

@RestController
@RequestMapping(value = "/solicitacoes")
public class SolicitacaoResource {

	@Autowired
	private SolicitacaoService solicitacaoService; 
	
	@PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<SolicitacaoDTO>> listPerPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "dataSolicitacao") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "DESC")String direction)
	{
		Page<Solicitacao> list = solicitacaoService.findPage(page, linesPerPage, orderBy, direction);
		Page<SolicitacaoDTO> listDTO = list.map(solicitacao -> new SolicitacaoDTO(solicitacao));
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(value = "/doUsuarioLogado", method = RequestMethod.GET)
	public ResponseEntity<List<Solicitacao>> findByUser()
	{
		List<Solicitacao> solicitacao = solicitacaoService.findByUser();
		
		return ResponseEntity.ok().body(solicitacao);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Solicitacao> find(@PathVariable Integer id)
	{
		Solicitacao solicitacao = solicitacaoService.find(id);
		
		return ResponseEntity.ok().body(solicitacao);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Solicitacao> create(@Valid @RequestBody Solicitacao solicitacao)
	{
		solicitacao = solicitacaoService.save(solicitacao);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(solicitacao.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('PROFESSOR')")
	@RequestMapping(value = "/{id}/rejeitar", method = RequestMethod.PUT)
	public ResponseEntity<SolicitacaoDTO> rejeitar(@Valid @RequestBody RejeitarDTO dto, @PathVariable Integer id )
	{
		Solicitacao solicitacao = solicitacaoService.fromDTO(dto);
		
		solicitacaoService.rejeitar(id, solicitacao.getJustificativa());
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
	@RequestMapping(value="/findByStatus/{status}",method = RequestMethod.GET)
	public ResponseEntity<Page<SolicitacaoDTO>> listPerPageSolicitacaoPendente(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "dataSolicitacao") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "DESC")String direction,
			@PathVariable Integer status)
	{
		Page<Solicitacao> list = solicitacaoService.findPageSolicitacaoPendente(page, linesPerPage, orderBy, direction, status);
		Page<SolicitacaoDTO> listDTO = list.map(solicitacao -> new SolicitacaoDTO(solicitacao));
		return ResponseEntity.ok().body(listDTO);
	}
    //List<SolicitacaoDTO> listDTO = list.stream().map(solicitacao -> new SolicitacaoDTO(solicitacao)).collect(Collectors.toList());
	
	@PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
	@RequestMapping(value="/name", method = RequestMethod.GET)
	public ResponseEntity<List<SolicitacaoDTO>> findBySolicitante(@RequestParam(value="name") String nome)
	{
		List<Solicitacao> list = solicitacaoService.findBySolicitante(nome);
		
		List<SolicitacaoDTO> listDto = list.stream().map(solicitacao -> new SolicitacaoDTO(solicitacao)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ResponseEntity<List<Solicitacao>> dashboard()
	{
		List<Solicitacao> solicitacao = solicitacaoService.findDashboard();
		
		return ResponseEntity.ok().body(solicitacao);
	}
	
	@RequestMapping(value = "/myDashboard", method = RequestMethod.GET)
	public ResponseEntity<List<Solicitacao>> myDashboard()
	{
		List<Solicitacao> solicitacao = solicitacaoService.findMyDashboard();
		
		return ResponseEntity.ok().body(solicitacao);
	}
}

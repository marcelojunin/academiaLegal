package br.com.academia.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.academia.domain.Usuario;
import br.com.academia.domain.dto.UsuarioDTO;
import br.com.academia.services.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<UsuarioDTO>> listPerPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC")String direction)
	{
		Page<Usuario> list = usuarioService.findPage(page, linesPerPage, orderBy, direction);
		Page<UsuarioDTO> listDto = list.map(usuario -> new UsuarioDTO(usuario));
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> create(@Valid @RequestBody UsuarioDTO dto)
	{
		Usuario usuario = usuarioService.fromDTO(dto);
		
		usuario = usuarioService.save(usuario);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(usuario.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody UsuarioDTO dto, @PathVariable Integer id)
	{
		Usuario usuario = usuarioService.fromDTO(dto);
		
		usuario = usuarioService.update(usuario, id);

		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> find(@PathVariable Integer id)
	{
		Usuario usuario = usuarioService.find(id);
		
		return ResponseEntity.ok().body(usuario);
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Usuario> findByEmail(@RequestParam(value="email") String email)
	{
		Usuario usuario = usuarioService.findByEmail(email);
				
		return ResponseEntity.ok().body(usuario);
	}

	public void uploadProfilePicture(MultipartFile multipartFile)
	{

	}

}
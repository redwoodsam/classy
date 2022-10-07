package com.samuelaraujo.classy.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.model.DadosPessoais;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.CadastroDto;
import com.samuelaraujo.classy.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Lazy
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}
	
	public Usuario cadastrar(CadastroDto cadastroDto) {
		
		Usuario usuario = new Usuario();
		
		List<String> nomes = Arrays.asList(cadastroDto.getNomeCompleto().split(" "));
		String sobrenome = String.join(" ", nomes.subList(1, (nomes.size() - 1)));
		
		DadosPessoais dadosPessoais = new DadosPessoais();
		dadosPessoais.setEmail(cadastroDto.getEmail());
		dadosPessoais.setNome(nomes.get(0));
		dadosPessoais.setSobrenome(sobrenome);
		
		String senha = cadastroDto.getSenha();
		
		validarSenhas(senha, cadastroDto.getSenha2());
		
		usuario.setDadosPessoais(dadosPessoais);
		usuario.setPassword(passwordEncoder.encode(senha));
		
		return usuarioRepository.save(usuario);
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.buscarPorEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha incorretos"));
	}
	
	public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
            isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
	
	private void validarSenhas(String senha, String senha2) {
		if(!senha.equals(senha2)) throw new DadoInvalidoException("As senhas informadas não coincidem");
	}
	
}

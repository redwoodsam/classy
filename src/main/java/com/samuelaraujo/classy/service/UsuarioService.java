package com.samuelaraujo.classy.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
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
import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.DadosPessoais;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.CadastroDto;
import com.samuelaraujo.classy.model.dto.ContaUsuarioDTO;
import com.samuelaraujo.classy.repository.UsuarioRepository;
import com.samuelaraujo.classy.util.AutenticacaoUtil;

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

	public Usuario buscarPorId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
	}

	// Cadastra um novo usuário no sistema
	@Transactional
	public Usuario cadastrar(CadastroDto cadastroDto) {

		Optional<Usuario> usuarioSave = usuarioRepository.buscarPorEmail(cadastroDto.getEmail());
		if(usuarioSave.isPresent()) {
			throw new DadoInvalidoException("O e-mail informado já existe, por favor tente outro.");
		}
		
		Usuario usuario = new Usuario();

		List<String> nomes = Arrays.asList(cadastroDto.getNomeCompleto().split(" "));
		String sobrenome = String.join(" ", nomes.subList(1, (nomes.size())));

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

	// Atualiza o cadastro de um usuário no sistema
	@Transactional
	public ContaUsuarioDTO atualizar(ContaUsuarioDTO contaUsuarioDTO) {
		Usuario contaSalva = buscarPorId(contaUsuarioDTO.getId());

		validarSenhas(contaUsuarioDTO.getSenha(), contaUsuarioDTO.getSenha2());
		validaCamposPreenchidos(contaUsuarioDTO);

		if(contaUsuarioDTO.getSenha().isEmpty()) {
			contaUsuarioDTO.setSenha(contaSalva.getPassword());
		} else {
			contaUsuarioDTO.setSenha(passwordEncoder.encode(contaUsuarioDTO.getSenha()));
		}
		
		BeanUtils.copyProperties(contaUsuarioDTO.obterUsuario(), contaSalva, "id", "authorities", "anuncios", "email");

		return new ContaUsuarioDTO(usuarioRepository.save(contaSalva));
	}

	// Valida os campos preenchidos no cadastro do usuário
	private void validaCamposPreenchidos(ContaUsuarioDTO contaUsuarioDTO) {
		if(contaUsuarioDTO.obterUsuario().getDadosPessoais().getEndereco() == null) {
			throw new DadoInvalidoException("Os campos de endereço são obrigatórios");
		}

		if(
			contaUsuarioDTO.obterUsuario().getDadosPessoais().getEndereco().getEndereco().isEmpty() ||
			contaUsuarioDTO.obterUsuario().getDadosPessoais().getEndereco().getBairro().isEmpty() ||
			contaUsuarioDTO.obterUsuario().getDadosPessoais().getEndereco().getCidade().isEmpty()
		) {
			throw new DadoInvalidoException("Os campos de endereço são obrigatórios");
		}

		if(contaUsuarioDTO.getNomeCompleto().trim().isEmpty()) {
			throw new DadoInvalidoException("O campo nome é obrigatório");
		}
	}
	
	// Valida se ambas as senhas enviadas pelo usuário ao cadastrar coincidem
    private void validarSenhas(String senha, String senha2) {
        if (!senha.equals(senha2))
            throw new DadoInvalidoException("As senhas informadas não coincidem");
    }

	// Busca um usuário pelo seu e-mail
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.buscarPorEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha incorretos"));
	}

	// Verifica se o usuário está autenticado no sistema
	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return false;
		}
		return authentication.isAuthenticated();
	}

	// Retorna informações completas do usuário logado
	public boolean informacoesCompletas() {
		Usuario usuarioLogado = AutenticacaoUtil.obterUsuarioLogado();
		Usuario informacoesUsuarioLogado = buscarPorId(usuarioLogado.getId());

		return informacoesUsuarioLogado.getDadosPessoais().getEndereco() != null;
	}

}

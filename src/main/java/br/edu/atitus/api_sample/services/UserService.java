package br.edu.atitus.api_sample.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserEntity save(UserEntity user) throws Exception {
		if (user == null)
			throw new Exception("Objeto não pode ser nulo");
		
		if (user.getName() == null || user.getName().isEmpty())
			throw new Exception("Nome inválido");
		user.setName(user.getName().trim());


		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().matches(emailRegex)) {
			throw new Exception("E-mail inválido");
		}
		user.setEmail(user.getEmail().trim().toLowerCase());


		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
		if (user.getPassword() == null
				|| user.getPassword().isEmpty()
				|| !user.getPassword().matches(passwordRegex))
			throw new Exception("Password inválido");

		if (user.getType() == null)
			throw new Exception("Tipo de usuário inválido");
		
		if (repository.existsByEmail(user.getEmail()))
			throw new Exception("Já existe usuário cadastrado com este e-mail");
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		repository.save(user);
		
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		return user;
	}

}
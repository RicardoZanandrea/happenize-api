package br.edu.atitus.api_sample.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_sample.dtos.PointDTO;
import br.edu.atitus.api_sample.entities.PointEntity;
import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.repositories.PointRepository;

@Service
public class PointService {

	private final PointRepository repository;

	public PointService(PointRepository repository) {
		super();
		this.repository = repository;
	}
	
	public PointEntity save(PointEntity point) throws Exception {
		if (point == null)
			throw new Exception("Objeto Nulo");
		
		if (point.getDescription() == null || point.getDescription().isEmpty())
			throw new Exception("Descrição Inválida");
		point.setDescription(point.getDescription().trim());
		
		if (point.getLatitude() < -90 || point.getLatitude() > 90)
			throw new Exception("Latitude Inválida");
		
		if (point.getLongitude() < -180 || point.getLongitude() > 180)
			throw new Exception("Longitude Inválida");
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		point.setUser(userAuth);
		
		return repository.save(point);
	}
	
	public void deleteById(UUID id) throws Exception {
		var point = repository.findById(id)
				.orElseThrow(() -> new Exception("Não existe ponto cadastrado com este ID"));
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!point.getUser().getId().equals(userAuth.getId()))
			throw new Exception("Você não tem permissão para apagar este registro");
		
		repository.deleteById(id);
	}
	
	public PointEntity updateById(UUID id, PointDTO dto) throws Exception {
		var point = repository.findById(id)
				.orElseThrow(() -> new Exception("Não existe ponto cadastrado com este ID"));
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!point.getUser().getId().equals(userAuth.getId()))
			throw new Exception("Você não tem permissão para atualizar este registro");
		
		// Validações do DTO
		if (dto.description() == null || dto.description().isEmpty())
			throw new Exception("Descrição Inválida");
		
		if (dto.latitude() < -90 || dto.latitude() > 90)
			throw new Exception("Latitude Inválida");
		
		if (dto.longitude() < -180 || dto.longitude() > 180)
			throw new Exception("Longitude Inválida");
		
		// Atualizar os dados
		point.setDescription(dto.description().trim());
		point.setLatitude(dto.latitude());
		point.setLongitude(dto.longitude());
		
		return repository.save(point);
	}
	
	public List<PointEntity> findByUser() {
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return repository.findByUser(userAuth);
	}
	
	public List<PointEntity> findAll() {
		return repository.findAll();
	}
	
}

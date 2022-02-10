package edu.val.microservicios.micorservicioalumnos.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import edu.val.microservicios.micorservicioalumnos.model.Alumno;
import edu.val.microservicios.micorservicioalumnos.model.repository.AlumnoRepository;
//import edu.val.microservicios.microservicioscomun.model.FraseChuckNorris;
//import edu.val.microservicios.microservicioscomun.model.entity.Alumno;

@Service//inversión de control
public class AlumnoServiceImpl implements AlumnoService {
	
	
	/**
	 * COmponente
	 * alumnoController = new AlumnoController//IOC @Controller 
	 * alumnoRepository = new AlumnoReposity//IOC @Repository
	 * alumnoserv = new AlumnoServiceImpl //IOC @Service
	 * alumnoserv.setReposutory (alumnoRepository)
	 */
	
	///acceso a base de datos
	@Autowired //inyección de dependencias
	AlumnoRepository alumnoRepository;

	
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Alumno> findById(Long id) {
		Optional<Alumno> op_a = null;
		
			op_a = this.alumnoRepository.findById(id);
		
		return op_a;
	}

	@Override
	@Transactional(readOnly = true)//métodos que sean de consulta o lectura de la base de datos
	public Iterable<Alumno> buscarTodosLosAlumnos() {
		// TODO Auto-generated method stub
		
		Iterable<Alumno> coleccion = null;
		
			coleccion = this.alumnoRepository.findAll();
		
		return coleccion;
	}

	@Override
	@Transactional
	public Alumno save(Alumno alumno) {
		Alumno alumno_nuevo = null;
		
			alumno_nuevo = this.alumnoRepository.save(alumno);
		
		return alumno_nuevo;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		alumnoRepository.deleteById(id);
		
	}

	
	
	/**
	 * @return null si no existía el alumno, o el alumno actualizado
	 */
	@Override
	@Transactional
	public Alumno update(Alumno alumno, Long id) {
		Alumno alumno_editado = null;
		Optional<Alumno> op_a = null;

			//leemos por id
			op_a = alumnoRepository.findById(id);
			//si existe, lo actualizamos
			if (op_a.isPresent())
			{
				Alumno alumno_leido = op_a.get();
				alumno_leido.setNombre(alumno.getNombre());
				alumno_leido.setApellido(alumno.getApellido());
				alumno_leido.setEmail(alumno.getEmail());
				alumno_leido.setEdad(alumno.getEdad());
				if (alumno.getFoto()!=null)
				{
					alumno_leido.setFoto(alumno.getFoto());
				}
				
				alumno_editado= alumnoRepository.save(alumno_leido);
			}
		
		return alumno_editado;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Alumno> findByNombre(String nombre) {
		List<Alumno> la = null;
		
			la = alumnoRepository.findByNombre(nombre);
		
		return la;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Alumno> findByEdadBetween(int edad, int edad2) {
		// TODO Auto-generated method stub
		List<Alumno> la = null;
					
			la = alumnoRepository.findByEdadBetween(edad, edad2); 
				
		return la;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Alumno> buscarPorNombreOApellido(String term) {
		List<Alumno> la = null;
			
			la = alumnoRepository.buscarPorNombreOApellido(term);
		
		return la;
	}

	@Override
	public Page<Alumno> findAll(Pageable pageable) {
		return alumnoRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Alumno updateConFoto(Alumno alumno, Long id, MultipartFile archivo) throws IOException {
		Alumno alumno_editado = null;
		Optional<Alumno> op_a = null;

			//leemos por id
			op_a = alumnoRepository.findById(id);
			//si existe, lo actualizamos
			if (op_a.isPresent())
			{
				Alumno alumno_leido = op_a.get();
				alumno_leido.setNombre(alumno.getNombre());
				alumno_leido.setApellido(alumno.getApellido());
				alumno_leido.setEmail(alumno.getEmail());
				alumno_leido.setEdad(alumno.getEdad());
//				if (!archivo.isEmpty())
//				{
//					alumno_leido.setFoto(archivo.getBytes());
//				}
				
				alumno_editado= alumnoRepository.save(alumno_leido);
			}
		
		return alumno_editado;
	}

	

	
	
	
	

	

}

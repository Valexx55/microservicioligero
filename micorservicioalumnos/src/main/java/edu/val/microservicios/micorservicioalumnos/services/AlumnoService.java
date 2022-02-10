package edu.val.microservicios.micorservicioalumnos.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import edu.val.microservicios.micorservicioalumnos.model.Alumno;

//import edu.val.microservicios.microservicioscomun.model.FraseChuckNorris;
//import edu.val.microservicios.microservicioscomun.model.entity.Alumno;

/**
 * Vamos a definir la operativa del Alumno en el servidor
 * ¿¿qué podemos hacer con un alumno
 * 
 * imprimirEvaluacion
 * enviarEvaluacion
 * 
 * @author valer
 *
 */
public interface AlumnoService {
	
	//buscar por su ID
	
	public Optional<Alumno> findById(Long id);
	
	public Iterable<Alumno> buscarTodosLosAlumnos();//lista/array de alumnos
	
	public Page<Alumno> findAll(Pageable pageable);//lista/array de alumnos
	
	public Alumno save (Alumno alumno);
	
	public void deleteById (Long id);
	
	public Alumno update (Alumno alumno, Long id);

	public Alumno updateConFoto (Alumno alumno, Long id, MultipartFile archivo) throws IOException;
	
	public List<Alumno> findByNombre (String nombre);
	
	public List<Alumno> findByEdadBetween(int edad, int edad2);
	
	public List<Alumno> buscarPorNombreOApellido (String term);
	
	

}

package edu.val.microservicios.micorservicioalumnos.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.val.microservicios.micorservicioalumnos.model.Alumno;

//import edu.val.microservicios.microservicioscomun.model.entity.Alumno;


//<Entidad, Tipo de la clave primaria o ID> @Repository
public interface AlumnoRepository extends PagingAndSortingRepository<Alumno, Long>{
	
	//SELECCIONAR ALUNNOS POR UN NOMBRE - KeyWord Query
	public List<Alumno> findByNombre (String nombre);
	
	//SELECCIONAR ALUNNOS POR UN RANGO DE EDAD - KeyWord Query
	public List<Alumno> findByEdadBetween(int edad, int edad2);
	
	//@QUERY SELECCIONAR ALUMNOS POR NOMBRE O APELLIDO
	@Query("select a from Alumno a where a.nombre like %?1% or a.apellido like %?1%")
	public List<Alumno> buscarPorNombreOApellido (String term);
	
	
	

}

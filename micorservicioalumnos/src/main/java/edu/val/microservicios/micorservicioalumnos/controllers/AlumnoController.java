package edu.val.microservicios.micorservicioalumnos.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.val.microservicios.micorservicioalumnos.model.Alumno;
//import edu.val.microservicios.micorservicioalumnos.model.AlumnoDTO;
import edu.val.microservicios.micorservicioalumnos.services.AlumnoService;
//import edu.val.microservicios.microservicioscomun.model.FraseChuckNorris;
//import edu.val.microservicios.microservicioscomun.model.entity.Alumno;

/**
 * Con Controller le digo que esta clase mapea las peticiones HTTP Con
 * RestController, automáticamente, cosnsigo que el objeto JAVA Devuelto se
 * convierta a JSON
 * 
 * @author valer
 *
 */
//NO ES NECESARIA ESTA ANOTACIÓN AL FILTRAR DESDE GATEWAY
//@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
public class AlumnoController {

	@Autowired
	AlumnoService alumnoService;

	@Value("${instancian}") // nombre de una propiedad del properties
	String nombre_instancia;

	
	@Autowired //para obtener el puerto desde el codigo
	Environment environment;

	Logger log = LoggerFactory.getLogger(AlumnoController.class);

	// con esta anotación, le digo a spring
	// oye, si viene una petición GET HTTP, se la mandas a este método
	/**
	 * Para listar todos los alumnos
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listaAlumnos() {
		ResponseEntity<?> responseEntity = null;
		Iterable<Alumno> ita = null;

		log.debug("Atendido por el MS " + nombre_instancia + " puerto " + environment.getProperty("local.server.port"));

		log.debug("Ha entrado en listaAlumnos ");
		ita = alumnoService.buscarTodosLosAlumnos();
		responseEntity = ResponseEntity.ok(ita);
		log.debug("Salida de listaAlumnos ");

		return responseEntity;
	}



	/**
	 * Consultar por ID
	 * 
	 * @param nombre
	 * @return
	 */
	@GetMapping("/{id}") // @Pathvariable hace corresponder una valor en la url con una variable de entrada
	public ResponseEntity<?> obtenerAlumnoPorId(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		Optional<Alumno> oa = null;

		log.debug("Ha entrado en obtenerAlumnoPorId");
		oa = alumnoService.findById(id);
		if (oa.isPresent()) {
			// hay un alumno con ese ID consultado
			responseEntity = ResponseEntity.ok(oa.get());
			log.debug("Alumno encontrado " + oa.get().toString());

		} else {
			// no hay
			responseEntity = ResponseEntity.notFound().build();
			log.debug("Alumno con id  " + id + " NO encontrado");
		}

		return responseEntity;
	}

	// crear
	@PostMapping
	public ResponseEntity<?> insertarAlumno(@Valid @RequestBody Alumno alumno_recibido, BindingResult br) {
		ResponseEntity<?> responseEntity = null;
		Alumno alumno_devuelto = null;

		if (br.hasErrors()) {
			log.error("Errores en la entrada");
			responseEntity = obtenerErrores(br);
		} else {

			log.debug("Entrando en insertarAlumno. Alumno rx validado ok ");
			alumno_devuelto = alumnoService.save(alumno_recibido);
			responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(alumno_devuelto);
			log.debug("Saliendo de insertarAlumno");

		}

		return responseEntity;
	}

	@DeleteMapping("/{id}") // borrar
	public ResponseEntity<?> borrarAlumno(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;

		log.debug("Entrando en borrarAlumno");
		alumnoService.deleteById(id);
		responseEntity = ResponseEntity.status(HttpStatus.OK).build();
		log.debug("Saliendo borrarAlumno");

		return responseEntity;
	}

	@PutMapping("/{id}") // modificar
	public ResponseEntity<?> modificarAlumno(@RequestBody Alumno a, @PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		Alumno alumno = null;

		log.debug("Entrando en put");
		alumno = alumnoService.update(a, id);
		// if ternario JAVA valor = (condicion) ? (valor_true) : (valor_false)
		responseEntity = (alumno != null) ? (ResponseEntity.ok(alumno))	: (ResponseEntity.status(HttpStatus.NO_CONTENT).build());
		log.debug("Saliendo de put");
		/*
		 * if (alumno!=null) { responseEntity = ResponseEntity.ok(alumno); } else {
		 * //era null responseEntity =
		 * ResponseEntity.status(HttpStatus.NO_CONTENT).build(); }
		 */

		return responseEntity;
	}

	// busquedad/nombre/vale
	@GetMapping("/busqueda/nombre/{termino}")
	public ResponseEntity<?> busquedaPorNombre(@PathVariable String termino) {
		return ResponseEntity.ok(alumnoService.findByNombre(termino));
	}

	@GetMapping("/busqueda/edad/{e1}/{e2}")
	public ResponseEntity<?> busquedadPorEdad(@PathVariable int e1, @PathVariable int e2) {
		return ResponseEntity.ok(alumnoService.findByEdadBetween(e1, e2));
	}

	@GetMapping("/busqueda/nomoape/{term}")
	public ResponseEntity<?> busquedaPorNombreOApellido(@PathVariable String term) {
		return ResponseEntity.ok(alumnoService.buscarPorNombreOApellido(term));
	}

	private ResponseEntity<?> obtenerErrores(BindingResult br) {
		ResponseEntity<?> respuesta_errores = null;

		List<ObjectError> lista_errores = br.getAllErrors();
		lista_errores.forEach(oe -> {
			log.error(oe.toString());
		});
		respuesta_errores = ResponseEntity.badRequest().body(lista_errores);

		return respuesta_errores;
	}

	@GetMapping("/pagina")
	public ResponseEntity<?> listarAlumnosPaginacion(Pageable pageable) {
		ResponseEntity<?> respuesta = null;
		Iterable<Alumno> it_alumnos = null;

		log.debug("Entrando en /pagina");
		it_alumnos = this.alumnoService.findAll(pageable);
		respuesta = ResponseEntity.ok(it_alumnos);
		log.debug("Saliendo en /pagina");

		return respuesta;

	}

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> insertarAlumnoConFoto(@Valid Alumno alumno_recibido, BindingResult br,
			MultipartFile archivo) throws IOException {
		ResponseEntity<?> responseEntity = null;
		Alumno alumno_devuelto = null;

		if (br.hasErrors()) {
			log.error("Errores en la entrada");
			responseEntity = obtenerErrores(br);
		} else {

			if (!archivo.isEmpty()) // si hay un fichero adjunto
			{
				alumno_recibido.setFoto(archivo.getBytes());
			}

			log.debug("Entrando en insertarAlumno. Alumno rx validado ok ");
			alumno_devuelto = alumnoService.save(alumno_recibido);
			responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(alumno_devuelto);
			log.debug("Saliendo de insertarAlumno");

		}

		return responseEntity;
	}

	@PutMapping("/editar-con-foto/{id}") // modificar
	public ResponseEntity<?> modificarAlumnoConFoto(@Valid Alumno a, BindingResult br, @PathVariable Long id,
			MultipartFile archivo) throws IOException {
		ResponseEntity<?> responseEntity = null;
		Alumno alumno = null;

		if (br.hasErrors()) {
			responseEntity = obtenerErrores(br);
		} else {

			log.debug("Entrando en put");
			if (!archivo.isEmpty()) // si hay un fichero adjunto
			{
				a.setFoto(archivo.getBytes());
			}
			alumno = alumnoService.update(a, id);
			// if ternario JAVA valor = (condicion) ? (valor_true) : (valor_false)
			responseEntity = (alumno != null) ? (ResponseEntity.ok(alumno))
					: (ResponseEntity.status(HttpStatus.NO_CONTENT).build());
			log.debug("Saliendo de put");
			/*
			 * if (alumno!=null) { responseEntity = ResponseEntity.ok(alumno); } else {
			 * //era null responseEntity =
			 * ResponseEntity.status(HttpStatus.NO_CONTENT).build(); }
			 */

		}

		return responseEntity;
	}

	@GetMapping("/obtenerfoto/{id}")
	public ResponseEntity<?> obtenerFotoAlumnoPorId(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		Optional<Alumno> alOptional = null;
		Resource imagen = null;

		log.debug("Ha entrado en obtenerFotoAlumnoPorId");
		alOptional = this.alumnoService.findById(id);
		if (alOptional.isPresent() && alOptional.get().getFoto() != null) {
			// si existe ese alumno y además, tiene foto
			imagen = new ByteArrayResource(alOptional.get().getFoto());
			responseEntity = ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
		} else {
			// o no existe ese alumno o no tiene foto
			responseEntity = ResponseEntity.notFound().build();
		}

		return responseEntity;
	}

	

	@GetMapping("/hateoas/{id}") // @Pathvariable hace corresponder una valor en la url con una variable de
	// entrada
	public ResponseEntity<?> obtenerAlumnoPorIdHateoas(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		Optional<Alumno> oa = null;
		Alumno alumno_aux = null;

		log.debug("Ha entrado en obtenerAlumnoPorId");
		oa = alumnoService.findById(id);
		if (oa.isPresent()) {
			// hay un alumno con ese ID consultado
			alumno_aux = oa.get();
			//añadir los link HATEOAS
			alumno_aux.add(linkTo(methodOn(AlumnoController.class).obtenerAlumnoPorIdHateoas(id)).withSelfRel());
			
			responseEntity = ResponseEntity.ok(alumno_aux);
			log.debug("Alumno encontrado " + alumno_aux.toString());

		} else {
// no hay
			responseEntity = ResponseEntity.notFound().build();
			log.debug("Alumno con id  " + id + " NO encontrado");
		}

		return responseEntity;
	}
	
	@GetMapping("/hateoas")
	public ResponseEntity<?> listaAlumnosHateoas() {
		ResponseEntity<?> responseEntity = null;
		Iterable<Alumno> ita = null;

		log.debug("Atendido por el MS " + nombre_instancia + " puerto " + environment.getProperty("local.server.port"));

		log.debug("Ha entrado en listaAlumnos ");
		ita = alumnoService.buscarTodosLosAlumnos();
		for (Alumno a :ita)
		{
			a.add(linkTo(methodOn(AlumnoController.class).obtenerAlumnoPorIdHateoas(a.getId())).withSelfRel());
			a.add(linkTo(methodOn(AlumnoController.class).borrarAlumno(a.getId())).withRel("borrarPorId"));
		}
		responseEntity = ResponseEntity.ok(ita);
		log.debug("Salida de listaAlumnos ");

		return responseEntity;
	}
	
	
	
	//otros MÉTODOS DE PRUEBA, USADOS EN EL CURSO
	
	
//	@GetMapping //prueba para httpinterceptor en angular
//	public ResponseEntity<?> listaAlumnos(@RequestHeader("miciudad") String miciudad) {
//		ResponseEntity<?> responseEntity = null;
//		Iterable<Alumno> ita = null;
//
//		log.debug("Ha entrado en listaAlumnos ");
//		log.debug("Propiedad miciudad =  "+miciudad);
//		ita = alumnoService.buscarTodosLosAlumnos();
//		responseEntity = ResponseEntity.ok(ita);
//		log.debug("Salida de listaAlumnos ");
//
//		return responseEntity;
//	}

	// TODO haced un método que reciba por URL el nombre de un alumno
	// y devuelva un JSON con el nombre recibido
	/**
	 * Consultar por nombre
	 * 
	 * @param nombre
	 * @return
	 */
//	@GetMapping("/{nombre}") // @Pathvariable hace corresponder una valor en la url con una variable de
//								// entrada
//	public AlumnoDTO obtenerAlumno(@PathVariable String nombre) {
//		AlumnoDTO alumno = null;
//
//		alumno = new AlumnoDTO(nombre, "cris@mail.es", 24);
//		alumno.getNombre();// Cristina
//
//		return alumno;
//	}

}

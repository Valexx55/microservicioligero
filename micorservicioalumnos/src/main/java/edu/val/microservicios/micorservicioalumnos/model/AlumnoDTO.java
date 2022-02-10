package edu.val.microservicios.micorservicioalumnos.model;

//POJO pLAIN OLD JAVA oBJECT
//BEAN JUDIA
//PascalCase
public class AlumnoDTO {
	
	
	//ATRIBUTOS
	private String nombre;
	private String email;
	private int edad;
	private int id;//primary key 
	
	
	
	
	//MÉTODOS O LA FUNCIONES
	//constructor
	//para generar alumnos "instaciar"
	public AlumnoDTO(String nombre, String email, int edad) {
		super();
		//this es el alumno nuevo
		this.nombre = nombre;
		this.email = email;
		this.edad = edad;
	}
	
	
	
	public AlumnoDTO(String nombre, String email, int edad, int id) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.edad = edad;
		this.id = id;
	}



	//MÉTODOS DE ACCESO
	public String getNombre() {
		return nombre;
	}
	
	//camelCase
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	

}

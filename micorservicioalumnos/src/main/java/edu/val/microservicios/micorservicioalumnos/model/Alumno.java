package edu.val.microservicios.micorservicioalumnos.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;//x --> estándar JPA
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "alumnos")
public class Alumno extends RepresentationModel<Alumno> {

	//CLASE ALUMNO --> TABLA ALUMNO DE LA BASE DE DATOS--ENTIDAD
	//OBJETO ALUMNO --> FILA/REGISTRO BASE DATOS
	//HIBERNATE --> ORM 	
	//JPA --> API métodos estándar java persistence api
	//spring JPA
	//incluir anotaciones en cada campo para validar --> JAVA BEAN VALIDATION
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)//contador autoincremental en Mysql
	private Long id;
	
	@NotNull
	@Size(min=4)
	private String nombre;
	@NotEmpty //vale por 2 valida que no sea null y que no esté vacío
	private String apellido;
	@NotEmpty
	@Email
	private String email;
	@Min(1)
	private int edad;

	@Lob
	@JsonIgnore
	private byte[]foto;
	
	@Column(name = "creado_en")
	@Temporal(TemporalType.TIMESTAMP) //la fecha con HHMMSS DDMMAA
	private Date creadoEn;
	
	@PrePersist
	private void generarFecha ()
	{
		this.creadoEn = new Date();
	}

	public Integer getFotoHashCode ()
	{
		Integer idev = null;
		
			if (this.foto!=null)
			{
				//hay foto almacenada
				idev = this.foto.hashCode();
			} //else si no hay foto asociada a este registros, devuelvo null
		
		return idev;
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreadoEn() {
		return creadoEn;
	}

	public void setCreadoEn(Date creadoEn) {
		this.creadoEn = creadoEn;
	}

	
	//SOBRRESCRIBIMOS EL DE object
	
	
	public int getEdad() {
		return edad;
	}

	@Override
	public String toString() {
		return "Alumno [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", edad="
				+ edad + ", creadoEn=" + creadoEn + "]";
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}



	public byte[] getFoto() {
		return foto;
	}



	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		Alumno alumno_aux = null;
		
			if (this == obj)
			{
				iguales = true;
			}
			else if (!(obj instanceof Alumno))
			{
				iguales = false;
			} else {
				alumno_aux = (Alumno) obj;
				iguales = ((this.id!=null) && (this.id.equals(alumno_aux.getId())));
			}
		
		return iguales;
	}
	
	
	
	
	
}

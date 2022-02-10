package edu.val.microservicios.micorservicioalumnos.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Controller
public class JSONPController {
	
	//JEE >> Servlets
	//JSON -serilizar y deserializar en cliente
	//GSON - otra libreria para trabjar con JSON - Google
	
	//http://localhost:8080/jsonp/alumno?callback=pepe
	@Autowired
	ObjectMapper om;
	
	@GetMapping("/jsonp/alumno")
	public void testJsonp (
			HttpServletRequest request, HttpServletResponse respuesta,
			@RequestParam (value = "callback", required = true) String callback) throws IOException
	{
		ObjectNode objectNode = om.createObjectNode();//nuestro alumno
		objectNode.put("id", 15);
		objectNode.put("nombre", "Nacho");
		objectNode.put("email", "nacho@fuerte.es");
		objectNode.put("apellido", "Vidal");
		objectNode.put("edad", 27);
		objectNode.put("creadoEn",  "2021-09-28");
		
		String alumno_json = objectNode.toString();
		String cuerpo_respuesta = callback + "("+ alumno_json+ ")"; //Función JS (JSON:param)
		
		System.out.println("CUERPO_RESPUESTA =  " + cuerpo_respuesta);
		
		respuesta.setContentType("text/javascript; charset=UTF-8");
		respuesta.getWriter().print(cuerpo_respuesta);//ACCEDO AL CUERPO DE LA RESPUESTA getWriter me da el body del http
		
		
	}

}

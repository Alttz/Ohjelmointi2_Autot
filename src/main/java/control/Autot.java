package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Auto;
import model.dao.Dao;


@WebServlet("/autot/*")
public class Autot extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Autot() {
        System.out.println("Autot.Autot()");        
    }

	// Tietojen hakeminen
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doGet()");
		String hakusana = request.getParameter("hakusana");
		String id = request.getParameter("id");
		Dao dao = new Dao();
		ArrayList<Auto> autot;
		String strJSON="";		
		if(hakusana!=null) {//Jos kutsun mukana tuli hakusana
			if(!hakusana.equals("")) {//Jos hakusana ei ole tyhjä
				autot = dao.getAllItems(hakusana); //Haetaan kaikki hakusanan mukaiset autot							
			}else {
				autot = dao.getAllItems(); //Haetaan kaikki autot
			}
			strJSON = new Gson().toJson(autot);	
		} else if(id!=null) {
			Auto auto = dao.getItem(Integer.parseInt(id));
			strJSON = new Gson().toJson(auto);
		} else {
			autot = dao.getAllItems(); //Haetaan kaikki autot
			strJSON = new Gson().toJson(autot);	
		}
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}

	// Tietojen lisääminen
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPost()");
		//Luetaan JSON-tiedot POST-pyynn�n bodysta ja luodaan niiden perusteella uusi auto
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		Auto auto = new Gson().fromJson(strJSONInput, Auto.class);	
		// System.out.println(auto);
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.addItem(auto)) {
			out.println("{\"response\":1}");  //Auton lis��minen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Auton lis��minen ep�onnistui {"response":0}
		}
	}

	// Tietojen muuttaminen
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPut()");
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		Auto auto = new Gson().fromJson(strJSONInput, Auto.class);
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();
		if(dao.changeItem(auto)) {
			out.println("{\"response\":1}");
		} else {
			out.println("{\"response\":0}");
		}
	}

	// Tietojen poistaminen
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doDelete()");
		int id = Integer.parseInt(request.getParameter("id"));
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.removeItem(id)) {
			out.println("{\"response\":1}");  //Auton poistaminen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Auton poistaminen epäonnistui {"response":0}
		}
	}

}

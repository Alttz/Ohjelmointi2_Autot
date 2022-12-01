package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Auto;

public class Dao {
	private Connection con = null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep = null;
	private String sql = "";
	private String db = "Autot.sqlite";

	private Connection yhdista() {
		Connection con = null;
		String path = System.getProperty("catalina.base");
		path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); // Eclipsessa
		// path =  new File(System.getProperty("user.dir")).getParentFile().toString() +"\\"; //Testauksessa
		// System.out.println(path); //Tästä näet mihin kansioon laitat tietokanta-tiedostosi
		// path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
		String url = "jdbc:sqlite:" + path + db;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus epäonnistui.");
			e.printStackTrace();
		}
		return con;
	}

	private void sulje() {		
		if (stmtPrep != null) {
			try {
				stmtPrep.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
				System.out.println("Yhteys suljettu.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	public ArrayList<Auto> getAllItems() {
		ArrayList<Auto> autot = new ArrayList<Auto>();
		sql = "SELECT * FROM autot ORDER BY id DESC"; //Suurin id tulee ensimmäisenä
		try {
			con = yhdista();
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				rs = stmtPrep.executeQuery();
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) {
						Auto auto = new Auto();
						auto.setId(rs.getInt(1));
						auto.setRekno(rs.getString(2));
						auto.setMerkki(rs.getString(3));
						auto.setMalli(rs.getString(4));
						auto.setVuosi(rs.getInt(5));
						autot.add(auto);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sulje();
		}
		return autot;
	}
	
	public ArrayList<Auto> getAllItems(String searchStr) { //Metodeja voi kuormittaa, kunhan parametreiss� eroja
		ArrayList<Auto> autot = new ArrayList<Auto>();
		sql = "SELECT * FROM autot WHERE rekno LIKE ? or merkki LIKE ? or malli LIKE ? ORDER BY id DESC";
		try {
			con = yhdista();
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + searchStr + "%");
				stmtPrep.setString(2, "%" + searchStr + "%");
				stmtPrep.setString(3, "%" + searchStr + "%");
				rs = stmtPrep.executeQuery();
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) {
						Auto auto = new Auto();
						auto.setId(rs.getInt(1));
						auto.setRekno(rs.getString(2));
						auto.setMerkki(rs.getString(3));
						auto.setMalli(rs.getString(4));
						auto.setVuosi(rs.getInt(5));
						autot.add(auto);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sulje();
		}
		return autot;
	}
	
	public boolean addItem(Auto auto) {
		boolean paluuArvo = true;
		sql = "INSERT INTO autot(rekno, merkki, malli, vuosi)VALUES(?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, auto.getRekno());
			stmtPrep.setString(2, auto.getMerkki());
			stmtPrep.setString(3, auto.getMalli());
			stmtPrep.setInt(4, auto.getVuosi());
			stmtPrep.executeUpdate();		
		} catch (Exception e) {
			paluuArvo=false;
			e.printStackTrace();
		} finally {
			sulje();
		}
		return paluuArvo;
	}
	
	public boolean removeItem(int id) {
		boolean paluuArvo = true;
		sql = "DELETE FROM autot WHERE id=?";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setInt(1, id);
			stmtPrep.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		} finally {
			sulje();
		}
		return paluuArvo;
	}
	
	public Auto getItem(int id) {
		Auto auto = null;
		sql = "SELECT * FROM autot WHERE id=?";
		try {
			con=yhdista();
			if(con!=null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setInt(1, id);
				rs = stmtPrep.executeQuery();
				if(rs.isBeforeFirst()) {
					rs.next();
					auto = new Auto();
					auto.setId(rs.getInt(1));
					auto.setRekno(rs.getString(2));
					auto.setMerkki(rs.getString(3));
					auto.setMalli(rs.getString(4));
					auto.setVuosi(rs.getInt(5));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sulje();
		}
		return auto;
	}
	
	public boolean changeItem(Auto auto) {
		boolean paluuArvo=true;
		sql="UPDATE autot SET rekno=?, merkki=?, malli=?, vuosi=? where id=?";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setString(1, auto.getRekno());
			stmtPrep.setString(2, auto.getMerkki());
			stmtPrep.setString(3, auto.getMalli());
			stmtPrep.setInt(4, auto.getVuosi());
			stmtPrep.setInt(5, auto.getId());
			stmtPrep.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo=false; 
		} finally {
			sulje();
		}
		return paluuArvo;
	}
	
	public boolean removeAllItems(String pwd){
		boolean paluuArvo=true;
		if(!pwd.equals("Nimda")) { //"Kovakoodattu" salasana -ei ole hyvä idea!
			return false;
		}
		sql="DELETE FROM Autot";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 			
			stmtPrep.executeUpdate();	        
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		} finally {
			sulje();
		}				
		return paluuArvo;
	}
}

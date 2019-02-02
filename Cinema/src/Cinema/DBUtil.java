package Cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	
	static Connection conn = null;
	
public static Connection getConnected() {
		
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/Cinema", "GenadiPreslav", "1234");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
		
		
	}//end method
	
public static MyModel getAllGenres() {
		
		conn = getConnected();
		String sql = "select * from genres ;";
		ResultSet result = null;
		MyModel genre = null;
		
		try {
			PreparedStatement state = conn.prepareStatement(sql);
			result = state.executeQuery();
			genre = new MyModel(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return genre;

		
	}//end getAllGenres	
	
public static MyModel getAllMovies() {
		
		conn = getConnected();
		String sql = "select movies.movie_id,movies.title,genres.genre,movies.releacedate,movies.languege,movies.subtitles,movies.rating from movies join genres on movies.genre_id = genres.genre_id;";
		ResultSet result = null;
		MyModel movie = null;
		
		try {
			PreparedStatement state = conn.prepareStatement(sql);
			result = state.executeQuery();
			movie = new MyModel(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movie;
		
	}//end getAllMovies	

public static MyModel getAllProjections() {
	
	conn = getConnected();
	String sql = "select projections.projection_id,movies.title,projections.time,projections.hall from projections join movies on projections.movie_id = movies.movie_id; ;";
	ResultSet result = null;
	MyModel projection = null;
	
	try {
		PreparedStatement state = conn.prepareStatement(sql);
		result = state.executeQuery();
		projection = new MyModel(result);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return projection;
	
  }//end getAllProjection

public static MyModel getAllSearch1(String genre,String languege) {
	
	conn = getConnected();
	String sql = "SELECT MOVIES.TITLE,GENRES.GENRE,MOVIES.RELEACEDATE,MOVIES.LANGUEGE FROM MOVIES JOIN GENRES ON MOVIES.GENRE_ID=GENRES.GENRE_ID WHERE GENRES.GENRE=? AND MOVIES.LANGUEGE=?;";
	ResultSet result = null;
	MyModel search = null;
	
	try {
		PreparedStatement state = conn.prepareStatement(sql);
		state.setString(1, genre);
		state.setString(2, languege);
		result = state.executeQuery();
		search = new MyModel(result);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return search;
	
  }//end Search

public static MyModel getAllSearch2(int rating,int hall) {
	
	conn = getConnected();
	String sql = "SELECT MOVIES.TITLE,MOVIES.RELEACEDATE,MOVIES.LANGUEGE,PROJECTIONS.TIME,PROJECTIONS.HALL FROM PROJECTIONS JOIN  MOVIES ON PROJECTIONS.MOVIE_ID= MOVIES.MOVIE_ID WHERE MOVIES.RATING=? AND PROJECTIONS.HALL=?;";
	ResultSet result = null;
	MyModel search = null;
	
	try {	
	    PreparedStatement state = conn.prepareStatement(sql);
		state.setInt(1, rating);
		state.setInt(2, hall);
		result = state.executeQuery();
		search = new MyModel(result);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return search;
	
  }//end search
	
}//end DBUtil

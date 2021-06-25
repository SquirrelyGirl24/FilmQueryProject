package com.skilldistillery.filmquery.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";
	private String user = "student";
	private String pass = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

// i. Implement the findFilmById method that takes an int film ID, 
// and returns a Film object (or null, if the film ID returns no data.)
	public Film findFilmById(int filmId) {
		Film film = null;
		String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length FROM film WHERE id = ?";
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet filmResult = stmt.executeQuery();
		if (filmResult.next()) {
			film = new Film(filmResult.getInt("id"));
			film.setId(filmResult.getInt(1));
			film.setTitle(filmResult.getString(2));
			film.setDescription(filmResult.getString(3));
			film.setReleaseYear(filmResult.getInt(4));
			film.setLanguageId(filmResult.getInt(5));
			film.setRentalDuration(filmResult.getInt(6));
			film.setRentalRate(filmResult.getDouble(7));
			film.setLength(filmResult.getInt(8));
			film.setReplacementCost(filmResult.getDouble(9));
			film.setRating(filmResult.getString(10));
			film.setSpecialFeatures(filmResult.getString(11));
			film.setCast(findActorsByFilmId(actorId));
		}
		return film;
	}

// ii. Implement findActorById method that takes an int actor ID, and 
// returns an Actor object (or null, if the actor ID returns no data.)
	public Actor findActorById(int actorId) {
		Actor actor = null;
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		if (actorResult.next()) {
			actor = new Actor(actorResult.getInt("id")
			,actorResult.getString("last_name")
			,actorResult.getString("first_name"));
			actor.setFilms(findFilmsByActorId(actorId)); // An Actor has Films
		}
		// ...
		return actor;
	}

// iii. Implement findActorsByFilmId with an appropriate List implementation 
// that will be populated using a ResultSet and returned.
	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, ";
			sql += " rental_rate, length, replacement_cost, rating, special_features "
					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id " + " WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmId = rs.getInt(1);
				String title = rs.getString(2);
				String desc = rs.getString(3);
				short releaseYear = rs.getShort(4);
				int langId = rs.getInt(5);
				int rentDur = rs.getInt(6);
				double rate = rs.getDouble(7);
				int length = rs.getInt(8);
				double repCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String features = rs.getString(11);
				Film film = new Film();
				films.add(film);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public Film findFilmById(int filmId) {
		return null;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		// TODO Auto-generated method stub
		return null;
	}

}

// iv. Make sure your JDBC code uses PreparedStatement with bind variables 
// instead of concatenating values into SQL strings.
package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Film findFilmById(int filmId) {
		Film film = null;
		String sql = "SELECT film.id, title, description, release_year, language_id, rental_duration,"
				+ "rental_rate, length, replacement_cost, rating, special_features "
				+ " FROM film JOIN language ON film.language_id = language.id" + " WHERE film.id = ?";
		try {
			PreparedStatement stmt = null;
			Connection conn;
			conn = DriverManager.getConnection(URL, user, pass);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				film = new Film();
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
				film.setActors(findActorsByFilmId(filmId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	public Actor findActorById(int actorId) {
		Actor actor = null;
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		try {
			PreparedStatement stmt = null;
			Connection conn;
			conn = DriverManager.getConnection(URL, user, pass);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor();
				actor.setFilms(findFilmsByActorId(actorId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmId) {
		Actor actor = null;
		List<Actor> actors = new ArrayList<Actor>();
		String sql = "SELECT id, first_name, last_name FROM actor JOIN film_actor ON id = actor_id WHERE film_id = ?";
		try (Connection conn = DriverManager.getConnection(URL, "student", "student");
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				actor = new Actor();
				actor.setId(rs.getInt(1));
				actor.setFirstName(rs.getString(2));
				actor.setLastName(rs.getString(3));
				actors.add(actor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	public List<Film> findFilmsByActorId(int actorId) {
		return null;
	}

	@Override
	public List<Film> findFilmBySearchWord(String filmSearch) {
		Film film = null;
		List<Film> films = new ArrayList<Film>();
		String sql = "SELECT title, description, film.id, release_year, rating, language_id"
				+ " FROM film JOIN language ON film.language_id = language.id "
				+ " WHERE film.title LIKE ? OR film.description LIKE ?";
		try (Connection conn = DriverManager.getConnection(URL, "student", "student");
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, "%" + filmSearch + "%");
			stmt.setString(2, "%" + filmSearch + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				film = new Film();
				film.setTitle(rs.getString(1));
				film.setDescription(rs.getString(2));
				film.setId(rs.getInt(3));
				film.setReleaseYear(rs.getInt(4));
				film.setRating(rs.getString(5));
				film.setLanguageId(rs.getInt(6));
				film.setActors(findActorsByFilmId(film.getId()));
				films.add(film);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	public List<Film> findFilmById(String filmSearch) {
		return null;
	}

}

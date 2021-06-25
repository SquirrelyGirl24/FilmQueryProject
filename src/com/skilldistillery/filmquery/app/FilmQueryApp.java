package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
	private Film film = new Film();
	private List<Film> films = new ArrayList<Film>();
	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}
//
//	private void test() {
//		Film film = db.findFilmById(1);
//		System.out.println(film);
//	}

	private void launch() {
		Scanner input = new Scanner(System.in);
		startUserInterface(input);
		input.close();
	}

	private void startUserInterface(Scanner input) {
		boolean keepGoing = true;
		while (keepGoing) {
			System.out.println("Please make a selection:");
			System.out.println("1. Lookup film by its ID");
			System.out.println("2. Lookup film by keyword"); 
			System.out.println("3. Exit the application");
			String choice = input.next();
			switch (choice) {
			case "1":
				System.out.println("Select film ID");
				int filmId = input.nextInt();
				film = db.findFilmById(filmId);
				if (film == null) {
					System.out.println("Film Not Found");
				} else {
					System.out.println(film);
				}
				break;
			case "2":
				System.out.println("Enter part of the title or description of the film");
				String filmSearch = input.next();
				films = db.findFilmBySearchWord(filmSearch);
				if (films.size() == 0) {
					System.out.println("Film Not Found");
				} else {
					System.out.println(films);
				}
				break;
			case "3":
				System.out.println("Exiting application");
				keepGoing = false;
				break;
			default:
				System.out.println("Invalid entry");
				break;
			}
		}
	}

}

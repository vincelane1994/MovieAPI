package co.grandcircus.movieApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.grandcircus.movieApi.entities.Movie;
import co.grandcircus.movieApi.entities.dao.MovieRepository;


@RestController
public class MovieApiController {
	@Autowired
	MovieRepository dao;

	@GetMapping("/")
	public ModelAndView redirect() {
		return new ModelAndView("redirect:/movies");
	}
	@GetMapping("/movies")
	public List<Movie> listMovies(
			@RequestParam(value="category", required=false)String category,
			@RequestParam(value="title", required=false)String title){
		if((category == null || category.isEmpty()) && (title == null || title.isEmpty())) {
			return dao.findAll();			
		}else if(category == null || category.isEmpty()){
			return dao.findByTitleContainsIgnoreCase(title);
		}else
		return dao.findByCategory(category);
	}
	
	@GetMapping("/movies/random") 
	public Movie randomMovie(@RequestParam(value="category", required=false)String category) {
		if(category == null || category.isEmpty()) {
			List<Movie> movieList = dao.findAll();
			int id = (int)(Math.random() * 100);
			return movieList.get(id);
		}else {
			List<Movie> movies = dao.findByCategory(category);
			int catSize = movies.size();
			int randMovie = (int)(Math.random() * catSize);
			return movies.get(randMovie);
		}
	}
//	@GetMapping("movies/random-list")
//	public List<Movie> randomMovieList(@RequestParam("quantity")Integer quantity){
//		ArrayList<Long> randMovies = new ArrayList<Long>();
//		do{
//			Long id = 1 + (long)(Math.random() * 100);
//			if(!randMovies.contains(id)) {
//				randMovies.add(id);
//			}
//		}while(randMovies.size() < quantity);
//		List<Movie> movieList = new ArrayList<Movie>();
//		for(int i = 0; i < randMovies.size(); i++) {
//			movieList.add(dao.findById(randMovies.get(i)).get());
//		}
//		return movieList;
//	}
	@GetMapping("movies/random-list")
	public List<Movie> randomMovieList(@RequestParam("quantity")Integer quantity){
		List<Movie> movieList = dao.findAll();
		List<Movie> randMovies = new ArrayList<Movie>();
		Collections.shuffle(movieList);
		for(int i = 0; i < quantity; i++) {
			randMovies.add(movieList.get(i));
		}
		return randMovies;
	}
	@GetMapping("/movies/{id}")
    public Movie getMovie(@PathVariable("id") Long id) {
        return dao.findById(id).get();
	}
	@GetMapping("/categories")
	public List<String> categories(){
		List<Movie> movieList = dao.findAll();
		Map<String, String> map = new HashMap<>();
		for(Movie movies: movieList) {
			map.put(movies.getCategory(), movies.getTitle());
		}
		List<String> result = new ArrayList(map.keySet());
		return result;
	}
}

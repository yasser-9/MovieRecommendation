package com.ensa.MovieRec.Controllers;

import com.ensa.MovieRec.Models.Movie;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    @GetMapping("/")
	String index(Model model) {
		HashMap<Integer, Movie> movieDict = new HashMap<>();
		try {
			final int selectedID = 1;
			Path pt = new Path("contentoutput9/part-r-00000");
			FileSystem fs = FileSystem.get(new Configuration());
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
			String line = br.readLine();
			while (line != null) {
				String[] fields = line.split("\t");
				int userID = Integer.parseInt(fields[0]);
				if (userID > selectedID) {
					break;
				}
				else if (userID < selectedID)
				{
					continue;
				}
				String[] movieScore = fields[1].split(",");
				int movieID = Integer.parseInt(movieScore[0]);
				Movie movie = new Movie(movieID, Double.parseDouble(movieScore[1]));
				movieDict.put(movieID, movie);
				line = br.readLine();
			}
			pt = new Path("movies.csv");
			br = new BufferedReader(new InputStreamReader(fs.open(pt)));
			line = br.readLine();
			while (line != null) {
				String[] fields = line.split(",");
				int movieID = Integer.parseInt(fields[0]);
				String movieName = fields[1];
				Movie movie = movieDict.get(movieID);
				if(movie != null) movie.setName(movieName);
				line = br.readLine();
			}
			pt = new Path("links.csv");
			br = new BufferedReader(new InputStreamReader(fs.open(pt)));
			line = br.readLine();
			while (line != null) {
				String[] fields = line.split(",");
				int movieID = Integer.parseInt(fields[0]);
				String imdbID = "tt" + fields[1];
				Movie movie = movieDict.get(movieID);
				if(movie != null) movie.setImdbID(imdbID);
				line = br.readLine();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		List<Movie> movieList = new ArrayList<>(movieDict.values());
		Collections.sort(movieList);
		Collections.reverse(movieList);
		for (Movie movie: movieList) {
			System.out.println(movie.getName() +" "+movie.getScore());
		}

		model.addAttribute("movieList", movieList);
		return "index";
	}
}

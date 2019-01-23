/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author azecoder
 */
public class WinnerImpl implements Winner {

    int age, year;
    String movie, name;

    WinnerImpl() {
    }

    WinnerImpl(int _year, int _age, String _name, String _movie) {
        this.name = _name;
        this.age = _age;
        this.movie = _movie;
        this.year = _year;
    }

    @Override
    public int getYear() {
        return this.year;
    }

    @Override
    public int getWinnerAge() {
        return this.age;
    }

    @Override
    public String getWinnerName() {
        return this.name;
    }

    @Override
    public String getFilmTitle() {
        return this.movie;
    }

    private static String removeQuote(String str) {
        return str.substring(1, str.length() - 1);
    }

    /**
     *
     * @param path
     * @return Collection
     * @throws java.io.IOException
     */
    static Collection<Winner> loadData(String[] path) throws IOException {

        Stream<Winner> database = null;

        for (String pp : path) {

            Stream<String> stream = Files.lines(Paths.get(pp));
            Stream current_stream = stream
                    .filter(s -> !s.startsWith("#"))
                    .map(s -> {
                        String[] line = s.split(",");
                        return new WinnerImpl(Integer.parseInt(line[1]),
                                Integer.parseInt(line[2]),
                                removeQuote(line[3]),
                                removeQuote(line[4])
                        );
                    });

            if (database == null) {
                database = current_stream;
            } else {
                database = Stream.concat(database, current_stream);
            }

        }

        return database.collect(Collectors.toList());

    }

}

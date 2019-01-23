/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winners;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author azecoder
 */
public class WinnerOperations {

    static final String DB_PATH = "/home/azecoder/Desktop/AP/HW2/";
    static String[] path = new String[]{
        DB_PATH + "oscar_age_male.csv",
        DB_PATH + "oscar_age_female.csv"
    };

    private static WinnerImpl getYoungestWinner(List<Winner> list) {

        WinnerImpl ret = (WinnerImpl) list.stream().collect(
                Collectors.reducing((d1, d2) -> d1.getWinnerAge() < d2.getWinnerAge() ? d1 : d2)
        ).get();

        return ret;

    }

    private static WinnerImpl getOldestWinner(List<Winner> list) {

        WinnerImpl ret = (WinnerImpl) list.stream().collect(
                Collectors.reducing((d1, d2) -> d1.getWinnerAge() > d2.getWinnerAge() ? d1 : d2)
        ).get();

        return ret;

    }

    /**
     *
     * @param db
     * @return Stream
     */
    private static Stream<String> oldWinners(Stream<Winner> db) {
        System.out.println("oldWinners function called..");
        // method oldWinners that given a Stream<Winner> returns a new Stream<String>
        // containing the names of the winners that are older than 35 sorted alphabetically.
        return db
                .sorted(Comparator.comparing(k -> k.getWinnerName())) // names sorted alphabetically
                .filter(s -> s.getWinnerAge() > 35) // filter data and get winners older than 35
                .map(s -> s.getWinnerName()) // get names of the winners
                .collect(Collectors.toList()) // collect names in the list
                .stream(); // return a stream string that contains name of the winners

    }

    /**
     *
     * @param db
     * @return Stream
     */
    private static Stream<String> extremeWinners(Stream<Winner> db) {
        System.out.println("extremeWinners function called..");
        // method extremeWinners that given a Stream<Winner> returns a Stream<String> 
        // containing the names of all the youngest and of all the oldest winners, sorted in inverse alphabetical ordering.
        List<Winner> list = db.collect(Collectors.toList()); // get all data as a list

        WinnerImpl A = getYoungestWinner(list); // get the youngest winner of the list
        WinnerImpl B = getOldestWinner(list); // get the oldest winner of the list

        return Arrays.stream(new WinnerImpl[]{A, B}) // all data of the youngest and oldest winner
                .map(s -> s.getWinnerName()) // get only names from data
                .sorted(Comparator.reverseOrder()) // sorted in inverse alphabeital order
                .collect(Collectors.toList()) // collect names in a list
                .stream(); // return a stream string that contains name of the winners

    }

    /**
     *
     * @param db
     * @return Stream
     */
    private static Stream<String> multiAwardedFilm(Stream<Winner> db) {
        System.out.println("multiAwardedFilm function called..");
        // method multiAwardedFilm that given a Stream<Winner> returns a Stream<String> 
        // containing the title of films who won two prizes. The elements of the stream must be in chronological order
        return db.sorted(Comparator.comparing(k -> k.getYear())).map(s -> s.getFilmTitle()).
                // get data which groupped by Film Title, and counting
                collect(Collectors.collectingAndThen(Collectors.groupingBy(Function.identity(), Collectors.counting()),
                        map -> {
                            map.values().removeIf(l -> l < 2); // remove if count is less than 2
                            return map.keySet();
                        })
                ).stream(); // return a stream string that contains name of the winners

    }

    private static <T, U> Stream<U> runJobs(Stream<Function<Stream<T>, Stream<U>>> f, Collection<T> c) {
        // method runJobs that given a Stream<Function<Stream<T>,Stream<U>>> of jobs and a Collection<T> coll returns a Stream<U> 
        // obtained by concatenating the results of the execution of all the jobs on the data contained in coll.

        List<Function<Stream<T>, Stream<U>>> functionList = f.collect(Collectors.toList());

        Stream<U> ret = null;
        for (int i = 0; i < functionList.size(); i++) {

            Stream<T> stream = c.stream();
            Stream<U> curr = functionList.get(i).apply(stream);
            if (ret == null) {
                ret = curr;
            } else {
                ret = Stream.concat(ret, curr);
            }

        }

        return ret;

    }

    public static void main(String[] args) throws IOException {

        Collection<Winner> database = WinnerImpl.loadData(path);
        List<Winner> list = database.stream().collect(Collectors.toList());

        /**
         * ***************************************************************************
         */
        System.out.println("NAME\t|\tMOVIE\n");
        list.forEach(s -> System.out.println(s.getWinnerName() + "\t|\t" + s.getFilmTitle()));

        /**
         * ***************************************************************************
         */
        System.out.println("\n\nOLD WINNERS\n");
        Stream<String> old_winners = oldWinners(list.stream());
        old_winners.forEach(s -> System.out.println(s));

        /**
         * ***************************************************************************
         */
        System.out.println("\n\nEXTREME WINNERS\n");
        Stream<String> extreme_winners = extremeWinners(list.stream());
        extreme_winners.forEach(s -> System.out.println(s));
        /**
         * ***************************************************************************
         */
        System.out.println("\n\nMULTI AWARDED FILMS\n");
        Stream<String> mul_films = multiAwardedFilm(list.stream());
        mul_films.forEach(s -> System.out.println(s));
        /**
         * ***************************************************************************
         */
        System.out.println("\n\nRUN JOBS LIST\n");
        Stream<Function<Stream<Winner>, Stream<String>>> forString = FuncWinnerToString();
        runJobs(forString, database);
//        Stream<String> run_jobs = runJobs(forString, database);
//        List<String> run_jobs_list = run_jobs.collect(Collectors.toList());
//        System.out.println("TEST : " + run_jobs_list.get(2));
        /**
         * ***************************************************************************
         */
    }

    private static final Function< Stream<Winner>, Stream<String>> F1 = WinnerOperations::oldWinners;
    private static final Function< Stream<Winner>, Stream<String>> F2 = WinnerOperations::extremeWinners;
    private static final Function< Stream<Winner>, Stream<String>> F3 = WinnerOperations::multiAwardedFilm;
    
    private static Stream<Function<Stream<Winner>, Stream<String>>> FuncWinnerToString() {

        List<Function<Stream<Winner>, Stream<String>>> f = Arrays.asList(F1, F2, F3);
        return f.stream();

    }

}

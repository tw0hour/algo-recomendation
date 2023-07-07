package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args){

        Person lisa_rose = new Person("Lisa Rose",Map.of("Lady in the Water", 2.5, "Snakes on a Plane", 3.5,
                "Just My Luck", 3.0, "Superman Returns", 3.5, "You, Me and Dupree", 2.5,
                "The Night Listener", 3.0));

        Person gene_seymour = new Person("Gene Seymour",Map.of("Lady in the Water", 3.0, "Snakes on a Plane", 3.5,
                "Just My Luck", 1.5, "Superman Returns", 5.0, "The Night Listener", 3.0,
                "You, Me and Dupree", 3.5));

        Person michael_phillips = new Person("Michael Phillips",Map.of("Lady in the Water", 2.5, "Snakes on a Plane", 3.0,
                "Superman Returns", 3.5, "The Night Listener", 4.0));
        Person claudia_puig = new Person("Claudia Puig",Map.of("Snakes on a Plane", 3.5, "Just My Luck", 3.0,
                "The Night Listener", 4.5, "Superman Returns", 4.0,
                "You, Me and Dupree", 2.5));

        Person mick_Lasalle = new Person("Mick LaSalle",Map.of("Lady in the Water", 3.0, "Snakes on a Plane", 4.0,
                "Just My Luck", 2.0, "Superman Returns", 3.0, "The Night Listener", 3.0,
                "You, Me and Dupree", 2.0));

        Person jack_mattews = new Person("Jack Matthews",Map.of("Lady in the Water", 3.0, "Snakes on a Plane", 4.0,
                "The Night Listener", 3.0, "Superman Returns", 5.0, "You, Me and Dupree", 3.5));

        Person toby = new Person("Toby",Map.of("Snakes on a Plane",4.5,"You, Me and Dupree",1.0,"Superman Returns",4.0));

        List<Person> mySet = List.of(lisa_rose,gene_seymour,michael_phillips,claudia_puig,mick_Lasalle,jack_mattews,toby);

        double distEucli = calculeSimilaritudeEuclidienWay(lisa_rose,gene_seymour);
        System.out.println("distance euclidienne entre lisa et gene: " + distEucli);

        double distEucli2 = calculeSimilaritudeEuclidienWay(lisa_rose,michael_phillips);
        System.out.println("distance euclidienne entre lisa et michael :" + distEucli2);

        double pearson = calculSimilaritudePearsonCorrelation(lisa_rose,gene_seymour);
        System.out.println("pearson correlation entre lisa et gene : " + pearson);

        double pearson2 = calculSimilaritudePearsonCorrelation(lisa_rose,michael_phillips);
        System.out.println("pearson entre lisa et michael : " + pearson2);

        List<Double> listRecomendedFilmsForToby = markingRecomendationForToby(mySet, toby);
        System.out.println("Recomended films for toby : " + listRecomendedFilmsForToby);
    }


    private static double calculeSimilaritudeEuclidienWay(Person p1, Person p2){
        double sum = 0.0;

        for (var entry : p1.films.entrySet()) {
            if(p2.films.containsKey(entry.getKey())){
                sum += (entry.getValue() - p2.films.get(entry.getKey())) * (entry.getValue() - p2.films.get(entry.getKey()));
            }
        }
        double distance = Math.sqrt(sum);
        return 1 / (1 + distance);
    }

    private static double calculSimilaritudePearsonCorrelation(Person p1, Person p2){
        double numberOfCommonFilms = 0.0;
        double sumProdXY = 0;
        double sumX = 0;
        double sumY = 0;
        double sumXPow = 0;
        double sumYPow = 0;
        double denominateur = 0.0;
        for (var p1Key : p1.films.entrySet()) {
            if(p2.films.containsKey(p1Key.getKey())){
                double valueFilmP2 = p2.films.get(p1Key.getKey());
                numberOfCommonFilms++;

                sumProdXY += p1Key.getValue() * valueFilmP2;

                sumX += p1Key.getValue();
                sumY += valueFilmP2;

                sumXPow += p1Key.getValue() * p1Key.getValue();
                sumYPow += valueFilmP2 * valueFilmP2;
            }
        }
        denominateur = Math.sqrt((numberOfCommonFilms * sumXPow) - (sumX * sumX)) * Math.sqrt((numberOfCommonFilms * sumYPow) - (sumY * sumY));
        if (denominateur==0.0) return 0.0;

        return (numberOfCommonFilms * sumProdXY - sumX * sumY) / denominateur;
    }

    private static List<Double> markingRecomendationForToby(List<Person> persons, Person toby) {
        double sumSimilarity = 0;
        List<Double> recomendedFilms = new ArrayList<>();
        double similarity = 0;
        Map<String,Double> prodSimilarityFilmMap = new HashMap<>();
        for(Person person: persons){
            if(!Objects.equals(person.name, toby.name) && !Objects.equals(person.name, "Michael Phillips")){
                similarity = calculSimilaritudePearsonCorrelation(person, toby);
                for (var film : person.films.entrySet()){
                    if(!toby.films.containsKey(film.getKey())){
                        if(prodSimilarityFilmMap.containsKey(film.getKey())){
                            prodSimilarityFilmMap.put(film.getKey(),prodSimilarityFilmMap.get(film.getKey()) + similarity * film.getValue());
                        }
                        else {
                            prodSimilarityFilmMap.put(film.getKey(),similarity * film.getValue());
                        }
                    }
                }
            }
        }
        for (var prodSimilarityFilm: prodSimilarityFilmMap.entrySet()){
            System.out.println("film : "+ prodSimilarityFilm.getKey() +" : "+ prodSimilarityFilm.getValue());
            sumSimilarity = 0;
            for(Person person: persons){
                if(person.films.containsKey(prodSimilarityFilm.getKey()) && !Objects.equals(person.name, toby.name) && !Objects.equals(person.name, "Michael Phillips")){
                    sumSimilarity += calculSimilaritudePearsonCorrelation(person, toby);
                }
            }
            recomendedFilms.add(prodSimilarityFilm.getValue()/sumSimilarity );
        }
        return recomendedFilms;
    }
}
package com.alkurop.mystreetplaces;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class StringTests {
    @Test
    public void stringTest() {
        System.out.println(super_reduced_string("baaba"));
    }

    static String super_reduced_string(String s) {
        String result = s;
        boolean isShouldFind = true;
        while (isShouldFind) {
            isShouldFind = false;

            for (int i = 0; i < result.length() - 1; i++) {

                char first = result.charAt(i);
                char second = result.charAt(i + 1);

                if (first == second) {
                    isShouldFind = true;
                    result = result.replace(new String(new char[] { first, first }), "");
                }
            }
        }
        return result;
    }

    @Test
    public void yearsTest() {
        Person[] groupOfPeple = new Person[] {
                new Person(1992, 2015),
                new Person(1986, 1991),
                new Person(1955, 1992),
                new Person(1992, 2015),
                new Person(1934, 1998),
                new Person(1988, 2002),
                new Person(1966, 1992),
        };

        LifeDeathRange result = yearsMostPeopleAlive(groupOfPeple);
        System.out.println(result);
        System.out.println(yearsMostPeopleAlive2(groupOfPeple));
    }

    LifeDeathRange yearsMostPeopleAlive(Person[] input) {
        TreeMap<Integer, Set<Person>> birthMargins = new TreeMap<>();
        TreeMap<Integer, Set<Person>> deathMargins = new TreeMap<>();

        for (Person person : input) {
            addPerson(person, birthMargins, deathMargins, input);
        }

        TreeSet<LifeDeathRange> resulSet = new TreeSet<>();
        for (Map.Entry<Integer, Set<Person>> birthEntrySet : birthMargins.entrySet()) {
            Integer birthYear = birthEntrySet.getKey();
            Set<Person> peopleBorn = birthEntrySet.getValue();
            for (Map.Entry<Integer, Set<Person>> deathEntrySet : deathMargins.entrySet()) {
                Set<Person> peopleAlive = deathEntrySet.getValue();
                Integer deathYear = deathEntrySet.getKey();
                if (deathYear <= birthYear) break;

                LifeDeathRange range = new LifeDeathRange(birthYear, deathYear);

                for (Person person : peopleBorn) {
                    if (peopleAlive.contains(person)) {
                        range.count++;
                        range.mPersonList.add(person);
                    }
                }

                if (range.count > 0) {
                    resulSet.add(range);
                }
            }

        }
        LifeDeathRange biggestRange = resulSet.last();
        return biggestRange;
    }

    void addPerson(Person person, TreeMap<Integer, Set<Person>> birthMargins, TreeMap<Integer, Set<Person>> deathMargins, Person[] persons) {
        for (Map.Entry<Integer, Set<Person>> entry : birthMargins.entrySet()) {
            Integer key = entry.getKey();
            Set<Person> value = entry.getValue();
            if (key >= person.birthYear && !value.contains(person)) {
                value.add(person);
            }
        }
        for (Map.Entry<Integer, Set<Person>> entry : deathMargins.entrySet()) {
            Integer key = entry.getKey();
            Set<Person> value = entry.getValue();
            if (key <= person.deathYear && !value.contains(person)) {
                value.add(person);
            }
        }
        if (!birthMargins.containsKey(person.birthYear)) {
            Set<Person> set = new HashSet<>();
            set.add(person);
            birthMargins.put(person.birthYear, set);
            for (Person person1 : persons) {
                addPerson(person1, birthMargins, deathMargins, persons);
            }
        }
        if (!deathMargins.containsKey(person.deathYear)) {
            Set<Person> set = new HashSet<>();
            set.add(person);
            deathMargins.put(person.deathYear, set);
            for (Person person1 : persons) {
                addPerson(person1, birthMargins, deathMargins, persons);
            }
        }
    }

    LifeDeathRange yearsMostPeopleAlive2(Person[] input) {
        TreeSet<Integer> birthYears = new TreeSet<>();
        for (Person person : input) {
            birthYears.add(person.birthYear);
        }

        int maxPersonCount = 0;
        int year = 0;
        List<Person> maxPeople = new LinkedList<>();

        for (Integer iterationYear : birthYears) {
            int personCount = 0;
            List<Person> people = new LinkedList<>();
            for (Person person : input) {
                if (person.isAlive(iterationYear)) {
                    personCount++;
                    people.add(person);
                }
            }
            if (personCount > maxPersonCount) {
                year = iterationYear;
                maxPersonCount = personCount;
                maxPeople = people;
            }
        }
        LifeDeathRange range = new LifeDeathRange(year, 0);
        range.count = maxPersonCount;
        range.mPersonList = maxPeople;
        return range;
    }

    class Person {
        final int birthYear;
        final int deathYear;

        Person(int birthYear, int deathYear) {
            this.birthYear = birthYear;
            this.deathYear = deathYear;
        }

        @Override
        public String toString() {
            return "\nPerson{" +
                    "birthYear=" + birthYear +
                    ", deathYear=" + deathYear +
                    '}';
        }

        boolean isAlive(int year) {
            return year >= birthYear && year < deathYear;
        }
    }

    class LifeDeathRange implements Comparable<LifeDeathRange> {
        final int birthYear;
        final int deathYear;
        int count = 0;
        List<Person> mPersonList = new LinkedList<>();

        LifeDeathRange(int birthYear, int deathYear) {
            this.birthYear = birthYear;
            this.deathYear = deathYear;
        }

        @Override
        public int compareTo(@NonNull LifeDeathRange o) {
            return Integer.valueOf(this.count).compareTo(o.count);
        }

        @Override
        public String toString() {
            return "\nLifeDeathRange{" +
                    "\nbirthYear=" + birthYear +
                    "\n, deathYear=" + deathYear +
                    "\n, count=" + count +
                    "\n, mPersonList=" + mPersonList +
                    '}';
        }
    }
}

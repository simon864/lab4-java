package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class Library {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Type libraryType = new TypeToken<List<Visitor>>() {}.getType();
        List<Visitor> visitors = gson.fromJson(new FileReader("books.json"), libraryType);

        // Задание 1
        System.out.println("Список посетителей:");
        visitors.forEach(visitor -> System.out.println(visitor.getName() + " " + visitor.getSurname()));
        System.out.println("Количество посетителей: " + visitors.size());

        // Задание 2
        Set<Book> uniqueBooks = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("\nСписок книг в избранном:");
        uniqueBooks.forEach(book -> System.out.println(book.getName() + " - " + book.getAuthor()));
        System.out.println("Количество книг: " + uniqueBooks.size());

        // Задание 3
        List<Book> sortedBooks = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .collect(Collectors.toList());
        System.out.println("\nСписок книг, отсортированный по году издания:");
        sortedBooks.forEach(book -> System.out.println(book.getName() + " (" + book.getPublishingYear() + ")"));

        // Задание 4
        boolean containsAusten = visitors.stream()
                .anyMatch(visitor -> visitor.getFavoriteBooks().stream()
                        .anyMatch(book -> book.getAuthor().equals("Jane Austen")));
        System.out.println("\nЕсть ли у кого-то в избранном книга автора \"Jane Austen\": " + containsAusten);

        // Задание 5
        Optional<Integer> maxFavoriteBooks = visitors.stream()
                .map(visitor -> visitor.getFavoriteBooks().size())
                .max(Comparator.comparingInt(Integer::intValue));
        System.out.println("\nМаксимальное число добавленных в избранное книг: " + maxFavoriteBooks.orElse(0));

        // Задание 6
        double averageFavoriteBooks = visitors.stream()
                .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                .average()
                .orElse(0);

        Map<String, List<Visitor>> groupedVisitors = visitors.stream()
                .filter(Visitor::isSubscribed)
                .collect(Collectors.groupingBy(visitor -> {
                    int favoriteBookCount = visitor.getFavoriteBooks().size();
                    if (favoriteBookCount > averageFavoriteBooks) {
                        return "bookworm";
                    } else if (favoriteBookCount < averageFavoriteBooks) {
                        return "read more";
                    } else {
                        return "fine";
                    }
                }));

        groupedVisitors.forEach((category, visitorsInCategory) -> {
            System.out.println("\nКатегория: " + category);
            visitorsInCategory.forEach(visitor -> {
                System.out.println("SMS для " + visitor.getName() + " " + visitor.getSurname() + ": " +
                        new Sms(visitor.getPhone(), category));
            });
        });
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Book {
    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;
}

@Data
@AllArgsConstructor
class Sms {
    private String phone;
    private String message;
}
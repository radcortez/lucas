package com.radcortez.personal;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.basistech.util.LanguageCode.ENGLISH;

/**
 * @author Roberto Cortez
 */
public class Names {
    public static void main(final String[] args) throws Exception {
        try (final Stream<String> names = Files.lines(Paths.get("src/main/resources/names.csv"))) {
            final List<String> filteredNames =
                    names.map(Name::valueOf)
                         .filter(Name::isAllowed)
                         .filter(Name::isNotFemale)
                         .filter(Name::withoutSpecialChars)
                         .filter(n -> n.between(3, 7))
                         .filter(n -> n.notStarstWith("N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"))
                         .map(Name::getName)
                         .filter(n -> NameUtils.isInLanguage(n, "english"))
                         .filter(n -> NameUtils.isInLanguage(n, "spanish"))
                         .filter(n -> NameUtils.soundsLikeLanguage(n, "english"))
                         .filter(n -> NameUtils.soundsLikeLanguage(n, "spanish"))
                         .collect(Collectors.toList());

            // Could use a parallel stream here, but API's have limits
            filteredNames.stream()
                         .filter(name -> NameUtils.sameTranslationAsLanguage(name, ENGLISH))
                         //.filter(name -> NameUtils.sameTranslationAsLanguage(name, SPANISH)) // They don't support Spanish
                         .filter(name -> NameUtils.existsInLanguages(name, "por", "eng", "spa"))
                         .forEach(System.out::println);
        }
    }
}

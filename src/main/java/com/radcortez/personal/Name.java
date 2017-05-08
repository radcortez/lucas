package com.radcortez.personal;

import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.endsWithAny;
import static org.apache.commons.lang3.StringUtils.startsWithAny;

/**
 * @author Roberto Cortez
 */
public class Name {
    private String name;
    private Gender gender;
    private boolean allowed;

    private Name(final String name, final Gender gender, final boolean allowed) {
        this.name = name;
        this.gender = gender;
        this.allowed = allowed;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public boolean isNotFemale() {
        return gender.equals(Gender.MALE) || (!name.endsWith("a") && !name.endsWith("i") && !gender.equals(
                Gender.FEMALE));
    }

    public boolean withoutSpecialChars() {
        return name.matches("[a-zA-Z]*");
    }

    public boolean between(final int min, final int max) {
        return name.length() > min && name.length() < max;
    }

    public boolean notStarstWith(final String... start) {
        return !startsWithAny(name.toLowerCase().toLowerCase(), stream(start).map(String::toLowerCase).toArray(String[]::new));
    }

    public boolean notEndsWith(final String... end) {
        return !endsWithAny(name.toLowerCase(), stream(end).map(String::toLowerCase).toArray(String[]::new));
    }

    static Name valueOf(final String value) {
        final String[] split = value.split(" ");
        if (split.length == 2) {
            return new Name(split[0], Gender.UNKNOWN, split[1].startsWith("S"));
        } else if (split.length == 3) {
            return new Name(split[0], Gender.of(split[1]), split[2].startsWith("S"));
        } else {
            return new Name("Unknown", Gender.UNKNOWN, false);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Name name1 = (Name) o;

        return allowed == name1.allowed && name.equals(name1.name) && gender == name1.gender;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (allowed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Name{" + "name='" + name + '\'' +
               ", gender=" + gender +
               ", allowed=" + allowed +
               '}';
    }

    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN;

        public static Gender of(final String value) {
            return Stream.of(Gender.values()).filter(g -> g.name().startsWith(value)).findFirst().orElse(UNKNOWN);
        }
    }
}

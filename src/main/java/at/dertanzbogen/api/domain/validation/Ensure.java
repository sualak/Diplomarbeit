package at.dertanzbogen.api.domain.validation;

import at.dertanzbogen.api.domain.main.Course.Booked;
import at.dertanzbogen.api.domain.main.Course.Course;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

@Getter
public abstract class Ensure {


    //constants
    private static final int maxListSize = 100;
    private static final int minListSize = 60;
    private static final int maxAnswerListSize = 8;
    private static final int zero = 0;
    private static final int one = 1;
    private static final int two = 2;
    private static final int string255Min = 1;
    private static final int string255Max = 255;
    private static final int passwordMin = 8;
    private static final int passwordMax = 20;
    private static final int string512Min = 1;
    private static final int string512Max = 512;
    private static final int drinkPriceMin = 0;
    private static final int drinkPriceMax = 10;
    private static final int coursePriceMin = 0;
    private static final int coursePriceMax = 200;
    //dynamisch
    private static final int maxUserMax = 300;
    private static final int maxUserMin = 0;

    //generelle ensurer
    private static void isInRange(int zuTesten, int min, int max, String attribut) {
        if (zuTesten < min || zuTesten > max)
            throw new IllegalArgumentException(format("%s muss zwischen %d und %d sein.", attribut, min, max));
    }

    private static void isSmallerThen(int zuTesten, int max, String attribut) {
        if (zuTesten > max)
            throw new IllegalArgumentException(format("%s muss kleiner sein als %d.", attribut, max));
    }

    private static <T> void isNotNull(T zuTesten, String attribut) {
        Objects.requireNonNull(zuTesten, format("%s darf nicht null sein", attribut));
    }

    private static void isAlphabetic(String zuTesten, String attribut) {
        if (!zuTesten.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException(format("%s muss aus dem Alphabet bestehen", attribut));
    }

    private static void isInTheFuture(Instant zuTesten, String attribut) {
        if (zuTesten.compareTo(Instant.now()) <= 0)
            throw new IllegalArgumentException(format("%s muss in der Zukunft sein", attribut));
    }

    private static void isAfter(Instant zuTesten, Instant startsAt, String attribut) {
        if (zuTesten.compareTo(startsAt) <= 0)
            throw new IllegalArgumentException(format("%s muss in der Zukunft sein", attribut));
    }

    private static void isInRange(double zuTesten, double min, double max, String attribut) {
        if (zuTesten < min || zuTesten > max)
            throw new IllegalArgumentException(format("%s muss zwischen %.2f und %.2f sein.", attribut, min, max));
    }

    private static void isNotBlank(String zuTesten, String attribut) {
        if (zuTesten.isBlank())
            throw new IllegalArgumentException(format("%s darf nicht leer sein", attribut));
    }

    private static <T> boolean equals(T zuTesten, T fix) {
        return zuTesten.equals(fix);
    }

    private static <T> boolean isContainedList(T zuTesten, List<T> list) {
        return list.contains(zuTesten);
    }

    private static <T> boolean isContainedSet(T zuTesten, Set<T> set) {
        return set.contains(zuTesten);
    }

    private static <T> boolean isContainedMap(T zuTesten, Map<T, T> map) {
        return map.containsKey(zuTesten);
    }

    public static boolean isValidEmail(String email) {
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }
    public static int ensureIntRange(int wert, int min, int max, String attribut) {
        isInRange(wert, min, max, attribut);

        return wert;
    }

    public static double ensureDoubleRange(double wert, double min, double max, String attribut) {
        isInRange(wert, min, max, attribut);

        return wert;
    }

    public static int isPositiv(int zuTesten, String attribut) {
        if (zuTesten < 0)
            throw new IllegalArgumentException(format("%s muss positiv sein", attribut));
        return zuTesten;
    }

    public static int isNegativ(int zuTesten, String attribut) {
        if (zuTesten > 0)
            throw new IllegalArgumentException(format("%s muss negativ sein", attribut));
        return zuTesten;
    }

    public static int isPositivNoZero(int zuTesten, String attribut) {
        if (zuTesten <= 0)
            throw new IllegalArgumentException(format("%s muss größer 0 sein", attribut));
        return zuTesten;
    }

    public static int isNegativNoZero(int zuTesten, String attribut) {
        if (zuTesten >= 0)
            throw new IllegalArgumentException(format("%s muss kleiner 0 sein", attribut));
        return zuTesten;
    }

    //spezielle ensurer
    public static String ensureStringValid(String wert, String attribut) {
        isNotNull(wert, attribut);
        isNotBlank(wert, attribut);
        isAlphabetic(wert, attribut);

        return wert;
    }

    public static String ensureNonNullValid(String wert, String attribut) {
        isNotNull(wert, attribut);

        return wert;
    }

    public static String ensureNonNullNonBlankValid(String wert, String attribut) {
        isNotNull(wert, attribut);
        isNotBlank(wert, attribut);

        return wert;
    }

    //MediaSection

    public static void ensurePathValid(String path) {
        if (!path.matches("^(?:[a-z]:)?[\\\\]{0,2}(?:[.\\\\ ](?![.\\\\\\n])|[^<>:\"|?*.\\\\ \\n])+$")) {
            throw new IllegalArgumentException("Der Pfad ist ungültig!");
        }
    }

    //------------------------------User ENSURERS------------------------------------------------
    public static String ensureValidMail(String email) {
        //https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
        //if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
        if(!isValidEmail(email))
            throw new IllegalArgumentException(email + " ist keine valide Email");
        return email;
    }

    public static String ensureValidPassword(String password) {
        //Minimum eight characters, at least one letter and one number
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$"))
            throw new IllegalArgumentException(password + " ist kein valides Passwort");
        return password;
    }

    public static int removeCountedCourseValid(int amount, int toRemove, String attribute) {
        isPositiv(amount - toRemove, attribute);
        return amount;
    }

    //---------------------------Personal ENSURERS---------------------------------------------

    public static String ensureValidName(String name) {
        if (!name.matches("^[a-zäüöA-ZÄÜÖ]+(([',. -][a-zäüöA-ZÄÜÖ ])?[a-zäüöA-ZÄÜÖ]*)*$"))
            throw new IllegalArgumentException(name + " ist kein valider Name");
        if (name.length() <= 1)
            throw new IllegalArgumentException(name + " ist kein valider Name");

        transformToValidName(name);
        return name;
    }

    public static String transformToValidName(String name) {
        name = name.trim().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    //---------------------------Notification ENSURERS---------------------------------------------
    public static String ensureNotificationContentValid(String content, String attribute) {
        ensureNonNullNonBlankValid(content, attribute);
        ensureIntRange(content.length(), string255Min, string255Max, attribute);
        return content;
    }

    public static String ensureAcceptedByValid(String creator, int maxAccepted, int currentAccepted) {
        isSmallerThen(currentAccepted, maxAccepted - one, "Accepted by");
        return creator;
    }

    public static int acceptAmountValid(int acceptAmount, String attribute) {
        isPositiv(acceptAmount, attribute);

        return acceptAmount;
    }

    //------------------------------Partner ENSURERS------------------------------------------------
    public static String addPartnerValid(String self, String user) {
        if (equals(user, self))
            throw new IllegalArgumentException("Sich selbst als Partner zu adden ist nicht erlaubt");

        return user;
    }

    public static String sendPartnerRequestValid(String self, String user, Set<String> partners) {
        if (isContainedSet(addPartnerValid(self, user), partners))
            throw new IllegalArgumentException("Angefragter User ist bereits ein Partner");

        return user;
    }


//isInstance and isAssignableFrom(Class.User)


//------------------------------Answer ENSURERS------------------------------------------------
    public static String ensureCreatorValid(String creator, String attribute) {
        ensureNonNullNonBlankValid(creator, attribute);
        return creator;
    }

    public static String ensureAnswerValid(String answer) {
        ensureNonNullNonBlankValid(answer, "Answer");
        isInRange(answer.length(), two, string512Max, "Answer");

        return answer;
    }

    //------------------------------ForumEntry ENSURERS------------------------------------------------
    public static String ensureTitelValid(String titel) {
        ensureNonNullNonBlankValid(titel, "Titel");
        isInRange(titel.length(), two, string255Max, "Titel");

        return titel;
    }

    public static String ensureQuestionValid(String question) {
        ensureNonNullNonBlankValid(question, "Question");
        isInRange(question.length(), two, string512Max, "Question");

        return question;
    }


    //------------------------------Drinks ENSURERS------------------------------------------------
    public static BigDecimal drinkPriceValid(BigDecimal price, String attribute) {
        isInRange(price.doubleValue(), drinkPriceMin, drinkPriceMax, attribute);
        return price;
    }

    public static int removeDrinkValid(int amount, int toRemove, String attribute) {
        isPositiv(amount - toRemove, attribute);
        return amount;
    }

    //------------------------------Course ENSURERS------------------------------------------------
    public static BigDecimal coursePriceValid(BigDecimal price, String attribute) {
        isInRange(price.doubleValue(), coursePriceMin, coursePriceMax, attribute);
        return price;
    }

    public static String addUserValid(String user, int maxAmount, String attribute, Set<Booked> booked, int maxFollowerLeaderDifference){
        AtomicInteger current = new AtomicInteger();
        AtomicInteger other = new AtomicInteger();
        booked.forEach(value -> {
            if (value.isLeader())
                current.getAndIncrement();
            else
                other.getAndIncrement();
        });
        isSmallerThen(current.get(), maxAmount, attribute);
        if(Math.abs(current.get() - other.get()) > maxFollowerLeaderDifference)
            throw new IllegalArgumentException("Die Anzahl der User ist nicht gleich");
        return user;
    }

    public static String addUserPairValid(String user, int maxAmount, String attribute, Set<Booked> booked){
        AtomicInteger current = new AtomicInteger();
        booked.forEach(value -> {
            if (value.isLeader())
                current.getAndIncrement();
        });
        isSmallerThen(current.get(), maxAmount, attribute);
        return user;
    }

    public static int maxUserValid(int maxUser, String attribute) {
        isInRange(maxUser, maxUserMin, maxUserMax, attribute);
        return maxUser;
    }

    //------------------------------Answer ENSURERS------------------------------------------------
    public static Instant startsAtValid(Instant startsAt) {
        isInTheFuture(startsAt, "Start");
        return startsAt;
    }

    public static Instant endsAtValid(Instant startsAt, Instant endsAt) {
        isInTheFuture(startsAt, "End");
        return startsAt;
    }

    public static Instant ensureSendDateValid(Instant sendDate) {
        isInTheFuture(sendDate, "Send Date");
        return sendDate;
    }
}
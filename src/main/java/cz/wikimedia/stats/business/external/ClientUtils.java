package cz.wikimedia.stats.business.external;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class ClientUtils {
    public static final int WM_API_LIMIT = 50;
    public static String collectNames(Collection<String> usernames) {
        if (usernames.isEmpty()) return "";

        Iterator<String> iter = usernames.iterator();
        StringBuilder res = new StringBuilder(iter.next());
        while (iter.hasNext())
            res.append('|')
                    .append(iter.next());
        return res.toString();
    }

    public static String collect(Collection<?> ids) {
        return collectNames(ids.stream().map(Object::toString).toList());
    }

    public static <T, U> Collection<T> applyWithLimit(Collection<U> elems, Function<Collection<U>, Collection<T>> func, int limit) {
        Collection<T> result = new HashSet<>();

        while (elems.size() > 0) {
            elems = elems.stream().filter(Objects::nonNull).toList();
            result.addAll(
                    func.apply(
                            elems.stream().limit(limit).toList()));

            elems = elems
                    .stream()
                    .skip(limit)
                    .toList();
        }
        return result;
    }

    public static <T, U> Collection<T> applyWithLimit(Collection<U> elems, Function<Collection<U>, Collection<T>> func) {
        return applyWithLimit(elems, func, WM_API_LIMIT);
    }
}

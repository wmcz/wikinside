package cz.wikimedia.stats.business.external;

import java.util.Collection;
import java.util.Iterator;

public class ClientUtils {
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
}

package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.business.internal.InternalService;
import cz.wikimedia.stats.model.IdAble;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConverterUtils {
    public static <T extends IdAble<Long>> Collection<Long> getIds(Collection<T> elems) {
        if (elems == null) return new ArrayList<>();

        Collection<Long> ids = new ArrayList<>(elems.size());
        elems.forEach(e -> ids.add(e.getId()));
        return ids;
    }

    public static <T extends IdAble<Long>, S extends InternalService<T, Long>>
    Set<T> getElems(Collection<Long> ids, S service) {
        Set<T> elems = new HashSet<>(ids.size());
        ids.forEach(id -> service.findById(id).ifPresent(elems::add));
        return elems;
    }

    public static <T extends IdAble<Long>, S extends InternalService<T, Long>>
    T getIfNotNull(@Nullable Long id, S service) {
        if (id == null) return null;
        else return service.findById(id).orElse(null);
    }

    public static <T extends IdAble<Long>> Long getIfNotNull(@Nullable T elem) {
        if (elem == null) return null;
        else return elem.getId();
    }

}

package cz.wikimedia.stats.api.dto.converter;

import cz.wikimedia.stats.business.AbstractService;
import cz.wikimedia.stats.model.IdAble;

import java.util.ArrayList;
import java.util.Collection;

public class ConverterUtils {
    public static <T extends IdAble<Long>> Collection<Long> getIds(Collection<T> elems) {
        if (elems == null) return new ArrayList<>();

        Collection<Long> ids = new ArrayList<>(elems.size());
        elems.forEach(e -> ids.add(e.getId()));
        return ids;
    }

    public static <T extends IdAble<Long>, S extends AbstractService<T, Long>>
    Collection<T> getElems(Collection<Long> ids, S service) {
        Collection<T> elems = new ArrayList<>(ids.size());
        ids.forEach(id -> service.findById(id).ifPresent(elems::add));
        return elems;
    }

}

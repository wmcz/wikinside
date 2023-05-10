package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.model.IdAble;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;


public abstract class InternalService<T extends IdAble<ID>, ID> {
    protected final CrudRepository<T, ID> repository;

    public InternalService(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    protected <E extends IdAble<ID>> Collection<E> applyToNonOwningField(T elem,
                                                                         Collection<ID> ids,
                                                                         Function<E, E> updatingFunc,
                                                                         CrudRepository<E, ID> eRepository,
                                                                         CrudRepository<T, ID> tRepository,
                                                                         Function<T, Collection<E>> fieldAccessFunc) {
        ids.forEach(
                id -> eRepository
                        .findById(id)
                        .ifPresent(e -> eRepository.save(updatingFunc.apply(e))));
        return tRepository
                .findById(elem.getId())
                .map(fieldAccessFunc)
                .orElseThrow(NoSuchElementException::new);
    }

    protected <E extends IdAble<ID>> void updateNonOwningField(T elem,
                                                               Collection<E> updated,
                                                               Function<T, Collection<E>> fieldAccessFunc,
                                                               BiFunction<T, Collection<ID>, ?> addingFunc,
                                                               BiFunction<T, Collection<ID>, ?> removingFunc) {

        Collection<E> current = fieldAccessFunc.apply(elem);

        Collection<E> added = new HashSet<>(updated);
        Collection<E> removed = new HashSet<>(current);

        added.removeAll(current);
        removed.removeAll(updated);

        addingFunc.apply(elem, toIds(added));
        removingFunc.apply(elem, toIds(removed));
    }

    protected <E extends IdAble<ID>> Collection<ID> toIds(Collection<E> elems) {
        return elems.stream().map(E::getId).toList();
    }
    public long count() {
        return repository.count();
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public Iterable<T> findAll() { return repository.findAll(); }

    private <S extends T> S save(S entity) {
        return repository.save(entity);
    }

    public  <S extends T> Optional<S> create(S entity) {
        if (entity.getId() != null && repository.existsById(entity.getId()))
             return Optional.empty();
        else {
            try {
                return Optional.of(repository.save(entity));
            } catch (DataIntegrityViolationException ignored) {
                return Optional.empty();
            }
        }
    }

    public <S extends T> Optional<S> update(S entity) {
        if (entity.getId() != null && repository.existsById(entity.getId()))
             return Optional.of(repository.save(entity));
        else return Optional.empty();
    }



}

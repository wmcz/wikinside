package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.model.IdAble;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public abstract class AbstractService<T extends IdAble<ID>, ID> {
    protected final CrudRepository<T, ID> repository;

    public AbstractService(CrudRepository<T, ID> repository) {
        this.repository = repository;
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
        else return Optional.of(repository.save(entity));
    }

    public <S extends T> Optional<S> update(S entity) {
        if (entity.getId() != null && repository.existsById(entity.getId()))
             return Optional.of(repository.save(entity));
        else return Optional.empty();
    }



}

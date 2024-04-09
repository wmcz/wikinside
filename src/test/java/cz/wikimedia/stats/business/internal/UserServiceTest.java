package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.api.client.dto.GlobalUserInfo;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.RevisionRepository;
import cz.wikimedia.stats.dao.UserRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository repository;

    @MockBean
    EventRepository eventRepository;

    @MockBean
    WmUserService wmUserService;

    @MockBean
    RevisionRepository revisionRepository;

    UserService service;

    @BeforeEach
    void setUp() {
        Event event1 = new Event(1001L, null, null, null, "one", null, null, new HashSet<>(), null, null, null);
        Event event2 = new Event(1002L, null, null, null, "two", null, null, new HashSet<>(), null, null, null);

        User user1 = new User(1L, 1L, "one",   Instant.EPOCH, new HashSet<>(), new HashSet<>());
        User user2 = new User(2L, 2L, "two",   Instant.EPOCH, new HashSet<>(), new HashSet<>());
        User user3 = new User(3L, 3L, "three", Instant.EPOCH, new HashSet<>(), new HashSet<>(List.of(event1, event2)));
        User user4 = new User(4L, 4L, "four",  Instant.EPOCH, new HashSet<>(), new HashSet<>());
        User user5 = new User(5L, 5L, "five",  Instant.EPOCH, new HashSet<>(), new HashSet<>());

        Mockito.when(wmUserService.getGlobalUserInfo("one")).thenReturn(new GlobalUserInfo("home", 1L, Instant.EPOCH, "one"));
        Mockito.when(wmUserService.getGlobalUserInfo("two")).thenReturn(new GlobalUserInfo("home", 2L, Instant.EPOCH, "two"));
        Mockito.when(wmUserService.getGlobalUserInfo("three")).thenReturn(new GlobalUserInfo("home", 3L, Instant.EPOCH, "three"));
        Mockito.when(wmUserService.getGlobalUserInfo("four")).thenReturn(new GlobalUserInfo("home", 4L, Instant.EPOCH, "four"));
        Mockito.when(wmUserService.getGlobalUserInfo("five")).thenReturn(new GlobalUserInfo("home", 5L, Instant.EPOCH, "five"));
        Mockito.when(wmUserService.getGlobalUserInfo("six")).thenReturn(null);


        Mockito.when(wmUserService.updateNames(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.when(wmUserService.getLocalId("one")).thenReturn(1L);
        Mockito.when(wmUserService.getLocalId("two")).thenReturn(2L);
        Mockito.when(wmUserService.getLocalId("three")).thenReturn(3L);
        Mockito.when(wmUserService.getLocalId("four")).thenReturn(4L);
        Mockito.when(wmUserService.getLocalId("five")).thenReturn(5L);


        Mockito.when(eventRepository.existsById(ArgumentMatchers.any())).thenReturn(true);
        Mockito.when(eventRepository.findById(1001L)).thenReturn(Optional.of(event1));
        Mockito.when(eventRepository.findById(1002L)).thenReturn(Optional.of(event2));

        Mockito.when(eventRepository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Mockito.doNothing().when(revisionRepository).deleteById(ArgumentMatchers.any());
        Mockito.doNothing().when(repository).deleteById(ArgumentMatchers.any());


        Mockito.when(repository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.when(repository.findByUsername("one")).thenReturn(Optional.of(user1));
        Mockito.when(repository.findByUsername("two")).thenReturn(Optional.of(user2));
        Mockito.when(repository.findByUsername("three")).thenReturn(Optional.of(user3));
        Mockito.when(repository.findByUsername("four")).thenReturn(Optional.of(user4));
        Mockito.when(repository.findByUsername("five")).thenReturn(Optional.empty());
        Mockito.when(repository.findByUsername("six")).thenReturn(Optional.empty());

        Mockito.when(repository.existsById(AdditionalMatchers.lt(5L))).thenReturn(true);
        Mockito.when(repository.existsById(5L)).thenReturn(false).thenReturn(true);
        Mockito.when(repository.existsById(AdditionalMatchers.gt(5L))).thenReturn(false);


        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(user1));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(user2));
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(user3));
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(user4));
        Mockito.when(repository.findById(5L)).thenReturn(Optional.of(user5));



        service = new UserService(repository, eventRepository, wmUserService, revisionRepository);
    }
    @Test
    void createFromGlobalUser() {
        Mockito.when(repository.existsById(1L)).thenReturn(false).thenReturn(true);
        Mockito.when(repository.existsById(2L)).thenReturn(true);

        Assertions.assertTrue(service.createFromGlobalUser(new User(null, "one")).isPresent());
        Assertions.assertTrue(service.createFromGlobalUser(new User(null, "two")).isEmpty());

        Mockito.verify(wmUserService, Mockito.atLeastOnce()).getGlobalUserInfo("one");
        Mockito.verify(wmUserService, Mockito.atLeastOnce()).getGlobalUserInfo("two");
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new User(1L, "one"));
        Mockito.verify(repository, Mockito.never()).save(new User(2L, "two"));
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        Mockito.verifyNoMoreInteractions(eventRepository);

        service.deleteById(3L);
        Mockito.verify(eventRepository, Mockito.atLeast(2)).save(ArgumentMatchers.any());

    }

    @Test
    void create() {
        Mockito.when(repository.existsById(6L)).thenReturn(false).thenReturn(true);
        Mockito.when(repository.findById(6L)).thenReturn(Optional.of(new User(6L, "six")));

        Assertions.assertTrue(service.create(new User(1L, "one")).isEmpty());

        Mockito.verify(repository, Mockito.never()).save(new User(1L, "one"));

        Assertions.assertTrue(service.create(new User(5L, "five")).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new User(5L, "five"));

        Assertions.assertTrue(service.create(new User(6L, 6L, "six",  Instant.EPOCH, new HashSet<>(), new HashSet<>(List.of(new Event(1001L, null, null, null, null, null, null, new HashSet<>(), null, null, null))))).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new User(6L, "six"));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1001L, null, null, null, null, null, null, null, null, null, null));
    }

    @Test
    void update() {
        Assertions.assertTrue(service.update(new User(5L, "five")).isEmpty());
        Mockito.verify(repository, Mockito.never()).save(new User(5L, "five"));

        Assertions.assertTrue(service.update(new User(1L, "one")).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new User(1L, "one"));

        Assertions.assertTrue(service.update(new User(3L, 3L, "six",  Instant.EPOCH, new HashSet<>(), new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new User(3L, "three"));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1001L, null, null, null, null, null, null, null, null, null, null));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1002L, null, null, null, null, null, null, null, null, null, null));
    }

    @Test
    void processUser() {
        Assertions.assertNull(service.processUser("127.0.0.1"));
        Assertions.assertNull(service.processUser("0000:0000:0000:0000:0000:0000:0000:0001"));

        Assertions.assertEquals(new User(1L, "one"), service.processUser("one"));

        Mockito.verifyNoInteractions(wmUserService);

        Assertions.assertEquals(new User(5L, "five"), service.processUser("five"));

        Mockito.verify(wmUserService).getGlobalUserInfo("five");
    }
}
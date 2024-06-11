package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class EventServiceTest {

    @MockBean
    EventRepository repository;

    EventService service;

    @Test
    void update() {
        service = new EventService(repository);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Project project1 = new Project(0L, "name1", "path1");
        Project project2 = new Project(1L, "name2", "path2");


        User user1 = new User(1001L, "one");
        User user2 = new User(1002L, "two");
        User user3 = new User(1003L, "three");

        Revision rev0 = new Revision(0L, 0L, 0L, 0L, 0L, user1, new HashSet<>(), project1, Instant.parse("1999-06-01T00:00:00.00Z"), "summary");
        Revision rev1 = new Revision(0L, 1L, 0L, 0L, 0L, user2, new HashSet<>(), project2, Instant.parse("1999-06-01T00:00:00.00Z"), "summary");
        Revision rev2 = new Revision(0L, 2L, 0L, 0L, 0L, user3, new HashSet<>(), project1, Instant.parse("2000-06-01T00:00:00.00Z"), "summary");
        Revision rev3 = new Revision(0L, 3L, 0L, 0L, 0L, user1, new HashSet<>(), project2, Instant.parse("2000-06-01T00:00:00.00Z"), "summary");
        Revision rev4 = new Revision(0L, 4L, 0L, 0L, 0L, user2, new HashSet<>(), project1, Instant.parse("2000-06-01T00:00:00.00Z"), "summary");
        Revision rev5 = new Revision(0L, 5L, 0L, 0L, 0L, user3, new HashSet<>(), project2, Instant.parse("2000-06-01T00:00:00.00Z"), "summary");
        Revision rev6 = new Revision(0L, 6L, 0L, 0L, 0L, user1, new HashSet<>(), project1, Instant.parse("2000-06-01T00:00:00.00Z"), "summary");
        Revision rev7 = new Revision(0L, 7L, 0L, 0L, 0L, user2, new HashSet<>(), project2, Instant.parse("2000-06-01T00:00:00.00Z"), "summary");
        Revision rev8 = new Revision(0L, 8L, 0L, 0L, 0L, user3, new HashSet<>(), project1, Instant.parse("2001-06-01T00:00:00.00Z"), "summary");
        Revision rev9 = new Revision(0L, 9L, 0L, 0L, 0L, user1, new HashSet<>(), project2, Instant.parse("2001-06-01T00:00:00.00Z"), "summary");

        Set<Revision> revs = Set.of(rev0, rev1, rev2, rev3, rev4, rev5, rev6, rev7, rev8, rev9);

        Event event1 = new Event(1L, null, null, "one", Event.DataCollectionStrategy.MANUAL,  null, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 12, 31), new HashSet<>(Set.of(user1, user2)), new HashSet<>(Set.of(project1)), new HashSet<>(revs), new HashSet<>());
        Event event2 = new Event(2L, null, null, "one", Event.DataCollectionStrategy.MANUAL,  null, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 12, 31), new HashSet<>(Set.of(user2, user3)), new HashSet<>(Set.of(project2)), new HashSet<>(revs), new HashSet<>());
        Event event3 = new Event(3L, null, null, "one", Event.DataCollectionStrategy.MANUAL,  null, LocalDate.of(1999, 1, 1), LocalDate.of(2001, 12, 31), new HashSet<>(Set.of(user1, user2, user3)), new HashSet<>(Set.of(project1, project2)), new HashSet<>(revs), new HashSet<>());
        Event event4 = new Event(4L, null, null, "one", Event.DataCollectionStrategy.HASHTAG, "##", LocalDate.of(1999, 1, 1), LocalDate.of(1999, 12, 31), new HashSet<>(Set.of(user1, user2, user3)), new HashSet<>(Set.of(project1)), new HashSet<>(revs), new HashSet<>());

        Mockito.when(repository.existsById(ArgumentMatchers.any())).thenReturn(true);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(event1));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(event2));
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(event3));
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(event4));


        event1 = service.update(event1).get();
        event2 = service.update(event2).get();
        event3 = service.update(event3).get();
        event4 = service.update(event4).get();

        //event1 contains only rev4 and rev6, etc. etc.
        Assertions.assertTrue(event1.getRevisions().containsAll(List.of(rev4, rev6)));
        Assertions.assertFalse(event1.getRevisions().retainAll(List.of(rev4, rev6)));

        Assertions.assertTrue(event2.getRevisions().containsAll(List.of(rev5, rev7)));
        Assertions.assertFalse(event2.getRevisions().retainAll(List.of(rev5, rev7)));

        Assertions.assertTrue(event3.getRevisions().containsAll(revs));

        Assertions.assertTrue(event4.getRevisions().contains(rev0));
        Assertions.assertFalse(event4.getRevisions().retainAll(List.of(rev0)));

        Assertions.assertTrue(event4.getParticipants().contains(user1));
        Assertions.assertFalse(event4.getParticipants().retainAll(List.of(user1)));

    }
}
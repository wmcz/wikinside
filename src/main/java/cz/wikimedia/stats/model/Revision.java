package cz.wikimedia.stats.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Revision {
    @Id
    @GeneratedValue
    private Long id;

    private Long revId; // is not unique across projects

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Project project;
}

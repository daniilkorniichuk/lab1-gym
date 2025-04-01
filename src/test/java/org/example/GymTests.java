package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GymTests {
    private Gym gym;
    private Visitor visitor1, visitor2;
    private Trainer trainer;
    private Membership membershipActive, membershipExpired;

    @BeforeEach
    void setUp() {
        gym = new Gym();
        visitor1 = new Visitor("1", "Іван", "Петренко", "123456789");
        visitor2 = new Visitor("2", "Марія", "Іванова", "987654321");
        trainer = new Trainer("T1", "Олександр");

        membershipActive = new Membership("1", LocalDate.now().minusDays(10), LocalDate.now().plusDays(10));
        membershipExpired = new Membership("2", LocalDate.now().minusDays(30), LocalDate.now().minusDays(5));
    }

    @Test
    void registerVisitorTest() {
        gym.registerVisitor(visitor1);
        assertEquals(1, gym.getVisitors().size());
        assertNotNull(gym.getVisitor("1"));
    }

    @Test
    void updateVisitorTest() {
        gym.registerVisitor(visitor1);
        assertTrue(gym.updateVisitor("1", "Олег", "Петров", "111222333"));
        assertEquals("Олег", gym.getVisitor("1").getName());
    }

    @Test
    void deleteVisitorTest() {
        gym.registerVisitor(visitor1);
        assertTrue(gym.deleteVisitor("1"));
        assertNull(gym.getVisitor("1"));
    }

    @Test
    void enrollToTrainerTest() {
        gym.registerVisitor(visitor1);
        gym.addTrainer(trainer);
        assertTrue(gym.enrollToTrainer("1", "T1"));
        assertTrue(trainer.isAssignedTo(visitor1));
    }

    @Test
    void markVisitTest() {
        gym.registerVisitor(visitor1);
        gym.assignMembership(membershipActive);
        assertTrue(gym.markVisit("1"));
        assertEquals(1, gym.getVisitHistory("1").size());
    }

    @Test
    void markVisitWithoutActiveMembershipTest() {
        gym.registerVisitor(visitor2);
        gym.assignMembership(membershipExpired);
        assertFalse(gym.markVisit("2"));
    }

    @Test
    void isActiveMembershipTest() {
        assertTrue(membershipActive.isActive());
    }

    @Test
    void isNotActiveMembershipTest() {
        assertFalse(membershipExpired.isActive());
    }

    @Test
    void trainerAssignVisitorTest() {
        trainer.assignVisitor(visitor1);
        assertTrue(trainer.isAssignedTo(visitor1));
    }
}

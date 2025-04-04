package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class GymTests {
    private Gym gym;
    private Visitor visitor;
    private Trainer trainer;
    private Membership activeMembership;
    private Membership expiredMembership;

    @BeforeEach
    public void setUp() {
        gym = new Gym();
        visitor = new Visitor("v1", "Іван", "Іванов", "123456789");
        trainer = new Trainer("t1", "Олександр");
        activeMembership = new Membership("v1", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
        expiredMembership = new Membership("v1", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        gym.registerVisitor(visitor);
        gym.addTrainer(trainer);
    }

    // Тест 1: реєстрація відвідувача
    @Test
    public void testRegisterVisitor() {
        gym.registerVisitor(new Visitor("v2", "Петро", "Петров", "987654321"));
        assertNotNull(gym.getVisitor("v2"));
    }

    // Тест 2: спроба реєстрації того самого відвідувача двічі
    @Test
    public void testRegisterVisitorTwice() {
        gym.registerVisitor(visitor);
        gym.registerVisitor(visitor);  // не має бути додано вдруге
        assertEquals(1, gym.getVisitors().size());
    }

    // Тест 3: оновлення даних відвідувача
    @Test
    public void testUpdateVisitor() {
        assertTrue(gym.updateVisitor("v1", "Іван", "Іванов", "111111111"));
        assertEquals("Іван", gym.getVisitor("v1").getName());
    }

    // Тест 4: оновлення даних неіснуючого відвідувача
    @Test
    public void testUpdateVisitorNotFound() {
        assertFalse(gym.updateVisitor("v2", "Петро", "Петров", "987654321"));
    }

    // Тест 5: видалення відвідувача
    @Test
    public void testDeleteVisitor() {
        gym.deleteVisitor("v1");
        assertNull(gym.getVisitor("v1"));
    }

    // Тест 6: спроба видалення неіснуючого відвідувача
    @Test
    public void testDeleteVisitorNotFound() {
        assertFalse(gym.deleteVisitor("v2"));
    }

    // Тест 7: маркування відвідування для активного абонемента
    @Test
    public void testMarkVisitActiveMembership() {
        gym.assignMembership(activeMembership);
        assertTrue(gym.markVisit("v1"));
        assertEquals(1, gym.getVisitHistory("v1").size());
    }

    // Тест 8: маркування відвідування для неактивного абонемента
    @Test
    public void testMarkVisitExpiredMembership() {
        gym.assignMembership(expiredMembership);
        assertFalse(gym.markVisit("v1"));
        assertEquals(0, gym.getVisitHistory("v1").size());
    }

    // Тест 9: маркування відвідування для неіснуючого відвідувача
    @Test
    public void testMarkVisitNonExistentVisitor() {
        assertFalse(gym.markVisit("v2"));
    }

    // Тест 10: реєстрація тренера та призначення відвідувача
    @Test
    public void testAssignTrainerToVisitor() {
        gym.assignMembership(activeMembership);
        assertTrue(gym.enrollToTrainer("v1", "t1"));
        assertTrue(trainer.isAssignedTo(visitor));
    }

    // Тест 11: спроба призначити тренера неіснуючому відвідувачу
    @Test
    public void testAssignTrainerToNonExistentVisitor() {
        assertFalse(gym.enrollToTrainer("v2", "t1"));
    }

    // Тест 12: спроба призначити неіснуючого тренера
    @Test
    public void testAssignNonExistentTrainerToVisitor() {
        gym.assignMembership(activeMembership);
        assertFalse(gym.enrollToTrainer("v1", "t2"));
    }
}

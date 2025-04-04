package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GymTests {
    private Gym gym;
    private Visitor visitor;
    private Trainer trainer;
    private Membership activeMembership;
    private Membership expiredMembership;

    @BeforeEach
    public void setUp() {
        gym = new Gym();
        visitor = new Visitor("v1", "Ivan", "Ivanov", "123456789");
        trainer = new Trainer("t1", "Alexander");
        activeMembership = new Membership("v1", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
        expiredMembership = new Membership("v1", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        gym.registerVisitor(visitor);
        gym.addTrainer(trainer);
    }

    // Test 1: Registering a visitor
    @Test
    public void testRegisterVisitor() {
        gym.registerVisitor(new Visitor("v2", "Petro", "Petrov", "987654321"));
        assertNotNull(gym.getVisitor("v2"));
    }

    // Test 2: Trying to register the same visitor twice
    @Test
    public void testRegisterVisitorTwice() {
        gym.registerVisitor(visitor);
        gym.registerVisitor(visitor);  // Should not be added twice
        assertEquals(1, gym.getVisitors().size());
    }

    // Test 3: Updating visitor data
    @Test
    public void testUpdateVisitor() {
        assertTrue(gym.updateVisitor("v1", "Ivan", "Ivanov", "111111111"));
        assertEquals("Ivan", gym.getVisitor("v1").getName());
    }

    // Test 4: Updating a non-existent visitor
    @Test
    public void testUpdateVisitorNotFound() {
        assertFalse(gym.updateVisitor("v2", "Petro", "Petrov", "987654321"));
    }

    // Test 5: Deleting a visitor
    @Test
    public void testDeleteVisitor() {
        gym.deleteVisitor("v1");
        assertNull(gym.getVisitor("v1"));
    }

    // Test 6: Deleting a non-existent visitor
    @Test
    public void testDeleteVisitorNotFound() {
        assertFalse(gym.deleteVisitor("v2"));
    }

    // Test 7: Marking a visit with active membership
    @Test
    public void testMarkVisitActiveMembership() {
        gym.assignMembership(activeMembership);
        assertTrue(gym.markVisit("v1"));
        assertEquals(1, gym.getVisitHistory("v1").size());
    }

    // Test 8: Marking a visit with expired membership
    @Test
    public void testMarkVisitExpiredMembership() {
        gym.assignMembership(expiredMembership);
        assertFalse(gym.markVisit("v1"));
        assertEquals(0, gym.getVisitHistory("v1").size());
    }

    // Test 9: Marking a visit for a non-existent visitor
    @Test
    public void testMarkVisitNonExistentVisitor() {
        assertFalse(gym.markVisit("v2"));
    }

    // Test 10: Registering trainer and assigning to visitor
    @Test
    public void testAssignTrainerToVisitor() {
        gym.assignMembership(activeMembership);
        assertTrue(gym.enrollToTrainer("v1", "t1"));
        assertTrue(trainer.isAssignedTo(visitor));
    }

    // Test 11: Assigning trainer to non-existent visitor
    @Test
    public void testAssignTrainerToNonExistentVisitor() {
        assertFalse(gym.enrollToTrainer("v2", "t1"));
    }

    // Test 12: Assigning non-existent trainer to visitor
    @Test
    public void testAssignNonExistentTrainerToVisitor() {
        gym.assignMembership(activeMembership);
        assertFalse(gym.enrollToTrainer("v1", "t2"));
    }

    @Test
    public void testExportVisitorsToJson() throws IOException {
        Visitor visitor1 = new Visitor("v1", "Andriy", "Petrenko", "+3800000001");
        Visitor visitor2 = new Visitor("v2", "Bogdan", "Ivanov", "+3800000002");

        Gym testGym = new Gym();
        testGym.registerVisitor(visitor1);
        testGym.registerVisitor(visitor2);

        testGym.exportVisitorsToJson("test_export.json", Comparator.comparing(Visitor::getName));

        File file = new File("test_export.json");
        assertTrue(file.exists());

        // Clean up
        file.delete();
    }

    @Test
    public void testImportVisitorsFromJson() throws IOException {
        String json = """
        [
            {"id":"v10","name":"Oleh","surname":"Olehiv","phoneNumber":"123123123"},
            {"id":"v11","name":"Iryna","surname":"Irynina","phoneNumber":"321321321"}
        ]
        """;
        File file = new File("test_import.json");
        Files.writeString(file.toPath(), json);

        Gym testGym = new Gym();
        testGym.importVisitorsFromJson("test_import.json");
        assertNotNull(testGym.getVisitor("v10"));
        assertNotNull(testGym.getVisitor("v11"));

        file.delete(); // Clean up
    }
}

package org.example;

import java.time.LocalDate;
import java.util.*;

public class Gym {
    private List<Visitor> visitors = new ArrayList<>();
    private Map<String, Membership> memberships = new HashMap<>();
    private List<Trainer> trainers = new ArrayList<>();
    private Map<String, List<LocalDate>> visitHistory = new HashMap<>();

    public void registerVisitor(Visitor visitor) {
        if (!visitors.contains(visitor)) {
            visitors.add(visitor);
            visitHistory.put(visitor.getId(), new ArrayList<>());
        }
    }
    public Visitor getVisitor(String id) {
        return visitors.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
    }
    public boolean updateVisitor(String id, String newName, String newSurname, String newPhoneNumber) {
        Visitor visitor = getVisitor(id);
        if (visitor != null) {
            visitor.setName(newName);
            return true;
        }
        return false;
    }
    public boolean deleteVisitor(String id) {
        return visitors.removeIf(v -> v.getId().equals(id));
    }
    public void addTrainer(Trainer trainer) {
        if (!trainers.contains(trainer)) {
            trainers.add(trainer);
        }
    }
    public Trainer getTrainer(String id) {
        return trainers.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
    public boolean updateTrainer(String id, String newName) {
        Trainer trainer = getTrainer(id);
        if (trainer != null) {
            trainer.setName(newName);
            return true;
        }
        return false;
    }
    public boolean deleteTrainer(String id) {
        return trainers.removeIf(t -> t.getId().equals(id));
    }
    public void assignMembership(Membership membership) {
        memberships.put(membership.getVisitorId(), membership);
    }
    public boolean markVisit(String visitorId) {
        Membership membership = memberships.get(visitorId);
        if (membership != null && membership.isActive()) {
            visitHistory.get(visitorId).add(LocalDate.now());
            return true;
        }
        return false;
    }
    public List<LocalDate> getVisitHistory(String visitorId) {
        return visitHistory.getOrDefault(visitorId, new ArrayList<>());
    }
    public boolean enrollToTrainer(String visitorId, String trainerId) {
        Visitor visitor = getVisitor(visitorId);
        Trainer trainer = getTrainer(trainerId);
        if (visitor != null && trainer != null) {
            trainer.assignVisitor(visitor);
            return true;
        }
        return false;
    }
    public String getVisitorInfo(String visitorId) {
        Visitor visitor = getVisitor(visitorId);
        if (visitor != null) return "Відвідувача не знайдено";

        Membership membership = memberships.get(visitorId);
        Trainer trainer = trainers.stream()
                .filter(t -> t.isAssignedTo(visitor))
                .findFirst()
                .orElse(null);
        return String.format("Ім'я: %s\nАбонемент: %s\nТренер: %s\nВідвідувань: %d",
                visitor.getName(),
                (membership != null && membership.isActive()) ? "Активний" : "неактивний",
                (trainer != null) ? trainer.getName() : "Не закріплено",
                visitHistory.getOrDefault(visitorId, new ArrayList<>()).size());
    }
    public List<Visitor> getVisitors() {return visitors;}
    public Map<String, Membership> getMemberships() {return memberships;}
    public List<Trainer> getTrainers() {return trainers;}
}


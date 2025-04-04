package org.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
            visitor.setSurname(newSurname);
            visitor.setPhoneNumber(newPhoneNumber);
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
    public List<Trainer> getTrainers() {
        return trainers;
    }
    public List<LocalDate> getVisitHistory(String visitorId) {
        return visitHistory.getOrDefault(visitorId, new ArrayList<>());
    }


    public Trainer getTrainer(String id) {
        return trainers.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
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
        if (visitor != null) {
            Membership membership = memberships.get(visitorId);
            Trainer trainer = trainers.stream()
                    .filter(t -> t.isAssignedTo(visitor))
                    .findFirst()
                    .orElse(null);
            return String.format("Name: %s\nMembership: %s\nTrainer: %s\nVisits: %d",
                    visitor.getName(),
                    (membership != null && membership.isActive()) ? "Active" : "Inactive",
                    (trainer != null) ? trainer.getName() : "Not assigned",
                    visitHistory.getOrDefault(visitorId, new ArrayList<>()).size());
        }
        return "Visitor not found";
    }


    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void exportVisitorsToJson(String filePath, Comparator<Visitor> comparator) throws IOException {
        List<Visitor> sorted = visitors.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filePath), sorted);
    }

    public void importVisitorsFromJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Visitor[] imported = mapper.readValue(new File(filePath), Visitor[].class);
        for (Visitor v : imported) {
            registerVisitor(v);
        }
    }
}

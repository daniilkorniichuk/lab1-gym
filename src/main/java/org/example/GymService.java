package org.example;

import java.util.Comparator;
import java.util.List;
import java.io.IOException;


public class GymService {
    private final Gym gym;

    public GymService(Gym gym) {
        this.gym = gym;
    }

    public void registerVisitor(Visitor visitor) {
        gym.registerVisitor(visitor);
    }

    public List<Visitor> getAllVisitors() {
        return gym.getVisitors();
    }

    public List<Trainer> getAllTrainers() {
        return gym.getTrainers();
    }

    public void registerTrainer(Trainer trainer) {
        gym.addTrainer(trainer);
    }

    public boolean assignTrainerToVisitor(String visitorId, String trainerId) {
        return gym.enrollToTrainer(visitorId, trainerId);
    }

    public String getVisitorInfo(String visitorId) {
        return gym.getVisitorInfo(visitorId);
    }

    public void purchaseMembership(String visitorId, Membership membership) {
        gym.assignMembership(membership);
    }

    public void exportVisitors(String filePath, Comparator<Visitor> comparator) throws IOException {
        gym.exportVisitorsToJson(filePath, comparator);
    }

    public List<Visitor> importVisitors(String filePath) throws IOException {
        gym.importVisitorsFromJson(filePath);
        return gym.getVisitors();
    }
}

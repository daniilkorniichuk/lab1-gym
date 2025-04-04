package org.example;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Trainer {
    private String id;
    private String name;
    private Set<Visitor> assignedVisitors = new HashSet<>();

    public Trainer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void assignVisitor(Visitor visitor) {
        assignedVisitors.add(visitor);
    }

    public boolean isAssignedTo(Visitor visitor) {
        return assignedVisitors.contains(visitor);
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;
        return Objects.equals(id, trainer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trainer{id='" + id + "', name='" + name + "'}";
    }
}

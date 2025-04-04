package org.example;

import java.time.LocalDate;
import java.util.Objects;

public class Membership {
    private String visitorId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Membership(String visitorId, LocalDate startDate, LocalDate endDate) {
        this.visitorId = visitorId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return today.isAfter(startDate) && today.isBefore(endDate);
    }

    public String getVisitorId() { return visitorId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Membership that)) return false;
        return Objects.equals(visitorId, that.visitorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitorId);
    }

    @Override
    public String toString() {
        return "Membership{visitorId='" + visitorId + "', startDate=" + startDate + ", endDate=" + endDate + "}";
    }
}

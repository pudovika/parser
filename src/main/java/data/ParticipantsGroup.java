package data;

import java.util.List;
import java.util.Map;

import data.Participant;

public class ParticipantsGroup {

    private final AgeCategory ageCategory;

    private final Map<Category,List<Participant>> categoryMap;

    public ParticipantsGroup(AgeCategory ageCategory, Map<Category, List<Participant>> categoryMap) {
        this.ageCategory = ageCategory;
        this.categoryMap = categoryMap;
    }

    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public Map<Category, List<Participant>> getCategoryMap() {
        return categoryMap;
    }
}

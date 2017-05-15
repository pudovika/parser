package data;

import java.util.List;
import java.util.Map;

public class ParticipantsGroup {

    private final AgeCategory ageCategory;

    private final Map<Category,List<Participant>> categoryMap;

    private final String gender;

    public ParticipantsGroup(AgeCategory ageCategory, Map<Category, List<Participant>> categoryMap, String gender) {
        this.ageCategory = ageCategory;
        this.categoryMap = categoryMap;
        this.gender = gender;
    }

    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public Map<Category, List<Participant>> getCategoryMap() {
        return categoryMap;
    }

    public String getGender() {
        return gender;
    }
}

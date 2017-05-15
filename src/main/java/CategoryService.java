import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import data.AgeCategory;
import data.Category;
import data.Participant;
import data.ParticipantsGroup;

public class CategoryService {

    private final String genderMale;

    private final String genderFemale;

    public CategoryService(String genderMale, String genderFemale) {
        this.genderMale = genderMale;
        this.genderFemale = genderFemale;
    }

    /**
     * Filters participants by age
     * @param participants List of participants for filter
     * @param ageCategory Age category
     * @param gender
     * @return List of participants in this category
     */

    public List<Participant> filterByAgeAndGender(List<Participant> participants, AgeCategory ageCategory, String gender) {
        return participants.stream()
                .filter(p -> p.getAge() >= ageCategory.getStartAge()
                        && p.getAge() <= ageCategory.getEndAge())
                .filter(p -> p.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }

    public List<Participant> filterByRatio(List<Participant> participants, int startRatio, int endRatio) {
        return participants.stream().
                filter(p -> p.getRatio() >= startRatio
                        && p.getRatio() < endRatio)
                .collect(Collectors.toList());
    }

    /**
     * Process participants, groups by categories
     * @param participants List of participants for process
     * @return List of ParticipantsGroup
     */
    public List<ParticipantsGroup> processParticipants(List<Participant> participants){
        List<ParticipantsGroup> participantsGroups = new ArrayList<>();
        for (AgeCategory ageCategory : AgeCategory.values()) {
            addNonEmptyParticipants(participantsGroups, generateParticipantGroup(ageCategory, participants, genderFemale));
            addNonEmptyParticipants(participantsGroups, generateParticipantGroup(ageCategory, participants, genderMale));
        }
        return  participantsGroups;
    }

    private void addNonEmptyParticipants(List<ParticipantsGroup> participantsGroups, ParticipantsGroup participantsGroup) {
        if (!participantsGroup.getCategoryMap().isEmpty()) {
            participantsGroups.add(participantsGroup);
        }
    }

    private ParticipantsGroup generateParticipantGroup(AgeCategory ageCategory, List<Participant> participants, String gender) {
        List<Participant> filteredParticipants = filterByAgeAndGender(participants, ageCategory, gender);
        Map<Category,List<Participant>> categoryMap = new HashMap<>();

        for(Category category : Category.values()) {
            List<Participant> filteredByRatio = filterByRatio(filteredParticipants,
                    category.getStartRatio() - ageCategory.getRatioOffset(),
                    category.getEndRatio() - ageCategory.getRatioOffset());
            if (filteredByRatio.size() > 0) {
                categoryMap.put(category,
                        filteredByRatio);
            }

        }

        return new ParticipantsGroup(ageCategory,categoryMap, gender);
    }


}

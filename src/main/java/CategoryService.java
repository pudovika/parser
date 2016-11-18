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

    /**
     * Filters participants by age
     * @param participants List of participants for filter
     * @param ageCategory Age category
     * @return List of participants in this category
     */

    public List<Participant> filterByAge(List<Participant> participants, AgeCategory ageCategory) {
        return participants.stream().
                filter(p -> p.getAge() >= ageCategory.getStartAge()
                        && p.getAge() <= ageCategory.getEndAge())
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

            List<Participant> filteredParticipants = filterByAge(participants, ageCategory);
            Map<Category,List<Participant>> categoryMap = new HashMap<>();

            for(Category category : Category.values()) {
                categoryMap.put(category,
                        filterByRatio(filteredParticipants,
                                category.getStartRatio() - ageCategory.getRatioOffset(),
                                category.getEndRatio() - ageCategory.getRatioOffset()));
            }

            participantsGroups.add(new ParticipantsGroup(ageCategory,categoryMap));

        }
        return  participantsGroups;
    }


}

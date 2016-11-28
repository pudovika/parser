package data;

import java.time.LocalDate;

public class Participant {

    private String name;

    private String lastName;

    private Double height;

    private Double weight;

    private Double ratio;

    private String gender;

    private LocalDate birthDate;

    private Integer age;

    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", ratio=" + ratio +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    public String toPrettyEnString() {
        return "Participant: \n" +
                " Name: " + name + "\n" +
                " LastName: " + lastName + "\n" +
                " Height: " + height + "\n" +
                " Weight: " + weight + "\n" +
                " Category: " + ratio + "\n" +
                " Gender: " + gender + "\n" +
                " BirthDate: " + birthDate.getYear() + "\n";
    }
}

package zur.koeln.kickertool.application.handler.commands.player;


public class PlayerCommandCreateDTO
    implements PlayerCommandDTO {

    private final String firstName;
    private final String lastName;

    public PlayerCommandCreateDTO(
        String firstName,
        String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
}

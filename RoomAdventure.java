import java.util.Scanner;                                               // Import scanner for reading user input

public class RoomAdventure {                                           // Main class containing game logic

    // class variables
    private static Room currentRoom;                                    // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status;                                       // Message to display after each selection 

    // constants
    final private static String DEFAULT_STATUS =                        
    // Default error message
    "Sorry, I do not understand, Try [verb] [noun]. Valid verbs include  'go', 'look', and 'take'.";



    private static void handleGo(String noun) {
        String[] exitDirections = currentRoom.getExitDirections();      // Get available exit directions
        Room[] exitDestinations = currentRoom.getExitDestinations();    // Get corresponding room objects  
        status = "I don't see that room.";                              // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) {               // Loop through exit directions
            if (noun.equals(exitDirections[i])) {                       // if direction matches
                currentRoom = exitDestinations[i];                      // Move to the new room
                status = "Changed Room";                                // Update status 
            }
        }
    }

    private static void handleLook(String noun) {                       // Handles inspecting items
        String[] items = currentRoom.getItems();                        // Visible Items in the room
        String[] itemDescriptions = currentRoom.getItemDescriptions();  // Descriptions of items in the room
        status = "I don't see that item.";                              // Default if item not found
        for (int i = 0; i < items.length; i++) {                        // Loop through items
            if (noun.equals(items[i])) {                                // if user noun matches an item
                status = itemDescriptions[i];                           // Update status with item description
            }
        }
    }

    private static void handleTake(String noun) {                       // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables();              // Items that can be picked up
        status = "I can't grab that.";                                  // Default if not grabbable
        for (String item : grabbables) {                                // Loop through grabbables
            if (noun.equals(item)) {                                    // If user noun matches a grabbable item
                for (int j = 0; j < inventory.length; j++) {            // Loop through inventory
                    if (inventory[j] == null) {                         // If empty slot found
                        inventory[j] = item;                            // Add item to inventory
                        status = "Item added to inventory.";            // Update status
                        break;                                          // Exit loop
                    }
                }
            }
        }
    }
    private static void setupGame(){
        // Room 1
        Room room1 = new Room("Room 1");                            // Create room 1
        Room room2 = new Room("Room 2");                            // Create room 2

        String[] room1ExitDirections = {"east"} ;                        // Set exit directions for room 1
        Room[] room1ExitDestinations = {room2};                          // Set exit directions and destinations for room 1
        String[] room1Items = {"desk", "chair"};                         // Set items in room 1
        String[] room1ItemDescriptions = {                               // Set item descriptions for room 1
            "A wooden desk.", "A comfortable chair."
        }; 
        String[] room1Grabbables = {"key"};                             // Set grabbables for room 1
        room1.setExitDirections(room1ExitDirections);                   // Set exit directions for room 1
        room1.setExitDestinations(room1ExitDestinations);               // Set exit destinations for room 1
        room1.setItems(room1Items);                                     // Set items for room 1
        room1.setItemDescriptions(room1ItemDescriptions);               // Set item descriptions for room 1
        room1.setGrabbables(room1Grabbables);                           // Set grabbables for room 1

        // Room 2
        String[] room2ExitDirections = {"west"};                        // Set exit directions for room 2
        Room[] room2ExitDestinations = {room1};                         // Set exit directions and destinations for room 2
        String[] room2Items = {"fireplace", "book", "rug"};             // Set items in room 2
        String[] room2ItemDescriptions = {                              // Set item descriptions for room 2
            "A cozy fireplace.", "An old book.", "A soft rug."
        };
        String[] room2Grabbables = {"book"};                            // Set grabbables for room 2
        room2.setExitDirections(room2ExitDirections);                   // Set exit directions for room 2
        room2.setExitDestinations(room2ExitDestinations);               // Set exit destinations for room 2
        room2.setItems(room2Items);                                     // Set items for room 2
        room2.setItemDescriptions(room2ItemDescriptions);               // Set item descriptions for room 2
        room2.setGrabbables(room2Grabbables);                           // Set grabbables for room 2

        currentRoom = room1;                                            // Set starting room to Room 1
    }

    @SuppressWarnings("java:S2189")
    public static void main(String[] args) {
        setupGame(); // Initialize game setup

        while (true) { 
            System.out.print(currentRoom.toString());                   // Print current room
            System.out.print("\nInventory: ");                        // Print inventory
            for (int i = 0; i < inventory.length; i++) {                // Loop through inventory slots
                System.out.print(inventory[i] + " ");                   // Print each item in inventory
            }
            System.out.println("\nWhat would you like to do? ");      // Prompt for user input

            Scanner s = new Scanner(System.in);                         // Create scanner for user input
            String input = s.nextLine();                                // Read entire line of input
            String[] words = input.split(" ");                    // Split input into words

            if (words.length != 2) {                                    // Check for proper two-word command
                status = DEFAULT_STATUS;                                // Set default status if input is not two words
                continue;
            }
            String verb = words[0];                                     // First word is the verb
            String noun = words[1];                                     // Second word is the noun

            switch (verb) {                                             // Handle different verbs
                case "go":                                              // If verb is "go"
                    handleGo(noun);                                     // Call method to handle movement
                    break;
                case "look":                                            // If verb is "look"
                    handleLook(noun);                                   // Call method to handle looking at items
                    break;
                case "take":                                            // If verb is "take"
                    handleTake(noun);                                   // Call method to handle taking items
                    break;
                default:                                                // Invalid command
                    status = DEFAULT_STATUS;                            // Set default status for invalid command
            }

            System.out.println(status);                                 // Print status message
        }
    }
}




class Room {                            // Represents a game room
    private String name;                // Name of the room
    private String[] exitDirections;    // Directions to other rooms
    private Room[] exitDestinations;    // Rooms corresponding to the exit directions
    private String[] items;             // Items visible in the room
    private String[] itemDescriptions;  // Descriptions of the items
    private String[] grabbables;        // Items that can be picked up

    public Room (String name) {         // Constructor
        this.name = name;               // Set the room name
    }

    public void setExitDirections(String[] exitDirections) {    // Setter for exits
        this.exitDirections = exitDirections; 
    }

    public String[] getExitDirections() {                       // Getter for exits
        return exitDirections; 
    }

    public void setExitDestinations(Room[] exitDestinations) {  // Setter for exit destinations
        this.exitDestinations = exitDestinations; 
    }

    public Room[] getExitDestinations() {                       // Getter for exit destinations
        return exitDestinations; 
    }

    public void setItems(String[] items) {                      // Setter for items
        this.items = items; 
    }

    public String[] getItems() {                                // Getter for items
        return items; 
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for item descriptions
        this.itemDescriptions = itemDescriptions; 
    }

    public String[] getItemDescriptions() {                     // Getter for item descriptions
        return itemDescriptions; 
    }

    public void setGrabbables(String[] grabbables) {            // Setter for grabbables
        this.grabbables = grabbables; 
    }

    public String[] getGrabbables() {                           // Getter for grabbables
        return grabbables; 
    }

    @Override
    public String toString() {                      // Custom print for the room
        String result = "\nLocation: " + name;      // Show room name
        result += "\nYou See: ";                    // List items
        for (String item : items) {                 // Loop items
            result += "\n" + item;                  // Append each item
        }
        result += "\nExits: ";                      // List exits
        for (String direction : exitDirections) {   // Loop exits
            result += direction + " ";              // Append each exit direction

        }
        return result + "\n";                       // Return full description
    }
}
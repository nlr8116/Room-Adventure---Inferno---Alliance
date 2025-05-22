import java.util.Scanner; // Import Scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action
    private static boolean isRunning = true; // Controls game loop


    // constants
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'quit game', and 'take'."; // Default error message



    private static void handleGo(String noun) {
        String[] exitDirections = currentRoom.getExitDirections();
        Room[] exitDestinations = currentRoom.getExitDestinations();
        String[] requiredItems = currentRoom.getRequiredItems();
        status = "I don't see that room.";
        for (int i = 0; i < exitDirections.length; i++) {
            if (noun.equals(exitDirections[i])) {

                // Check if this direction requires an item
                if (requiredItems != null && requiredItems[i] != null) {
                    String requiredItem = requiredItems[i];
                    boolean hasItem = false;

                    for (String item : inventory) {
                        if (requiredItem.equals(item)) {
                            hasItem = true;
                            break;
                        }
                    }

                    if (!hasItem) {
                        status = "You need the " + requiredItem + " to go " + noun + ".";
                        return; // Don't allow movement
                    }
                }

                currentRoom = exitDestinations[i];
                status = "You moved to a new room.";
                return;
            }
        }
    }


    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Items that can be taken
        status = "I can't grab that."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // If user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; // Update status
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    private static void handleQuit(String verb){
        System.out.println("Goodbye");
        isRunning = false;
    }

    

    private static void setupGame() { // Initializes game world
        Room room1 = new Room("Room 1"); // Create Room 1
        Room room2 = new Room("Room 2"); // Create Room 2
        Room lockedRoom = new Room("Locked Room");


        // Room 1
        String[] room1ExitDirections = {"east", "north"};
        Room[] room1ExitDestinations = {room2, lockedRoom};
        String[] room1RequiredItems = {"lightsaber", "keycard"}; // <-- key required for 'north', lightsaber for east
        String[] room1Items = {"chair", "desk"};
        String[] room1ItemDescriptions = {
            "It is a chair",
            "It's a desk, there is a keycard on it."
        };
        String[] room1Grabbables = {"keycard"};

        room1.setExitDirections(room1ExitDirections);
        room1.setExitDestinations(room1ExitDestinations);
        room1.setRequiredItems(room1RequiredItems);
        room1.setItems(room1Items);
        room1.setItemDescriptions(room1ItemDescriptions);
        room1.setGrabbables(room1Grabbables);

        String[] room2ExitDirections = {"west"}; // Room 2 exits
        Room[] room2ExitDestinations = {room1}; // Destination rooms for Room 2
        String[] room2Items = {"Holotable", "desk"}; // Items in Room 2
        String[] room2ItemDescriptions = { // Descriptions for Room 2 items
            "It's displaying a schematic of the Death Star",
            "There is a com-link on the desk. I can use it to contact the Rebel Fleet."
        };
        String[] room2Grabbables = {"com-link"}; // Items you can take in Room 2
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // Set grabbable items

        String[] lockedRoomExitDirections = {"south"};
        Room[] lockedRoomExitDestinations = {room1};
        String[] lockedRoomItems = {"chest"};
        String[] lockedRoomItemDescriptions = {
            "It's a heavy wooden chest with a Jedi emblem. There's a lightsaber inside."
        };
        String[] lockedRoomGrabbables = {"lightsaber"};

        lockedRoom.setExitDirections(lockedRoomExitDirections);
        lockedRoom.setExitDestinations(lockedRoomExitDestinations);
        lockedRoom.setItems(lockedRoomItems);
        lockedRoom.setItemDescriptions(lockedRoomItemDescriptions);
        lockedRoom.setGrabbables(lockedRoomGrabbables);

        currentRoom = room1; // Start game in Room 1
    }
    
    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (isRunning) { // Game loop, runs until program is terminated
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }

            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            Scanner s = new Scanner(System.in); // Create Scanner to read input
            String input = s.nextLine(); // Read entire line of input
            String[] words = input.split(" "); // Split input into words

            if (words.length != 2) { // Check for proper two-word command
                status = DEFAULT_STATUS; // Set status to error message
                continue; // Skip to next loop iteration
            }

            String verb = words[0]; // First word is the action verb
            String noun = words[1]; // Second word is the target noun

            switch (verb) { // Decide which action to take
                case "go": // If verb is 'go'
                    handleGo(noun); // Move to another room
                    break;
                case "look": // If verb is 'look'
                    handleLook(noun); // Describe an item
                    break;
                case "take": // If verb is 'take'
                    handleTake(noun); // Pick up an item
                    break;
                case "quit":
                    handleQuit(verb);
                    break;
                default: // If verb is unrecognized
                    status = DEFAULT_STATUS; // Set status to error message
            }

            System.out.println(status); // Print the status message
        }
    }
}

class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direction
    private String[] items; // Items visible in the room
    private String[] itemDescriptions; // Descriptions for those items
    private String[] grabbables; // Items you can take
    private String[] requiredItems; // Items required to use each exit (null if no requirement)


    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;
    }

    public void setItems(String[] items) { // Setter for items
        this.items = items;
    }

    public String[] getItems() { // Getter for items
        return items;
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions() { // Getter for descriptions
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) { // Setter for grabbable items
        this.grabbables = grabbables;
    }

    public String[] getGrabbables() { // Getter for grabbable items
        return grabbables;
    }

    public void setRequiredItems(String[] requiredItems) {
    this.requiredItems = requiredItems;
}

public String[] getRequiredItems() {
    return requiredItems;
}


    @Override
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; // List items
        for (String item : items) { // Loop items
            result += item + " "; // Append each item
        }
        result += "\nExits: "; // List exits
        for (String direction : exitDirections) { // Loop exits
            result += direction + " "; // Append each direction
        }
        return result + "\n"; // Return full description
    }
}

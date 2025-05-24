# Java GUI-Based Client-Server Chat Application

This project is a simple **Client-Server Chat Application** built using Java Swing for the GUI and Java Sockets for network communication. The application allows basic message exchange between a server and multiple clients.

## Features

- Java Swing GUI for both client and server
- Real-time bi-directional message exchange using sockets
- Connection status updates and message logs
- Easy-to-use interface for sending and receiving messages
- Scalable to support multiple clients
- Message area with auto-scrolling

## Functionalities

### Server
- Start and listen on a specific port
- Accept multiple client connections
- Broadcast received messages to all connected clients
- Display message logs and client connections
- GUI-based control panel for monitoring and managing chat sessions

### Client
- Connect to a server using IP address and port
- Send messages to the server
- Receive and display broadcast messages from server
- Scrollable text area for chat history
- Simple GUI with message input and send button

## Files Included

- `ClientGUI.java` – GUI and logic for the client-side application
- `ServerGUI.java` – GUI and logic for the server-side application

## Requirements

- Java Development Kit (JDK) 8 or later
- An IDE like IntelliJ IDEA, Eclipse, or simply a terminal to compile and run

## How to Run

### 1. Compile the source files

```bash
javac ServerGUI.java ClientGUI.java
```

### 2. Run the server

```bash
java ServerGUI
```

### 3. Run one or more clients

```bash
java ClientGUI
```

### 4. Start Chatting

- Enter messages in the input field and press send
- Messages will be exchanged in real-time between server and connected clients

## Notes

- Ensure the server is running before starting any client
- The default host is `localhost` and port is defined within the code

## License

This project is open-source and free to use under the MIT License.
